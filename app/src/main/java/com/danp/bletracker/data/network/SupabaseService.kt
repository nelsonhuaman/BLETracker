package com.danp.bletracker.data.network

import com.danp.bletracker.data.model.ContactoCercanoDto
import com.danp.bletracker.data.model.ContactoCercanoPostDto
import com.danp.bletracker.data.model.UsuarioDto
import retrofit2.http.*

interface SupabaseService {

    @POST("rest/v1/contactos_cercanos")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun insertarContactoCercano(
        @Header("apikey") apiKey: String,
        @Body contacto: ContactoCercanoPostDto
    )

    @POST("rest/v1/usuarios")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun insertarUsuario(
        @Header("apikey") apiKey: String,
        @Body usuario: UsuarioDto
    )

    @GET("rest/v1/usuarios")
    @Headers("Content-Type: application/json")
    suspend fun obtenerUsuarioPorDni(
        @Header("apikey") apiKey: String,
        @Query("dni") dniFiltro: String, // NO pongas default aquí
        @Query("select") select: String = "*"
    ): List<UsuarioDto>
}