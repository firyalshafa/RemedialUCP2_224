package com.example.remedialucp2.view.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.remedialucp2.view.screen.HalamanBuku
import com.example.remedialucp2.view.screen.HalamanEditBuku
import com.example.remedialucp2.view.screen.HalamanEditKategori
import com.example.remedialucp2.view.screen.HalamanEntryBuku
import com.example.remedialucp2.view.screen.HalamanEntryKategori
import com.example.remedialucp2.view.screen.HalamanHome
import com.example.remedialucp2.view.screen.HalamanKategori


@Composable
fun DestinasiNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinasihome.route,
        modifier = modifier
    ) {
        composable(Destinasihome.route) {
            HalamanHome(navController = navController)
        }

        composable(destinasibuku.route) {
            HalamanBuku(navController = navController)
        }

        composable(Destinasienterybuku.route) {
            HalamanEntryBuku(navController = navController)
        }

        composable(
            route = Destinasieditbuku.routeWithArg,
            arguments = listOf(navArgument(Destinasieditbuku.bukuIdArg) { type = NavType.LongType })
        ) {
            HalamanEditBuku(navController = navController)
        }

        composable(destinasikategori.route) {
            HalamanKategori(navController = navController)
        }

        composable(Destinasienterykategori.route) {
            HalamanEntryKategori(navController = navController)
        }

        composable(
            route = Destinasieditkategori.routeWithArg,
            arguments = listOf(navArgument(Destinasieditkategori.kategoriIdArg) { type = NavType.LongType })
        ) {
            HalamanEditKategori(navController = navController)
        }
    }
}
