package com.example.foodstorage

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.NavGraph.Companion.findStartDestination

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val navItems = listOf(
    NavItem("All", Icons.Default.List, "list_all"),
    NavItem("Store", Icons.Default.Inventory, "storage_list"),
    NavItem("Eat", Icons.Default.Restaurant, "eat_list"),
    NavItem("Expired", Icons.Default.Warning, "expired_list"),
    NavItem("Add", Icons.Default.AddCircle, "add_item"),
    NavItem("Settings", Icons.Default.Settings, "settings")
)

@Composable
fun MainAppScreen(
    viewModel: FoodViewModel,
    sessionManager: SessionManager,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list_all",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("list_all") { ListAllScreen(viewModel) }
            composable("storage_list") { StorageListScreen(viewModel) }
            composable("eat_list") { EatListScreen(viewModel) }
            composable("expired_list") { ExpiredListScreen(viewModel) }
            composable("add_item") { AddItemScreen(viewModel) }
            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    sessionManager = sessionManager,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}