package com.anna.entrebicisanna.Usuaris.UI.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anna.entrebicisanna.Core.UI.Components.BottomNav
import com.anna.entrebicisanna.Core.UI.Components.TopNav
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

@Composable
fun VisualitzarPerfilScreen(navController: NavController, usuariViewModel: UsuariViewModel) {

    val context = LocalContext.current
    val usuari by usuariViewModel.currentUser.collectAsState()

    val scrollState = rememberScrollState()
    val groc = colorResource(id = R.color.groc)
    val blau = colorResource(id = R.color.blau)
    val rosa = colorResource(id = R.color.rosa)
    val fontMono = FontFamily.Monospace

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(scrollState)
            //.padding(16.dp)
    ) {
        TopNav(navController, usuariViewModel)


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(groc)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Títol i botó d’editar
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = usuari?.nomComplet ?: "",
                        fontSize = 24.sp,
                        fontFamily = fontMono
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Imatge de perfil amb icona d’editar
                Box(contentAlignment = Alignment.BottomEnd) {
                    val imageBitmap = usuari?.imatgePerfilBase64?.let {
                        usuariViewModel.base64ToBitmap(it)
                    }

                    if (imageBitmap == null) {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder_perfil), // imatge per defecte
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                        )
                    } else {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                        )
                    }
                    IconButton(
                        onClick = { /* Navegar a editar foto */ },
                        modifier = Modifier
                            .background(rosa, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Editar foto",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = blau),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "${usuari?.nomComplet}", fontFamily = fontMono, fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Email: ${usuari?.email}", fontFamily = fontMono
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Saldo: ${usuari?.saldo ?: 0.0} punts", fontFamily = fontMono
                        )
                        // Afegeix més dades aquí si cal
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clickable { navController.navigate("llistarRecompensesPerUsuari") },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = rosa)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painterResource(id = R.drawable.gift),
                                contentDescription = "Recompenses"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Recompenses", fontFamily = fontMono)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clickable { navController.navigate("LlistarRutesScreen") },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = rosa)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painterResource(id = R.drawable.map_marker),
                                contentDescription = "Rutes"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Rutes", fontFamily = fontMono)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(64.dp)) // espai pel bottomNavBar
            }
        }
        BottomNav(navController, usuariViewModel, 2) // El teu bottom nav existent

    }
}
