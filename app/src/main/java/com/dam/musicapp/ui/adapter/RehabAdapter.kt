package com.dam.musicapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dam.musicapp.R
import com.dam.musicapp.model.Rehab

class RehabAdapter(private val rehabs:List<Rehab>): RecyclerView.Adapter<RehabAdapter.RehabViewHolder>() {

    class RehabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNameRehab: TextView = itemView.findViewById(R.id.txtNameRehab)
        val icRehabImg: ImageView = itemView.findViewById(R.id.icRehabImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RehabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rehab_card, parent, false)
        return RehabViewHolder(view)
    }

    override fun onBindViewHolder(holder: RehabViewHolder, position: Int) {
        val rehab = rehabs[position]
        holder.txtNameRehab.text = rehab.name
        holder.icRehabImg.setImageResource(rehab.imgRes)
    }

    override fun getItemCount(): Int = rehabs.size
}