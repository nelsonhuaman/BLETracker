package com.danp.bletracker

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.danp.bletracker.data.local.UserPreferences
import com.danp.bletracker.ui.navigation.AppNavigation
import com.danp.bletracker.ui.theme.BLETrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userPreferences: UserPreferences

    companion object {
        private const val REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permisos necesarios para BLE y ubicación
        ActivityCompat.requestPermissions(
            this,
            mutableListOf(
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).apply {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }.toTypedArray(),
            REQUEST_CODE
        )

        setContent {
            BLETrackerTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController, userPreferences = userPreferences)
            }
        }
    }
}