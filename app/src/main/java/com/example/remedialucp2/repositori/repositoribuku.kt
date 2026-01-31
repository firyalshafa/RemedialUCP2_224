package com.example.remedialucp2.repositori



import com.example.remedialucp2.room.auditlog
import com.example.remedialucp2.room.auditlogDao
import com.example.remedialucp2.room.buku
import com.example.remedialucp2.room.bukuDao
import com.example.remedialucp2.room.copybuku
import com.example.remedialucp2.room.copybukuDao
import com.example.remedialucp2.room.pengarangDao
import kotlinx.coroutines.flow.Flow

interface repositoribuku {
    fun getSemuaBuku(): Flow<List<buku>>
    fun searchBuku(keyword: String): Flow<List<buku>>
    fun getBukuById(id: Long): Flow<buku?>

    suspend fun insertBuku(data: buku)
    suspend fun updateBuku(data: buku)
    suspend fun softDeleteBuku(id: Long)

    fun getCopyByBukuId(bookId: Long): Flow<List<copybuku>>
    suspend fun insertCopy(data: copybuku)
    suspend fun updateStatusCopy(copyId: String, status: String)
    suspend fun softDeleteCopy(copyId: String)
}

class repositoribukuimpl(
    private val bukuDao: bukuDao,
    private val copybukuDao: copybukuDao,
    private val pengarangDao: pengarangDao,
    private val auditDao: auditlogDao
) : repositoribuku {

    override fun getSemuaBuku(): Flow<List<buku>> = bukuDao.getSemuaBuku()

    override fun searchBuku(keyword: String): Flow<List<buku>> = bukuDao.searchBuku("%$keyword%")

    override fun getBukuById(id: Long): Flow<buku?> = bukuDao.getBukuById(id)

    override suspend fun insertBuku(data: buku) {
        val id = bukuDao.insertBuku(data)
        auditDao.insertLog(
            auditlog(
                tableName = "buku",
                recordId = id.toString(),
                action = "INSERT",
                before = null,
                after = data.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateBuku(data: buku) {
        val before = bukuDao.getBukuByIdOnce(data.id)
        bukuDao.updateBuku(data)
        auditDao.insertLog(
            auditlog(
                tableName = "buku",
                recordId = data.id.toString(),
                action = "UPDATE",
                before = before?.toString(),
                after = data.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun softDeleteBuku(id: Long) {
        val before = bukuDao.getBukuByIdOnce(id)
        bukuDao.softDeleteBuku(id, System.currentTimeMillis())
        auditDao.insertLog(
            auditlog(
                tableName = "buku",
                recordId = id.toString(),
                action = "SOFT_DELETE",
                before = before?.toString(),
                after = null,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override fun getCopyByBukuId(bookId: Long): Flow<List<copybuku>> =
        copybukuDao.getCopyByBukuId(bookId)

    override suspend fun insertCopy(data: copybuku) {
        copybukuDao.insertCopy(data)
        auditDao.insertLog(
            auditlog(
                tableName = "copybuku",
                recordId = data.copyId,
                action = "INSERT",
                before = null,
                after = data.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateStatusCopy(copyId: String, status: String) {
        val before = copybukuDao.getCopyByIdOnce(copyId)
        copybukuDao.updateStatusCopy(copyId, status)
        val after = before?.copy(status = status)
        auditDao.insertLog(
            auditlog(
                tableName = "copybuku",
                recordId = copyId,
                action = "UPDATE_STATUS",
                before = before?.toString(),
                after = after?.toString(),
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun softDeleteCopy(copyId: String) {
        val before = copybukuDao.getCopyByIdOnce(copyId)
        copybukuDao.softDeleteCopy(copyId, System.currentTimeMillis())
        auditDao.insertLog(
            auditlog(
                tableName = "copybuku",
                recordId = copyId,
                action = "SOFT_DELETE",
                before = before?.toString(),
                after = null,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
