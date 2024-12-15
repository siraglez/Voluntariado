package com.example.voluntariado.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.model.Actividad
import com.example.voluntariado.model.Inscripcion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetallesActividadActivity : AppCompatActivity() {

    private lateinit var actividad: Actividad
    private lateinit var btnInscribirse: Button
    private lateinit var btnVolver: Button
    private lateinit var tvCategoria: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvVoluntariosActuales: TextView
    private lateinit var tvVoluntariosMax: TextView
    private lateinit var tvInscritos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_actividad)

        // Referencias a los elementos de la UI
        tvCategoria = findViewById(R.id.tvCategoria)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvFecha = findViewById(R.id.tvFecha)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        tvVoluntariosActuales = findViewById(R.id.tvVoluntariosActuales)
        tvVoluntariosMax = findViewById(R.id.tvVoluntariosMax)
        tvInscritos = findViewById(R.id.tvInscritos)

        btnInscribirse = findViewById(R.id.btnInscribirse)
        btnVolver = findViewById(R.id.btnVolver)

        // Obtener los detalles de la actividad desde el Intent
        val actividadId = intent.getStringExtra("ACTIVIDAD_ID") ?: return
        obtenerDetallesActividad(actividadId)

        // Acción para inscribirse
        btnInscribirse.setOnClickListener {
            val usuarioId = FirebaseAuth.getInstance().currentUser?.uid
            if (usuarioId != null) {
                inscribirseEnActividad(usuarioId)
            } else {
                Toast.makeText(this, "Debe iniciar sesión para inscribirse", Toast.LENGTH_SHORT).show()
            }
        }

        // Acción para volver a la lista
        btnVolver.setOnClickListener {
            finish() // Vuelve a la actividad anterior
        }
    }

    private fun obtenerDetallesActividad(id: String) {
        FirebaseFirestore.getInstance().collection("actividades")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    actividad = document.toObject(Actividad::class.java)!!
                    cargarDetallesActividad()
                } else {
                    Toast.makeText(this, "Actividad no encontrada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al obtener los detalles", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun cargarDetallesActividad() {
        tvCategoria.text = actividad.categoria
        tvDescripcion.text = actividad.descripcion
        tvFecha.text = actividad.fecha
        tvUbicacion.text = actividad.ubicacion
        tvVoluntariosActuales.text = actividad.voluntariosActuales.toString()
        tvVoluntariosMax.text = actividad.voluntariosMax.toString()
        tvInscritos.text = actividad.inscritos.joinToString(", ")
    }

    private fun inscribirseEnActividad(usuarioId: String) {
        if (actividad.voluntariosActuales < actividad.voluntariosMax) {
            val db = FirebaseFirestore.getInstance()

            // Actualizar el array de inscritos y voluntarios_actuales en la actividad
            val nuevosInscritos = actividad.inscritos.toMutableList()
            nuevosInscritos.add(usuarioId)

            db.collection("actividades").document(actividad.id)
                .update(
                    "inscritos", nuevosInscritos,
                    "voluntarios_actuales", actividad.voluntariosActuales + 1
                ).addOnSuccessListener {
                    // Crear una nueva inscripción en la colección `inscripciones`
                    val nuevaInscripcion = Inscripcion(
                        actividadId = "/actividades/${actividad.id}",
                        usuarioId = "/usuarios/$usuarioId",
                        estado = "Confirmada",
                        fechaInscripcion = getCurrentDate()
                    )
                    db.collection("inscripciones").add(nuevaInscripcion)
                        .addOnSuccessListener {
                            db.collection("usuarios").document(usuarioId)
                                .update("inscripciones", FieldValue.arrayUnion(it.id))
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Te has inscrito correctamente", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar la inscripción", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar la actividad", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No hay más espacio en esta actividad", Toast.LENGTH_SHORT).show()
        }
    }

    // Función auxiliar para obtener la fecha actual
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
