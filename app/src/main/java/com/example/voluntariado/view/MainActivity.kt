package com.example.voluntariado.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()

        val rol = intent.getStringExtra("ROL")
        if (rol == "admin") {
            mostrarInterfazAdministrador()
        } else {
            mostrarInterfazUsuario()
        }
    }

    private fun mostrarInterfazAdministrador() {
        // Configurar el fragmento para administradores
        cargarFragment(ActividadVoluntariadoFragment())

        // Configurar el botón para agregar actividades
        val btnAgregarActividad = findViewById<Button>(R.id.btnAgregarActividad)
        btnAgregarActividad.setOnClickListener {
            val intent = Intent(this, AgregarActividadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarInterfazUsuario() {
        // Configurar el fragmento para usuarios normales
        cargarFragment(ActividadVoluntariadoFragment())
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
