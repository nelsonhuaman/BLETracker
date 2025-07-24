package com.danp.bletracker.data.repository

import com.danp.bletracker.data.local.ContactoCercanoDao
import com.danp.bletracker.data.mapper.toDomain
import com.danp.bletracker.data.mapper.toEntity
import com.danp.bletracker.domain.model.ContactoCercano
import com.danp.bletracker.domain.repository.LocalContactoRepository
import javax.inject.Inject

class LocalContactoRepositoryImpl @Inject constructor(
    private val dao: ContactoCercanoDao
) : LocalContactoRepository {

    override suspend fun insertarContacto(contacto: ContactoCercano) {
        dao.insertarContacto(contacto.toEntity())
    }

    override suspend fun obtenerNoSincronizados(): List<ContactoCercano> {
        return dao.obtenerNoSincronizados().map { it.toDomain() }
    }

    override suspend fun marcarComoSincronizado(id: String) {
        dao.marcarComoSincronizado(id)
    }

    override suspend fun borrarTodos() {
        dao.borrarTodo()
    }

    override suspend fun insertarOActualizarContacto(contacto: ContactoCercano) {
        val existente = dao.obtenerPorUuidReceptor(contacto.uuidReceptor)

        if (existente == null) {
            dao.insertarContacto(contacto.toEntity())
        } else {
            if (contacto.fuerzaSenal > existente.fuerzaSenal) {
                dao.actualizarContacto(existente.id, contacto.fechaHora, contacto.fuerzaSenal)
            }
        }
    }
}