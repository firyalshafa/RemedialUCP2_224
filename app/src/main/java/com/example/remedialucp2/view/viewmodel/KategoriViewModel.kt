package com.example.remedialucp2.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.room.kategori
import com.example.remedialucp2.room.kategoriDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ModeHapusKategoriVM {
    HAPUS_BUKU,
    PINDAH_TANPA_KATEGORI
}

class KategoriViewModel(
    private val kategoriDao: kategoriDao
) : ViewModel() {

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword

    val listKategori: StateFlow<List<kategori>> =
        keyword
            .combine(MutableStateFlow(Unit)) { key, _ -> key }
            .let { flowKey ->
                flowKey.combine(kategoriDao.getSemuaKategori()) { key, all ->
                    if (key.isBlank()) all else all.filter { it.nama.contains(key, ignoreCase = true) }
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

    fun insertKategori(nama: String, parentIdText: String) {
        val namaTrim = nama.trim()
        if (namaTrim.isEmpty()) {
            _error.value = "Nama kategori tidak boleh kosong"
            return
        }

        val parentId: Long? = if (parentIdText.trim().isEmpty()) {
            null
        } else {
            parentIdText.toLongOrNull().also {
                if (it == null) _error.value = "Parent ID harus angka"
            }
        }

        if (_error.value != null) return

        viewModelScope.launch {
            kategoriDao.insertKategori(
                kategori(
                    nama = namaTrim,
                    parentId = parentId
                )
            )
        }
    }

    fun updateKategori(id: Long, nama: String, parentIdText: String) {
        val namaTrim = nama.trim()
        if (namaTrim.isEmpty()) {
            _error.value = "Nama kategori tidak boleh kosong"
            return
        }

        val parentId: Long? = if (parentIdText.trim().isEmpty()) {
            null
        } else {
            parentIdText.toLongOrNull().also {
                if (it == null) _error.value = "Parent ID harus angka"
                if (it == id) _error.value = "Parent ID tidak boleh sama dengan ID sendiri"
            }
        }

        if (_error.value != null) return

        viewModelScope.launch {
            val before = kategoriDao.getKategoriByIdOnce(id) ?: return@launch
            kategoriDao.updateKategori(before.copy(nama = namaTrim, parentId = parentId))
        }
    }

    fun hapusKategoriDenganAturan(id: Long, mode: ModeHapusKategoriVM) {
        viewModelScope.launch {
            try {
                when (mode) {
                    ModeHapusKategoriVM.HAPUS_BUKU -> kategoriDao.hapusKategoriJikaAmanHapusBuku(id)
                    ModeHapusKategoriVM.PINDAH_TANPA_KATEGORI -> kategoriDao.hapusKategoriJikaAmanPindahTanpaKategori(id)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal menghapus kategori"
            }
        }
    }

    companion object {
        fun factory(kategoriDao: kategoriDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return KategoriViewModel(kategoriDao) as T
                }
            }
    }
}
