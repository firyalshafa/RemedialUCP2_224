package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku")
data class buku(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val judul: String,
    val kategoriId: Long? = null,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)
