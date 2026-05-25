package com.borrajo.runingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borrajo.runingapp.data.MarcaOficialModel
import com.borrajo.runingapp.viewmodel.MarcasViewModel

@Composable
fun MarcasScreen(viewModel: MarcasViewModel) {
    val listaRecords by viewModel.records

    var mostrarDialogoFormulario by remember { mutableStateOf(false) }
    var mostrarDialogoConfirmarBorrar by remember { mutableStateOf(false) }

    var inputDistancia by remember { mutableStateOf("") }
    var inputTiempo by remember { mutableStateOf("") }

    var idMarcaSeleccionada by remember { mutableStateOf<String?>(null) }
    var marcaParaBorrar by remember { mutableStateOf<MarcaOficialModel?>(null) }

    Scaffold(
        containerColor = Color.Black,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    idMarcaSeleccionada = null
                    inputDistancia = ""
                    inputTiempo = ""
                    mostrarDialogoFormulario = true
                },
                containerColor = Color(0xFF00FF00),
                contentColor = Color.Black,
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir récord")
            }
        }
    ) { paddingValores ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .padding(24.dp)
        ) {
            Text(
                text = "MIS RÉCORDS OFICIALES",
                color = Color(0xFF00FF00),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )

            if (listaRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No hay registros oficiales. ¡Añade el primero!", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(listaRecords) { record ->
                        TarjetaRecord(
                            record = record,
                            onBorrarClick = {
                                marcaParaBorrar = record
                                mostrarDialogoConfirmarBorrar = true
                            },
                            onEditar = {
                                idMarcaSeleccionada = record.id
                                inputDistancia = record.distancia
                                inputTiempo = record.tiempo
                                mostrarDialogoFormulario = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogoFormulario) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoFormulario = false },
            containerColor = Color(0xFF121212),
            title = {
                Text(
                    text = if (idMarcaSeleccionada == null) "NUEVA MARCA OFICIAL" else "EDITAR MARCA OFICIAL",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = inputDistancia,
                        onValueChange = { inputDistancia = it },
                        label = { Text("Distancia (Ej: 10 KM)", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF00),
                            unfocusedBorderColor = Color.DarkGray,
                            focusedLabelColor = Color(0xFF00FF00),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = inputTiempo,
                        onValueChange = { inputTiempo = it },
                        label = { Text("Tiempo (Ej: 42:15)", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00FF00),
                            unfocusedBorderColor = Color.DarkGray,
                            focusedLabelColor = Color(0xFF00FF00),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (inputDistancia.isNotBlank() && inputTiempo.isNotBlank()) {
                            val id = idMarcaSeleccionada
                            if (id == null) {
                                viewModel.agregarRecord(inputDistancia, inputTiempo)
                            } else {
                                viewModel.actualizarRecord(id, inputDistancia, inputTiempo)
                            }
                            mostrarDialogoFormulario = false
                            inputDistancia = ""
                            inputTiempo = ""
                        }
                    }
                ) {
                    Text("GUARDAR", color = Color(0xFF00FF00), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoFormulario = false }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            }
        )
    }

    if (mostrarDialogoConfirmarBorrar && marcaParaBorrar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmarBorrar = false },
            containerColor = Color(0xFF121212),
            title = {
                Text("¿ELIMINAR REGISTRO?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Text(
                    text = "Vas a borrar permanentemente tu marca de ${marcaParaBorrar?.distancia}. ¿Estás seguro?",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        marcaParaBorrar?.let { viewModel.eliminarRecord(it.id) }
                        mostrarDialogoConfirmarBorrar = false
                        marcaParaBorrar = null
                    }
                ) {
                    Text("SÍ, BORRAR", color = Color(0xFFFF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoConfirmarBorrar = false
                    marcaParaBorrar = null
                }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun TarjetaRecord(
    record: MarcaOficialModel,
    onBorrarClick: () -> Unit,
    onEditar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212), RoundedCornerShape(16.dp))
            .clickable { onEditar() }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = record.distancia.uppercase(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = record.tiempo,
                color = Color(0xFF00FF00),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = record.fecha,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        IconButton(
            onClick = onBorrarClick,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Borrar marca",
                tint = Color(0xFFFF4444)
            )
        }
    }
}