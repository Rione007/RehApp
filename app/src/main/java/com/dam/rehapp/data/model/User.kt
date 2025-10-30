package com.dam.rehapp.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val role: String = "Paciente",
    val age: Int? = null,
    val phone: String? = null,
    val birthDate: String? = null,
    val sex: String? = null,
    val emergencyContact: String? = null,
    val diagnosis: String? = null,
    val limitations: String? = null
)