package com.borrajo.runingapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.borrajo.runingapp.data.RecordModel
import com.google.firebase.firestore.FirebaseFirestore

class RecordViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _records = mutableStateOf<List<RecordModel>>(emptyList())

    val records: State<List<RecordModel>> = _records

    init {
        obtenerRecords()
    }

    private fun obtenerRecords() {
        db.collection("records")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val listaDeRecords = snapshot.toObjects(RecordModel::class.java)
                    _records.value = listaDeRecords
                }
            }
    }

    fun agregarRecord(distancia: String, tiempo: String) {
        val nuevoDoc = db.collection("records").document()
        val nuevoRecord = RecordModel(
            id = nuevoDoc.id,
            distancia = distancia,
            tiempo = tiempo,
            fecha = "19 Mayo 2026"
        )

        nuevoDoc.set(nuevoRecord)
    }
}