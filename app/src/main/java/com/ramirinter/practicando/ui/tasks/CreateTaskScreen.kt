package com.ramirinter.practicando.ui.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramirinter.practicando.R
import com.ramirinter.practicando.network.CreateUserRequest
import com.ramirinter.practicando.network.CreateUserResponse
import com.ramirinter.practicando.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ramirinter.practicando.network.TaskRepository
import com.ramirinter.practicando.network.CreateTaskRequest


@Composable
fun CreateTaskScreen(navController: NavController) {
    var message by remember { mutableStateOf("") } // Para mostrar el mensaje en pantalla
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val taskRepository = TaskRepository() // Instancia del repositorio
    Scaffold(
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
                        text = "¡Bienvenido a la pantalla de Registro!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Campo de texto para el nombre de usuario
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titulo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de texto para el nombre de usuario
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))



                    Button(onClick = {
                        if (title.isBlank() || description.isBlank()  ) {
                            message = "Por favor, complete todos los campos."
                            return@Button
                        }
                        val createTaskRequest = CreateTaskRequest(
                            title = title,
                            description = description,
                        )


                        taskRepository.createTask(createTaskRequest) { createTaskRequest, errorMessage ->
                            if (createTaskRequest != null) {
                                message = "Tarea creada con éxito"
                                navController.popBackStack() // Regresa a la lista de tareas
                            } else {
                                message = "Error al crear la tarea: $errorMessage"
                            }
                        }

                    }) {
                        Text(text = "Crear Tarea")
                    }

                    // Mostrar mensaje en pantalla
                    if (message.isNotBlank()) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.gold),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    )


}