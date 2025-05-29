package com.anna.entrebicisanna.Core.Models

data class Recompensa(

    val idRecompensa: Long,
    val descripcio: String,
    val observacions: String? = null,
    val saldoNecessari: Double,
    val imatgeRecompensa: String,
    var imatgeBase64: String,
    val estatRecompensa: EstatRecompensaType,
    val nomPuntBescanvi: String,
    val adresaPuntBescanvi: String,
    val dataRecollida: String?,
    val usuari: Usuari? = null

)
