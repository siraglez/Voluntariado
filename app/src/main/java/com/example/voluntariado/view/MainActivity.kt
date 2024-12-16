package com.example.voluntariado.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voluntariado.R
import com.example.voluntariado.adapter.ActividadAdapter
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Actividad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var firestore: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var actividadAdapter: ActividadAdapter
    private val listaActividades = mutableListOf<Actividad>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener el rol del usuario (admin o usuario normal)
        val rol = intent.getStringExtra("ROL")

        auth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()
        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(listaActividades) { actividad ->
            val intent = Intent(this, DetallesActividadActivity::class.java)
            intent.putExtra("ACTIVIDAD_ID", actividad.id)
            intent.putExtra("ROL", rol)
            startActivity(intent)
        }
        recyclerView.adapter = actividadAdapter

        // Mostrar la interfaz adecuada según el rol
        if (rol == "admin") {
            mostrarInterfazAdministrador()
        } else {
            mostrarInterfazUsuario()
        }

        // Cargar las actividades desde Firestore
        cargarActividades(rol)
    }

    private fun mostrarInterfazAdministrador() {
        // Mostrar el botón "Agregar Actividad" solo para administradores
        val btnAgregarActividad = findViewById<Button>(R.id.btnAgregarActividad)
        btnAgregarActividad.visibility = View.VISIBLE

        btnAgregarActividad.setOnClickListener {
            val intent = Intent(this, AgregarActividadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarInterfazUsuario() {
        // Ocultar el botón "Agregar Actividad" para usuarios normales
        val btnAgregarActividad = findViewById<Button>(R.id.btnAgregarActividad)
        btnAgregarActividad.visibility = View.GONE
    }

    private fun cargarActividades(rol: String?) {
        firestore.collection("actividades").get()
            .addOnSuccessListener { result ->
                val nuevasActividades = result.map { it.toObject(Actividad::class.java) }
                actividadAdapter = ActividadAdapter(nuevasActividades) { actividad ->
                    val intent = Intent(this, DetallesActividadActivity::class.java)
                    intent.putExtra("ACTIVIDAD_ID", actividad.id)
                    intent.putExtra("ROL", rol)
                    startActivity(intent)
                }
                recyclerView.adapter = actividadAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar actividades: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
