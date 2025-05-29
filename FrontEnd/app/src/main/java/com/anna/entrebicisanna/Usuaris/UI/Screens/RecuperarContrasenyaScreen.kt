package com.anna.entrebicisanna.Usuaris.UI.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasenyaScreen(
    navController: NavController,
    usuariViewModel: UsuariViewModel
) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val successMessage = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorResource(R.color.groc))
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {

        // 🔹 TopBar separada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.verd))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logosensefonsamblletra),
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Enrere",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() }
                )
            }
        }

        // 🔸 Cos de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Recuperar contrasenya",
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Correu electrònic") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (emailState.value.isBlank() || !emailState.value.contains("@")) {
                        errorMessage.value = "Introdueix un correu vàlid."
                        return@Button
                    }

                    isLoading.value = true
                    errorMessage.value = null
                    successMessage.value = null

                    usuariViewModel.recuperarContrasenya(
                        email = emailState.value,
                        onSuccess = {
                            isLoading.value = false
                            successMessage.value = "S'ha enviat una nova contrasenya al teu correu."
                        },
                        onError = {
                            isLoading.value = false
                            errorMessage.value = "No s'ha pogut enviar el correu. Comprova l'email."
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.verd)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar correu", fontFamily = FontFamily.Monospace, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading.value) {
                CircularProgressIndicator()
            }

            errorMessage.value?.let {
                Text(text = it, color = Color.Red, fontFamily = FontFamily.Monospace)
            }

            successMessage.value?.let {
                Text(text = it, color = Color(0xFF2E7D32), fontFamily = FontFamily.Monospace)
            }
        }
    }
}