package com.ramirinter.practicando.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramirinter.practicando.R
import com.ramirinter.practicando.network.LoginRequest
import com.ramirinter.practicando.network.LoginResponse
import com.ramirinter.practicando.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoginContent(navController)
            }
        }
    )
}

@Composable
fun LoginContent(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") } // Para mostrar el mensaje en pantalla

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.normal),
            contentDescription = "Login Image",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Hola , Teddy",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        // Campo de texto para el nombre de usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Correo Electronico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de texto para la contraseña con botón de mostrar/ocultar
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

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                // Aquí navegas a la pantalla de registro
                navController.navigate("register")
            }
        )

        // Botón para iniciar sesión
        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    message = "Por favor, complete todos los campos."
                    return@Button
                }

                val api = RetrofitInstance.api
                val loginRequest = LoginRequest(username, password)

                // Llamada a la API
                api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            message = "Inicio de sesión exitoso. Token: ${loginResponse?.token} Bienvenido ${loginResponse?.user?.username}"
                            navController.navigate("home")
                        } else {
                            message = "Error al iniciar sesión: ${response.code()}"
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        message = "Error de red: ${t.message}"
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar sesión", fontSize = 16.sp)
        }

        // Mostrar mensaje en pantalla
        if (message.isNotBlank()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun previewLoginScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoginContent(navController = rememberNavController())
            }
        }
    )
}
