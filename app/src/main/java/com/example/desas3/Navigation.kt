package com.example.desas3

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("perfil") { PerfilUsuarioCompacto(navController) }
        composable("chat") { Desas3Chat(navController) }
        composable("publicar") { PublicationScreen(navController) }
    }
}