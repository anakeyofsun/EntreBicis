package com.anna.entrebicisanna.Rutes.UI.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anna.entrebicisanna.Core.Models.PuntsGPS
import com.anna.entrebicisanna.Core.Models.Usuari
import com.anna.entrebicisanna.Core.UI.Components.BottomNav
import com.anna.entrebicisanna.Core.UI.Components.TopNav
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Rutes.Data.RutaRepository
import com.anna.entrebicisanna.Rutes.Domain.RutaUseCases
import com.anna.entrebicisanna.Rutes.UI.ViewModel.RutaViewModel
import com.anna.entrebicisanna.Rutes.UI.ViewModel.RutaViewModelFactory
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MenuScreen(navController: NavController, usuariViewModel: UsuariViewModel) {

    val rutaViewModel: RutaViewModel = viewModel(
        factory = RutaViewModelFactory(RutaUseCases(RutaRepository()))
    )

    val context = LocalContext.current


    var tePermis by remember { mutableStateOf(false) }
    val rutaActiva by rutaViewModel.rutaActiva.collectAsState()
    val usuari by usuariViewModel.currentUser.collectAsState()


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                if (rutaViewModel.rutaActiva.value) {
                    rutaViewModel.finalitzarRuta()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            tePermis = isGranted
        }

    LaunchedEffect(Unit) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            tePermis = true
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorResource(R.color.groc)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopNav(navController, usuariViewModel)

        MapView(rutaActiva, rutaViewModel, tePermis)

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!rutaActiva) {
                BotoEstat("Iniciar Ruta", Color(0xFF4A90E2), rutaViewModel, usuari)
            } else {
                BotoEstat("Finalitzar Ruta", Color(0xFFE94E4E), rutaViewModel, usuari)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNav(navController, usuariViewModel)
    }
}

@Composable
fun BotoEstat(
    text: String,
    color: Color,
    rutaViewModel: RutaViewModel,
    usuari: Usuari?
) {
    Button(
        onClick = {
            if (rutaViewModel.rutaActiva.value) {
                rutaViewModel.finalitzarRuta()
            } else {
                usuari?.let { rutaViewModel.iniciarRuta(it) }
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Text(text = text, fontSize = 18.sp, color = Color.White)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapView(rutaActiva: Boolean, rutaViewModel: RutaViewModel, hasLocationPermission: Boolean) {
    val context = LocalContext.current

    var ubicacioActual by remember { mutableStateOf<LatLng?>(null) }
    var ubicacioAnterior by remember { mutableStateOf<LatLng?>(null) }
    val puntsRuta = remember { mutableStateListOf<LatLng>() }
    val clientLocalitzacio = LocationServices.getFusedLocationProviderClient(context)

    val callbackLocalitzacio = remember {
        object : LocationCallback() {
            override fun onLocationResult(resultat: LocationResult) {
                resultat.lastLocation?.let { location ->
                    val novaUbicacio = LatLng(location.latitude, location.longitude)
                    ubicacioActual = novaUbicacio

                    if (ubicacioAnterior == null || distanceBetween(ubicacioAnterior!!, novaUbicacio) > 1f) {
                        ubicacioAnterior = novaUbicacio
                        puntsRuta.add(novaUbicacio)
                        rutaViewModel.afegirPunt(
                            PuntsGPS(
                                idPuntsGPS = 0L,
                                latitud = novaUbicacio.latitude,
                                longitud = novaUbicacio.longitude,
                                temps = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

    // Aquest és el canvi important: només escoltem ubicació si la ruta està activa
    LaunchedEffect(rutaActiva) {
        if (rutaActiva) {
            val peticioLocalitzacio = LocationRequest.create().apply {
                interval = 1000
                fastestInterval = 500
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            clientLocalitzacio.requestLocationUpdates(
                peticioLocalitzacio,
                callbackLocalitzacio,
                Looper.getMainLooper()
            )
        } else {
            clientLocalitzacio.removeLocationUpdates(callbackLocalitzacio)
        }
    }

    val estatCamera = rememberCameraPositionState {
        position = ubicacioActual?.let {
            CameraPosition.fromLatLngZoom(it, 19f)
        } ?: CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 1f)
    }

    LaunchedEffect(ubicacioActual) {
        ubicacioActual?.let {
            estatCamera.animate(update = CameraUpdateFactory.newLatLngZoom(it, 19f))
        }
    }

    LaunchedEffect(rutaActiva) {
        puntsRuta.clear()
        if (!rutaActiva) {
            ubicacioAnterior = null
            ubicacioActual = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = estatCamera,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = false)
        ) {
            if (rutaActiva) {
                Polyline(
                    points = puntsRuta.toList(),
                    color = androidx.compose.ui.graphics.Color.Red,
                    width = 8f
                )
            }
        }
    }
}

// Funció auxiliar per calcular la distància entre dues coordenades
fun distanceBetween(a: LatLng, b: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
    return results[0]
}