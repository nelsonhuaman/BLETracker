package com.danp.bletracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsuarioDto(
    @Json(name = "dni") val dni: String,
    @Json(name = "uuid") val uuid: String
)