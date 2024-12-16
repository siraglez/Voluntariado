package com.example.voluntariado.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etUbicacion = findViewById<EditText>(R.id.etUbicacion)
        val etIntereses = findViewById<EditText>(R.id.etIntereses)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val ubicacion = etUbicacion.text.toString().trim()
            val intereses = etIntereses.text.toString().split(",").map { it.trim() }

            if (email.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty() && ubicacion.isNotEmpty()) {
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
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    guardarUsuarioEnFirestore(uid, email, nombre, ubicacion, intereses)
                } else {
                    Toast.makeText(this, "Error al registrar usuario: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun guardarUsuarioEnFirestore(uid: String, email: String, nombre: String, ubicacion: String, intereses: List<String>) {
        val usuario = Usuario(
            uid = uid,
            email = email,
            nombre = nombre,
            ubicacion = ubicacion,
            intereses = intereses,
            inscripciones = emptyList(),
            rol = "usuario" // Todos los usuarios registrados son normales por defecto
        )

        firestore.collection("usuarios").document(uid).set(usuario)
            .addOnSuccessListener {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
