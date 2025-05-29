package com.anna.entrebicisanna.Rutes.UI.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anna.entrebicisanna.Core.Models.PuntsGPS
import com.anna.entrebicisanna.Core.Models.Ruta
import com.anna.entrebicisanna.Core.UI.Components.BottomNav
import com.anna.entrebicisanna.Core.UI.Components.TopNav
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Recompenses.Data.RecompensaRepository
import com.anna.entrebicisanna.Recompenses.Domain.RecompensaUseCases
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModel
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModelFactory
import com.anna.entrebicisanna.Rutes.Data.RutaRepository
import com.anna.entrebicisanna.Rutes.Domain.RutaUseCases
import com.anna.entrebicisanna.Rutes.UI.ViewModel.RutaViewModel
import com.anna.entrebicisanna.Rutes.UI.ViewModel.RutaViewModelFactory
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun VisualitzarRutaScreen(
    navController: NavController,
    usuariViewModel: UsuariViewModel
) {
    val usuari by usuariViewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val rutaId = navController.currentBackStackEntry?.arguments?.getString("rutaId")?.toLongOrNull()
    val rutaViewModel: RutaViewModel = viewModel(factory = RutaViewModelFactory(RutaUseCases(RutaRepository())))
    val ruta by rutaViewModel.rutaById.collectAsState()

    LaunchedEffect(rutaId) {
        rutaId?.let { rutaViewModel.getRutaById(it) }
    }

    Scaffold(
        containerColor = colorResource(R.color.groc),
        topBar = { TopNav(navController, usuariViewModel) },
        bottomBar = { BottomNav(navController, usuariViewModel) }
    ) { paddingValues ->
        ruta?.let { ruta ->
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    "Detalls de la ruta",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blau)),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Data: ${ruta.data}", fontWeight = FontWeight.Bold)
                        Text("Distància recorreguda: %.2f km".format(ruta.distanciaTotal))
                        Text("Temps total: %.2f min".format(ruta.tempsTotal))
                        Text("Velocitat màxima: %.2f km/h".format(ruta.velocitatMaxima))
                        Text("Velocitat mitjana: %.2f km/h".format(ruta.velocitatMitjana))
                        Text("Saldo obtingut: %.2f punts".format(ruta.saldoObtingut))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Mapa de la ruta",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                MapaRuta(punts = ruta.puntsGPS ?: emptyList())
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun MapaRuta(punts: List<PuntsGPS>) {
    val context = LocalContext.current

    val coordenades = punts.map { LatLng(it.latitud, it.longitud) }
    val cameraPosition = rememberCameraPositionState()

    LaunchedEffect(coordenades) {
        if (coordenades.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            coordenades.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            cameraPosition.move(
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) {
            coordenades.forEach {
                Marker(
                    state = MarkerState(position = it),
                    title = "Punt GPS",
                    snippet = "Lat: ${it.latitude}, Lng: ${it.longitude}"
                )
            }

            if (coordenades.size >= 2) {
                Polyline(
                    points = coordenades,
                    color = Color.Red,
                    width = 8f
                )
            }
        }
    }
}