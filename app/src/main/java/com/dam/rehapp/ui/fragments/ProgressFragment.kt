package com.dam.rehapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dam.rehapp.R
import com.dam.rehapp.data.model.ProgresoNivel
import com.dam.rehapp.helpers.ProgresoStorage
import com.dam.rehapp.model.NivelRehab

class ProgressFragment : Fragment() {

    private lateinit var progresoBar: ProgressBar
    private lateinit var tvProgressPercent: TextView
    private lateinit var tvNivelesCompletados: TextView
    private lateinit var tvTiempoTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        progresoBar = view.findViewById(R.id.progresoNivel)
        tvProgressPercent = view.findViewById(R.id.tvProgressPercent)
        tvNivelesCompletados = view.findViewById(R.id.tvNivelesCompletados)
        tvTiempoTotal = view.findViewById(R.id.tvTiempoTotal)

        calcularProgresoGlobal()

        return view
    }

    private fun calcularProgresoGlobal() {
        val prefs = requireContext().getSharedPreferences("rehapp_user", Context.MODE_PRIVATE)
        val usuarioId = prefs.getString("usuario_id", "default") ?: "default"

        val nivelesTotales = NivelRehab.getNiveles()
        val progresos: List<ProgresoNivel> = ProgresoStorage.obtenerProgresos(requireContext(), usuarioId)

        val completados = progresos.count { it.progreso >= 100 }
        val porcentaje = if (nivelesTotales.isNotEmpty()) {
            (completados.toFloat() / nivelesTotales.size * 100).toInt()
        } else 0

        val tiempoTotalMin = progresos.sumOf { it.tiempoTotalMin }
        val horas = tiempoTotalMin / 60
        val minutos = tiempoTotalMin % 60
        val tiempoTexto = if (horas > 0) "${horas}h ${minutos}m" else "${minutos} min"

        progresoBar.progress = porcentaje
        tvProgressPercent.text = "$porcentaje% completado"
        tvNivelesCompletados.text = completados.toString()
        tvTiempoTotal.text = tiempoTexto
    }

    companion object {
        fun newInstance() = ProgressFragment()
    }
}
