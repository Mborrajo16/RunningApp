package com.borrajo.runingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borrajo.runingapp.viewmodel.EntrenosViewModel
import com.borrajo.runingapp.data.RecordModel // Asegúrate de que apunte a tu paquete correcto de data o viewmodel

@Composable
fun EntrenosScreen(viewModel: EntrenosViewModel) {
    val entrenos = viewModel.listaEntrenos

    var mostrarDialogoConfirmarBorrar by remember { mutableStateOf(false) }
    var entrenoParaBorrar by remember { mutableStateOf<RecordModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F13))
            .padding(16.dp)
    ) {
        Text(
            text = "HISTORIAL DE ENTRENOS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00FF00),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (entrenos.isEmpty()) {
            Text(
                text = "Aún no has registrado ninguna carrera.",
                color = Color.Gray,
                fontSize = 16.sp
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entrenos) { entreno ->
                    CardEntreno(
                        entreno = entreno,
                        onBorrarClick = {
                            entrenoParaBorrar = entreno
                            mostrarDialogoConfirmarBorrar = true
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoConfirmarBorrar && entrenoParaBorrar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmarBorrar = false },
            containerColor = Color(0xFF121212),
            title = {
                Text("¿ELIMINAR ENTRENAMIENTO?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Text(
                    text = "Vas a borrar permanentemente tu entrenamiento de ${entrenoParaBorrar?.distancia} del día ${entrenoParaBorrar?.fecha}. ¿Estás seguro?",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        entrenoParaBorrar?.let { entreno ->
                            viewModel.eliminarEntreno(entreno.id)
                        }
                        mostrarDialogoConfirmarBorrar = false
                        entrenoParaBorrar = null
                    }
                ) {
                    Text("SÍ, BORRAR", color = Color(0xFFFF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogoConfirmarBorrar = false
                    entrenoParaBorrar = null
                }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun CardEntreno(
    entreno: RecordModel,
    onBorrarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "CARRERA LIBRE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = entreno.distancia ?: "0.0 KM",
                        color = Color(0xFF00FF00),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "⏱️ ${entreno.tiempo ?: "00:00"}",
                        color = Color.LightGray,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = entreno.fecha ?: "Sin fecha",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(
                onClick = onBorrarClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar entrenamiento",
                    tint = Color(0xFFFF4444)
                )
            }
        }
    }
}