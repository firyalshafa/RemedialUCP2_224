package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface auditlogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(data: auditlog)

    @Query("SELECT * FROM auditlog ORDER BY timestamp DESC")
    fun getSemuaLog(): Flow<List<auditlog>>

    @Query("SELECT * FROM auditlog WHERE tableName = :tableName ORDER BY timestamp DESC")
    fun getLogByTable(tableName: String): Flow<List<auditlog>>

    @Query("DELETE FROM auditlog")
    suspend fun hapusSemuaLog()
}
