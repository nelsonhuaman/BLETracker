package com.danp.bletracker.ui.screens.login


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.danp.bletracker.ui.navigation.NavigationRoutes

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    val dni by viewModel.dni.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ingrese su DNI", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dni,
            onValueChange = {
                viewModel.onDniChanged(it)
                viewModel.clearError()
            },
            label = { Text("DNI") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.registrarUsuario {
                    navController.navigate(NavigationRoutes.TRANSMISSION_SCREEN) {
                        popUpTo(NavigationRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                }
            }
        ) {
            Text("Continuar")
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}