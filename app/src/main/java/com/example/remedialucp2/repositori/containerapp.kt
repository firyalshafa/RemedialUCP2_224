package com.example.remedialucp2.repositori


import android.content.Context
import com.example.remedialucp2.room.databaseperpustakaan
import com.example.remedialucp2.room.auditlogDao
import com.example.remedialucp2.room.bukuDao
import com.example.remedialucp2.room.kategoriDao
import com.example.remedialucp2.room.copybukuDao
import com.example.remedialucp2.room.pengarangDao

interface containerapp {
    val repositoribuku: repositoribuku
    val repositorikategori: repositorikategori
    val repositoriaudit: repositoriaudit
}

class containerappimpl(context: Context) : containerapp {

    private val db: databaseperpustakaan =
        databaseperpustakaan.getDatabase(context.applicationContext)

    private val bukuDao: bukuDao = db.bukuDao()
    private val kategoriDao: kategoriDao = db.kategoriDao()
    private val copybukuDao: copybukuDao = db.copybukuDao()
    private val pengarangDao: pengarangDao = db.pengarangDao()
    private val auditDao: auditlogDao = db.auditlogDao()

    override val repositoribuku: repositoribuku by lazy {
        repositoribukuimpl(
            bukuDao = bukuDao,
            copybukuDao = copybukuDao,
            pengarangDao = pengarangDao,
            auditDao = auditDao
        )
    }

    override val repositorikategori: repositorikategori by lazy {
        repositorikategoriimpl(
            kategoriDao = kategoriDao,
            bukuDao = bukuDao,
            copybukuDao = copybukuDao,
            auditDao = auditDao
        )
    }

    override val repositoriaudit: repositoriaudit by lazy {
        repositoriauditimpl(auditDao)
    }
}
