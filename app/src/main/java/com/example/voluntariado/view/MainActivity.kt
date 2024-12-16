package com.example.voluntariado.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        auth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()
        firestore = FirebaseFirestore.getInstance()

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(listaActividades)
        recyclerView.adapter = actividadAdapter

        // Identificar el rol del usuario
        val rol = intent.getStringExtra("ROL")
        if (rol == "admin") {
            mostrarInterfazAdministrador()
        } else {
            mostrarInterfazUsuario()
        }

        // Cargar lista de actividades desde Firestore
        cargarActividades()
    }

    private fun mostrarInterfazAdministrador() {
        // Configurar el fragmento para administradores
        cargarFragment(ActividadVoluntariadoFragment())

        // Configurar el botón para agregar actividades
        val btnAgregarActividad = findViewById<Button>(R.id.btnAgregarActividad)
        btnAgregarActividad.setOnClickListener {
            val intent = Intent(this, AgregarActividadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarInterfazUsuario() {
        // Configurar el fragmento para usuarios normales
        cargarFragment(ActividadVoluntariadoFragment())
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun cargarActividades() {
        firestore.collection("actividades")
            .get()
            .addOnSuccessListener { result ->
                val nuevasActividades = mutableListOf<Actividad>()
                for (document in result) {
                    val actividad = document.toObject(Actividad::class.java)
                    nuevasActividades.add(actividad)
                }
                // Actualizar adaptador con las nuevas actividades
                actividadAdapter.actualizarLista(nuevasActividades)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar actividades: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
