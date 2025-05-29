package com.anna.entrebicisanna.Core

import com.anna.entrebicisanna.Core.Models.Ruta
import com.anna.entrebicisanna.Recompenses.Data.RecompensaApiRest
import com.anna.entrebicisanna.Rutes.Data.RutaApiRest
import com.anna.entrebicisanna.Usuaris.Data.UsuariApiRest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objecte que proporciona una instància de Retrofit configurada per a les operacions de la API REST d'usuaris.
 */
object RetrofitInstance {
    /**
     * Interceptor per registrar les peticions i respostes HTTP.
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Client HTTP configurat amb l'interceptor de registre.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    /**
     * Instància de Retrofit configurada amb la URL base i el client HTTP.
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://entrebicisanna.hopto.org:8080/") // Canvia per l'IP del teu backend
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /**
     * Instància de l'API d'usuaris creada per Retrofit.
     */
    val api: UsuariApiRest by lazy {
        retrofit.create(UsuariApiRest::class.java)
    }
    val recompensaApi: RecompensaApiRest by lazy {
        retrofit.create(RecompensaApiRest::class.java)
    }
    val rutaApi: RutaApiRest by lazy {
        retrofit.create(RutaApiRest::class.java)
    }
}