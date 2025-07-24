package com.danp.bletracker.data.repository


import android.util.Log
import com.danp.bletracker.data.local.UserPreferences
import com.danp.bletracker.data.model.UsuarioDto
import com.danp.bletracker.data.network.SupabaseService
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class UsuarioRepository @Inject constructor(
    private val api: SupabaseService,
    @Named("supabaseApiKey") private val apiKey: String
) {
    suspend fun registrarUsuario(usuario: UsuarioDto) {
        api.insertarUsuario(apiKey, usuario)
    }

    suspend fun verificarUsuarioPorDni(dni: String): UsuarioDto? {
        val usuarios = api.obtenerUsuarioPorDni(apiKey, "eq.$dni")
        return usuarios.firstOrNull()
    }


    suspend fun obtenerORegistrarUsuario(dni: String, userPreferences: UserPreferences) {
        val usuarioRemoto = verificarUsuarioPorDni(dni)

        if (usuarioRemoto != null) {
            userPreferences.guardarUuid(usuarioRemoto.uuid)
            userPreferences.guardarDni(dni)
        } else {
            val nuevoUuid = UUID.randomUUID().toString()
            registrarUsuario(UsuarioDto(uuid = nuevoUuid, dni = dni))
            userPreferences.guardarUuid(nuevoUuid)
            userPreferences.guardarDni(dni)
        }
    }
}