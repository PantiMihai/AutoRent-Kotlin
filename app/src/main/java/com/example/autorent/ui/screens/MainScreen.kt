package com.example.autorent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.autorent.ui.components.BottomNavItem
import com.example.autorent.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedNavItem by remember { mutableStateOf(BottomNavItem.CATALOG) }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedNavItem,
                onItemSelected = { selectedNavItem = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedNavItem) {
                BottomNavItem.HOME -> {
                    // For now, show the old car list screen as home
                    CarListScreen()
                }
                BottomNavItem.CATALOG -> {
                    CatalogScreen()
                }
                BottomNavItem.FAVORITES -> {
                    // TODO: Create favorites screen
                    PlaceholderScreen("Favorites")
                }
                BottomNavItem.PROFILE -> {
                    // TODO: Create profile screen
                    PlaceholderScreen("Profile")
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "$title Screen\nComing Soon!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
} 