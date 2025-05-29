package com.anna.entrebicisanna.Recompenses.UI.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anna.entrebicisanna.Core.UI.Components.BottomNav
import com.anna.entrebicisanna.Core.UI.Components.TopNav
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Recompenses.Data.RecompensaRepository
import com.anna.entrebicisanna.Recompenses.Domain.RecompensaUseCases
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModel
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModelFactory
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel


@Composable
fun LlistarRecompensesUsuariScreen(
    navController: NavController, usuariViewModel: UsuariViewModel
) {
    val recompensaUseCases = RecompensaUseCases(RecompensaRepository())
    val recompensaViewModel: RecompensaViewModel =
        viewModel(factory = RecompensaViewModelFactory(recompensaUseCases))

    val scrollState = rememberScrollState()
    val recompenses by recompensaViewModel.recompensesUsuari.collectAsState()
    val usuari by usuariViewModel.currentUser.collectAsState()
    val error by recompensaViewModel.error.collectAsState()
    val fontMono = FontFamily.Monospace

    LaunchedEffect(usuari) {
        usuari?.email?.let {
            recompensaViewModel.carregarRecompensesPerUsuari(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(scrollState)
            //.padding(16.dp)
    ) {
        TopNav(navController, usuariViewModel)

        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.groc)) // o qualsevol altre color
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Recompenses d'usuari",
                fontFamily = FontFamily.Monospace,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(color = (colorResource(id = R.color.groc)))
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(recompenses) { recompensa ->
                RecompensaCard(recompensa, usuariViewModel) {
                    // aquí pots fer el `navigate` o mostrar un `dialog` per veure detalls
                    navController.navigate("visualitzarRecompensaScreen/${recompensa.idRecompensa}")
                }

            }

            item {
                error?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("⚠️ $it", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(64.dp)) // Espai pel bottom nav
            }
        }

        BottomNav(navController, usuariViewModel, 4) // Posició activa: recompenses
    }
}