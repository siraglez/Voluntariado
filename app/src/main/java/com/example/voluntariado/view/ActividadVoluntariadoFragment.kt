package com.example.voluntariado.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voluntariado.R
import com.example.voluntariado.adapter.ActividadAdapter
import com.example.voluntariado.data.FirebaseHelper
import com.example.voluntariado.model.Actividad

class ActividadVoluntariadoFragment : Fragment() {

    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActividadAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_actividad_voluntariado, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        firebaseHelper = FirebaseHelper()

        // Cargar actividades desde Firestore
        cargarActividades()

        return view
    }

    private fun cargarActividades() {
        firebaseHelper.obtenerActividades { actividades ->
            if (actividades.isNotEmpty()) {
                configurarAdaptador(actividades)
            } else {
                Toast.makeText(requireContext(), "No hay actividades disponibles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarAdaptador(actividades: List<Actividad>) {
        // Crear el adaptador y configurar los clics en las actividades
        adapter = ActividadAdapter(actividades) { actividad ->
            mostrarDetallesActividad(actividad)
        }
        recyclerView.adapter = adapter
    }

    private fun mostrarDetallesActividad(actividad: Actividad) {
        // Redirigir al usuario a la pantalla de detalles de la actividad
        val intent = Intent(requireContext(), DetallesActividadActivity::class.java)
        intent.putExtra("ACTIVIDAD_ID", actividad.id)
        intent.putExtra("ROL", requireActivity().intent.getStringExtra("ROL"))
        startActivity(intent)
    }
}
