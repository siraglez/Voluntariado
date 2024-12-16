package com.example.voluntariado.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val listaActividades = mutableListOf<Actividad>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_actividad_voluntariado, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        firebaseHelper = FirebaseHelper()

        // Cargar las actividades desde Firebase
        cargarActividades()

        // Botón de agregar actividad
        val btnAgregarActividad: View = view.findViewById(R.id.btnAgregarActividad)
        btnAgregarActividad.setOnClickListener {
            val intent = Intent(requireContext(), AgregarActividadActivity::class.java)
            startActivityForResult(intent, 1)
        }

        return view
    }

    private fun cargarActividades() {
        firebaseHelper.obtenerActividades { actividades ->
            // Actualizar la lista de actividades y el adaptador
            listaActividades.clear()
            listaActividades.addAll(actividades)
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
            } else {
                // Inicializa el adaptador si aún no se ha creado
                adapter = ActividadAdapter(actividades) { actividad ->
                    mostrarDetallesActividad(actividad)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun mostrarDetallesActividad(actividad: Actividad) {
        val intent = Intent(requireContext(), DetallesActividadActivity::class.java)
        intent.putExtra("ACTIVIDAD_ID", actividad.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Cuando se ha agregado una actividad, recargar la lista
            cargarActividades()
        }
    }
}
