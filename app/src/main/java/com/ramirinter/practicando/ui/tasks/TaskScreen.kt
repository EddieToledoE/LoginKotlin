package com.ramirinter.practicando.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material3.ConfirmationDialog
import com.ramirinter.practicando.network.RetrofitInstance
import com.ramirinter.practicando.network.Task
import com.ramirinter.practicando.network.TaskRepository
import com.ramirinter.practicando.ui.bottombar.BottomBarComponent
import com.ramirinter.practicando.ui.topbar.ToolbarComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TaskListScreen(navController: NavController) {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var message by remember { mutableStateOf("") }
    val taskRepository = TaskRepository() // Instancia del repositorio

    // Fetch tasks from the backend
    LaunchedEffect(Unit) {
        taskRepository.getTasks { result: List<Task>?, error: String? ->
            if (error != null) {
                message = error
            } else {
                tasks = result ?: emptyList()
            }
        }
    }

    // UI
    Scaffold(
        topBar = { ToolbarComponent() },
        bottomBar = { BottomBarComponent(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("task/create") },
                content = { Icon(Icons.Default.Add, contentDescription = "Crear tarea") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (tasks.isEmpty() && message.isBlank()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (tasks.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onEdit = { taskToEdit ->
                                // Navega a la pantalla de edición pasando la tarea como argumento
                                navController.navigate("task/edit/${taskToEdit.id}")
                            },
                            onDelete = { deletedTask ->
                                taskRepository.deleteTask(deletedTask.id) { success ->
                                    if (success) {
                                        tasks = tasks.filter { it.id != deletedTask.id }
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = message,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

