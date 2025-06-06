package com.example.autorent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.autorent.data.Car
import com.example.autorent.database.Booking
import com.example.autorent.ui.components.BottomNavItem
import com.example.autorent.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit = {}) {
    var selectedNavItem by remember { mutableStateOf(BottomNavItem.CATALOG) }
    var selectedCar by remember { mutableStateOf<Car?>(null) }
    var showBookingSummary by remember { mutableStateOf(false) }
    var carToBook by remember { mutableStateOf<Car?>(null) }
    var showEndBookingScreen by remember { mutableStateOf(false) }
    var bookingToEnd by remember { mutableStateOf<Booking?>(null) }
    
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
            when {
                showEndBookingScreen && bookingToEnd != null -> {
                    EndBookingScreen(
                        booking = bookingToEnd!!,
                        onBackClick = {
                            showEndBookingScreen = false
                            bookingToEnd = null
                        },
                        onTripEnded = {
                            showEndBookingScreen = false
                            bookingToEnd = null
                        }
                    )
                }
                showBookingSummary && carToBook != null -> {
                    CarBookingSummaryScreen(
                        car = carToBook!!,
                        onBackClick = { 
                            showBookingSummary = false
                            carToBook = null
                        },
                        onBookingConfirmed = {
                            showBookingSummary = false
                            carToBook = null
                            selectedCar = null
                        }
                    )
                }
                selectedCar != null -> {
                    CarDetailsScreen(
                        car = selectedCar!!,
                        onBackClick = { selectedCar = null },
                        onBookNow = { car ->
                            carToBook = car
                            showBookingSummary = true
                        }
                    )
                }
                else -> {
                when (selectedNavItem) {
                    BottomNavItem.HOME -> {
                        HomeScreen(
                            onCarClick = { car -> selectedCar = car },
                            onSearchClick = { selectedNavItem = BottomNavItem.CATALOG },
                            onViewAllClick = { selectedNavItem = BottomNavItem.CATALOG }
                        )
                    }
                    BottomNavItem.CATALOG -> {
                        CatalogScreen(
                            onCarClick = { car -> selectedCar = car }
                        )
                    }
                    BottomNavItem.FAVORITES -> {
                        FavoritesScreen(
                            onCarClick = { car -> selectedCar = car }
                        )
                    }
                    BottomNavItem.PROFILE -> {
                        MyAccountScreen(
                            onLogout = onLogout,
                            onEndTrip = { booking ->
                                bookingToEnd = booking
                                showEndBookingScreen = true
                            }
                        )
                    }
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
}}