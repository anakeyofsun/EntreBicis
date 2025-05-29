package com.anna.entrebicisanna.Recompenses.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anna.entrebicisanna.Recompenses.Domain.RecompensaUseCases
import com.anna.entrebicisanna.Usuaris.Domain.UsuariUseCases
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

class RecompensaViewModelFactory(private val useCases: RecompensaUseCases) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecompensaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecompensaViewModel(useCases) as T
        }
        throw IllegalArgumentException("No s'ha pogut inicialitzar el viewModel d'Usuaris,")
    }
}
