package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auditlog")
data class auditlog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tableName: String,
    val recordId: String,
    val action: String,
    val before: String? = null,
    val after: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
