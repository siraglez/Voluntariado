package com.example.voluntariado.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voluntariado.R
import com.example.voluntariado.model.Actividad

class ActividadAdapter(private val actividades: List<Actividad>) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        // TODO: Configurar los datos de cada actividad
    }

    override fun getItemCount() = actividades.size

    class ActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TODO: Referenciar los elementos del layout
    }
}