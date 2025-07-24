package com.danp.bletracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactoCercanoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarContacto(contacto: ContactoCercanoEntity)

    @Query("SELECT * FROM contactos_cercanos WHERE uuid_receptor = :uuidReceptor LIMIT 1")
    suspend fun obtenerPorUuidReceptor(uuidReceptor: String): ContactoCercanoEntity?

    @Query("UPDATE contactos_cercanos SET fecha_hora = :fechaHora, fuerza_senal = :fuerzaSenal WHERE id = :id")
    suspend fun actualizarContacto(id: String, fechaHora: Long, fuerzaSenal: Float)

    @Query("SELECT * FROM contactos_cercanos WHERE sincronizado = 0")
    suspend fun obtenerNoSincronizados(): List<ContactoCercanoEntity>

    @Query("UPDATE contactos_cercanos SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarComoSincronizado(id: String)

    @Query("DELETE FROM contactos_cercanos")
    suspend fun borrarTodo()
}