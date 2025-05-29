package com.anna.entrebicisanna.Recompenses.Data

import com.anna.entrebicisanna.Core.Models.Recompensa
import com.anna.entrebicisanna.Core.RetrofitInstance
import retrofit2.Response

class RecompensaRepository {
    suspend fun getRecompensesPerUsuari(email: String): List<Recompensa> {
        return RetrofitInstance.recompensaApi.getRecompensesPerUsuari(email)
    }
    suspend fun getRecompensesDisponibles(): List<Recompensa> {
        return RetrofitInstance.recompensaApi.getRecompensesDisponibles()
    }

    suspend fun getRecompensaById(id: String): Response<Recompensa> {
        return RetrofitInstance.recompensaApi.getRecompensaById(id)
    }
     suspend fun reservarRecompensa(idRecompensa: Long, emailUsuari: String): Response<Recompensa> {
        return RetrofitInstance.recompensaApi.reservarRecompensa(idRecompensa, emailUsuari)
    }
    suspend fun recollirRecompensa(idRecompensa: Long): Response<Recompensa> {
        return RetrofitInstance.recompensaApi.recollirRecompensa(idRecompensa)
    }
}