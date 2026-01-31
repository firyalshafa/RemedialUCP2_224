package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface bukuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuku(data: buku): Long

    @Update
    suspend fun updateBuku(data: buku)

    @Query("SELECT * FROM buku WHERE isDeleted = 0 ORDER BY id DESC")
    fun getSemuaBuku(): Flow<List<buku>>

    @Query("SELECT * FROM buku WHERE isDeleted = 0 AND judul LIKE :keyword ORDER BY id DESC")
    fun searchBuku(keyword: String): Flow<List<buku>>

    @Query("SELECT * FROM buku WHERE id = :id AND isDeleted = 0 LIMIT 1")
    fun getBukuById(id: Long): Flow<buku?>

    @Query("SELECT * FROM buku WHERE id = :id LIMIT 1")
    suspend fun getBukuByIdOnce(id: Long): buku?

    @Query("UPDATE buku SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDeleteBuku(id: Long, deletedAt: Long)
}
