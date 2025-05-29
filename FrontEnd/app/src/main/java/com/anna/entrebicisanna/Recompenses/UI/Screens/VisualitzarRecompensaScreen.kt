package com.anna.entrebicisanna.Recompenses.UI.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anna.entrebicisanna.Core.Models.EstatRecompensaType
import com.anna.entrebicisanna.Core.Models.Recompensa
import com.anna.entrebicisanna.Core.Models.Usuari
import com.anna.entrebicisanna.Core.UI.Components.BottomNav
import com.anna.entrebicisanna.Core.UI.Components.TopNav
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Recompenses.Data.RecompensaRepository
import com.anna.entrebicisanna.Recompenses.Domain.RecompensaUseCases
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModel
import com.anna.entrebicisanna.Recompenses.UI.ViewModel.RecompensaViewModelFactory
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

@Composable
fun VisualitzarRecompensaScreen(
    navController: NavController,
    usuariViewModel: UsuariViewModel
) {
    val context = LocalContext.current
    val recompensaId = navController.currentBackStackEntry
        ?.arguments?.getString("recompensaId")

    if (recompensaId == null) {
        Text("Recompensa no trobada")
        return
    }

    val recompensaViewModel: RecompensaViewModel = viewModel(
        factory = RecompensaViewModelFactory(RecompensaUseCases(RecompensaRepository()))
    )

    val recompensa by recompensaViewModel.recompensaById.collectAsState()
    val usuari by usuariViewModel.currentUser.collectAsState()

    val reservaFeta by recompensaViewModel.reservaExitosa.collectAsState()
    val error by recompensaViewModel.error.collectAsState()

    LaunchedEffect(recompensaId) {
        recompensaViewModel.getRecompensaById(recompensaId)
    }



    if (recompensa != null && usuari != null) {
        VisualitzarRecompensaContent(
            recompensa = recompensa!!,
            usuari = usuari!!,
            navController = navController,
            usuariViewModel = usuariViewModel,
            recompensaViewModel = recompensaViewModel,
            error = error)
    } else {
        Text("Carregant recompensa...")
    }
}


@Composable
fun VisualitzarRecompensaContent(
    recompensa: Recompensa,
    usuari: Usuari,
    navController: NavController,
    usuariViewModel: UsuariViewModel,
    recompensaViewModel: RecompensaViewModel,
    error: String? // 👉 Afegeix això

) {
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let { msg: String ->  // Especificar explícitament que 'msg' és un String
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show() // Passar 'msg' com CharSequence
            recompensaViewModel.resetError()
        }
    }

    val groc = colorResource(id = R.color.groc)
    val blau = colorResource(id = R.color.blau)
    val rosa = colorResource(id = R.color.rosa)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState()),
            //padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        // TOP NAV
        TopNav(navController, usuariViewModel)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(groc)
                .padding(16.dp),


        ) {
            // Títol de la recompensa
            Text(
                text = recompensa.descripcio,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )

            // Imatge
            recompensa.imatgeRecompensa?.let {
                val imageBitmap = usuariViewModel.base64ToBitmap(it)
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = recompensa.descripcio,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(180.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    )
                }
            }

            // Contenidor d’informació bàsica
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = CardDefaults.cardColors(containerColor = blau),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informació bàsica",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)

                    )
    //TODO Revisar que quan estigui reservada si que aparegui l'usuari.

                    recompensa.usuari?.let { InfoRow(label = "Usuari:", value = it.nomComplet) }
                    InfoRow(label = "Punt bescanvi:", value = recompensa.nomPuntBescanvi)
                    InfoRow(label = "Adreça:", value = recompensa.adresaPuntBescanvi)
                    InfoRow(label = "Estat:", value = recompensa.estatRecompensa.toString())
                    if (recompensa.estatRecompensa == EstatRecompensaType.RECOLLIDA){
                        InfoRow(label = "Data recollida:", value = recompensa.dataRecollida.toString())
                    }


                    // Botó reserva/recollir
                    if (recompensa.estatRecompensa == EstatRecompensaType.DISPONIBLE) {
                        Button(
                            onClick = {
                                recompensaViewModel.reservarRecompensa(
                                    idRecompensa = recompensa.idRecompensa,
                                    emailUsuari = usuari.email
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = rosa),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 24.dp)
                        ) {
                            Text("Reservar", fontFamily = FontFamily.Monospace)
                        }
                    }
                    if (recompensa.estatRecompensa == EstatRecompensaType.ASSIGNADA){
                        Button(
                            onClick = { recompensaViewModel.recollirRecompensa(idRecompensa = recompensa.idRecompensa) },
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(containerColor = rosa),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 24.dp)
                        ) {
                            Text("Recollir", fontFamily = FontFamily.Monospace)
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    if (recompensa.estatRecompensa == EstatRecompensaType.RECOLLIDA) {
                        //InfoRow(label = "Data de recollida:", value = recompensa.dataRecollida.toString())

                        Text(
                            text = "RECOLLIDA",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(120.dp)) // espai per al bottom nav

            }
        }

        BottomNav(navController, usuariViewModel, 3)
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.6f)
        )
    }
}