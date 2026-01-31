package com.example.remedialucp2.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.example.remedialucp2.view.route.Destinasieditkategori
import com.example.remedialucp2.view.route.Destinasienterykategori
import kotlinx.coroutines.launch

@Composable
fun HalamanKategori(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = remember { databaseperpustakaan.getDatabase(context) }
    val kategoriDao = remember { db.kategoriDao() }
    val scope = rememberCoroutineScope()

    var keyword by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var selectedKategori by remember { mutableStateOf<kategori?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val listKategoriFlow = remember(keyword) {
        if (keyword.isBlank()) kategoriDao.getSemuaKategori()
        else kategoriDao.searchKategori("%$keyword%")
    }
    val listKategori by listKategoriFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Data Kategori",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = keyword,
            onValueChange = { keyword = it },
            label = { Text("Cari nama kategori") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { navController.navigate(Destinasienterykategori.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Kategori")
        }

        if (errorText != null) {
            Text(
                text = errorText!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (listKategori.isEmpty()) {
            Text("Belum ada data kategori.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listKategori, key = { it.id }) { item ->
                    KartuKategori(
                        data = item,
                        onEdit = {
                            navController.navigate("${Destinasieditkategori.route}/${item.id}")
                        },
                        onHapus = {
                            selectedKategori = item
                            showDialog = true
                            errorText = null
                        }
                    )
                }
            }
        }
    }

    if (showDialog && selectedKategori != null) {
        DialogHapusKategori(
            namaKategori = selectedKategori!!.nama,
            onDismiss = { showDialog = false },
            onHapusBuku = {
                scope.launch {
                    try {
                        kategoriDao.hapusKategoriJikaAmanHapusBuku(selectedKategori!!.id)
                        showDialog = false
                    } catch (e: Exception) {
                        errorText = e.message ?: "Gagal hapus kategori"
                        showDialog = false
                    }
                }
            },
            onPindahTanpaKategori = {
                scope.launch {
                    try {
                        kategoriDao.hapusKategoriJikaAmanPindahTanpaKategori(selectedKategori!!.id)
                        showDialog = false
                    } catch (e: Exception) {
                        errorText = e.message ?: "Gagal hapus kategori"
                        showDialog = false
                    }
                }
            }
        )
    }
}

@Composable
private fun KartuKategori(
    data: kategori,
    onEdit: () -> Unit,
    onHapus: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = data.nama,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Parent ID: ${data.parentId ?: "-"}",
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) { Text("Edit") }
                TextButton(onClick = onHapus) { Text("Hapus") }
            }
        }
    }
}

@Composable
private fun DialogHapusKategori(
    namaKategori: String,
    onDismiss: () -> Unit,
    onHapusBuku: () -> Unit,
    onPindahTanpaKategori: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Kategori") },
        text = {
            Text("Kategori \"$namaKategori\" mau diapakan bukunya?")
        },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onHapusBuku,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hapus Buku (Soft Delete)")
                }
                Button(
                    onClick = onPindahTanpaKategori,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pindah ke Tanpa Kategori")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
