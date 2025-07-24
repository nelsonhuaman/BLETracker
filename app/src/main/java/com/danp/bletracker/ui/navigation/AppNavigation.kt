package com.danp.bletracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danp.bletracker.data.local.UserPreferences
import com.danp.bletracker.ui.screens.login.LoginScreen
import com.danp.bletracker.ui.screens.login.LoginViewModel
import com.danp.bletracker.ui.screens.transmission.TransmisionScreen
import com.danp.bletracker.ui.screens.transmission.TransmisionViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AppNavigation(navController: NavHostController, userPreferences: UserPreferences) {
    // Usa un estado para guardar si ya hay UUID
    val uuidFlow = userPreferences.obtenerUuid  // Flow<String>
    val uuid by uuidFlow.collectAsState(initial = "")

    val startDestination = if (uuid.isNotEmpty()) {
        NavigationRoutes.TRANSMISSION_SCREEN
    } else {
        NavigationRoutes.LOGIN_SCREEN
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationRoutes.LOGIN_SCREEN) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(viewModel = viewModel, navController = navController)
        }
        composable(NavigationRoutes.TRANSMISSION_SCREEN) {
            val viewModel: TransmisionViewModel = hiltViewModel()
            TransmisionScreen(viewModel = viewModel, navController = navController)
        }
    }
}