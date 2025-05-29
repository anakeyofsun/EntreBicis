package com.anna.entrebicisanna.Rutes.UI.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anna.entrebicisanna.Core.Models.Ruta
import com.anna.entrebicisanna.Core.UI.Components.BottomNav
import com.anna.entrebicisanna.Core.UI.Components.TopNav
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Rutes.Data.RutaRepository
import com.anna.entrebicisanna.Rutes.Domain.RutaUseCases
import com.anna.entrebicisanna.Rutes.UI.ViewModel.RutaViewModel
import com.anna.entrebicisanna.Rutes.UI.ViewModel.RutaViewModelFactory
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

@Composable
fun LlistarRutesScreen(
    navController: NavController,
    usuariViewModel: UsuariViewModel
) {
    val rutaViewModel: RutaViewModel = viewModel(
        factory = RutaViewModelFactory(RutaUseCases(RutaRepository()))
    )

    val usuari by usuariViewModel.currentUser.collectAsState()
    val rutes by rutaViewModel.rutesUsuari.collectAsState()

    LaunchedEffect(usuari) {
        usuari?.let {
            rutaViewModel.carregarRutesPerUsuari(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorResource(R.color.groc))
    ) {
        TopNav(navController, usuariViewModel)

        Text(
            text = "Llistat de rutes",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            items(rutes) { ruta ->
                RutaItem(ruta = ruta, onClickDetalls = {
                    // Exemple de navegació passant l'idRuta (assegura't que la ruta existeix a NavGraph)
                   // navController.navigate("detallRuta/${it.idRuta}")
                    navController.navigate("visualitzarRutaScreen/${it.idRuta}")
                })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        BottomNav(navController, usuariViewModel,1)
    }

}

@Composable
fun RutaItem(ruta: Ruta, onClickDetalls: (Ruta) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blau)),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Data destacada a dalt
            Text(
                text = "${ruta.data}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Contingut en columnes
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Distància:")
                    Text("Temps total:")
                    Text("Velocitat mitjana:")
                    Text("Velocitat màxima:")
                    Text("Saldo atorgat:")
                    Text("Estat:")
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("%.2f km".format(ruta.distanciaTotal))
                    Text("%.2f min".format(ruta.tempsTotal))
                    Text("%.2f km/h".format(ruta.velocitatMitjana))
                    Text("%.2f km/h".format(ruta.velocitatMaxima))
                    Text("%.2f punts".format(ruta.saldoObtingut))
                    Text(
                        text = if (ruta.validada) "✅ Validada" else "❌ No validada",
                        color = if (ruta.validada) Color(0xFF4CAF50) else Color(0xFFE91E63),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { onClickDetalls(ruta) },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.rosa)
                    )                ) {
                    Icon(Icons.Default.Info, contentDescription = "Detalls", tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Veure detalls", color = Color.Black)
                }
            }
        }
    }
}
