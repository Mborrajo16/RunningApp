package com.borrajo.runingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.borrajo.runingapp.ui.Screen
import com.borrajo.runingapp.ui.screens.*
import com.borrajo.runingapp.ui.theme.RuningAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.borrajo.runingapp.viewmodel.InicioViewModel
import com.borrajo.runingapp.viewmodel.EntrenosViewModel
import com.borrajo.runingapp.viewmodel.MarcasViewModel
import com.borrajo.runingapp.viewmodel.ProgresoViewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuningAppTheme {
                val navController = rememberNavController()

                val inicioViewModel: InicioViewModel = viewModel()
                val entrenosViewModel: EntrenosViewModel = viewModel()
                val progresoViewModel: ProgresoViewModel = viewModel()
                val marcasViewModel: MarcasViewModel = viewModel()

                Scaffold(
                    containerColor = Color(0xFF000000),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF121212),
                            tonalElevation = 0.dp
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            val items = listOf(
                                Triple(Screen.Inicio, Icons.Default.Home, "Inicio"),
                                Triple(Screen.Entrenos, Icons.Default.DateRange, "Entrenos"),
                                Triple(Screen.Progreso, Icons.Default.Star, "Progreso"),
                                Triple(Screen.Marcas, Icons.Default.List, "Marcas"),
                                Triple(Screen.Perfil, Icons.Default.Person, "Perfil")
                            )

                            items.forEach { (screen, icon, label) ->
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF00FF00),
                                        selectedTextColor = Color(0xFF00FF00),
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = Color(0xFF1A1A1A)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Inicio.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Screen.Inicio.route) {
                            InicioScreen(viewModel = inicioViewModel)
                        }

                        composable(route = Screen.Entrenos.route) {
                            EntrenosScreen(viewModel = entrenosViewModel)
                        }

                        composable(route = Screen.Progreso.route) {
                            ProgresoScreen(viewModel = progresoViewModel)
                        }

                        composable(route = Screen.Marcas.route) {
                            MarcasScreen(viewModel = marcasViewModel)
                        }

                        composable(route = Screen.Perfil.route) {
                            PerfilScreen(progresoViewModel = progresoViewModel)
                        }
                    }
                }
            }
        }
    }
}