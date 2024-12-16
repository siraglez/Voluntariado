package com.example.voluntariado.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        firestore = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etUbicacion = findViewById<EditText>(R.id.etUbicacion)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etIntereses = findViewById<EditText>(R.id.etIntereses)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val ubicacion = etUbicacion.text.toString().trim()
            val contrasena = etPassword.text.toString().trim()
            val intereses = etIntereses.text.toString().split(",").map { it.trim() }

            if (email.isNotEmpty() && nombre.isNotEmpty() && ubicacion.isNotEmpty() && contrasena.isNotEmpty()) {
                if (email == "admin@example.com") {
                    registrarUsuario("001", email, nombre, ubicacion, contrasena, intereses, "admin")
                } else {
                    registrarUsuario(null, email, nombre, ubicacion, contrasena, intereses, "usuario")
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(
        uid: String?, // null para dejar que Firestore genere el ID
        email: String,
        nombre: String,
        ubicacion: String,
        contrasena: String,
        intereses: List<String>,
        rol: String
    ) {
        val usuario = Usuario(
            uid = uid ?: "",
            email = email,
            nombre = nombre,
            ubicacion = ubicacion,
            contrasena = contrasena,
            intereses = intereses,
            inscripciones = emptyList(),
            rol = rol
        )

        // Si el UID es nulo, Firestore generará el ID automáticamente
        val documentRef = if (uid != null) {
            firestore.collection("usuarios").document(uid)
        } else {
            firestore.collection("usuarios").document()
        }

        documentRef.set(usuario)
            .addOnSuccessListener {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
