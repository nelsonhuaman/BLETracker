package com.danp.bletracker.data.repository

import com.danp.bletracker.data.mapper.toDto
import com.danp.bletracker.data.mapper.toPostDto
import com.danp.bletracker.data.model.ContactoCercanoDto
import com.danp.bletracker.data.model.ContactoCercanoPostDto
import com.danp.bletracker.data.network.SupabaseService
import com.danp.bletracker.domain.repository.ContactoRepository
import com.danp.bletracker.domain.repository.LocalContactoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ContactoRepositoryImpl @Inject constructor(
    private val localRepository: LocalContactoRepository,
    private val api: SupabaseService,
    @Named("supabaseApiKey") private val apiKey: String
) : ContactoRepository {

    override suspend fun sincronizarContactos() = withContext(Dispatchers.IO) {
        val noSincronizados = localRepository.obtenerNoSincronizados()

        noSincronizados.forEach { contacto ->
            try {
                val dto = contacto.toPostDto()
                api.insertarContactoCercano(apiKey, dto)
                localRepository.marcarComoSincronizado(contacto.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}