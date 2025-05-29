package com.anna.entrebicisanna.Rutes.Data

import com.anna.entrebicisanna.Core.Models.PuntsGPS
import com.anna.entrebicisanna.Core.Models.Ruta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RutaApiRest {

    @POST("api/ruta/iniciar")
    suspend fun iniciarRuta(@Body ruta: Ruta): Response<Ruta>

    @PUT("api/ruta/finalitzar")
    suspend fun finalitzarRuta(@Body ruta: Ruta): Response<Ruta>

    @POST("api/ruta/guardarPunts/{rutaId}/punts")
    suspend fun enviarPuntsGPS(@Path("rutaId") rutaId: Long, @Body punts: List<PuntsGPS>): Response<Unit>

    @GET("api/ruta/llistar/{email}")
    suspend fun getRutesByUsuari(@Path("email") email: String): List<Ruta>

    @GET("api/ruta/consulta/{idRuta}")
    suspend fun getRutaById(@Path("idRuta") id: Long):  Response<Ruta>
}

