package com.example.voluntariado.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.voluntariado.model.Usuario
import com.example.voluntariado.model.Actividad
import com.example.voluntariado.model.Inscripcion

class FirebaseHelper {

    private val db = FirebaseFirestore.getInstance()

    // Función para agregar un usuario
    fun agregarUsuario(usuario: Usuario) {
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(usuario.uid).set(usuario)
            .addOnSuccessListener {
                Log.d("FirebaseHelper", "Usuario agregado exitosamente")
            }
            .addOnFailureListener {
                Log.e("FirebaseHelper", "Error al agregar usuario: ${it.message}")
            }
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
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("actividades").document()
        actividad.id = docRef.id
        docRef.set(actividad)
    }

    fun agregarActividadConIdSecuencial(nuevaActividad: Actividad, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Consulta todas las actividades para encontrar el último ID
        db.collection("actividades")
            .orderBy("id") // Asegura que las actividades estén ordenadas por el campo "id"
            .get()
            .addOnSuccessListener { documentos ->
                val ultimoId = documentos.documents.lastOrNull()?.getString("id")
                val nuevoId = if (ultimoId != null) {
                    // Incrementa el último ID
                    val nuevoNumero = ultimoId.toInt() + 1
                    String.format("%03d", nuevoNumero)
                } else {
                    "001" // Si no hay actividades, comienza con 001
                }

                // Asigna el nuevo ID a la actividad
                nuevaActividad.id = nuevoId

                // Guarda la actividad con el nuevo ID
                db.collection("actividades").document(nuevoId).set(nuevaActividad)
                    .addOnSuccessListener {
                        callback(true) // Operación exitosa
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        callback(false) // Error al guardar
                    }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                callback(false) // Error al consultar las actividades
            }
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