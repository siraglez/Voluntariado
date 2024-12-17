package com.example.voluntariado.view

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
    private lateinit var tvEmptyState: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_actividad_voluntariado, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        firebaseHelper = FirebaseHelper()

        // Obtener el rol desde los argumentos
        val rol = arguments?.getString("ROL")

        // Cargar las actividades desde Firebase
        cargarActividades()

        return view
    }

    private fun cargarActividades() {
        firebaseHelper.obtenerActividades { actividades ->
            listaActividades.clear()
            listaActividades.addAll(actividades)

            // Si hay actividades, muestra el RecyclerView y oculta el estado vacío
            if (actividades.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                tvEmptyState.visibility = View.GONE
                if (::adapter.isInitialized) {
                    adapter.notifyDataSetChanged()
                } else {
                    adapter = ActividadAdapter(actividades) { actividad ->
                        mostrarDetallesActividad(actividad)
                    }
                    recyclerView.adapter = adapter
                }
            } else {
                // Si no hay actividades, muestra el estado vacío
                recyclerView.visibility = View.GONE
                tvEmptyState.visibility = View.VISIBLE
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
            // Recargar la lista cuando se haya agregado una nueva actividad
            cargarActividades()
        }
    }
}
