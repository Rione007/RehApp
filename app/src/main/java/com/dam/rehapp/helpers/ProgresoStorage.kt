package com.dam.rehapp.helpers

import android.content.Context
import com.dam.rehapp.data.model.ProgresoNivel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ProgresoStorage {

    /**
     * Guarda el progreso de los niveles por usuario.
     */
    fun guardarProgresos(context: Context, lista: List<ProgresoNivel>, usuarioId: String) {
        val prefs = context.getSharedPreferences("rehapp_progresos_$usuarioId", Context.MODE_PRIVATE)
        val json = Gson().toJson(lista)
        prefs.edit().putString("niveles_progreso", json).apply()
    }

    /**
     * Obtiene el progreso de los niveles del usuario actual.
     */
    fun obtenerProgresos(context: Context, usuarioId: String): MutableList<ProgresoNivel> {
        val prefs = context.getSharedPreferences("rehapp_progresos_$usuarioId", Context.MODE_PRIVATE)
        val json = prefs.getString("niveles_progreso", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<ProgresoNivel>>() {}.type
            Gson().fromJson(json, type)
        } else mutableListOf()
    }
}
