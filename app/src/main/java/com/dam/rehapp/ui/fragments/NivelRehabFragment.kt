package com.dam.rehapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.rehapp.R
import com.dam.rehapp.helpers.ProgresoStorage
import com.dam.rehapp.model.EstadoNivel
import com.dam.rehapp.model.NivelRehab
import com.dam.rehapp.model.Rehab
import com.dam.rehapp.ui.adapter.NivelAdapter

class NivelRehabFragment : Fragment() {

    private lateinit var recyclerNiveles: RecyclerView
    private lateinit var niveles: MutableList<NivelRehab>
    private var rehabId: Int = 0
    private var usuarioId: String = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rehabId = arguments?.getInt("rehabId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_nivel_rehab, container, false)

        recyclerNiveles = view.findViewById(R.id.recyclerNiveles)

        // 🧩 Obtener usuario actual
        val prefsUser = requireContext().getSharedPreferences("rehapp_user", Context.MODE_PRIVATE)
        usuarioId = prefsUser.getString("usuario_id", "default") ?: "default"

        // 🔄 Cargar niveles con progreso del usuario
        niveles = cargarNivelesConProgresos(rehabId, usuarioId)

        recyclerNiveles.layoutManager = LinearLayoutManager(requireContext())
        recyclerNiveles.adapter = NivelAdapter(niveles) { nivelSeleccionado ->
            if (nivelSeleccionado.estado != EstadoNivel.BLOQUEADO) {
                val detalleFragment = DetalleNivelFragment.newInstance(nivelSeleccionado)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detalleFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // 🔁 Recargar niveles cada vez que se vuelve desde el detalle
        niveles = cargarNivelesConProgresos(rehabId, usuarioId)
        (recyclerNiveles.adapter as? NivelAdapter)?.updateData(niveles)
    }

    /**
     * Carga los niveles según el progreso guardado del usuario actual.
     */
    private fun cargarNivelesConProgresos(rehabId: Int, usuarioId: String): MutableList<NivelRehab> {
        val niveles = NivelRehab.getNiveles().filter { it.rehabId == rehabId }.toMutableList()
        val progresos = ProgresoStorage.obtenerProgresos(requireContext(), usuarioId)

        niveles.forEach { nivel ->
            val progreso = progresos.find { it.nivelId == nivel.id }
            if (progreso != null) {
                nivel.progreso = progreso.progreso
                nivel.estado = if (progreso.progreso >= 100) {
                    EstadoNivel.COMPLETADO
                } else {
                    EstadoNivel.DISPONIBLE
                }
            } else {
                nivel.progreso = 0
                nivel.estado = EstadoNivel.BLOQUEADO
            }
        }

        // 🔓 El primer nivel siempre disponible
        if (niveles.isNotEmpty() && niveles[0].estado == EstadoNivel.BLOQUEADO) {
            niveles[0].estado = EstadoNivel.DISPONIBLE
        }

        // 🔓 Desbloquear el siguiente nivel tras completar uno
        for (i in 0 until niveles.size - 1) {
            val actual = niveles[i]
            val siguiente = niveles[i + 1]
            if (actual.estado == EstadoNivel.COMPLETADO && siguiente.estado == EstadoNivel.BLOQUEADO) {
                siguiente.estado = EstadoNivel.DISPONIBLE
            }
        }

        return niveles
    }

    companion object {
        fun newInstance(rehab: Rehab): NivelRehabFragment {
            val fragment = NivelRehabFragment()
            val bundle = Bundle()
            bundle.putInt("rehabId", rehab.id)
            fragment.arguments = bundle
            return fragment
        }
    }
}
