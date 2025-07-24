package com.danp.bletracker.domain.usecase


import com.danp.bletracker.domain.repository.ContactoRepository
import javax.inject.Inject

class SincronizarContactosUseCase @Inject constructor(
    private val repository: ContactoRepository
) {
    suspend operator fun invoke() {
        repository.sincronizarContactos()
    }
}