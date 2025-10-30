package com.dam.rehapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dam.rehapp.R
import com.dam.rehapp.model.EstadoNivel
import com.dam.rehapp.model.NivelRehab

class NivelAdapter(
    private val niveles: MutableList<NivelRehab>,
    private val onItemClick: (NivelRehab) -> Unit
) : RecyclerView.Adapter<NivelAdapter.NivelViewHolder>() {

    inner class NivelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo = itemView.findViewById<TextView>(R.id.txtTituloNivel)
        private val subtitulo = itemView.findViewById<TextView>(R.id.txtDescripcionNivel)
        private val progreso = itemView.findViewById<ProgressBar>(R.id.progresoNivel)
        private val iconNivel = itemView.findViewById<ImageView>(R.id.iconNivel)

        fun bind(nivel: NivelRehab) {
            titulo.text = nivel.titulo
            subtitulo.text = nivel.descripcion
            progreso.progress = nivel.progreso

            when (nivel.estado) {
                EstadoNivel.COMPLETADO -> {
                    iconNivel.setImageResource(R.drawable.ic_check)
                    itemView.alpha = 1f
                    iconNivel.setColorFilter(ContextCompat.getColor(itemView.context, R.color.white))
                }
                EstadoNivel.DISPONIBLE -> {
                    iconNivel.setImageResource(R.drawable.ic_play)
                    itemView.alpha = 1f
                    iconNivel.setColorFilter(ContextCompat.getColor(itemView.context, R.color.white))
                }
                EstadoNivel.BLOQUEADO -> {
                    iconNivel.setImageResource(R.drawable.ic_lock)
                    itemView.alpha = 0.4f
                    iconNivel.setColorFilter(
                        ContextCompat.getColor(itemView.context, R.color.placeholder_dark)
                    )
                }
            }

            itemView.setOnClickListener {
                if (nivel.estado != EstadoNivel.BLOQUEADO) {
                    onItemClick(nivel)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NivelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nivel, parent, false)
        return NivelViewHolder(view)
    }

    override fun onBindViewHolder(holder: NivelViewHolder, position: Int) {
        holder.bind(niveles[position])
    }

    override fun getItemCount() = niveles.size

    fun updateData(nuevos: List<NivelRehab>) {
        niveles.clear()
        niveles.addAll(nuevos)
        notifyDataSetChanged()
    }
}
