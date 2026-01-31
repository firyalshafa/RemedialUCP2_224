package com.example.remedialucp2.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.room.buku
import com.example.remedialucp2.room.bukuDao
import com.example.remedialucp2.room.kategori
import com.example.remedialucp2.room.kategoriDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeResult(
    val buku: List<buku> = emptyList(),
    val kategori: List<kategori> = emptyList()
)

class HomeViewModel(
    private val bukuDao: bukuDao,
    private val kategoriDao: kategoriDao
) : ViewModel() {

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword

    val hasil: StateFlow<HomeResult> =
        combine(
            bukuDao.getSemuaBuku(),
            kategoriDao.getSemuaKategori(),
            keyword
        ) { bukuList, kategoriList, key ->
            if (key.isBlank()) {
                HomeResult(
                    buku = bukuList.take(5),
                    kategori = kategoriList.take(5)
                )
            } else {
                val k = key.trim()
                HomeResult(
                    buku = bukuList.filter { it.judul.contains(k, ignoreCase = true) }.take(10),
                    kategori = kategoriList.filter { it.nama.contains(k, ignoreCase = true) }.take(10)
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeResult())

    fun setKeyword(value: String) {
        _keyword.value = value
    }

    companion object {
        fun factory(bukuDao: bukuDao, kategoriDao: kategoriDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(bukuDao, kategoriDao) as T
                }
            }
    }
}
