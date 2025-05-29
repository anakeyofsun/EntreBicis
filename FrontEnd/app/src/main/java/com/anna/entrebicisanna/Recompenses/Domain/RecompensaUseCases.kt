package com.anna.entrebicisanna.Recompenses.Domain

import com.anna.entrebicisanna.Core.Models.Recompensa
import com.anna.entrebicisanna.Recompenses.Data.RecompensaRepository
import retrofit2.Response

class RecompensaUseCases(private val repository: RecompensaRepository) {
    suspend fun getRecompensaPerUsuari(email: String): List<Recompensa> {
        return repository.getRecompensesPerUsuari(email)
    }
    suspend fun getRecompensesDisponibles(): List<Recompensa> {
        return repository.getRecompensesDisponibles()
    }
    suspend fun getRecompensaById(id: String): Response<Recompensa> {
        return repository.getRecompensaById(id)
    }
    suspend fun reservarRecompensa(idRecompensa: Long, emailUsuari: String): Response<Recompensa> {
        return repository.reservarRecompensa(idRecompensa, emailUsuari)
    }
    suspend fun recollirRecompensa(idRecompensa: Long): Response<Recompensa> {
        return repository.recollirRecompensa(idRecompensa)
    }
}