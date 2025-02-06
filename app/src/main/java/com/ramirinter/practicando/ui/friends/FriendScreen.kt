package com.ramirinter.practicando.ui.friends

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramirinter.practicando.R
import com.ramirinter.practicando.network.RetrofitInstance
import com.ramirinter.practicando.network.SearchFriendResponse
import com.ramirinter.practicando.ui.bottombar.BottomBarComponent
import com.ramirinter.practicando.ui.camera.CameraScreen
import com.ramirinter.practicando.ui.topbar.ToolbarComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun FriendScreen(navController: NavController) {
    var usernametofind by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showCamera by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showCamera = true
        } else {
            message = "Se requiere permiso de cámara para esta funcionalidad"
        }
    }

    Scaffold(
        topBar = { ToolbarComponent() },
        bottomBar = { BottomBarComponent(navController) },
        floatingActionButton = {
            if (!showCamera) {
                FloatingActionButton(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Abrir cámara",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (showCamera) {
                CameraScreen(
                    onCloseCamera = {
                        showCamera = false
                    },
                    onImageCaptured = { uri ->
                        capturedImageUri = uri
                        showCamera = false
                        message = "Imagen capturada exitosamente"
                    }
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Busca a tus amigos por su nombre de usuario",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = usernametofind,
                        onValueChange = { usernametofind = it },
                        label = { Text("Nombre de usuario") },
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        onClick = {
                            if (usernametofind.isBlank()) {
                                message = "Por favor, Ingresa un nombre de usuario para realizar la búsqueda."
                                return@Button
                            }

                            val api = RetrofitInstance.api
                            api.searchFriend(usernametofind).enqueue(object :
                                Callback<List<SearchFriendResponse>> {
                                override fun onResponse(
                                    call: Call<List<SearchFriendResponse>>,
                                    response: Response<List<SearchFriendResponse>>
                                ) {
                                    if (response.isSuccessful) {
                                        val searchFriendResponse = response.body()
                                        if (!searchFriendResponse.isNullOrEmpty()) {
                                            message = "Usuario encontrado: ${searchFriendResponse[0].username}"
                                        } else {
                                            message = "Usuario no encontrado"
                                        }
                                    } else {
                                        message = "Usuario no encontrado"
                                    }
                                }

                                override fun onFailure(
                                    call: Call<List<SearchFriendResponse>>,
                                    t: Throwable
                                ) {
                                    message = "Error al buscar usuario ${t.message}"
                                }
                            })
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Buscar")
                        Text("Buscar")
                    }

                    if (message.isNotBlank()) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.gold),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}