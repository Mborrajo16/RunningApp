package com.borrajo.runingapp.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borrajo.runingapp.data.RecordModel
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class InicioViewModel : ViewModel() {

    enum class EstadoCarrera { PARADO, CORRIENDO, PAUSADO }

    private val db = FirebaseFirestore.getInstance()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    var estadoActual = mutableStateOf(EstadoCarrera.PARADO)
        private set

    var tiempoFormateado = mutableStateOf("00:00:00")
        private set

    var kilometrosFormateados = mutableStateOf("0.00 KM")
        private set

    var ritmoFormateado = mutableStateOf("0:00 /KM")
        private set

    var enCuentaAtras = mutableStateOf(false)
        private set

    var numeroCuentaAtras = mutableStateOf(3)
        private set

    private var segundosTotales = 0
    private var distanciaMetros = 0.0
    private var ultimaUbicacion: Location? = null
    private var cronometroJob: Job? = null

    fun inicializarGPS(context: Context) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun iniciarConCuentaAtras() {
        enCuentaAtras.value = true
        numeroCuentaAtras.value = 3

        viewModelScope.launch {
            while (numeroCuentaAtras.value > 0) {
                delay(1000)
                numeroCuentaAtras.value -= 1
            }
            enCuentaAtras.value = false
            iniciarEntrenamiento()
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun iniciarEntrenamiento() {
        estadoActual.value = EstadoCarrera.CORRIENDO
        ultimaUbicacion = null

        cronometroJob = viewModelScope.launch {
            while (estadoActual.value == EstadoCarrera.CORRIENDO) {
                delay(1000)
                segundosTotales++

                val metrosSimulados = (25..35).random() / 10.0
                distanciaMetros += metrosSimulados

                actualizarMetricas()
            }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMinUpdateDistanceMeters(1f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (estadoActual.value != EstadoCarrera.CORRIENDO) return
            }
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback!!, null)
    }

    fun pausarEntrenamiento() {
        estadoActual.value = EstadoCarrera.PAUSADO
        cronometroJob?.cancel()
        ultimaUbicacion = null
    }

    fun detenerYGuardarEntrenamiento(nombreCustom: String) {
        cronometroJob?.cancel()
        fusedLocationClient?.removeLocationUpdates(locationCallback ?: object : LocationCallback() {})

        if (segundosTotales > 0) {
            val docEntreno = db.collection("entrenos").document()
            val nuevoEntreno = RecordModel(
                id = docEntreno.id,
                distancia = kilometrosFormateados.value,
                tiempo = tiempoFormateado.value.substring(3),
                fecha = "24 Mayo 2026",
                nombre = nombreCustom
            )
            docEntreno.set(nuevoEntreno)
        }
        limpiarMetricas()
    }

    fun descartarEntrenamiento() {
        cronometroJob?.cancel()
        fusedLocationClient?.removeLocationUpdates(locationCallback ?: object : LocationCallback() {})
        limpiarMetricas()
    }

    private fun limpiarMetricas() {
        estadoActual.value = EstadoCarrera.PARADO
        segundosTotales = 0
        distanciaMetros = 0.0
        ultimaUbicacion = null
        actualizarMetricas()
    }

    private fun actualizarMetricas() {
        val horas = segundosTotales / 3600
        val minutos = (segundosTotales % 3600) / 60
        val segundos = segundosTotales % 60
        tiempoFormateado.value = String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos)

        val kms = distanciaMetros / 1000.0
        kilometrosFormateados.value = String.format(Locale.getDefault(), "%.2f KM", kms)

        if (kms > 0.0) {
            val minutosTotalesDecimal = segundosTotales / 60.0
            val ritmoPorKmDecimal = minutosTotalesDecimal / kms
            val ritmoMinutos = ritmoPorKmDecimal.toInt()
            val ritmoSegundos = ((ritmoPorKmDecimal - ritmoMinutos) * 60).toInt()
            ritmoFormateado.value = String.format(Locale.getDefault(), "%d:%02d /KM", ritmoMinutos, ritmoSegundos)
        } else {
            ritmoFormateado.value = "0:00 /KM"
        }
    }
}