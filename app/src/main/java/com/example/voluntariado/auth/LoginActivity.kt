package com.example.voluntariado.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.view.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authHelper = AuthHelper()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            iniciarSesion(email, password)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion(email: String, password: String) {
        authHelper.iniciarSesion(email, password) { user ->
            if (user != null) {
                FirebaseFirestore.getInstance().collection("usuarios")
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val rol = document.getString("rol")
                            if (rol == "admin") {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("ROL", "admin")
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("ROL", "usuario")
                                startActivity(intent)
                            }
                            finish()
                        } else {
                            Toast.makeText(this, "Usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al verificar el rol del usuario", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}