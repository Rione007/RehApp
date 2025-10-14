package com.dam.rehapp.model

import java.io.Serializable

enum class EstadoNivel {
    COMPLETADO, DISPONIBLE, BLOQUEADO
}

class NivelRehab(
    val id: Int,
    val rehabId: Int,
    val titulo: String,
    val descripcion: String,
    val duracionSegundos: Int,
    var progreso: Int,
    var estado: EstadoNivel
) : Serializable {
    companion object {
        fun getNiveles(): List<NivelRehab> {
            return listOf(
                NivelRehab(1, 1, "Introducción", "Nivel 1", 90, 100, EstadoNivel.COMPLETADO),
                NivelRehab(2, 1, "Movilidad", "Nivel 2", 90, 60, EstadoNivel.DISPONIBLE),
                NivelRehab(3, 2, "Fuerza", "Nivel 3", 90, 0, EstadoNivel.DISPONIBLE),
                NivelRehab(4, 2, "Resistencia", "Nivel 4", 90, 0, EstadoNivel.BLOQUEADO),
                NivelRehab(5, 3, "Equilibrio", "Nivel 5", 90, 0, EstadoNivel.BLOQUEADO)
            )
        }
    }
}
