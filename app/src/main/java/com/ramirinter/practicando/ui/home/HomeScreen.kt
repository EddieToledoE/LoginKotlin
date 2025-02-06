package com.ramirinter.practicando.ui.home

import android.graphics.Color
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramirinter.practicando.ui.bottombar.BottomBarComponent
import com.ramirinter.practicando.ui.topbar.ToolbarComponent

@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        topBar = { ToolbarComponent() },
        bottomBar = { BottomBarComponent(navController)},
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido a la pantalla de inicio!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        navController.popBackStack("login", inclusive = false) // Volver al login
                    }) {
                        Text(text = "Cerrar sesión")
                    }
                }
            }
        }
    )
}






