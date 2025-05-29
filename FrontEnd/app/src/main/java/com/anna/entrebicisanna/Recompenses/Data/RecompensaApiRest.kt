package com.anna.entrebicisanna.Recompenses.Data

import com.anna.entrebicisanna.Core.Models.Recompensa
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RecompensaApiRest {

    @GET("api/recompensa/usuari/{email}")
    suspend fun getRecompensesPerUsuari(@Path("email") email: String): List<Recompensa>

    @GET("api/recompensa/get/disponibles")
    suspend fun getRecompensesDisponibles(): List<Recompensa>

    @GET("api/recompensa/consultar/{id}")
    suspend fun getRecompensaById(@Path("id") id: String): Response<Recompensa>

    @POST("api/recompensa/reservar/{idRecompensa}/{emailUsuari}")
    suspend fun reservarRecompensa(
        @Path("idRecompensa") idRecompensa: Long,
        @Path("emailUsuari") emailUsuari: String
    ): Response<Recompensa>

    @POST("api/recompensa/recollir/{idRecompensa}")
    suspend fun recollirRecompensa(@Path("idRecompensa") idRecompensa: Long): Response<Recompensa>
}