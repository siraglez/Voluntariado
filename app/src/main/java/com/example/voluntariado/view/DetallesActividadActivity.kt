package com.example.voluntariado.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.model.Actividad
import com.google.firebase.firestore.FirebaseFirestore

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

        // Obtener los detalles de la actividad desde el Intent
        val actividadId = intent.getStringExtra("ACTIVIDAD_ID") ?: return
        obtenerDetallesActividad(actividadId)

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

        // Cargar los detalles de la actividad
        cargarDetallesActividad()

        // Acción para inscribirse
        btnInscribirse.setOnClickListener {
            inscribirseEnActividad()
        }

        // Acción para volver a la lista
        btnVolver.setOnClickListener {
            finish()  // Vuelve a la actividad anterior
        }
    }

    private fun obtenerDetallesActividad(id: String) {
        FirebaseFirestore.getInstance().collection("actividades")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    actividad = document.toObject(Actividad::class.java)!!
                    cargarDetallesActividad()
                }
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

    private fun inscribirseEnActividad() {
        // Lógica para inscribir al usuario en la actividad
        // Verifica si el número de voluntarios no ha alcanzado el máximo
        if (actividad.voluntariosActuales < actividad.voluntariosMax) {
            val usuarioId = "user123" // TODO: hacerlo dinámico según el usuario que haya iniciado sesión
            val nuevosInscritos = actividad.inscritos.toMutableList()
            nuevosInscritos.add(usuarioId)

            actividad.inscritos = nuevosInscritos
            actividad.voluntariosActuales++

            // Actualiza la actividad en la base de datos
            FirebaseFirestore.getInstance().collection("actividades")
                .document(actividad.id)
                .update(
                    "inscritos", nuevosInscritos,
                    "voluntariosActuales", actividad.voluntariosActuales
                )
                .addOnSuccessListener {
                    Toast.makeText(this, "Te has inscrito en la actividad", Toast.LENGTH_SHORT).show()
                    cargarDetallesActividad()  // Actualizar UI con la nueva lista de inscritos
                }
        } else {
            Toast.makeText(this, "No hay más espacio en esta actividad", Toast.LENGTH_SHORT).show()
        }
    }
}
