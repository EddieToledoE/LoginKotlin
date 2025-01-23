package com.ramirinter.practicando.ui.register

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.ramirinter.practicando.R
import com.ramirinter.practicando.network.CreateUserRequest
import com.ramirinter.practicando.network.CreateUserResponse
import com.ramirinter.practicando.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@Composable
fun RegisterScreen(navController: NavController) {
    var message by remember { mutableStateOf("") } // Para mostrar el mensaje en pantalla
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
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
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de texto para el nombre de usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de texto para el nombre de usuario
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electronico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                painterResource(id = R.drawable.normal)
                            else
                                painterResource(id = R.drawable.oculto)

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                // Usamos Image en lugar de Icon para evitar el tintado
                                Image(painter = image, contentDescription = "Toggle password visibility", modifier = Modifier.size(24.dp))
                            }
                        }

                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        if (username.isBlank() || password.isBlank() || email.isBlank() || name.isBlank() ) {
                            message = "Por favor, complete todos los campos."
                            return@Button
                        }

                        val api = RetrofitInstance.api
                        val registerRequest = CreateUserRequest(email, password, username, name)

                        api.createUser(registerRequest).enqueue(object : Callback<CreateUserResponse> {
                            override fun onResponse(
                                call: Call<CreateUserResponse>,
                                response: Response<CreateUserResponse>) {
                                if (response.isSuccessful) {
                                    message = response.body()?.message ?: "Usuario creado con éxito, bienvenido ${response.body()?.username}"
                                } else {
                                    message = response.errorBody()?.string() ?: "Error al registrar usuario : ${response.body()} ${response.code()}"
                                }
                            }

                            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                                message = t.localizedMessage ?: "Error desconocido"
                            }
                        })

                    }) {
                        Text(text = "Registrarse")
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

                    Button(onClick = {
                        navController.popBackStack("login", inclusive = false) //
                    }) {
                        Text(text = "Volver al login")
                    }
                }
            }
        }
    )
}