package com.borrajo.runingapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel

// Importamos tus pantallas
import com.borrajo.runingapp.ui.screens.InicioScreen
import com.borrajo.runingapp.ui.screens.EntrenosScreen
import com.borrajo.runingapp.ui.screens.ProgresoScreen
import com.borrajo.runingapp.ui.screens.MarcasScreen
import com.borrajo.runingapp.ui.screens.PerfilScreen

// Importamos tus ViewModels
import com.borrajo.runingapp.viewmodel.InicioViewModel
import com.borrajo.runingapp.viewmodel.EntrenosViewModel
import com.borrajo.runingapp.viewmodel.ProgresoViewModel
import com.borrajo.runingapp.viewmodel.MarcasViewModel

sealed class Screen(val route: String, val title: String) {
    object Inicio : Screen("inicio", "Inicio")
    object Entrenos : Screen("entrenos", "Entrenos")
    object Progreso : Screen("progreso", "Progreso")
    object Marcas : Screen("marcas", "Marcas")
    object Perfil : Screen("perfil", "Perfil")
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val inicioViewModel: InicioViewModel = viewModel()
    val entrenosViewModel: EntrenosViewModel = viewModel()
    val progresoViewModel: ProgresoViewModel = viewModel()
    val marcasViewModel: MarcasViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route,
        modifier = modifier
    ) {
        composable(Screen.Inicio.route) {
            InicioScreen(viewModel = inicioViewModel)
        }

        composable(Screen.Entrenos.route) {
            EntrenosScreen(viewModel = entrenosViewModel)
        }

        composable(Screen.Progreso.route) {
            ProgresoScreen(viewModel = progresoViewModel)
        }

        composable(Screen.Marcas.route) {
            MarcasScreen(viewModel = marcasViewModel)
        }

        composable(Screen.Perfil.route) {
            PerfilScreen(progresoViewModel = progresoViewModel)
        }
    }
}