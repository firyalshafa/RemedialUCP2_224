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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.remedialucp2.room.databaseperpustakaan
import com.example.remedialucp2.view.route.Destinasieditkategori
import kotlinx.coroutines.launch

@Composable
fun HalamanEditKategori(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = remember { databaseperpustakaan.getDatabase(context) }
    val kategoriDao = remember { db.kategoriDao() }
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val kategoriId = backStackEntry?.arguments?.getLong(Destinasieditkategori.kategoriIdArg) ?: -1L

    val kategoriData by kategoriDao.getKategoriById(kategoriId).collectAsState(initial = null)

    var nama by remember { mutableStateOf("") }
    var parentIdText by remember { mutableStateOf("") }

    var errorNama by remember { mutableStateOf<String?>(null) }
    var errorParent by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(kategoriData?.id) {
        kategoriData?.let {
            nama = it.nama
            parentIdText = it.parentId?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Edit Kategori",
            style = MaterialTheme.typography.headlineMedium
        )

        if (kategoriId <= 0) {
            Text("ID kategori tidak valid.")
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Kembali")
            }
            return@Column
        }

        if (kategoriData == null) {
            Text("Memuat data...")
            return@Column
        }

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
            label = { Text("Parent ID (kosongkan jika induk)") },
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
                        if (it == kategoriId) errorParent = "Parent ID tidak boleh sama dengan ID sendiri"
                    }
                }

                if (errorParent != null) return@Button

                scope.launch {
                    val sebelum = kategoriDao.getKategoriByIdOnce(kategoriId)
                    if (sebelum == null) {
                        navController.popBackStack()
                        return@launch
                    }

                    val updated = sebelum.copy(
                        nama = namaTrim,
                        parentId = parentId
                    )

                    kategoriDao.updateKategori(updated)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Perubahan")
        }

        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Batal")
        }
    }
}
