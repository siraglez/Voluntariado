package com.example.voluntariado.model

data class Actividad(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val ubicacion: Map<String, Double> = mapOf(),
    val categoria: String = "",
    val voluntariosMax: Int = 0,
    val voluntariosActuales: Int = 0,
    val estado: String = "",
    val inscritos: List<String> = listOf()
)
