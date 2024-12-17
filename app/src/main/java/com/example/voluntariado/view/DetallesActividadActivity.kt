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
    private lateinit var actividadId: String
    private var rol: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_actividad)

        // Inicializar vistas
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

        // Obtener datos del Intent
        actividadId = intent.getStringExtra("ACTIVIDAD_ID") ?: ""
        rol = intent.getStringExtra("ROL")

        if (actividadId.isNotEmpty()) {
            obtenerDetallesActividad()
        } else {
            Toast.makeText(this, "No se encontró la actividad", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Botón "Volver"
        btnVolver.setOnClickListener {
            onBackPressed()
        }
    }

    private fun obtenerDetallesActividad() {
        firebaseHelper.obtenerActividadPorId(actividadId) { actividad ->
            if (actividad != null) {
                // Mostrar detalles de la actividad
                tvNombre.text = "Nombre: ${actividad.nombre}"
                tvCategoria.text = "Categoría: ${actividad.categoria}"
                tvDescripcion.text = "Descripción: ${actividad.descripcion}"
                tvEstado.text = "Estado: ${actividad.estado}"
                tvFecha.text = "Fecha: ${actividad.fecha}"
                tvUbicacion.text = "Ubicación: ${actividad.ubicacion}"
                tvVoluntariosActuales.text = "Voluntarios Actuales: ${actividad.voluntariosActuales}"
                tvVoluntariosMax.text = "Voluntarios Máximos: ${actividad.voluntariosMax}"

                // Lógica según el rol
                if (rol == "admin") {
                    // Mostrar lista de usuarios inscritos para administradores
                    if (actividad.inscritos.isNotEmpty()) {
                        tvInscritos.text =
                            "Usuarios inscritos: ${actividad.inscritos.joinToString(", ")}"
                        tvInscritos.visibility = View.VISIBLE
                    } else {
                        tvInscritos.text = "No hay usuarios inscritos aún."
                        tvInscritos.visibility = View.VISIBLE
                    }

                    // Ocultar botón de inscripción para administradores
                    btnInscribirse.visibility = View.GONE
                } else {
                    // Ocultar lista de inscritos para usuarios normales
                    tvInscritos.visibility = View.GONE

                    // Mostrar botón de inscripción si hay espacio disponible
                    if (actividad.voluntariosActuales < actividad.voluntariosMax) {
                        btnInscribirse.visibility = View.VISIBLE
                        btnInscribirse.setOnClickListener {
                            inscribirseEnActividad(actividad)
                        }
                    } else {
                        btnInscribirse.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(this, "No se pudo cargar la actividad", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun inscribirseEnActividad(actividad: Actividad) {
        val usuarioId = firebaseHelper.obtenerUsuarioActualId()

        if (usuarioId != null) {
            firebaseHelper.inscribirUsuarioEnActividad(actividadId, usuarioId) { exito ->
                if (exito) {
                    Toast.makeText(
                        this,
                        "Te has inscrito correctamente en la actividad",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnInscribirse.visibility = View.GONE
                    tvVoluntariosActuales.text =
                        "Voluntarios Actuales: ${actividad.voluntariosActuales + 1}"
                } else {
                    Toast.makeText(this, "Error al inscribirse", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}
