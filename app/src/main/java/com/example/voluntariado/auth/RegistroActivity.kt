package com.example.voluntariado.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Usuario
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etUbicacion = findViewById<EditText>(R.id.etUbicacion)
        val etIntereses = findViewById<EditText>(R.id.etIntereses)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val nombre = etNombre.text.toString()
            val ubicacion = etUbicacion.text.toString()
            val intereses = etIntereses.text.toString().split(",").map { it.trim() }

            if (email.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty()) {
                registrarUsuario(email, password, nombre, ubicacion, intereses)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(email: String, password: String, nombre: String, ubicacion: String, intereses: List<String>) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val nuevoUsuario = Usuario(
                        uid = uid,
                        email = email,
                        nombre = nombre,
                        ubicacion = ubicacion,
                        intereses = intereses,
                        inscripciones = emptyList()
                    )
                    firebaseHelper.agregarUsuario(nuevoUsuario)
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Regresa a la pantalla de inicio de sesión
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
