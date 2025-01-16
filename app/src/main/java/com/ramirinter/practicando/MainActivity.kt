package com.ramirinter.practicando

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramirinter.practicando.ui.theme.PracticandoTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ramirinter.practicando.network.LoginRequest
import com.ramirinter.practicando.network.LoginResponse
import com.ramirinter.practicando.network.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticandoTheme{
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoginContent()
            }
        }
    )
}

@Composable
fun LoginContent() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
fun LoginScreenPreview() {
    PracticandoTheme {
        LoginScreen()
    }
}