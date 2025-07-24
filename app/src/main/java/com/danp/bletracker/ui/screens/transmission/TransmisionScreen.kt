package com.danp.bletracker.ui.screens.transmission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransmisionScreen(
    viewModel: TransmisionViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as Activity
    val isTransmitting by viewModel.isTransmitting
    val dni by viewModel.dni
    val scope = rememberCoroutineScope()

    // Definir permisos necesarios según la versión de Android
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_ADVERTISE,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        listOf(
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    var permissionsGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionsGranted = result.all { it.value }
        if (permissionsGranted) {
            viewModel.iniciarServicioBLE(context)
        }
    }

    LaunchedEffect(Unit) {
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        permissionsGranted = allGranted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Modo transmisión BLE", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (dni.isNotEmpty()) {
            Text("DNI: $dni", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isTransmitting) {
            Text("🔴 Transmitiendo datos...", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (permissionsGranted) {
                    viewModel.iniciarServicioBLE(context)
                } else {
                    permissionLauncher.launch(permissions.toTypedArray())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Transmisión + Escaneo")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.detenerServicioBLE(context) // << CORREGIDO
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Detener")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        viewModel.cerrarSesion()
                        navController.navigate("login") {
                            popUpTo("transmission") { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión", color = MaterialTheme.colorScheme.onError)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.ejecutarSincronizacionManual(context)
                Toast.makeText(context, "Sincronización manual iniciada", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Probar sincronización (subir al backend)")
        }
    }
}
