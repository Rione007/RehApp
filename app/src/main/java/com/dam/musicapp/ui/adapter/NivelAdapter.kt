package com.dam.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dam.musicapp.R
import com.dam.musicapp.model.EstadoNivel
import com.dam.musicapp.model.NivelRehab

class NivelAdapter(private val niveles: List<NivelRehab>) :
    RecyclerView.Adapter<NivelAdapter.NivelViewHolder>() {

    inner class NivelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.txtTituloNivel)
        val subtitulo = itemView.findViewById<TextView>(R.id.txtDescripcionNivel)
        val progreso = itemView.findViewById<ProgressBar>(R.id.progresoNivel)
        val iconNivel = itemView.findViewById<ImageView>(R.id.iconNivel)
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

        when (nivel.estado) {
            EstadoNivel.COMPLETADO -> {
                holder.iconNivel.setImageResource(R.drawable.ic_check)
                holder.itemView.alpha = 1f
                holder.iconNivel.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
            EstadoNivel.DISPONIBLE -> {
                holder.iconNivel.setImageResource(R.drawable.ic_play)
                holder.itemView.alpha = 1f
                holder.iconNivel.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
            EstadoNivel.BLOQUEADO -> {
                holder.iconNivel.setImageResource(R.drawable.ic_lock)
                holder.itemView.alpha = 0.4f
                holder.iconNivel.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.placeholder_dark))
            }
        }
    }

    override fun getItemCount() = niveles.size
}
