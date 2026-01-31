package com.example.remedialucp2.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.remedialucp2.room.buku
import com.example.remedialucp2.room.databaseperpustakaan
import kotlinx.coroutines.launch

@Composable
fun HalamanEntryBuku(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = remember { databaseperpustakaan.getDatabase(context) }
    val bukuDao = remember { db.bukuDao() }
    val scope = rememberCoroutineScope()

    var judul by remember { mutableStateOf("") }
    var kategoriIdText by remember { mutableStateOf("") }

    var errorJudul by remember { mutableStateOf<String?>(null) }
    var errorKategori by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Tambah Buku",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = judul,
            onValueChange = {
                judul = it
                errorJudul = null
            },
            label = { Text("Judul Buku") },
            singleLine = true,
            isError = errorJudul != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorJudul != null) {
            Text(
                text = errorJudul!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = kategoriIdText,
            onValueChange = {
                kategoriIdText = it
                errorKategori = null
            },
            label = { Text("Kategori ID (opsional)") },
            singleLine = true,
            isError = errorKategori != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorKategori != null) {
            Text(
                text = errorKategori!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                val judulTrim = judul.trim()
                if (judulTrim.isEmpty()) {
                    errorJudul = "Judul tidak boleh kosong"
                    return@Button
                }

                val kategoriId: Long? = if (kategoriIdText.trim().isEmpty()) {
                    null
                } else {
                    kategoriIdText.toLongOrNull().also {
                        if (it == null) errorKategori = "Kategori ID harus angka"
                    }
                }

                if (errorKategori != null) return@Button

                scope.launch {
                    bukuDao.insertBuku(
                        buku(
                            judul = judulTrim,
                            kategoriId = kategoriId
                        )
                    )
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }

        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Batal")
        }
    }
}
