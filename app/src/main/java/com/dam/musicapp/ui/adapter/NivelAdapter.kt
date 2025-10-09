package com.dam.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dam.musicapp.R
import com.dam.musicapp.model.NivelRehab

class NivelAdapter(private val niveles: List<NivelRehab>) :
    RecyclerView.Adapter<NivelAdapter.NivelViewHolder>() {

    inner class NivelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.txtTituloNivel)
        val subtitulo = itemView.findViewById<TextView>(R.id.txtDescripcionNivel)
        val progreso = itemView.findViewById<ProgressBar>(R.id.progresoNivel)
        val imgEstado = itemView.findViewById<ImageView>(R.id.iconNivel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NivelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nivel, parent, false)
        return NivelViewHolder(view)
    }

    override fun onBindViewHolder(holder: NivelViewHolder, position: Int) {
        val nivel = niveles[position]
        holder.titulo.text = nivel.titulo
        holder.subtitulo.text = nivel.descripcion
        holder.progreso.progress = nivel.progreso

        if (nivel.desbloqueado) {
            holder.imgEstado.setImageResource(R.drawable.ic_check)
        } else {
            holder.imgEstado.setImageResource(R.drawable.ic_lock)
            holder.itemView.alpha = 0.5f
        }
    }

    override fun getItemCount() = niveles.size
}
