package com.borrajo.runingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borrajo.runingapp.viewmodel.InicioViewModel

@android.annotation.SuppressLint("MissingPermission")
@Composable
fun InicioScreen(viewModel: InicioViewModel) {
    val context = LocalContext.current
    var mostrarDialogoStop by remember { mutableStateOf(false) }
    var nombreActividad by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.inicializarGPS(context)
    }

    val estado = viewModel.estadoActual.value
    val tiempo = viewModel.tiempoFormateado.value
    val kms = viewModel.kilometrosFormateados.value
    val ritmo = viewModel.ritmoFormateado.value

    val enCuentaAtras = viewModel.enCuentaAtras.value
    val numeroCuentaAtras = viewModel.numeroCuentaAtras.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (enCuentaAtras) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = numeroCuentaAtras.toString(),
                    fontSize = 120.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00FF00)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RUNNING APP",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = tiempo, color = Color.White, fontSize = 54.sp, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "DISTANCIA", color = Color.Gray, fontSize = 12.sp)
                            Text(text = kms, color = Color(0xFF00FF00), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "RITMO MEDIO", color = Color.Gray, fontSize = 12.sp)
                            Text(text = ritmo, color = Color(0xFF00FF00), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (estado == InicioViewModel.EstadoCarrera.PARADO) {
                        Button(
                            onClick = { viewModel.iniciarConCuentaAtras() },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF00)),
                            modifier = Modifier.size(140.dp)
                        ) {
                            Text(text = "START", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    if (estado == InicioViewModel.EstadoCarrera.CORRIENDO) {
                                        viewModel.pausarEntrenamiento()
                                    } else {
                                        viewModel.iniciarEntrenamiento()
                                    }
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (estado == InicioViewModel.EstadoCarrera.CORRIENDO) Color(0xFFFF9800) else Color(0xFF00FF00)
                                ),
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(60.dp)
                            ) {
                                Text(
                                    text = if (estado == InicioViewModel.EstadoCarrera.CORRIENDO) "PAUSA" else "REANUDAR",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.pausarEntrenamiento()
                                    mostrarDialogoStop = true
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(60.dp)
                            ) {
                                Text(text = "STOP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogoStop) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoStop = false },
                title = { Text(text = "Finalizar Entrenamiento", color = Color.White, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "¿Quieres guardar esta carrera en tu historial?", color = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = nombreActividad,
                            onValueChange = { nombreActividad = it },
                            label = { Text("Nombre de la actividad") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF00FF00),
                                focusedLabelColor = Color(0xFF00FF00),
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                },
                containerColor = Color(0xFF1A1A24),
                confirmButton = {
                    TextButton(
                        onClick = {
                            val nombreFinal = nombreActividad.trim()
                            viewModel.detenerYGuardarEntrenamiento(nombreCustom = nombreFinal.ifBlank { "Carrera Completa" })
                            mostrarDialogoStop = false
                            nombreActividad = ""
                        }
                    ) {
                        Text("Guardar", color = Color(0xFF00FF00), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.descartarEntrenamiento()
                            mostrarDialogoStop = false
                            nombreActividad = ""
                        }
                    ) {
                        Text("Descartar", color = Color.Red)
                    }
                }
            )
        }
    }
}