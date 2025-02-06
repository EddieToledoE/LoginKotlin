package com.ramirinter.practicando.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramirinter.practicando.network.Task

@Composable
fun TaskItem(task: Task, onEdit: (Task) -> Unit, onDelete: (Task) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Título: ${task.title}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Descripción: ${task.description}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Estado: ${task.status}", style = MaterialTheme.typography.bodyMedium)
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onEdit(task) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }

    if (showDialog) {
        TaskConfirmationDialog(
            visible = showDialog,
            onConfirm = {
                showDialog = false
                onDelete(task)
            },
            onDismiss = { showDialog = false },
            title = "Confirmar eliminación",
            message = "¿Estás seguro de que deseas eliminar esta tarea?"
        )
    }
}
