package com.example.remedialucp2.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
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
import com.example.remedialucp2.view.route.Destinasieditbuku
import com.example.remedialucp2.view.route.Destinasienterybuku
import kotlinx.coroutines.launch

@Composable
fun HalamanBuku(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = remember { databaseperpustakaan.getDatabase(context) }
    val bukuDao = remember { db.bukuDao() }
    val scope = rememberCoroutineScope()

    var keyword by remember { mutableStateOf("") }

    val listBukuFlow = remember(keyword) {
        if (keyword.isBlank()) bukuDao.getSemuaBuku()
        else bukuDao.searchBuku("%$keyword%")
    }
    val listBuku by listBukuFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Data Buku",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = keyword,
            onValueChange = { keyword = it },
            label = { Text("Cari judul buku") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { navController.navigate(Destinasienterybuku.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Buku")
        }

        if (listBuku.isEmpty()) {
            Text("Belum ada data buku.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listBuku, key = { it.id }) { item ->
                    KartuBuku(
                        data = item,
                        onEdit = {
                            navController.navigate("${Destinasieditbuku.route}/${item.id}")
                        },
                        onHapus = {
                            scope.launch {
                                bukuDao.softDeleteBuku(item.id, System.currentTimeMillis())
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun KartuBuku(
    data: buku,
    onEdit: () -> Unit,
    onHapus: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = data.judul,
                style = MaterialTheme.typography.titleLarge
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
