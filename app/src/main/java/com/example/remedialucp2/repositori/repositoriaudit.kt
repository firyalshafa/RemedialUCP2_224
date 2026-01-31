package com.example.remedialucp2.repositori

import com.example.remedialucp2.room.auditlog
import com.example.remedialucp2.room.auditlogDao
import kotlinx.coroutines.flow.Flow

interface repositoriaudit {
    fun getSemuaLog(): Flow<List<auditlog>>
    fun getLogByTable(tableName: String): Flow<List<auditlog>>
    suspend fun insertLog(data: auditlog)
    suspend fun hapusSemuaLog()
}

class repositoriauditimpl(
    private val auditDao: auditlogDao
) : repositoriaudit {

    override fun getSemuaLog(): Flow<List<auditlog>> = auditDao.getSemuaLog()

    override fun getLogByTable(tableName: String): Flow<List<auditlog>> =
        auditDao.getLogByTable(tableName)

    override suspend fun insertLog(data: auditlog) {
        auditDao.insertLog(data)
    }

    override suspend fun hapusSemuaLog() {
        auditDao.hapusSemuaLog()
    }
}
