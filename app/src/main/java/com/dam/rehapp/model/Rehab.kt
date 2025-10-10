package com.dam.rehapp.model

import com.dam.rehapp.R

class Rehab {
    var id: Int = 0
    var name: String = ""
    var imgRes: Int = 0

    constructor(id: Int, name: String, imgRes: Int) {
        this.id = id
        this.name = name
        this.imgRes = imgRes
    }

    companion object{
        fun getRehabs(): List<Rehab>{
            return listOf(
                Rehab(1,"Rehabilitacion de rodilla", R.drawable.ic_rodilla),
                Rehab(2,"Rehabilitacion de hombro",R.drawable.ic_hombro),
                Rehab(3,"Rehabilitacion de espalda",R.drawable.ic_espalda),
                Rehab(4,"Rehabilitacion de tobillo",R.drawable.ic_tobillo)
            )
        }
    }
}