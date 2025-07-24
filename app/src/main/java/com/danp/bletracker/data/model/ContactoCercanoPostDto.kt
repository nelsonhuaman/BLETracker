package com.danp.bletracker.data.model

import com.squareup.moshi.Json

data class ContactoCercanoPostDto(
    @Json(name = "uuid_emisor") val uuidEmisor: String,
    @Json(name = "uuid_receptor") val uuidReceptor: String,
    @Json(name = "fecha_hora") val fechaHora: String,
    @Json(name = "fuerza_senal") val fuerzaSenal: Float
)