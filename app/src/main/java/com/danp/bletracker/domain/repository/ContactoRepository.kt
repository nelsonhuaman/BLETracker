package com.danp.bletracker.domain.repository

interface ContactoRepository {
    suspend fun sincronizarContactos()
}