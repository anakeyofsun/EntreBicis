package com.anna.entrebicisanna.Usuaris.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anna.entrebicisanna.Usuaris.Domain.UsuariUseCases


class UsuariViewModelFactory(private val useCases: UsuariUseCases) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuariViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuariViewModel(useCases) as T
        }
        throw IllegalArgumentException("No s'ha pogut inicialitzar el viewModel d'Usuaris,")
    }
}
