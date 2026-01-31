package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategori")
data class kategori(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val parentId: Long? = null,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)
