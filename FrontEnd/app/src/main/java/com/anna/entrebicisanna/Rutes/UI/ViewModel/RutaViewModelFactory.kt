package com.anna.entrebicisanna.Rutes.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anna.entrebicisanna.Recompenses.Domain.RecompensaUseCases
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModel
import com.anna.entrebicisanna.Rutes.Domain.RutaUseCases

class RutaViewModelFactory(private val useCases: RutaUseCases) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RutaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RutaViewModel(useCases) as T
        }
        throw IllegalArgumentException("No s'ha pogut inicialitzar el viewModel de ruta,")
    }
}
