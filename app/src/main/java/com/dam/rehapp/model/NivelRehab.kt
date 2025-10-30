package com.dam.rehapp.model

import java.io.Serializable

enum class EstadoNivel {
    COMPLETADO,
    DISPONIBLE,
    BLOQUEADO
}

class NivelRehab(
    val id: Int,
    val rehabId: Int,           // ID del tipo de rehabilitación (1=rodilla, 2=hombro, etc.)
    val titulo: String,         // Nombre del nivel
    val descripcion: String,    // Descripción breve o número del nivel
    val duracionSegundos: Int,  // Duración del video o actividad
    var progreso: Int,          // Porcentaje completado
    var estado: EstadoNivel     // Estado actual del nivel
) : Serializable {

    companion object {
        fun getNiveles(): List<NivelRehab> {
            val niveles = mutableListOf<NivelRehab>()
            val nombres = listOf(
                "Introducción",
                "Movilidad",
                "Fuerza",
                "Resistencia",
                "Equilibrio"
            )

            var id = 1
            // 🔁 Crear los 5 niveles para cada tipo de rehabilitación (1 a 4)
            for (rehabId in 1..4) {
                for ((index, nombre) in nombres.withIndex()) {
                    niveles.add(
                        NivelRehab(
                            id = id++,
                            rehabId = rehabId,
                            titulo = nombre,
                            descripcion = "Nivel ${index + 1}",
                            duracionSegundos = 90,
                            progreso = 0,
                            estado = EstadoNivel.BLOQUEADO
                        )
                    )
                }
            }
            return niveles
        }
    }
}
