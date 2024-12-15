package com.example.voluntariado.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.voluntariado.R
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Usuario
import com.example.voluntariado.view.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var authHelper: AuthHelper
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authHelper = AuthHelper()
        firebaseHelper = FirebaseHelper()

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
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            registrarUsuario(email, password)
        }
    }

    private fun iniciarSesion(email: String, password: String) {
        authHelper.iniciarSesion(email, password) { user ->
            if (user != null) {
                firebaseHelper.obtenerUsuario(user.uid) { usuario ->
                    if (usuario != null) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("ROL", if (usuario.email == "admin@example.com") "ADMIN" else "USER")
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(email: String, password: String) {
        authHelper.registrarse(email, password) { user ->
            if (user != null) {
                val nuevoUsuario = Usuario(uid = user.uid, email = email)
                firebaseHelper.agregarUsuario(nuevoUsuario)
                Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
