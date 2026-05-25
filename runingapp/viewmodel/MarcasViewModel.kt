package com.borrajo.runingapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.borrajo.runingapp.data.MarcaOficialModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarcasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var records = mutableStateOf<List<MarcaOficialModel>>(emptyList())
        private set

    init {
        escucharMarcasOficiales()
    }

    private fun escucharMarcasOficiales() {
        db.collection("marcas_oficiales")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val lista = snapshot.documents.mapNotNull { doc ->
                    val marca = doc.toObject(MarcaOficialModel::class.java)
                    marca?.copy(id = doc.id)
                }
                records.value = lista
            }
    }

    fun agregarRecord(distancia: String, tiempo: String) {
        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val nuevaMarca = hashMapOf(
            "distancia" to distancia,
            "tiempo" to tiempo,
            "fecha" to fechaHoy
        )

        db.collection("marcas_oficiales").add(nuevaMarca)
    }

    fun eliminarRecord(idDocumento: String) {
        if (idDocumento.isBlank()) return
        db.collection("marcas_oficiales")
            .document(idDocumento)
            .delete()
    }

    fun actualizarRecord(idDocumento: String, nuevaDistancia: String, nuevoTiempo: String) {
        if (idDocumento.isBlank()) return

        val datosActualizados = mapOf(
            "distancia" to nuevaDistancia,
            "tiempo" to nuevoTiempo
        )

        db.collection("marcas_oficiales")
            .document(idDocumento)
            .update(datosActualizados)
    }
}