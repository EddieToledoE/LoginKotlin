package com.ramirinter.practicando

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramirinter.practicando.ui.theme.PracticandoTheme
import com.ramirinter.practicando.ui.home.HomeScreen
import com.ramirinter.practicando.ui.login.LoginScreen
import com.ramirinter.practicando.ui.register.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticandoTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "login" // Pantalla inicial
    ) {
        composable("login") {
            LoginScreen(navController) // Pantalla de login
        }
        composable("home") {
            HomeScreen(navController) // Pantalla de inicio
        }
        composable("register") {
            RegisterScreen(navController) // Pantalla de registro
        }
    }
}
