package com.anna.entrebicisanna.Usuaris.Domain

import com.anna.entrebicisanna.Usuaris.Data.UsuariRepository
import retrofit2.Response

class UsuariUseCases(private val repository: UsuariRepository) {

    /**
     * Verifica les credencials d'inici de sessió.
     *
     * @param email L'email de l'usuari.
     * @param contrasenya La contrasenya.
     * @return La resposta de la API amb la informació de l'usuari.
     */
    suspend fun loginUser(email: String, contrasenya: String) = repository.loginUser(email, contrasenya)

    /**
     * Obté un usuari pel seu nom d'usuari.
     *
     * @param email L'email de l'usuari.
     * @return La resposta de la API amb l'usuari obtingut.
     */
    suspend fun getUser(email: String) = repository.getUser(email)

    suspend fun enviarCorreuRecuperacio(email: String) = repository.enviarCorreuRecuperacio(email)


}