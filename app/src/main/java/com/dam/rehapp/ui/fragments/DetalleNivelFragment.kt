package com.dam.rehapp.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.dam.rehapp.R
import com.dam.rehapp.data.model.ProgresoNivel
import com.dam.rehapp.helpers.ProgresoStorage
import com.dam.rehapp.model.NivelRehab
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetalleNivelFragment : Fragment() {

    private lateinit var nivel: NivelRehab
    private lateinit var videoNivel: VideoView
    private lateinit var mediaController: MediaController
    private lateinit var progressBar: ProgressBar
    private var videoPreparado = false
    private var videoPosition = 0
    private var usuarioId: String = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nivel = arguments?.getSerializable("nivel") as NivelRehab
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_detalle_nivel, container, false)

        val txtTitulo = view.findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcion)
        val txtInstrucciones = view.findViewById<TextView>(R.id.txtInstrucciones)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabMarcarCompleto)
        progressBar = view.findViewById(R.id.progressBar)
        videoNivel = view.findViewById(R.id.videoNivel)

        txtTitulo.text = nivel.titulo
        txtDescripcion.text = nivel.descripcion
        progressBar.progress = nivel.progreso

        txtInstrucciones.text = when (nivel.id % 5) {
            1 -> "- Realiza movimientos suaves\n- Mantén el equilibrio"
            2 -> "- Eleva el brazo lentamente\n- Haz 10 repeticiones"
            3 -> "- Trabaja la fuerza con bandas elásticas"
            4 -> "- Incrementa la resistencia con movimientos constantes"
            else -> "- Mejora tu equilibrio manteniendo la postura"
        }

        // 🔐 Obtener ID del usuario activo
        val prefsUser = requireContext().getSharedPreferences("rehapp_user", Context.MODE_PRIVATE)
        usuarioId = prefsUser.getString("usuario_id", "default") ?: "default"

        // 🎬 Configurar VideoView
        mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoNivel)
        videoNivel.setMediaController(mediaController)

// 🧩 Ajuste para que todas las rehabilitaciones usen los mismos videos base
        val numeroVideo = ((nivel.id - 1) % 5) + 1  // valores 1..5
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/raw/nivel$numeroVideo")
        videoNivel.setVideoURI(videoUri)


        // Cuando el video está listo
        videoNivel.setOnPreparedListener { mp ->
            videoPreparado = true
            mp.isLooping = false
            mediaController.show(0)
        }

        // Si ocurre error con el video (archivo faltante o incompatible)
        videoNivel.setOnErrorListener { _, _, _ ->
            videoPreparado = false
            Toast.makeText(requireContext(), "⚠️ No se pudo reproducir nivel${nivel.id}.mp4", Toast.LENGTH_LONG).show()
            true
        }

        // Solo marca completado si el video se reprodujo entero
        videoNivel.setOnCompletionListener {
            if (videoPreparado) marcarCompletado()
        }

        // Botón para marcar manualmente (solo si se reprodujo)
        fab.setOnClickListener {
            if (videoPreparado) marcarCompletado()
            else Toast.makeText(requireContext(), "🎥 Reproduce el video antes de marcarlo.", Toast.LENGTH_SHORT).show()
        }

        // 🔄 Barra de progreso del video
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (videoNivel.isPlaying && videoNivel.duration > 0) {
                    val progreso = (videoNivel.currentPosition * 100) / videoNivel.duration
                    progressBar.progress = progreso
                }
                handler.postDelayed(this, 1000)
            }
        })

        return view
    }

    private fun marcarCompletado() {
        val context = requireContext()
        val progresos = ProgresoStorage.obtenerProgresos(context, usuarioId)

        // ✅ Guardar progreso de este nivel
        val existente = progresos.find { it.nivelId == nivel.id }
        if (existente != null) {
            existente.progreso = 100
        } else {
            progresos.add(ProgresoNivel(nivel.id, 100, System.currentTimeMillis()))
        }

        // 🔓 Desbloquear siguiente nivel solo para este usuario
        val nivelesRehab = NivelRehab.getNiveles().filter { it.rehabId == nivel.rehabId }
        val indice = nivelesRehab.indexOfFirst { it.id == nivel.id }
        if (indice in 0 until nivelesRehab.lastIndex) {
            val siguiente = nivelesRehab[indice + 1]
            if (progresos.none { it.nivelId == siguiente.id }) {
                progresos.add(ProgresoNivel(siguiente.id, 0, System.currentTimeMillis()))
            }
        }

        ProgresoStorage.guardarProgresos(context, progresos, usuarioId)

        Toast.makeText(context, "✅ ¡Nivel ${nivel.id} completado!", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    override fun onPause() {
        super.onPause()
        if (videoNivel.isPlaying) {
            videoPosition = videoNivel.currentPosition
            val prefs = requireContext().getSharedPreferences("rehapp_prefs", Context.MODE_PRIVATE)
            prefs.edit().putInt("posicion_${nivel.id}", videoPosition).apply()
            videoNivel.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireContext().getSharedPreferences("rehapp_prefs", Context.MODE_PRIVATE)
        val posicion = prefs.getInt("posicion_${nivel.id}", 0)
        if (videoPreparado) {
            videoNivel.seekTo(posicion)
            videoNivel.start()
        }
    }

    companion object {
        fun newInstance(nivel: NivelRehab): DetalleNivelFragment {
            val fragment = DetalleNivelFragment()
            val args = Bundle()
            args.putSerializable("nivel", nivel)
            fragment.arguments = args
            return fragment
        }
    }
}
