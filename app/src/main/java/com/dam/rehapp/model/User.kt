package com.dam.rehapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var email: String = "",// nombre de usuario (tu campo actual)
    var password: String = ""
)