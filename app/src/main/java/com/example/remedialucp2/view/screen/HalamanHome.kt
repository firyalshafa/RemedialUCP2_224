package com.example.remedialucp2.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.remedialucp2.view.route.destinasibuku
import com.example.remedialucp2.view.route.destinasikategori

@Composable
fun HalamanHome(
    navController: NavHostController
) {
    var keyword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Perpustakaan",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Pencarian Global",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = keyword,
                    onValueChange = { keyword = it },
                    label = { Text("Cari judul buku / kategori") },
                    singleLine = true,
                    modifier = Modifier.fillMaxSize()
                )

                Button(
                    onClick = { navController.navigate(destinasibuku.route) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Kelola Buku")
                }

                Button(
                    onClick = { navController.navigate(destinasikategori.route) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Kelola Kategori")
                }
            }
        }
    }
}
