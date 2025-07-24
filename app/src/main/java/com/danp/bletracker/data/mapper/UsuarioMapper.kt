package com.danp.bletracker.data.mapper

import com.danp.bletracker.data.model.UsuarioDto
import com.danp.bletracker.domain.model.Usuario

fun Usuario.toDto(): UsuarioDto {
    return UsuarioDto(
        dni = dni,
        uuid = uuid
    )
}