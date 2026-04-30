package com.example.foodstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.foodstorage.ui.theme.FoodStorageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionManager = SessionManager(this)

        val database = Room.databaseBuilder(
            applicationContext,
            FoodDatabase::class.java,
            "food_database"
        ).build()

        val repository = FoodRepository(database.foodDao(), database.userDao())

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FoodViewModel(repository, sessionManager) as T
            }
        }

        setContent {
            FoodStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn()) }
                    var authScreen by remember { mutableStateOf("login") }

                    val foodViewModel: FoodViewModel = viewModel(factory = factory)

                    if (isLoggedIn) {
                        MainAppScreen(
                            viewModel = foodViewModel,
                            sessionManager = sessionManager,
                            onLogout = {
                                sessionManager.logoutUser()
                                isLoggedIn = false
                                authScreen = "login"
                            }
                        )
                    } else {
                        if (authScreen == "login") {
                            LoginScreen(
                                viewModel = foodViewModel,
                                sessionManager = sessionManager,
                                onLoginSuccess = { isLoggedIn = true },
                                onCreateAccountClick = { authScreen = "register" }
                            )
                        } else {
                            CreateAccountScreen(
                                viewModel = foodViewModel,
                                sessionManager = sessionManager,
                                onAccountCreated = { isLoggedIn = true },
                                onBackToLogin = { authScreen = "login" }
                            )
                        }
                    }
                }
            }
        }
    }
}