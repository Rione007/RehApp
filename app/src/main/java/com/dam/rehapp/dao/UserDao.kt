package com.dam.rehapp.dao

import android.content.ContentValues
import android.database.Cursor
import com.dam.rehapp.bd.BDHelper
import com.dam.rehapp.model.User

class UserDao(private val dbHelper: BDHelper) {

    fun registerUser(user: User): Boolean {
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(user.email))

        // Verificar si ya existe
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            return false
        }

        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
        }

        val result = db.insert("users", null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, password: String): User? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM users WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            )
        }

        cursor.close()
        db.close()
        return user
    }
}
