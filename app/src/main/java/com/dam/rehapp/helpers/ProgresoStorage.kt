package com.dam.rehapp.helpers

import android.content.Context
import com.dam.rehapp.data.model.ProgresoNivel
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


object ProgresoStorage {

    private const val PREFS_NAME = "rehapp_prefs"
    private const val KEY_PROGRESOS = "niveles_progreso"

    fun guardarProgresos(context: Context, lista: List<ProgresoNivel>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(lista)
        prefs.edit().putString(KEY_PROGRESOS, json).apply()
    }

    fun obtenerProgresos(context: Context): MutableList<ProgresoNivel> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_PROGRESOS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<ProgresoNivel>>() {}.type
            Gson().fromJson(json, type)
        } else mutableListOf()
    }
}
