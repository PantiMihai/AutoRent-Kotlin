package com.example.autorent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autorent.ui.screens.LoginScreen
import com.example.autorent.ui.screens.MainScreen
import com.example.autorent.ui.screens.RegisterScreen
import com.example.autorent.ui.theme.AutoRentTheme
import com.example.autorent.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutoRentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AutoRentApp()
                }
            }
        }
    }
}

@Composable
fun AutoRentApp() {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    var isLoggedIn by remember { mutableStateOf(false) }
    var showRegisterScreen by remember { mutableStateOf(false) }
    
    // Check authentication state
    LaunchedEffect(currentUser) {
        isLoggedIn = currentUser != null
    }
    
    // Initialize with current authentication state
    LaunchedEffect(Unit) {
        isLoggedIn = authViewModel.isUserLoggedIn()
    }
    
    when {
        isLoggedIn -> {
            MainScreen(
                onLogout = { 
                    authViewModel.signOut()
                    isLoggedIn = false
                    showRegisterScreen = false
                }
            )
        }
        showRegisterScreen -> {
            RegisterScreen(
                onRegisterSuccess = { 
                    isLoggedIn = true 
                    showRegisterScreen = false
                },
                onNavigateToLogin = {
                    showRegisterScreen = false
                    authViewModel.clearAuthState()
                },
                authViewModel = authViewModel
            )
        }
        else -> {
            LoginScreen(
                onLoginSuccess = { 
                    isLoggedIn = true 
                    showRegisterScreen = false
                },
                onNavigateToRegister = {
                    showRegisterScreen = true
                    authViewModel.clearAuthState()
                },
                authViewModel = authViewModel
            )
        }
    }
}