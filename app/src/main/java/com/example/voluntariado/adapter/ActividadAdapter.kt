package com.example.voluntariado.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voluntariado.R
import com.example.voluntariado.model.Actividad

class ActividadAdapter(
    private var listaActividades: List<Actividad>,
    private val onClick: (Actividad) -> Unit
) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreActividad)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionActividad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = listaActividades[position]
        holder.tvNombre.text = actividad.nombre
        holder.tvDescripcion.text = actividad.descripcion

        holder.itemView.setOnClickListener {
            onClick(actividad) // Manejar clic
        }
    }

    override fun getItemCount(): Int = listaActividades.size

    fun actualizarLista(nuevaLista: List<Actividad>) {
        listaActividades = nuevaLista
        notifyDataSetChanged()
    }
}
