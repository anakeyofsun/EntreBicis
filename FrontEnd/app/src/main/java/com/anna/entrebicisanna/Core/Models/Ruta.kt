package com.anna.entrebicisanna.Core.Models

data class Ruta(
    val idRuta: Long? = 0L,
    var estat: EstatRutaType,
    var distanciaTotal: Double,
    var tempsTotal: Double,
    var velocitatMitjana: Double,
    var velocitatMaxima: Double,
    var saldoObtingut: Double,
    var validada: Boolean,
    val data: String,
    val puntsGPS: List<PuntsGPS>?,
    val usuari: Usuari
)