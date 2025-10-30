package com.dam.rehapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.dam.rehapp.R
import com.dam.rehapp.helpers.ProgresoStorage
import com.dam.rehapp.model.NivelRehab
import com.dam.rehapp.model.Rehab

class HomeFragment : Fragment() {

    private lateinit var tvGreetingSmall: TextView
    private lateinit var tvNextSessionSubtitle: TextView
    private lateinit var tvNextSessionTime: TextView
    private lateinit var tvProgressTitle: TextView
    private lateinit var tvProgressPercent: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnStart: Button

    private var rehabId: Int = 1
    private var rehabNombre: String = "Rehabilitación de rodilla"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Enlaces con XML
        tvGreetingSmall = view.findViewById(R.id.tvGreetingSmall)
        tvNextSessionSubtitle = view.findViewById(R.id.tvNextSessionSubtitle)
        tvNextSessionTime = view.findViewById(R.id.tvNextSessionTime)
        tvProgressTitle = view.findViewById(R.id.tvProgressTitle)
        tvProgressPercent = view.findViewById(R.id.tvProgressPercent)
        progressBar = view.findViewById(R.id.progresoNivel)
        btnStart = view.findViewById(R.id.btnStart)

        // Cargar datos iniciales
        cargarDatosUsuario()
        cargarProgreso()

        // Botón iniciar sesión
        btnStart.setOnClickListener {
            val fragment = NivelRehabFragment.newInstance(
                Rehab(rehabId, rehabNombre)
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // 🔄 Recargar datos al volver a este fragment
        cargarDatosUsuario()
        cargarProgreso()
    }

    private fun cargarDatosUsuario() {
        val prefs = requireContext().getSharedPreferences("rehapp_user", Context.MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "Usuario")

        // Verificar si hay una rehabilitación seleccionada
        rehabId = prefs.getInt("rehab_id", 1)
        rehabNombre = prefs.getString("rehab_nombre", "Rehabilitación de rodilla") ?: "Rehabilitación de rodilla"

        // 🧠 Mostrar valores cargados en Logcat para debug
        println("DEBUG 🧩 Rehab actual: ID=$rehabId, Nombre=$rehabNombre")

        // Mostrar datos actualizados
        tvGreetingSmall.text = "Hola, $nombre 👋"
        tvNextSessionSubtitle.text = rehabNombre
        tvNextSessionTime.text = "Hoy, 4:00 PM"
    }

    private fun cargarProgreso() {
        val prefs = requireContext().getSharedPreferences("rehapp_user", Context.MODE_PRIVATE)
        val usuarioId = prefs.getString("usuario_id", "default") ?: "default"

        val progresos = ProgresoStorage.obtenerProgresos(requireContext(), usuarioId)
        val niveles = NivelRehab.getNiveles().filter { it.rehabId == rehabId }

        if (niveles.isEmpty()) return

        var completados = 0
        niveles.forEach { nivel ->
            val p = progresos.find { it.nivelId == nivel.id }
            if (p != null && p.progreso >= 100) completados++
        }

        val porcentaje = (completados.toFloat() / niveles.size * 100).toInt()
        tvProgressTitle.text = rehabNombre
        tvProgressPercent.text = "$porcentaje%"
        progressBar.progress = porcentaje
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}
