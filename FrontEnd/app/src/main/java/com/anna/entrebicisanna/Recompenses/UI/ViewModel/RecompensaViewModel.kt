package com.anna.entrebicisanna.Recompenses.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anna.entrebicisanna.Core.Models.Recompensa
import com.anna.entrebicisanna.Recompenses.Domain.RecompensaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar la lògica de presentació relacionada amb les recompenses.
 *
 * S'encarrega de recuperar recompenses disponibles, recompenses d'un usuari,
 * consultar una recompensa per ID, reservar i recollir recompenses.
 *
 * Utilitza `StateFlow` per exposar l'estat actual de la UI de manera reactiva.
 *
 * @param useCases Casos d'ús relacionats amb recompenses.
 */
class RecompensaViewModel(private val useCases: RecompensaUseCases) : ViewModel() {

    private val _recompensesDisponibles = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensesDisponibles: StateFlow<List<Recompensa>> get() = _recompensesDisponibles

    private val _recompensesUsuari = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensesUsuari: StateFlow<List<Recompensa>> get() = _recompensesUsuari

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _recompensaById = MutableStateFlow<Recompensa?>(null)
    val recompensaById: StateFlow<Recompensa?> = _recompensaById

    private val _reservaExitosa = MutableStateFlow<Boolean?>(null)
    val reservaExitosa: StateFlow<Boolean?> = _reservaExitosa

    private val _recollidaExitosa = MutableStateFlow<Boolean?>(null)
    val recollidaExitosa: StateFlow<Boolean?> = _recollidaExitosa

    /**
     * Obté totes les recompenses disponibles.
     */
    fun carregarRecompensesDisponibles() {
        viewModelScope.launch {
            try {
                _recompensesDisponibles.value = useCases.getRecompensesDisponibles()
            } catch (e: Exception) {
                _error.value = "Error carregant recompenses disponibles: ${e.message}"
            }
        }
    }

    /**
     * Obté les recompenses reservades per un usuari concret.
     *
     * @param email Email de l’usuari
     */
    fun carregarRecompensesPerUsuari(email: String) {
        viewModelScope.launch {
            try {
                _recompensesUsuari.value = useCases.getRecompensaPerUsuari(email)
            } catch (e: Exception) {
                _error.value = "Error carregant recompenses de l'usuari: ${e.message}"
            }
        }
    }

    /**
     * Consulta una recompensa concreta pel seu identificador i actualitza l’estat
     * amb el resultat rebut del servidor.
     *
     * @param id Identificador de la recompensa
     */
    fun getRecompensaById(id: String) {
        viewModelScope.launch {
            val response = useCases.getRecompensaById(id)
            if (response.isSuccessful) {
                _recompensaById.value = response.body()
            }
        }
    }

    /**
     * Reserva una recompensa per a un usuari
     *
     * @param idRecompensa ID de la recompensa
     * @param emailUsuari Email de l'usuari
     */
    fun reservarRecompensa(idRecompensa: Long, emailUsuari: String) {
        viewModelScope.launch {
            try {
                val response = useCases.reservarRecompensa(idRecompensa, emailUsuari)
                if (response.isSuccessful) {
                    _reservaExitosa.value = true
                    _recompensaById.value = response.body()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconegut"
                    _reservaExitosa.value = false
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                _reservaExitosa.value = false
                _error.value = "Error de connexió: ${e.message}"
            }
        }
    }

    /**
     * Recull una recompensa prèviament reservada.
     *
     * Actualitza l’estat de la recompensa i l’indicador de recollida si la petició
     * al servidor és satisfactòria.
     *
     * @param idRecompensa ID de la recompensa a recollir
     */
    fun recollirRecompensa(idRecompensa: Long) {
        viewModelScope.launch {
            try {
                val response = useCases.recollirRecompensa(idRecompensa)
                if (response.isSuccessful) {
                    _recollidaExitosa.value = true
                    _recompensaById.value = response.body()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconegut"
                    _recollidaExitosa.value = false
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                _recollidaExitosa.value = false
                _error.value = "Error de connexió: ${e.message}"
            }
        }
    }

    /**
     * Reinicia l’error actual, netejant l’estat d’error exposat a la UI.
     */
    fun resetError() {
        _error.value = null
    }
}