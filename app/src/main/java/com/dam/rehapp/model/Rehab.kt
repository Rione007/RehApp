package com.dam.rehapp.model

import com.dam.rehapp.R

class Rehab(
    var id: Int,
    var name: String,
    var imgRes: Int = R.drawable.ic_rodilla // valor por defecto
) {
    companion object {
        fun getRehabs(): List<Rehab> {
            return listOf(
                Rehab(1, "Rehabilitación de rodilla", R.drawable.ic_rodilla),
                Rehab(2, "Rehabilitación de hombro", R.drawable.ic_hombro),
                Rehab(3, "Rehabilitación de espalda", R.drawable.ic_espalda),
                Rehab(4, "Rehabilitación de tobillo", R.drawable.ic_tobillo)
            )
        }
    }
}
