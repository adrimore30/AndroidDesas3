package com.example.desas3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desas3.ui.theme.Desas3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Desas3Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // 1. Crea el controlador de navegación
                    val navController = rememberNavController()




                    // 2. Aquí se coloca el NavHost que contiene las pantallas
                    NavHost(
                        navController = navController,
                        startDestination = "login" // 👈 pantalla inicial
                    ) {

                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("register") {
                            RegisterScreen(navController)
                        }
                        composable("home") {
                            HomeScreen()
                        }

                    }
                }
            }
        }
    }
}


