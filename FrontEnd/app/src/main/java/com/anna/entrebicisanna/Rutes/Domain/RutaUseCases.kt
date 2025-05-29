package com.anna.entrebicisanna.Rutes.Domain

import com.anna.entrebicisanna.Core.Models.PuntsGPS
import com.anna.entrebicisanna.Core.Models.Ruta
import com.anna.entrebicisanna.Core.Models.Usuari
import com.anna.entrebicisanna.Recompenses.Data.RecompensaRepository
import com.anna.entrebicisanna.Rutes.Data.RutaRepository
import retrofit2.Response

class RutaUseCases(private val rutaRepository: RutaRepository) {

    suspend fun iniciarRuta(ruta: Ruta): Response<Ruta> {
        return rutaRepository.iniciarRuta(ruta)
    }

    suspend fun finalitzarRuta(ruta: Ruta): Response<Ruta> {
        return rutaRepository.finalitzarRuta(ruta)
    }

    suspend fun enviarPuntsGPS(punts: List<PuntsGPS>, rutaId: Long): Response<Unit> {
        return rutaRepository.enviarPuntsGPS(punts, rutaId)
    }

    suspend fun getRutesPerUsuari(email: String): List<Ruta> {
        return rutaRepository.getRutesByUsuari(email)
    }

    suspend fun getRutaById(id : Long): Response<Ruta> {
        return rutaRepository.getRutaById(id)
    }
}
