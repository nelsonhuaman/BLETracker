package com.danp.bletracker.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danp.bletracker.data.local.UserPreferences
import com.danp.bletracker.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val userPreferences: UserPreferences // nuevo parámetro
) : ViewModel() {

    private val _dni = MutableStateFlow("")
    val dni: StateFlow<String> = _dni

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onDniChanged(newDni: String) {
        _dni.value = newDni
    }

    fun registrarUsuario(onSuccess: () -> Unit) {
        val currentDni = _dni.value.trim()
        if (currentDni.isBlank()) {
            _errorMessage.value = "El DNI no puede estar vacío"
            return
        }

        viewModelScope.launch {
            try {
                repository.obtenerORegistrarUsuario(currentDni, userPreferences)
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrar usuario: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}