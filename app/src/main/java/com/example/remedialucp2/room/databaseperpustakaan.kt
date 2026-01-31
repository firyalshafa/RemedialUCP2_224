package com.example.remedialucp2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        buku::class,
        kategori::class,
        copybuku::class,
        pengarang::class,
        bukuPengarangCrossRef::class,
        auditlog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class databaseperpustakaan : RoomDatabase() {

    abstract fun bukuDao(): bukuDao
    abstract fun kategoriDao(): kategoriDao
    abstract fun copybukuDao(): copybukuDao
    abstract fun pengarangDao(): pengarangDao
    abstract fun auditlogDao(): auditlogDao

    companion object {
        @Volatile
        private var INSTANCE: databaseperpustakaan? = null

        fun getDatabase(context: Context): databaseperpustakaan {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    databaseperpustakaan::class.java,
                    "database_perpustakaan"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
