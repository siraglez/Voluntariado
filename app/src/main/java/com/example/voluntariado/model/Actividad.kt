package com.example.voluntariado.model

data class Actividad(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val ubicacion: String = "",
    val categoria: String = "",
    val voluntariosMax: Int = 0,
    var voluntariosActuales: Int = 0,
    val estado: String = "",
    var inscritos: List<String> = emptyList()
)
