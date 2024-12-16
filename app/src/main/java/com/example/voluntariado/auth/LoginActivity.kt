package com.example.voluntariado.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.view.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firestore = FirebaseFirestore.getInstance()

        // Inicializar vistas
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Botón de Iniciar Sesión
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                iniciarSesion(email, password)
            }
        }

        // Botón de Registrar
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion(email: String, password: String) {
        // Buscar el usuario por el campo 'email' en Firestore
        firestore.collection("usuarios")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val contrasenaGuardada = document.getString("contrasena")
                    val rol = document.getString("rol")

                    if (contrasenaGuardada == password) {
                        // Redirigir según el rol
                        when (rol) {
                            "admin" -> {
                                Toast.makeText(this, "Bienvenido Admin", Toast.LENGTH_SHORT).show()
                                navegarMainActivity("admin")
                            }
                            "usuario" -> {
                                Toast.makeText(this, "Bienvenido Usuario", Toast.LENGTH_SHORT).show()
                                navegarMainActivity("usuario")
                            }
                            else -> Toast.makeText(this, "Rol desconocido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun navegarMainActivity(rol: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ROL", rol)
        startActivity(intent)
        finish()
    }
}
