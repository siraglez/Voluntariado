package com.example.voluntariado.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Actividad

class AgregarActividadActivity : AppCompatActivity() {

    private lateinit var firebaseHelper: FirebaseHelper

    private lateinit var nombreInput: EditText
    private lateinit var descripcionInput: EditText
    private lateinit var fechaInput: EditText
    private lateinit var ubicacionInput: EditText
    private lateinit var categoriaInput: EditText
    private lateinit var maxVoluntariosInput: EditText
    private lateinit var estadoInput: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_actividad)

        firebaseHelper = FirebaseHelper()

        // Vincular vistas
        nombreInput = findViewById(R.id.etNombre)
        descripcionInput = findViewById(R.id.etDescripcion)
        fechaInput = findViewById(R.id.etFecha)
        ubicacionInput = findViewById(R.id.etUbicacion)
        categoriaInput = findViewById(R.id.etCategoria)
        maxVoluntariosInput = findViewById(R.id.etVoluntariosMax)
        estadoInput = findViewById(R.id.etEstado)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Configurar botón para guardar
        btnGuardar.setOnClickListener {
            guardarActividad()
        }
    }

    private fun guardarActividad() {
        // Obtener valores de los campos
        val nombre = nombreInput.text.toString().trim()
        val descripcion = descripcionInput.text.toString().trim()
        val fecha = fechaInput.text.toString().trim()
        val ubicacion = ubicacionInput.text.toString().trim()
        val categoria = categoriaInput.text.toString().trim()
        val maxVoluntarios = maxVoluntariosInput.text.toString().trim()
        val estado = estadoInput.text.toString().trim()

        // Validar campos obligatorios
        if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() ||
            categoria.isEmpty() || maxVoluntarios.isEmpty() || estado.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear nueva actividad
        val nuevaActividad = Actividad(
            id = "", // Se generará automáticamente
            nombre = nombre,
            descripcion = descripcion,
            fecha = fecha,
            ubicacion = ubicacion,
            categoria = categoria,
            voluntariosMax = maxVoluntarios.toIntOrNull() ?: 0,
            voluntariosActuales = 0, // Por defecto, 0
            estado = estado,
            inscritos = emptyList() // Por defecto, vacío
        )

        // Guardar actividad con ID secuencial
        firebaseHelper.agregarActividadConIdSecuencial(nuevaActividad) { exito ->
            if (exito) {
                Toast.makeText(this, "Actividad agregada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al agregar la actividad", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
