package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface pengarangDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengarang(data: pengarang): Long

    @Update
    suspend fun updatePengarang(data: pengarang)

    @Query("SELECT * FROM pengarang WHERE isDeleted = 0 ORDER BY nama ASC")
    fun getSemuaPengarang(): Flow<List<pengarang>>

    @Query("SELECT * FROM pengarang WHERE isDeleted = 0 AND nama LIKE :keyword ORDER BY nama ASC")
    fun searchPengarang(keyword: String): Flow<List<pengarang>>

    @Query("SELECT * FROM pengarang WHERE id = :id AND isDeleted = 0 LIMIT 1")
    fun getPengarangById(id: Long): Flow<pengarang?>

    @Query("SELECT * FROM pengarang WHERE id = :id LIMIT 1")
    suspend fun getPengarangByIdOnce(id: Long): pengarang?

    @Query("UPDATE pengarang SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDeletePengarang(id: Long, deletedAt: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelasiBukuPengarang(data: bukuPengarangCrossRef)

    @Query("DELETE FROM buku_pengarang WHERE bukuId = :bukuId AND pengarangId = :pengarangId")
    suspend fun deleteRelasiBukuPengarang(bukuId: Long, pengarangId: Long)

    @Query(
        """
        SELECT p.* FROM pengarang p
        INNER JOIN buku_pengarang bp ON bp.pengarangId = p.id
        WHERE bp.bukuId = :bukuId
          AND p.isDeleted = 0
        ORDER BY p.nama ASC
        """
    )
    fun getPengarangByBukuId(bukuId: Long): Flow<List<pengarang>>

    @Transaction
    suspend fun replacePengarangUntukBuku(bukuId: Long, pengarangIds: List<Long>) {
        deleteSemuaPengarangDariBuku(bukuId)
        pengarangIds.distinct().forEach { idPengarang ->
            insertRelasiBukuPengarang(
                bukuPengarangCrossRef(
                    bukuId = bukuId,
                    pengarangId = idPengarang
                )
            )
        }
    }

    @Query("DELETE FROM buku_pengarang WHERE bukuId = :bukuId")
    suspend fun deleteSemuaPengarangDariBuku(bukuId: Long)
}
