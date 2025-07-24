package com.danp.bletracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactos_cercanos")
data class ContactoCercanoEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "uuid_emisor") val uuidEmisor: String,
    @ColumnInfo(name = "uuid_receptor") val uuidReceptor: String,
    @ColumnInfo(name = "fecha_hora") val fechaHora: Long,
    @ColumnInfo(name = "fuerza_senal") val fuerzaSenal: Float,
    @ColumnInfo(name = "sincronizado") val sincronizado: Boolean = false
)