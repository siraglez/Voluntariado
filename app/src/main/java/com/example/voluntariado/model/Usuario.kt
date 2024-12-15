package com.example.voluntariado.model

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val email: String = "",
    val ubicacion: String = "",
    val intereses: List<String> = emptyList(),
    val inscripciones: List<String> = emptyList()
)
