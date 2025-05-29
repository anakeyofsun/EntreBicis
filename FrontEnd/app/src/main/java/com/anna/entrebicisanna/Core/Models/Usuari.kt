package com.anna.entrebicisanna.Core.Models

import java.time.LocalDate


data class Usuari(

    val email: String,
    val telefon: String,
    val nomComplet: String,
    val rol: UsuariType,
    val dataAlta: String,
    val imatgePerfil: String? = null,
    val imatgePerfilBase64: String,
    val saldo: Double,
    val poblacio: String,
    val observacions: String,
    val contrasenya: String,
    val rutes: List<Ruta> = ArrayList(),
    //val recompenses: List<Recompensa> = ArrayList()
)
