package com.anna.entrebicisanna.Core.UI.Components

import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Recompenses.UI.Screens.LlistarRecompensesDisponiblesScreen
import com.anna.entrebicisanna.Recompenses.UI.Screens.LlistarRecompensesUsuariScreen
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

@Composable
fun BottomNav(navController: NavController, usuariViewModel: UsuariViewModel, selectedItem: Int = 0) {
    NavigationBar(
        containerColor = colorResource(id = R.color.verd)
    ) {
        //val currentUser by userViewModel.currentUser.collectAsState()
        val icons = listOf(
            R.drawable.marker,
            R.drawable.map_marker,
            R.drawable.profile,
            R.drawable.gift,
            R.drawable.book_open_cover
        )

        val destinations = listOf(
             "novaRuta",
             "LlistarRutesScreen",
             "perfil",
             "LlistarRecompensesDisponibles",
             "llistarRecompensesPerUsuari"
         )
        val descriptions = listOf(
            "nova ruta",
            "rutes",
            "perfil",
            "llista de recompenses",
            "recompenses"
        )
        icons.forEachIndexed { index, iconRes ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = descriptions[index],
                        modifier = Modifier.size(32.dp)
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    if (index < destinations.size) {
                        navController.navigate(destinations[index])
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.White
                ),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


/*
/**
 * Funció de previsualització per a la barra de navegació inferior.
 */
@Preview(showBackground = true)
@Composable
fun PreviewBottomBar() {
    //val fakeNavController = rememberNavController()
    BottomNav(/*fakeNavController, */selectedItem = 0)
}
*/