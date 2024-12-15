package com.example.voluntariado.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rol = intent.getStringExtra("ROL")
        if (rol == "ADMIN") {
            // Mostrar interfaz para administrador
            mostrarInterfazAdministrador()
        } else {
            // Mostrar interfaz para usuario normal
            mostrarInterfazUsuario()
        }
    }

    private fun mostrarInterfazAdministrador() {
        // Permitir agregar actividades
        val intent = Intent(this, AgregarActividadActivity::class.java)
        startActivity(intent)
    }

    private fun mostrarInterfazUsuario() {
        // Mostrar lista de actividades
        val fragment = ActividadVoluntariadoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

}