package com.anna.entrebicisanna.Rutes.Data

import com.anna.entrebicisanna.Core.Models.PuntsGPS
import com.anna.entrebicisanna.Core.Models.Ruta
import com.anna.entrebicisanna.Core.RetrofitInstance
import retrofit2.Response

class RutaRepository {

    suspend fun iniciarRuta(ruta: Ruta): Response<Ruta> {
        return RetrofitInstance.rutaApi.iniciarRuta(ruta)
    }

    suspend fun finalitzarRuta(ruta: Ruta): Response<Ruta> {
        return RetrofitInstance.rutaApi.finalitzarRuta(ruta)
    }

    suspend fun enviarPuntsGPS(punts: List<PuntsGPS>, rutaId: Long): Response<Unit>{
        return RetrofitInstance.rutaApi.enviarPuntsGPS(rutaId, punts)
    }
    suspend fun getRutesByUsuari(email: String): List<Ruta> {
        return RetrofitInstance.rutaApi.getRutesByUsuari(email)
    }
    suspend fun getRutaById(id: Long): Response<Ruta> {
        return RetrofitInstance.rutaApi.getRutaById(id)
    }
}