package com.example.remedialucp2.room

import androidx.room.Entity

@Entity(
    tableName = "buku_pengarang",
    primaryKeys = ["bukuId", "pengarangId"]
)
data class bukuPengarangCrossRef(
    val bukuId: Long,
    val pengarangId: Long
)
