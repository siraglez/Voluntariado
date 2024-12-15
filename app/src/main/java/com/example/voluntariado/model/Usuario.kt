package com.example.voluntariado.model

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val email: String = "",
    val ubicacion: String = "",
    val intereses: List<String> = listOf(),
    val inscripciones: List<String> = listOf()
)
