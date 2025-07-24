package com.danp.bletracker.ui.screens.transmission

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.danp.bletracker.data.local.UserPreferences
import com.danp.bletracker.service.ContactTracingService
import com.danp.bletracker.worker.SincronizarContactosWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransmisionViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    application: Application
) : AndroidViewModel(application) {

    var dni = mutableStateOf("")
        private set

    var isTransmitting = mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            dni.value = userPreferences.obtenerDni.first()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun iniciarServicioBLE(context: Context) {
        val intent = Intent(context, ContactTracingService::class.java)
        context.startForegroundService(intent)
        isTransmitting.value = true
    }

    fun detenerServicioBLE(context: Context) {
        val intent = Intent(context, ContactTracingService::class.java)
        context.stopService(intent)
        isTransmitting.value = false
    }

    suspend fun cerrarSesion() {
        userPreferences.clear()
    }

    fun ejecutarSincronizacionManual(context: Context) {
        val request = OneTimeWorkRequestBuilder<SincronizarContactosWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}