package com.anna.entrebicisanna.Core.UI

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anna.entrebicisanna.Recompenses.UI.Screens.LlistarRecompensesDisponiblesScreen
import com.anna.entrebicisanna.Recompenses.UI.Screens.LlistarRecompensesUsuariScreen
import com.anna.entrebicisanna.Recompenses.UI.Screens.VisualitzarRecompensaScreen
import com.anna.entrebicisanna.Rutes.UI.Screens.LlistarRutesScreen
import com.anna.entrebicisanna.Rutes.UI.Screens.MenuScreen
import com.anna.entrebicisanna.Rutes.UI.Screens.VisualitzarRutaScreen
import com.anna.entrebicisanna.Usuaris.UI.Screens.IniciSessioScreen
import com.anna.entrebicisanna.Usuaris.UI.Screens.RecuperarContrasenyaScreen
import com.anna.entrebicisanna.Usuaris.UI.Screens.VisualitzarPerfilScreen
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel


@Composable
fun AppNavigation(usuariViewModel: UsuariViewModel) {
    val navController = rememberNavController()

    //Permet la navegació i portar un historial d'aquesta
    NavHost(navController = navController, startDestination = "iniciarSessio") {
        composable("iniciarSessio") { IniciSessioScreen(navController, usuariViewModel) }
        composable("novaRuta") { MenuScreen(navController, usuariViewModel) }
        composable("perfil") { VisualitzarPerfilScreen(navController,usuariViewModel)}
        composable("llistarRecompensesPerUsuari") { LlistarRecompensesUsuariScreen(navController, usuariViewModel)}
        composable("LlistarRecompensesDisponibles") {LlistarRecompensesDisponiblesScreen(navController, usuariViewModel)}
        composable("visualitzarRecompensaScreen/{recompensaId}") {VisualitzarRecompensaScreen(navController,usuariViewModel)}
        composable("llistarRutesScreen") {LlistarRutesScreen(navController,usuariViewModel)}
        composable("visualitzarRutaScreen/{rutaId}") { VisualitzarRutaScreen(navController,usuariViewModel) }
        composable("recuperarContrasenyaScreen") {RecuperarContrasenyaScreen(navController,usuariViewModel)  }
    }
}
