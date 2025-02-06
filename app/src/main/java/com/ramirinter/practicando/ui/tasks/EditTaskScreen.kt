package com.ramirinter.practicando.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramirinter.practicando.network.Task
import com.ramirinter.practicando.network.TaskRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(navController: NavController, task: Task) {
    val taskRepository = TaskRepository() // Instancia del repositorio

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var status by remember { mutableStateOf(task.status) }
    var message by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("pending", "in progress", "completed")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tarea") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Build, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo de título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
// Campo de estado

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = status,
                    onValueChange = { },
                    label = { Text("Estado") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                        DropdownMenuItem(
                            text = { Text("Pending") },
                            onClick = {
                                status = "pending"
                                expanded = true
                            }
                        )
                    DropdownMenuItem(
                        text = { Text("Completed") },
                        onClick = {
                            status = "completed"
                            expanded = true
                        }
                    )
                }
            }
            // Botón de guardar
            Button(
                onClick = {
                    val updatedTask = task.copy(
                        title = title,
                        description = description,
                        status = status
                    )
                    taskRepository.updateTask(updatedTask) { task, errorMessage ->
                        if (task != null) {
                            message = "Tarea actualizada con éxito"
                            navController.popBackStack() // Regresa a la lista de tareas
                        } else {
                            message = "Error al actualizar la tarea: $errorMessage"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }

            // Mensaje de error o éxito
            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = if (message.contains("éxito", ignoreCase = true)) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
