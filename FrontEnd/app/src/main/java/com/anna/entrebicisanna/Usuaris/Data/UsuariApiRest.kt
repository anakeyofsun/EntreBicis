package com.anna.entrebicisanna.Usuaris.Data

import com.anna.entrebicisanna.Core.Models.LoginRequest
import com.anna.entrebicisanna.Core.Models.LoginResponse
import com.anna.entrebicisanna.Core.Models.Usuari
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface UsuariApiRest {

    /**
     * Verifica les credencials d'inici de sessió.
     *
     * @param credentials Les credencials d'inici de sessió.
     * @return La resposta de la API amb la informació de l'usuari.
     */
    @POST("api/login/verify")
    suspend fun loginUser(@Body credentials: LoginRequest): Response<LoginResponse>

    /**
     * Obté un usuari pel seu ID.
     *
     * @param email L'email de l'usuari.
     * @return La resposta de la API amb l'usuari obtingut.
     */
    @GET("api/usuari/mostrar/{email}")
    suspend fun getUser(@Path("email") email: String): Response<Usuari>

    @PUT("api/auth/recuperarContrasenya")
    suspend fun recuperarContrasenya(@Query("email") email: String): Response<Unit>


}