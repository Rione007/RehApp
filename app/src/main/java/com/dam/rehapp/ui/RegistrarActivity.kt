package com.dam.rehapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dam.rehapp.R
import com.dam.rehapp.bd.BDHelper
import com.dam.rehapp.dao.UserDao
import com.dam.rehapp.model.User


class RegistrarActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        // Inicializar BD y DAO
        val dbHelper = BDHelper(this)
        userDao = UserDao(dbHelper)

        // Referencias a los campos del layout (IDs correctos)
        val txtName = findViewById<EditText>(R.id.txtName)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val txtLogin = findViewById<TextView>(R.id.txtLogin)

        // Acción del botón Registrar
        btnRegistrar.setOnClickListener {
            val name = txtName.text.toString().trim()
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val nuevoUser = User(0, name, email, password)
                val registrado = userDao.registerUser(nuevoUser)

                if (registrado) {
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "El usuario ya existe o hubo un error", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Volver al login
        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
