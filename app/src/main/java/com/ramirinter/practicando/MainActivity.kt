package com.ramirinter.practicando

import android.content.pm.PackageManager
import android.os.Build
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
import com.google.firebase.messaging.FirebaseMessaging
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import android.Manifest
import androidx.activity.compose.setContent

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // Verificar y solicitar permisos de notificación en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Obtener token de FCM
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Error obteniendo el token", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM", "Token actual: $token")
            }

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }

    // Solicitar permiso de notificación en Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "Permiso concedido")
        } else {
            Log.w("FCM", "Permiso de notificación denegado")
        }
    }
}

@Composable
fun AppNavigation(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("friends") { FriendScreen(navController) }
        composable("tasks") { TaskListScreen(navController) }
        composable("task/create") { CreateTaskScreen(navController) }
        composable("task/edit/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val taskRepository = TaskRepository()

            var taskToEdit: Task? = null
            var errorMessage = ""
            var isLoading = true

            LaunchedEffect(taskId) {
                if (taskId != null) {
                    taskRepository.getTaskById(taskId) { task, error ->
                        taskToEdit = task
                        errorMessage = error ?: ""
                        isLoading = false
                    }
                }
            }

            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                taskToEdit != null -> EditTaskScreen(navController, taskToEdit!!)
                errorMessage.isNotEmpty() -> Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
