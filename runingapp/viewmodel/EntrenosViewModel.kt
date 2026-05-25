package com.borrajo.runingapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.borrajo.runingapp.data.RecordModel

class EntrenosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val listaEntrenos = mutableStateListOf<RecordModel>()

    init {
        cargarHistorialEntrenos()
    }

    fun cargarHistorialEntrenos() {
        db.collection("entrenos")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    listaEntrenos.clear()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    listaEntrenos.clear()
                    for (document in snapshot.documents) {
                        val entreno = document.toObject(RecordModel::class.java)
                        if (entreno != null) {
                            val entrenoConId = entreno.copy(id = document.id)
                            listaEntrenos.add(entrenoConId)
                        }
                    }
                }
            }
    }

    fun eliminarEntreno(idEntreno: String) {
        if (idEntreno.isNotBlank()) {
            db.collection("entrenos")
                .document(idEntreno)
                .delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        }
    }
}