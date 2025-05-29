package com.anna.entrebicisanna.Rutes.UI.ViewModel


import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anna.entrebicisanna.Core.Models.EstatRutaType
import com.anna.entrebicisanna.Core.Models.PuntsGPS
import com.anna.entrebicisanna.Core.Models.Recompensa
import com.anna.entrebicisanna.Core.Models.Ruta
import com.anna.entrebicisanna.Core.Models.Usuari
import com.anna.entrebicisanna.Rutes.Domain.RutaUseCases
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar la lògica de presentació relacionada amb les rutes.
 *
 * Permet iniciar, finalitzar i consultar rutes, així com gestionar els punts GPS
 * capturats durant la ruta. També calcula mètriques com la distància, velocitat
 * mitjana i velocitat màxima.
 *
 * Utilitza `StateFlow` per exposar l'estat reactiu de la ruta activa, les rutes
 * de l’usuari, errors i estat de desament.
 *
 * @param useCases Casos d'ús relacionats amb rutes.
 */
class RutaViewModel(private val useCases: RutaUseCases) : ViewModel() {

    private val _puntsGPS = MutableStateFlow<List<PuntsGPS>>(emptyList())
    val puntsGPS: StateFlow<List<PuntsGPS>> = _puntsGPS

    private val _estatDesarRuta = MutableStateFlow<Result<Ruta>?>(null)
    val estatDesarRuta: StateFlow<Result<Ruta>?> = _estatDesarRuta

    private val _rutaActiva = MutableStateFlow(false)
    val rutaActiva: StateFlow<Boolean> = _rutaActiva

    private val _ruta = MutableStateFlow<Ruta?>(null)
    val ruta: StateFlow<Ruta?> = _ruta.asStateFlow()

    private val _rutesUsuari = MutableStateFlow<List<Ruta>>(emptyList())
    val rutesUsuari: StateFlow<List<Ruta>> = _rutesUsuari.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _rutaById = MutableStateFlow<Ruta?>(null)
    val rutaById: StateFlow<Ruta?> = _rutaById

    /**
     * Inicia una nova ruta associada a l'usuari.
     */
    fun iniciarRuta(usuari: Usuari) {
        viewModelScope.launch {
            val novaRuta = Ruta(
                idRuta = null,
                estat = EstatRutaType.PENDENT,
                distanciaTotal = 0.0,
                velocitatMitjana = 0.0,
                velocitatMaxima = 0.0,
                tempsTotal = 0.0,
                saldoObtingut = 0.0,
                validada = false,
                data = "", // O pots posar la data actual si vols
                puntsGPS = null,
                usuari = usuari
            )
            val response = useCases.iniciarRuta(novaRuta)
            if (response.isSuccessful) {
                _ruta.value = response.body()
                _rutaActiva.value = true
            }
        }
    }

    /**
     * Finalitza la ruta actual, calcula les dades i desa al backend.
     */
    fun finalitzarRuta() {
        viewModelScope.launch {
            val rutaActual = _ruta.value
            val punts = _puntsGPS.value

            if (rutaActual != null && punts.isNotEmpty()) {
                // Calcular distància i velocitats
                val distanciaMetres = calculaDistanciaTotal(punts)
                val distanciaKm = distanciaMetres / 1000.0

                val velocitatMitjana = calculaVelocitatMitjana(punts)
                val velocitatMaxima = calculaVelocitatMaxima(punts)

                val tempsTotalSegons = (punts.last().temps - punts.first().temps) / 1000.0
                val tempsTotalMinuts = tempsTotalSegons / 60.0

                // Actualitzar la ruta
                rutaActual.estat = EstatRutaType.FINALITZADA
                rutaActual.distanciaTotal = distanciaKm         // ✔️ Ara en KM
                rutaActual.tempsTotal = tempsTotalMinuts        // ✔️ Ara en MINUTS
                rutaActual.velocitatMitjana = velocitatMitjana
                rutaActual.velocitatMaxima = velocitatMaxima
                rutaActual.saldoObtingut = distanciaKm          // ✔️ El saldo també en KM
                rutaActual.validada = false

                // Finalitzar ruta al servidor
                val respostaFinalitzar = useCases.finalitzarRuta(rutaActual)
                if (respostaFinalitzar.isSuccessful) {
                    _estatDesarRuta.value = Result.success(respostaFinalitzar.body()!!)
                } else {
                    _estatDesarRuta.value = Result.failure(Exception("Error finalitzant la ruta"))
                }

                // Guardar punts GPS
                rutaActual.idRuta?.let { idRuta ->
                    val respostaPunts = useCases.enviarPuntsGPS(punts, idRuta)
                    if (!respostaPunts.isSuccessful) {
                        // Aquí pots afegir log d'error si vols
                    }
                }

                // Reiniciar estat
                _rutaActiva.value = false
                _puntsGPS.value = emptyList()
                _ruta.value = null
            }
        }
    }

