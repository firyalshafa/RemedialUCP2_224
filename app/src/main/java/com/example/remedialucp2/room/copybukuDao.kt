package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface copybukuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCopy(data: copybuku)

    @Update
    suspend fun updateCopy(data: copybuku)

    @Query("SELECT * FROM copybuku WHERE bukuId = :bukuId AND isDeleted = 0 ORDER BY copyId ASC")
    fun getCopyByBukuId(bukuId: Long): Flow<List<copybuku>>

    @Query("SELECT * FROM copybuku WHERE copyId = :copyId AND isDeleted = 0 LIMIT 1")
    fun getCopyById(copyId: String): Flow<copybuku?>

    @Query("SELECT * FROM copybuku WHERE copyId = :copyId LIMIT 1")
    suspend fun getCopyByIdOnce(copyId: String): copybuku?

    @Query("UPDATE copybuku SET status = :status WHERE copyId = :copyId AND isDeleted = 0")
    suspend fun updateStatusCopy(copyId: String, status: String)

    @Query("UPDATE copybuku SET isDeleted = 1, deletedAt = :deletedAt WHERE copyId = :copyId")
    suspend fun softDeleteCopy(copyId: String, deletedAt: Long)

    @Query("SELECT COUNT(*) FROM copybuku WHERE bukuId = :bukuId AND status = 'BORROWED' AND isDeleted = 0")
    suspend fun countDipinjamByBukuId(bukuId: Long): Int
}
