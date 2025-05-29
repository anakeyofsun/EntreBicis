package com.anna.entrebicisanna.Usuaris.UI.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciSessioScreen(
    navController: NavHostController,
    usuariViewModel: UsuariViewModel
) {
    val context = LocalContext.current
    val loginSuccess by usuariViewModel.loginSuccess.collectAsState()
    val loginError by usuariViewModel.loginError.collectAsState()
    var email by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }

    // Navegació o errors
    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            navController.navigate("novaRuta")
        }
    }

    // Mostrar Toast d'error si n'hi ha
    LaunchedEffect(loginError) {
        loginError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }


}

    val backgroundColor = colorResource(id = R.color.groc)
    val cardColor = colorResource(id = R.color.blau)
    val verd = colorResource(id = R.color.verd)
    val rosa = colorResource(id = R.color.rosa)
    val fontMono = FontFamily.Monospace

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo i títol
            Image(
                painter = painterResource(id = R.drawable.logosensefonsamblletra),
                contentDescription = "Logo EntreBicis",
                modifier = Modifier.size(320.dp)
            )
            /*Text(
                text = "ENTREBICIS",
                fontSize = 32.sp,
                fontFamily = fontMono,
                color = Color.Black
            )*/

            // Tarja d'inici de sessió
            Card(
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Iniciar Sessió",
                        fontSize = 24.sp,
                        fontFamily = fontMono,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Correu electrònic", fontFamily = fontMono)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Gray
                        ))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Contrasenya", fontFamily = fontMono)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contrasenya,
                        onValueChange = { contrasenya = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botó Iniciar sessió
                    Button(
                        onClick = {
                            usuariViewModel.loginUser(email, contrasenya)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = verd),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar sessió", fontFamily = fontMono)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "He oblidat la contrasenya",
                        fontFamily = fontMono,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("recuperarContrasenyaScreen")
                            }
                    )


                    // Botó Donar-se d’alta
                    /*Button(
                        onClick = {
                            navController.navigate("RegistrarUsuari")
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = rosa),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dóna’t d’alta", fontFamily = fontMono)
                    }*/
                }
            }
        }
    }
}