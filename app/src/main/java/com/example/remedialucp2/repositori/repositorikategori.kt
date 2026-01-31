package com.example.remedialucp2.repositori


import com.example.remedialucp2.room.auditlog
import com.example.remedialucp2.room.auditlogDao
import com.example.remedialucp2.room.bukuDao
import com.example.remedialucp2.room.copybukuDao
import com.example.remedialucp2.room.kategori
import com.example.remedialucp2.room.kategoriDao
import kotlinx.coroutines.flow.Flow

enum class ModeHapusKategori {
    HAPUS_BUKU,
    PINDAH_TANPA_KATEGORI
}

interface repositorikategori {
    fun getSemuaKategori(): Flow<List<kategori>>
    fun searchKategori(keyword: String): Flow<List<kategori>>
    fun getKategoriById(id: Long): Flow<kategori?>

    suspend fun insertKategori(data: kategori)
    suspend fun updateKategori(data: kategori)
    suspend fun hapusKategoriDenganAturan(categoryId: Long, mode: ModeHapusKategori)
}

class repositorikategoriimpl(
    private val kategoriDao: kategoriDao,
    private val bukuDao: bukuDao,
    private val copybukuDao: copybukuDao,
    private val auditDao: auditlogDao
) : repositorikategori {

    override fun getSemuaKategori(): Flow<List<kategori>> = kategoriDao.getSemuaKategori()

    override fun searchKategori(keyword: String): Flow<List<kategori>> = kategoriDao.searchKategori("%$keyword%")

    override fun getKategoriById(id: Long): Flow<kategori?> = kategoriDao.getKategoriById(id)

    override suspend fun insertKategori(data: kategori) {
        val id = kategoriDao.insertKategori(data)
        auditDao.insertLog(
            auditlog(
                tableName = "kategori",
                recordId = id.toString(),
                action = "INSERT",
                before = null,
                after = data.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateKategori(data: kategori) {
        val before = kategoriDao.getKategoriByIdOnce(data.id)
        kategoriDao.updateKategori(data)
        auditDao.insertLog(
            auditlog(
                tableName = "kategori",
                recordId = data.id.toString(),
                action = "UPDATE",
                before = before?.toString(),
                after = data.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun hapusKategoriDenganAturan(categoryId: Long, mode: ModeHapusKategori) {
        val before = kategoriDao.getKategoriByIdOnce(categoryId)

        when (mode) {
            ModeHapusKategori.HAPUS_BUKU -> {
                kategoriDao.hapusKategoriJikaAmanHapusBuku(categoryId)
            }
            ModeHapusKategori.PINDAH_TANPA_KATEGORI -> {
                kategoriDao.hapusKategoriJikaAmanPindahTanpaKategori(categoryId)
            }
        }

        auditDao.insertLog(
            auditlog(
                tableName = "kategori",
                recordId = categoryId.toString(),
                action = "DELETE_RULED",
                before = before?.toString(),
                after = null,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
