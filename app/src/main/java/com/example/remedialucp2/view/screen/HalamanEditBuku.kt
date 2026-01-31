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
import com.example.remedialucp2.room.buku
import com.example.remedialucp2.room.databaseperpustakaan
import com.example.remedialucp2.view.route.Destinasieditbuku
import kotlinx.coroutines.launch

@Composable
fun HalamanEditBuku(
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = remember { databaseperpustakaan.getDatabase(context) }
    val bukuDao = remember { db.bukuDao() }
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val bukuId = backStackEntry?.arguments?.getLong(Destinasieditbuku.bukuIdArg) ?: -1L

    val bukuData by bukuDao.getBukuById(bukuId).collectAsState(initial = null)

    var judul by remember { mutableStateOf("") }
    var errorJudul by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(bukuData?.id) {
        bukuData?.let {
            judul = it.judul
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Edit Buku",
            style = MaterialTheme.typography.headlineMedium
        )

        if (bukuId <= 0) {
            Text("ID buku tidak valid.")
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Kembali")
            }
            return@Column
        }

        if (bukuData == null) {
            Text("Memuat data...")
            return@Column
        }

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

        Button(
            onClick = {
                val judulTrim = judul.trim()
                if (judulTrim.isEmpty()) {
                    errorJudul = "Judul tidak boleh kosong"
                    return@Button
                }

                scope.launch {
                    val sebelum = bukuDao.getBukuByIdOnce(bukuId)
                    if (sebelum == null) {
                        navController.popBackStack()
                        return@launch
                    }

                    val updated = sebelum.copy(judul = judulTrim)
                    bukuDao.updateBuku(updated)
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
