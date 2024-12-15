package com.example.voluntariado.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Actividad
import com.google.firebase.firestore.FirebaseFirestore

class AgregarActividadActivity : AppCompatActivity() {

    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_actividad)

        firebaseHelper = FirebaseHelper()
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val nombreInput = findViewById<EditText>(R.id.etNombre)
        val descripcionInput = findViewById<EditText>(R.id.etDescripcion)
        val fechaInput = findViewById<EditText>(R.id.etFecha)
        val maxVoluntariosInput = findViewById<EditText>(R.id.etVoluntariosMax)

        btnGuardar.setOnClickListener {
            val nuevaActividad = Actividad(
                id = FirebaseFirestore.getInstance().collection("actividades").document().id,
                nombre = nombreInput.text.toString(),
                descripcion = descripcionInput.text.toString(),
                fecha = fechaInput.text.toString(),
                voluntariosMax = maxVoluntariosInput.text.toString().toInt(),
                estado = "Activa"
            )
            firebaseHelper.agregarActividad(nuevaActividad)
            Toast.makeText(this, "Actividad agregada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
