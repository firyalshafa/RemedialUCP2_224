package com.example.remedialucp2.view.uicontroller

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.remedialucp2.view.route.Destinasihome
import com.example.remedialucp2.view.route.destinasibuku
import com.example.remedialucp2.view.route.destinasikategori

@Composable
fun BottomBar(
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    val items = listOf(
        Destinasihome.route to Destinasihome.title,
        destinasibuku.route to destinasibuku.title,
        destinasikategori.route to destinasikategori.title
    )

    NavigationBar {
        items.forEach { (route, title) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(Destinasihome.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(title) },
                icon = {}
            )
        }
    }
}
