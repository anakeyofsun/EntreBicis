package com.anna.entrebicisanna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anna.entrebicisanna.Core.UI.AppNavigation
import com.anna.entrebicisanna.Rutes.UI.Screens.MenuScreen
import com.anna.entrebicisanna.Usuaris.Data.UsuariRepository
import com.anna.entrebicisanna.Usuaris.Domain.UsuariUseCases
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModelFactory
import com.anna.entrebicisanna.ui.theme.EntreBicisAnnaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val usuariCases = UsuariUseCases(UsuariRepository())
            val usuariViewModel: UsuariViewModel =
                viewModel(factory = UsuariViewModelFactory(usuariCases))
            AppNavigation(usuariViewModel)

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EntreBicisAnnaTheme {
        Greeting("Android")
    }
}