    /**
     * Calcula la distància total entre els punts GPS.
     */
    private fun calculaDistanciaTotal(punts: List<PuntsGPS>): Double {
        var distancia = 0.0
        for (i in 1 until punts.size) {
            val a = punts[i - 1]
            val b = punts[i]
            val results = FloatArray(1)
            Location.distanceBetween(a.latitud, a.longitud, b.latitud, b.longitud, results)
            distancia += results[0]
        }
        return distancia
    }

    /**
     * Calcula la velocitat mitjana de la ruta.
     */
    private fun calculaVelocitatMitjana(punts: List<PuntsGPS>): Double {
        if (punts.size < 2) return 0.0

        var distanciaTotal = 0.0
        var tempsTotalSegons = (punts.last().temps - punts.first().temps) / 1000.0

        for (i in 1 until punts.size) {
            val a = punts[i - 1]
            val b = punts[i]
            val results = FloatArray(1)
            Location.distanceBetween(a.latitud, a.longitud, b.latitud, b.longitud, results)
            distanciaTotal += results[0]
        }

        return if (tempsTotalSegons > 0) {
            (distanciaTotal / 1000.0) / (tempsTotalSegons / 3600.0) // km/h
        } else {
            0.0
        }
    }

    /**
     * Calcula la velocitat màxima entre dos punts GPS consecutius.
     */
    private fun calculaVelocitatMaxima(punts: List<PuntsGPS>): Double {
        var velocitatMaxima = 0.0
        for (i in 1 until punts.size) {
            val a = punts[i - 1]
            val b = punts[i]
            val results = FloatArray(1)
            Location.distanceBetween(a.latitud, a.longitud, b.latitud, b.longitud, results)
            val distanciaMetres = results[0]
            val tempsSegons = (b.temps - a.temps) / 1000.0
            if (tempsSegons > 0) {
                val velocitat = (distanciaMetres / tempsSegons) * 3.6 // m/s -> km/h
                if (velocitat > velocitatMaxima) {
                    velocitatMaxima = velocitat
                }
            }
        }
        return velocitatMaxima
    }

    /**
     * Reinicia la llista de punts GPS.
     */
    fun reiniciarRuta() {
        viewModelScope.launch {
            _puntsGPS.value = emptyList()
        }
    }

    /**
     * Afegeix un nou punt GPS a la llista.
     */
    fun afegirPunt(punt: PuntsGPS) {
        _puntsGPS.value = _puntsGPS.value + punt
    }

    /**
     * Carrega totes les rutes associades a l’usuari actual i actualitza l’estat intern.
     *
     * @param usuari Objecte Usuari amb l’email necessari per fer la petició.
     */
    fun carregarRutesPerUsuari(usuari: Usuari) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _rutesUsuari.value = useCases.getRutesPerUsuari(usuari.email)
            } catch (e: Exception) {
                _error.value = "Error en carregar les rutes: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Obté una ruta concreta pel seu ID i actualitza l’estat corresponent.
     *
     * @param id ID de la ruta a consultar
     */
    fun getRutaById(id: Long) {
        viewModelScope.launch {
            val response = useCases.getRutaById(id)
            if (response.isSuccessful) {
                _rutaById.value = response.body()
            }
        }
    }
}
