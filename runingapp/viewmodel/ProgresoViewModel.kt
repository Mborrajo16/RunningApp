package com.borrajo.runingapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.borrajo.runingapp.data.RecordModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class ProgresoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var totalKms = mutableStateOf("0.00 KM")
        private set
    var totalSesiones = mutableStateOf("0")
        private set
    var totalTiempo = mutableStateOf("00:00:00")
        private set
    var ritmoMedioGlobal = mutableStateOf("0:00 /KM")
        private set
    var recordDistancia = mutableStateOf("0.00 KM")
        private set

    init {
        cargarEstadisticas()
    }

    fun cargarEstadisticas() {
        db.collection("entrenos")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val cantidadEntrenos = snapshot.documents.size
                totalSesiones.value = cantidadEntrenos.toString()

                var sumaMetros = 0.0
                var sumaSegundos = 0
                var maxMetrosEnUnaCarrera = 0.0

                for (document in snapshot.documents) {
                    val entreno = document.toObject(RecordModel::class.java)
                    if (entreno != null) {
                        val kmsTexto = entreno.distancia.replace(" KM", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                        val metros = kmsTexto * 1000.0
                        sumaMetros += metros

                        if (metros > maxMetrosEnUnaCarrera) {
                            maxMetrosEnUnaCarrera = metros
                        }

                        val partesTiempo = entreno.tiempo.split(":")
                        var segundosCarrera = 0
                        if (partesTiempo.size == 2) { // MM:SS
                            val min = partesTiempo[0].toIntOrNull() ?: 0
                            val seg = partesTiempo[1].toIntOrNull() ?: 0
                            segundosCarrera = (min * 60) + seg
                        } else if (partesTiempo.size == 3) { // HH:MM:SS
                            val hor = partesTiempo[0].toIntOrNull() ?: 0
                            val min = partesTiempo[1].toIntOrNull() ?: 0
                            val seg = partesTiempo[2].toIntOrNull() ?: 0
                            segundosCarrera = (hor * 3600) + (min * 60) + seg
                        }
                        sumaSegundos += segundosCarrera
                    }
                }

                val kmsTotales = sumaMetros / 1000.0
                totalKms.value = String.format(Locale.getDefault(), "%.2f KM", kmsTotales)

                recordDistancia.value = String.format(Locale.getDefault(), "%.2f KM", maxMetrosEnUnaCarrera / 1000.0)

                val horas = sumaSegundos / 3600
                val minutos = (sumaSegundos % 3600) / 60
                val segundos = sumaSegundos % 60
                totalTiempo.value = String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos)

                if (kmsTotales > 0.0) {
                    val minutosTotalesDecimal = sumaSegundos / 60.0
                    val ritmoDecimal = minutosTotalesDecimal / kmsTotales
                    val ritmoMin = ritmoDecimal.toInt()
                    val ritmoSeg = ((ritmoDecimal - ritmoMin) * 60).toInt()
                    ritmoMedioGlobal.value = String.format(Locale.getDefault(), "%d:%02d /KM", ritmoMin, ritmoSeg)
                } else {
                    ritmoMedioGlobal.value = "0:00 /KM"
                }
            }
    }
}