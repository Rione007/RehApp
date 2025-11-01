package com.dam.rehapp.bd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.dam.rehapp.model.User

class UserRepository(context: Context) {
    private val dbHelper = BDHelper(context)

    fun getUserByEmail(email: String): User? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))

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

    fun updateUser(user: User): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
        }
        val result = db.update("users", values, "id = ?", arrayOf(user.id.toString()))
        db.close()
        return result > 0
    }

    fun changePassword(userId: Int, newPassword: String): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("password", newPassword)
        }
        val result = db.update("users", values, "id = ?", arrayOf(userId.toString()))
        db.close()
        return result > 0
    }
}
