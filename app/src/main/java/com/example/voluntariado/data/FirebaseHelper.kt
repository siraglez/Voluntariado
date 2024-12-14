package com.example.voluntariado.data

import com.google.firebase.firestore.FirebaseFirestore
import com.example.voluntariado.model.Usuario
import com.example.voluntariado.model.Actividad
import com.example.voluntariado.model.Inscripcion

class FirebaseHelper {

    private val db = FirebaseFirestore.getInstance()

    // Función para agregar un usuario
    fun agregarUsuario(usuario: Usuario) {
        db.collection("usuarios").document(usuario.uid).set(usuario)
    }

    // Función para obtener los datos de un usuario
    fun obtenerUsuario(uid: String, callback: (Usuario?) -> Unit) {
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    callback(document.toObject(Usuario::class.java))
                } else {
                    callback(null)
                }
            }
    }

    // Función para agregar una actividad
    fun agregarActividad(actividad: Actividad) {
        db.collection("actividades").document(actividad.id).set(actividad)
    }

    // Función para obtener una lista de actividades
    fun obtenerActividades(callback: (List<Actividad>) -> Unit) {
        db.collection("actividades").get()
            .addOnSuccessListener { result ->
                val actividades = result.map { it.toObject(Actividad::class.java) }
                callback(actividades)
            }
    }

    // Función para inscribir a un usuario a una actividad
    fun inscribirUsuario(inscripcion: Inscripcion) {
        db.collection("inscripciones").document(inscripcion.usuarioId + "_" + inscripcion.actividadId).set(inscripcion)
    }

    // Función para obtener las inscripciones de un usuario
    fun obtenerInscripciones(usuarioId: String, callback: (List<Inscripcion>) -> Unit) {
        db.collection("inscripciones").whereEqualTo("usuarioId", usuarioId).get()
            .addOnSuccessListener { result ->
                val inscripciones = result.map { it.toObject(Inscripcion::class.java) }
                callback(inscripciones)
            }
    }
}