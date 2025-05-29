package com.anna.entrebicisanna.Core.UI.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anna.entrebicisanna.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anna.entrebicisanna.Usuaris.Data.UsuariRepository
import com.anna.entrebicisanna.Usuaris.Domain.UsuariUseCases
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModelFactory


@Composable
fun TopNav(navController: NavController, usuariViewModel: UsuariViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.verd))
            .padding(horizontal = 12.dp),
            //.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.placeholder_perfil),
            contentDescription = "Imatge de perfil",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .padding(2.dp)
                //.clickable { navController.navigate("profile/${currentUser?.username}") }
        )
        Image(
            painter = painterResource(id = R.drawable.logosensefonsamblletra),
            contentDescription = "Logo",
            //contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                //.padding(2.dp)
        )


        Icon(
            Icons.Default.ExitToApp,
            contentDescription = "Tancar sessió",
            Modifier.size(40.dp)
                .clickable {
                    usuariViewModel.tancarSessio()
                    navController.navigate("iniciarSessio")
                }
        )
    }
}

/**
 * Funció composable que defineix la barra superior de l'aplicació amb una imatge de perfil.
 *
 * @param navController El controlador de navegació.
 * @param profileImageRes La imatge de perfil de l'usuari.
 * @param userViewModel El ViewModel de l'usuari.
 */
/*@Composable
fun TopNavImage(navController: NavController, profileImageRes: ImageBitmap?, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.header))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (profileImageRes != null) {
            Image(
                profileImageRes,
                contentDescription = stringResource(R.string.profile_avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White, shape = CircleShape)
                    .padding(2.dp)
                    .clickable { navController.navigate("profile/${currentUser?.username}") }
            )
        }

        Text(
            style = GameDexTypography.headlineMedium.copy(fontSize = 48.sp),
            text = "GDEX",
            color = Color.White,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        Icon(
            Icons.Default.ExitToApp,
            contentDescription = stringResource(R.string.logout),
            Modifier.size(40.dp)
                .clickable {
                    userViewModel.logoutUser()
                    navController.navigate("login")
                }
        )
    }
}*/

/**
 * Funció de previsualització per a la barra superior.
 */
@Preview(showBackground = true)
@Composable
fun PreviewTopNav() {
    val useCases = UsuariUseCases(UsuariRepository())
    val usuariViewModel: UsuariViewModel = viewModel(factory = UsuariViewModelFactory(useCases))
    val fakeNavController = rememberNavController() // Crear un NavController fals per la preview*/
    TopNav(navController = fakeNavController, usuariViewModel = usuariViewModel)
}
