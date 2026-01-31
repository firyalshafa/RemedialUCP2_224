package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "copybuku",
    foreignKeys = [
        ForeignKey(
            entity = buku::class,
            parentColumns = ["id"],
            childColumns = ["bukuId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bukuId"])]
)
data class copybuku(
    @PrimaryKey
    val copyId: String,
    val bukuId: Long,
    val status: String = "AVAILABLE",
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)
