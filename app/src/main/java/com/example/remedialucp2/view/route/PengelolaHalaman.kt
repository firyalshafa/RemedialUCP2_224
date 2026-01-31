package com.example.remedialucp2.view.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.remedialucp2.view.uicontroller.AppBar
import com.example.remedialucp2.view.uicontroller.BottomBar

@Composable
fun PengelolaHalaman(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val title = when {
        currentRoute == Destinasihome.route -> Destinasihome.title
        currentRoute == destinasibuku.route -> destinasibuku.title
        currentRoute == destinasikategori.route -> destinasikategori.title
        currentRoute == Destinasienterybuku.route -> Destinasienterybuku.title
        currentRoute == Destinasienterykategori.route -> Destinasienterykategori.title
        currentRoute?.startsWith(Destinasieditbuku.route) == true -> Destinasieditbuku.title
        currentRoute?.startsWith(Destinasieditkategori.route) == true -> Destinasieditkategori.title
        else -> "Perpustakaan"
    }

    val showBottomBar = currentRoute == Destinasihome.route ||
            currentRoute == destinasibuku.route ||
            currentRoute == destinasikategori.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AppBar(title = title) },
        bottomBar = { if (showBottomBar) BottomBar(navController) }
    ) { innerPadding ->
        DestinasiNavigasi(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
