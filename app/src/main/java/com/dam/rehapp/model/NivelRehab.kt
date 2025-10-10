package com.dam.rehapp.model

enum class EstadoNivel {
    COMPLETADO, DISPONIBLE, BLOQUEADO
}
class NivelRehab(
    val id: Int,
    val rehabId: Int,
    val titulo: String,
    val descripcion: String,
    val progreso: Int,
    val estado: EstadoNivel


) {
    companion object {
        fun getNiveles(): List<NivelRehab> {
            return listOf(
                NivelRehab(1, 1, "Introducción", "Nivel 1", 100, EstadoNivel.COMPLETADO),
                NivelRehab(2, 1, "Movilidad", "Nivel 2", 60, EstadoNivel.DISPONIBLE),
                NivelRehab(3, 2, "Fuerza", "Nivel 3", 0, EstadoNivel.DISPONIBLE),
                NivelRehab(4, 2, "Resistencia", "Nivel 4", 0, EstadoNivel.BLOQUEADO),
                NivelRehab(5, 3, "Equilibrio", "Nivel 5", 0, EstadoNivel.BLOQUEADO)
            )
        }
    }
}
