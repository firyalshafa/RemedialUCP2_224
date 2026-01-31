package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface kategoriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategori(data: kategori): Long

    @Update
    suspend fun updateKategori(data: kategori)

    @Query("SELECT * FROM kategori WHERE isDeleted = 0 ORDER BY nama ASC")
    fun getSemuaKategori(): Flow<List<kategori>>

    @Query("SELECT * FROM kategori WHERE isDeleted = 0 AND nama LIKE :keyword ORDER BY nama ASC")
    fun searchKategori(keyword: String): Flow<List<kategori>>

    @Query("SELECT * FROM kategori WHERE id = :id AND isDeleted = 0 LIMIT 1")
    fun getKategoriById(id: Long): Flow<kategori?>

    @Query("SELECT * FROM kategori WHERE id = :id LIMIT 1")
    suspend fun getKategoriByIdOnce(id: Long): kategori?

    @Query("UPDATE kategori SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDeleteKategori(id: Long, deletedAt: Long)

    @Query("SELECT * FROM kategori WHERE parentId = :parentId AND isDeleted = 0 ORDER BY nama ASC")
    fun getSubKategori(parentId: Long): Flow<List<kategori>>

    @Query(
        """
        SELECT COUNT(*) 
        FROM copybuku cb
        INNER JOIN buku b ON b.id = cb.bukuId
        WHERE b.kategoriId = :kategoriId
          AND b.isDeleted = 0
          AND cb.isDeleted = 0
          AND cb.status = 'BORROWED'
        """
    )
    suspend fun countCopyDipinjamDiKategori(kategoriId: Long): Int

    @Query("UPDATE buku SET kategoriId = NULL WHERE kategoriId = :kategoriId AND isDeleted = 0")
    suspend fun pindahBukuKeTanpaKategori(kategoriId: Long)

    @Query("UPDATE buku SET isDeleted = 1, deletedAt = :deletedAt WHERE kategoriId = :kategoriId AND isDeleted = 0")
    suspend fun softDeleteBukuDalamKategori(kategoriId: Long, deletedAt: Long)

    @Query(
        """
        UPDATE copybuku 
        SET isDeleted = 1, deletedAt = :deletedAt
        WHERE bukuId IN (
            SELECT id FROM buku WHERE kategoriId = :kategoriId AND isDeleted = 0
        )
        AND isDeleted = 0
        """
    )
    suspend fun softDeleteCopyDalamKategori(kategoriId: Long, deletedAt: Long)

    @Transaction
    suspend fun hapusKategoriJikaAmanHapusBuku(kategoriId: Long) {
        val jumlahDipinjam = countCopyDipinjamDiKategori(kategoriId)
        if (jumlahDipinjam > 0) throw IllegalStateException("Masih ada buku yang dipinjam")

        val waktu = System.currentTimeMillis()
        softDeleteCopyDalamKategori(kategoriId, waktu)
        softDeleteBukuDalamKategori(kategoriId, waktu)
        softDeleteKategori(kategoriId, waktu)
    }

    @Transaction
    suspend fun hapusKategoriJikaAmanPindahTanpaKategori(kategoriId: Long) {
        val jumlahDipinjam = countCopyDipinjamDiKategori(kategoriId)
        if (jumlahDipinjam > 0) throw IllegalStateException("Masih ada buku yang dipinjam")

        val waktu = System.currentTimeMillis()
        pindahBukuKeTanpaKategori(kategoriId)
        softDeleteKategori(kategoriId, waktu)
    }
}
