package com.danp.bletracker.data.mapper

import com.danp.bletracker.data.local.ContactoCercanoEntity
import com.danp.bletracker.data.model.ContactoCercanoDto
import com.danp.bletracker.data.model.ContactoCercanoPostDto
import com.danp.bletracker.domain.model.ContactoCercano
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Entity → Domain
fun ContactoCercanoEntity.toDomain(): ContactoCercano {
    return ContactoCercano(
        id = id,
        uuidEmisor = uuidEmisor,
        uuidReceptor = uuidReceptor,
        fechaHora = fechaHora,
        fuerzaSenal = fuerzaSenal,
        sincronizado = sincronizado
    )
}

// Domain → Entity
fun ContactoCercano.toEntity(): ContactoCercanoEntity {
    return ContactoCercanoEntity(
        id = id,
        uuidEmisor = uuidEmisor,
        uuidReceptor = uuidReceptor,
        fechaHora = fechaHora,
        fuerzaSenal = fuerzaSenal,
        sincronizado = sincronizado
    )
}

// Domain → Dto (para enviar al backend)
fun ContactoCercano.toDto(): ContactoCercanoDto {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val fechaFormateada = formatter.format(Date(fechaHora))  // fechaHora es Long

    return ContactoCercanoDto(
        id = id,
        uuidEmisor = uuidEmisor,
        uuidReceptor = uuidReceptor,
        fechaHora = fechaFormateada,
        fuerzaSenal = fuerzaSenal,
        sincronizado = sincronizado
    )

}
fun ContactoCercano.toPostDto(): ContactoCercanoPostDto {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val fechaFormateada = formatter.format(Date(fechaHora))
    return ContactoCercanoPostDto(
        uuidEmisor = uuidEmisor,
        uuidReceptor = uuidReceptor,
        fechaHora = fechaFormateada,
        fuerzaSenal = fuerzaSenal
    )
}
