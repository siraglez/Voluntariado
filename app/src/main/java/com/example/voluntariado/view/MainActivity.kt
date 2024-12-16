package com.example.voluntariado.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener el rol del usuario (admin o usuario normal)
        val rol = intent.getStringExtra("ROL")

        auth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()
        firestore = FirebaseFirestore.getInstance()

        // Mostrar la interfaz adecuada según el rol
        mostrarInterfaz(rol)
    }

    private fun mostrarInterfaz(rol: String?) {
        // Cargar el fragmento de actividades
        val fragment = ActividadVoluntariadoFragment()

        // Pasa el rol al fragmento
        val bundle = Bundle()
        bundle.putString("ROL", rol)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        // Configurar el botón "Agregar Actividad" solo para administradores
        val btnAgregarActividad = findViewById<Button>(R.id.btnAgregarActividad)
        if (rol == "admin") {
            btnAgregarActividad.visibility = View.VISIBLE
            btnAgregarActividad.setOnClickListener {
                val intent = Intent(this, AgregarActividadActivity::class.java)
                startActivity(intent)
            }
        } else {
            btnAgregarActividad.visibility = View.GONE
        }
    }
}
