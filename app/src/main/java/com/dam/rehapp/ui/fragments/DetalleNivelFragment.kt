package com.dam.rehapp.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
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
import com.dam.rehapp.model.NivelRehab
import androidx.core.net.toUri
import com.dam.rehapp.helpers.ProgresoStorage
import java.util.logging.Handler

class DetalleNivelFragment : Fragment() {

    private lateinit var nivel: NivelRehab
    private lateinit var mediaController: MediaController
    private lateinit var videoNivel: VideoView

    private var videoPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nivel = arguments?.getSerializable("nivel") as NivelRehab
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalle_nivel, container, false)

        val txtTitulo = view.findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcion)
        val txtInstrucciones = view.findViewById<TextView>(R.id.txtInstrucciones)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        videoNivel = view.findViewById(R.id.videoNivel)

        txtTitulo.text = nivel.titulo
        txtDescripcion.text = nivel.descripcion
        progressBar.progress = nivel.progreso

        // (Opcional) Podrías adaptar las instrucciones según el nivel
        txtInstrucciones.text = when (nivel.id) {
            1 -> "- Realiza movimientos suaves\n- Mantén el equilibrio"
            2 -> "- Eleva el brazo lentamente\n- Haz 10 repeticiones"
            else -> "- Sigue las indicaciones del video"
        }

        mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoNivel)
        videoNivel.setMediaController(mediaController)

        val videoUri = "android.resource://${requireContext().packageName}/${R.raw.nivel1}".toUri()
        videoNivel.setVideoURI(videoUri)
        videoNivel.requestFocus()

        // Reproduce automáticamente
        videoNivel.setOnPreparedListener {
            it.isLooping = false
            mediaController.show(0)

        }
        videoNivel.setOnCompletionListener {

            Toast.makeText(requireContext(), "¡Nivel completado! 🎉", Toast.LENGTH_SHORT).show()
        }

        val handler = android.os.Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (videoNivel.isPlaying) {
                    val progreso = (videoNivel.currentPosition * 100) / videoNivel.duration
                    progressBar.progress = progreso
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)



        return view
    }

    override fun onPause() {
        super.onPause()
        videoPosition = videoNivel.currentPosition
        val prefs = requireContext().getSharedPreferences("rehapp_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("posicion_${nivel.id}", videoPosition).apply()
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireContext().getSharedPreferences("rehapp_prefs", Context.MODE_PRIVATE)
        val posicionGuardada = prefs.getInt("posicion_${nivel.id}", 0)
        videoNivel.seekTo(posicionGuardada)
        videoNivel.start()
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
