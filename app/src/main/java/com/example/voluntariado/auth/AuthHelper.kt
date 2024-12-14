package com.example.voluntariado.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Función para registrarse
    fun registrarse(email: String, password: String, callback: (FirebaseUser?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(auth.currentUser)
                } else {
                    callback(null)
                }
            }
    }

    // Función para iniciar sesión
    fun iniciarSesion(email: String, password: String, callback: (FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(auth.currentUser)
                } else {
                    callback(null)
                }
            }
    }

    // Función para obtener el usuario actual
    fun obtenerUsuarioActual(): FirebaseUser? {
        return auth.currentUser
    }
}