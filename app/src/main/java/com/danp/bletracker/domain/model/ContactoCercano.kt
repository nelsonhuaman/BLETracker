package com.danp.bletracker.domain.model

data class ContactoCercano(
    val id: String,
    val uuidEmisor: String,
    val uuidReceptor: String,
    val fechaHora: Long,
    val fuerzaSenal: Float,
    val sincronizado: Boolean = false
)