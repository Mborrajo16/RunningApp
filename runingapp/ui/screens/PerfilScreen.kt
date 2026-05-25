package com.borrajo.runingapp.ui.screens

import androidx.compose.foundation.BorderStroke // ¡Añadido el import correcto!
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borrajo.runingapp.viewmodel.ProgresoViewModel

@Composable
fun PerfilScreen(progresoViewModel: ProgresoViewModel) {


    val kmsTexto = progresoViewModel.totalKms.value.replace(" KM", "").replace(",", ".").toDoubleOrNull() ?: 0.0


    val (rango, colorRango) = when {
        kmsTexto < 5.0 -> "RUNNER NOVATO" to Color.Gray
        kmsTexto < 15.0 -> "RUNNER MEJORADO" to Color(0xFFFF9800)
        kmsTexto < 30.0 -> "ATLETA" to Color(0xFF00FF00)
        else -> "LEYENDA URBANA" to Color(0xFF00FF00)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PERFIL DE USUARIO",
            color = Color(0xFF00FF00),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .size(110.dp)
                .border(3.dp, colorRango, CircleShape)
                .padding(6.dp)
                .background(Color(0xFF121212), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "B",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Marcos Borrajo",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Surface(
            modifier = Modifier.padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            color = colorRango.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, colorRango.copy(alpha = 0.5f))
        ) {
            Text(
                text = rango,
                color = colorRango,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DISTANCIA BASE", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(progresoViewModel.totalKms.value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("NUBE FIREBASE", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ONLINE", color = Color(0xFF00FF00), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "OPCIONES DE CUENTA",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )

            OpcionPerfilRow(icon = Icons.Default.Person, titulo = "Editar Perfil", colorContenido = Color.White)
            OpcionPerfilRow(icon = Icons.Default.Settings, titulo = "Preferencias del Sistema", colorContenido = Color.White)
            OpcionPerfilRow(icon = Icons.Default.ExitToApp, titulo = "Cerrar Sesión", colorContenido = Color(0xFFFF4444))
        }
    }
}

@Composable
fun OpcionPerfilRow(icon: ImageVector, titulo: String, colorContenido: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = titulo,
                tint = colorContenido,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = titulo,
                color = colorContenido,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}