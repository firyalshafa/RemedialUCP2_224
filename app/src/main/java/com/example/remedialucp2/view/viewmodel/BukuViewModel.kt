package com.example.remedialucp2.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.room.buku
import com.example.remedialucp2.room.bukuDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BukuViewModel(
    private val bukuDao: bukuDao
) : ViewModel() {

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword

    val listBuku: StateFlow<List<buku>> =
        keyword
            .combine(MutableStateFlow(Unit)) { key, _ -> key }
            .let { flowKey ->
                flowKey.combine(bukuDao.getSemuaBuku()) { key, all ->
                    if (key.isBlank()) all else all.filter { it.judul.contains(key, ignoreCase = true) }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setKeyword(value: String) {
        _keyword.value = value
    }

    fun clearError() {
        _error.value = null
    }

    fun insertBuku(judul: String, kategoriIdText: String) {
        val judulTrim = judul.trim()
        if (judulTrim.isEmpty()) {
            _error.value = "Judul tidak boleh kosong"
            return
        }

        val kategoriId: Long? = if (kategoriIdText.trim().isEmpty()) {
            null
        } else {
            kategoriIdText.toLongOrNull().also {
                if (it == null) _error.value = "Kategori ID harus angka"
            }
        }

        if (_error.value != null) return

        viewModelScope.launch {
            bukuDao.insertBuku(
                buku(
                    judul = judulTrim,
                    kategoriId = kategoriId
                )
            )
        }
    }

    fun updateBuku(id: Long, judul: String, kategoriIdText: String) {
        val judulTrim = judul.trim()
        if (judulTrim.isEmpty()) {
            _error.value = "Judul tidak boleh kosong"
            return
        }

        val kategoriId: Long? = if (kategoriIdText.trim().isEmpty()) {
            null
        } else {
            kategoriIdText.toLongOrNull().also {
                if (it == null) _error.value = "Kategori ID harus angka"
            }
        }

        if (_error.value != null) return

        viewModelScope.launch {
            val before = bukuDao.getBukuByIdOnce(id) ?: return@launch
            bukuDao.updateBuku(before.copy(judul = judulTrim, kategoriId = kategoriId))
        }
    }

    fun hapusBuku(id: Long) {
        viewModelScope.launch {
            bukuDao.softDeleteBuku(id, System.currentTimeMillis())
        }
    }

    companion object {
        fun factory(bukuDao: bukuDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BukuViewModel(bukuDao) as T
                }
            }
    }
}
