package com.example.voluntariado.view

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

        auth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            firebaseHelper.obtenerUsuario(currentUser.uid) { usuario ->
                if (usuario != null) {
                    // Mostrar la información del usuario o hacer algo con los datos
                }
            }
        }
    }
}