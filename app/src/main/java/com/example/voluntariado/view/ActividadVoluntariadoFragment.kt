package com.example.voluntariado.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voluntariado.R
import com.example.voluntariado.adapter.ActividadAdapter
import com.example.voluntariado.data.FirebaseHelper

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
        firebaseHelper.obtenerActividades { actividades ->
            adapter = ActividadAdapter(actividades)
            recyclerView.adapter = adapter
        }
        return view
    }
}