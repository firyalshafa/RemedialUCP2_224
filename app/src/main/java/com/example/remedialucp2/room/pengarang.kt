package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengarang")
data class pengarang(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nama: String,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)
