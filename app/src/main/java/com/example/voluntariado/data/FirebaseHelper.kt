package com.example.voluntariado.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.voluntariado.model.Usuario
import com.example.voluntariado.model.Actividad
import com.example.voluntariado.model.Inscripcion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

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

    fun obtenerUsuarioActualId(): String? {
        // Comprueba si hay un usuario autenticado y devuelve su UID
        return FirebaseAuth.getInstance().currentUser?.uid
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

    fun obtenerActividadPorId(actividadId: String, callback: (Actividad?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("actividades").document(actividadId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val actividad = document.toObject(Actividad::class.java)
                    callback(actividad)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    // Función para inscribir a un usuario a una actividad
    fun inscribirUsuarioEnActividad(
        actividadId: String,
        usuarioId: String,
        callback: (Boolean) -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val actividadRef = firestore.collection("actividades").document(actividadId)
        val usuarioRef = firestore.collection("usuarios").document(usuarioId)
        val inscripcionRef = firestore.collection("inscripciones").document()

        firestore.runTransaction { transaction ->
            // 1. Actualizar "voluntariosActuales" y la lista "inscritos" en la colección "actividades"
            val actividadSnapshot = transaction.get(actividadRef)
            val actividad = actividadSnapshot.toObject(Actividad::class.java)

            if (actividad != null) {
                if (actividad.voluntariosActuales < actividad.voluntariosMax) {
                    val nuevosVoluntariosActuales = actividad.voluntariosActuales + 1
                    val nuevosInscritos = actividad.inscritos.toMutableList().apply { add(usuarioId) }

                    transaction.update(actividadRef, "voluntariosActuales", nuevosVoluntariosActuales)
                    transaction.update(actividadRef, "inscritos", nuevosInscritos)
                } else {
                    throw Exception("No hay espacio disponible en la actividad")
                }
            } else {
                throw Exception("La actividad no existe")
            }

            // 2. Actualizar la colección "usuarios" con la actividad inscrita
            val usuarioSnapshot = transaction.get(usuarioRef)
            val inscripciones = usuarioSnapshot.get("inscripciones") as? List<String> ?: listOf()
            val nuevasInscripciones = inscripciones.toMutableList().apply { add(actividadId) }
            transaction.update(usuarioRef, "inscripciones", nuevasInscripciones)

            // 3. Crear un nuevo documento en la colección "inscripciones"
            val inscripcionData = hashMapOf(
                "actividad_id" to actividadId,
                "usuario_id" to usuarioId,
                "fecha_inscripcion" to System.currentTimeMillis()
            )
            transaction.set(inscripcionRef, inscripcionData)
        }.addOnSuccessListener {
            callback(true) // La transacción fue exitosa
        }.addOnFailureListener { e ->
            callback(false) // Ocurrió un error
        }
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