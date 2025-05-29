package com.anna.entrebicisanna.Usuaris.Data

import com.anna.entrebicisanna.Core.Models.LoginRequest
import com.anna.entrebicisanna.Core.Models.LoginResponse
import com.anna.entrebicisanna.Core.Models.Usuari
import com.anna.entrebicisanna.Core.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuariRepository {

    /**
     * Verifica les credencials d'inici de sessió.
     *
     * @param email L'email de l'usuari.
     * @param contrasenya La contrasenya.
     * @return La resposta de la API amb la informació de l'usuari.
     */
    suspend fun loginUser(email: String, contrasenya: String): Response<LoginResponse> {
        val request = LoginRequest(email, contrasenya)
        return RetrofitInstance.api.loginUser(request)
    }

    /**
     * Obté un usuari pel seu nom d'usuari.
     *
     * @param email L'email de l'usuari.
     * @return La resposta de la API amb l'usuari obtingut.
     */
    suspend fun getUser(email: String): Response<Usuari> {
        return RetrofitInstance.api.getUser(email)
    }

    suspend fun enviarCorreuRecuperacio(email: String): Response<Unit> {
        return RetrofitInstance.api.recuperarContrasenya(email)
    }
}