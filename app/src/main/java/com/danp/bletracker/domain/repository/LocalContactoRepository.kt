package com.danp.bletracker.domain.repository


import com.danp.bletracker.domain.model.ContactoCercano
import kotlinx.coroutines.flow.Flow

interface LocalContactoRepository {
    suspend fun insertarContacto(contacto: ContactoCercano)
    suspend fun obtenerNoSincronizados(): List<ContactoCercano>
    suspend fun marcarComoSincronizado(id: String)
    suspend fun borrarTodos()
    suspend fun insertarOActualizarContacto(contacto: ContactoCercano)
}