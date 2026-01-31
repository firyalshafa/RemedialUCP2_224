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
import com.example.remedialucp2.room.databaseperpustakaan
import com.example.remedialucp2.room.kategori
import kotlinx.coroutines.launch

@Composable
fun HalamanEntryKategori(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = remember { databaseperpustakaan.getDatabase(context) }
    val kategoriDao = remember { db.kategoriDao() }
    val scope = rememberCoroutineScope()

    var nama by remember { mutableStateOf("") }
    var parentIdText by remember { mutableStateOf("") }

    var errorNama by remember { mutableStateOf<String?>(null) }
    var errorParent by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Tambah Kategori",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = nama,
            onValueChange = {
                nama = it
                errorNama = null
            },
            label = { Text("Nama Kategori") },
            singleLine = true,
            isError = errorNama != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorNama != null) {
            Text(
                text = errorNama!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = parentIdText,
            onValueChange = {
                parentIdText = it
                errorParent = null
            },
            label = { Text("Parent ID (opsional)") },
            singleLine = true,
            isError = errorParent != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorParent != null) {
            Text(
                text = errorParent!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                val namaTrim = nama.trim()
                if (namaTrim.isEmpty()) {
                    errorNama = "Nama kategori tidak boleh kosong"
                    return@Button
                }

                val parentId: Long? = if (parentIdText.trim().isEmpty()) {
                    null
                } else {
                    parentIdText.toLongOrNull().also {
                        if (it == null) errorParent = "Parent ID harus angka"
                    }
                }

                if (errorParent != null) return@Button

                scope.launch {
                    kategoriDao.insertKategori(
                        kategori(
                            nama = namaTrim,
                            parentId = parentId
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
