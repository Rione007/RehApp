package com.dam.musicapp.model

class User {
    var id: Int = 0
    var name: String = ""
    var user: String = ""
    var password: String = ""

    constructor(id: Int, name: String, user: String, password: String){
        this.id = id
        this.name = name
        this.user = user
        this.password = password
    }

    companion object {
        fun getUsers(): List<User> {
            return listOf(
                User(1, "Anderson Villegas", "admin", "1234")
            )
        }
    }
}