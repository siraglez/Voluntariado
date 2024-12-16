package com.example.voluntariado.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Actividad

class DetallesActividadActivity : AppCompatActivity() {

    private lateinit var tvNombre: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvEstado: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvVoluntariosActuales: TextView
    private lateinit var tvVoluntariosMax: TextView
    private lateinit var tvInscritos: TextView
    private lateinit var btnInscribirse: Button
    private lateinit var btnVolver: Button

    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_actividad)

        tvNombre = findViewById(R.id.tvNombre)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvEstado = findViewById(R.id.tvEstado)
        tvFecha = findViewById(R.id.tvFecha)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        tvVoluntariosActuales = findViewById(R.id.tvVoluntariosActuales)
        tvVoluntariosMax = findViewById(R.id.tvVoluntariosMax)
        tvInscritos = findViewById(R.id.tvInscritos)
        btnInscribirse = findViewById(R.id.btnInscribirse)
        btnVolver = findViewById(R.id.btnVolver)

        firebaseHelper = FirebaseHelper()

        // Obtener el ID de la actividad desde el Intent
        val actividadId = intent.getStringExtra("ACTIVIDAD_ID")

        if (actividadId != null) {
            // Obtener los detalles de la actividad
            obtenerDetallesActividad(actividadId)
        } else {
            Toast.makeText(this, "No se encontró la actividad", Toast.LENGTH_SHORT).show()
            finish() // Finaliza la actividad si no se encontró el ID
        }

        // Configurar el botón "Volver a la lista"
        btnVolver.setOnClickListener {
            onBackPressed() // Volver a la actividad anterior
        }
    }

    private fun obtenerDetallesActividad(actividadId: String) {
        firebaseHelper.obtenerActividadPorId(actividadId) { actividad ->
            if (actividad != null) {
                // Acceder al campo correctamente
                val maxVoluntarios = actividad.voluntariosMax
                // Mostrar el valor de voluntariosMax
                tvVoluntariosMax.text = "Voluntarios Máximos: $maxVoluntarios"

                // Actualizar los demás campos
                tvNombre.text = "Nombre: ${actividad.nombre}"
                tvCategoria.text = "Categoría: ${actividad.categoria}"
                tvDescripcion.text = "Descripción: ${actividad.descripcion}"
                tvEstado.text = "Estado: ${actividad.estado}"
                tvFecha.text = "Fecha: ${actividad.fecha}"
                tvUbicacion.text = "Ubicación: ${actividad.ubicacion}"
                tvVoluntariosActuales.text = "Voluntarios Actuales: ${actividad.voluntariosActuales}"

                // Mostrar inscritos si hay alguna persona inscrita
                if (actividad.inscritos.isNotEmpty()) {
                    tvInscritos.text = "Usuarios inscritos: ${actividad.inscritos.joinToString(", ")}"
                    tvInscritos.visibility = View.VISIBLE
                } else {
                    tvInscritos.visibility = View.GONE
                }

                // Si hay espacio, habilitar el botón de inscripción
                if (actividad.voluntariosActuales < actividad.voluntariosMax) {
                    btnInscribirse.visibility = View.VISIBLE
                    btnInscribirse.setOnClickListener {
                        inscribirseEnActividad(actividad)
                    }
                } else {
                    btnInscribirse.visibility = View.GONE
                }
            } else {
                Toast.makeText(this, "No se pudo obtener la actividad", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inscribirseEnActividad(actividad: Actividad) {
        // Lógica para inscribirse en la actividad
        val usuarioId = "current_user_id"

        // Actualizar la lista de inscritos en Firestore
        firebaseHelper.inscribirUsuarioEnActividad(actividad.id, usuarioId) { exito ->
            if (exito) {
                Toast.makeText(this, "Inscripción realizada con éxito", Toast.LENGTH_SHORT).show()
                // Actualizar los voluntarios actuales
                tvVoluntariosActuales.text = "Voluntarios Actuales: ${actividad.voluntariosActuales + 1}"
                btnInscribirse.visibility = View.GONE // Ocultar el botón después de inscribirse
            } else {
                Toast.makeText(this, "Error al inscribirse", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
