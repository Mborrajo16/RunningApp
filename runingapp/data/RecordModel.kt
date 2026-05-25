package com.borrajo.runingapp.data

data class RecordModel(
    val id: String = "",
    val distancia: String = "",
    val tiempo: String = "",
    val fecha: String = "" ,
    val nombre: String = ""
) {
    constructor() : this("", "", "", "", "")
}