package com.ramirinter.practicando

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramirinter.practicando.network.RetrofitInstance
import com.ramirinter.practicando.network.Task
import com.ramirinter.practicando.network.TaskRepository
import com.ramirinter.practicando.ui.friends.FriendScreen
import com.ramirinter.practicando.ui.theme.PracticandoTheme
import com.ramirinter.practicando.ui.home.HomeScreen
import com.ramirinter.practicando.ui.login.LoginScreen
import com.ramirinter.practicando.ui.register.RegisterScreen
import com.ramirinter.practicando.ui.tasks.EditTaskScreen
import com.ramirinter.practicando.ui.tasks.TaskListScreen
import com.ramirinter.practicando.ui.tasks.CreateTaskScreen

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
        composable("friends") {
            FriendScreen(navController) // Pantalla de amigos
        }
        composable("tasks"){
            TaskListScreen(navController) // Pantalla de tareas
        }
        composable("task/create") {
           CreateTaskScreen(navController) // Pantalla de creación de tarea
        }
        composable("task/edit/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val taskRepository = TaskRepository()

            var taskToEdit by remember { mutableStateOf<Task?>(null) }
            var errorMessage by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(true) }

            // Recuperar la tarea usando el repositorio
            LaunchedEffect(taskId) {
                if (taskId != null) {
                    taskRepository.getTaskById(taskId) { task, error ->
                        if (task != null) {
                            taskToEdit = task
                        } else {
                            errorMessage = error ?: "Error desconocido"
                        }
                        isLoading = false
                    }
                } else {
                    errorMessage = "ID de tarea inválido"
                    isLoading = false
                }
            }

            // Mostrar diferentes estados de la UI
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                taskToEdit != null -> {
                    EditTaskScreen(navController = navController, task = taskToEdit!!)
                }
                errorMessage.isNotEmpty() -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }



    }
}
