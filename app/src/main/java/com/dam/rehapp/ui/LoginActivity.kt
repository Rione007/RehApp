package com.dam.rehapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.dam.rehapp.R
import com.dam.rehapp.bd.BDHelper
import com.dam.rehapp.dao.UserDao


class LoginActivity : AppCompatActivity() {


    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Base de datos local
        val dbHelper = BDHelper(this)
        userDao = UserDao(dbHelper)

        val txtUser = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoogle = findViewById<LinearLayout>(R.id.btnGoogle)
        val signUpText = findViewById<TextView>(R.id.sign_up)

        // Ir a registro
        signUpText.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }

        // 🔹 LOGIN LOCAL (SQLite)
        btnLogin.setOnClickListener {
            val user = txtUser.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (user.isNotEmpty() && password.isNotEmpty()) {
                val usuario = userDao.loginUser(user, password)
                if (usuario != null) {
                    // ✅ Guardar usuario logueado en SharedPreferences para progreso individual
                    val prefs = getSharedPreferences("rehapp_user", MODE_PRIVATE)
                    prefs.edit {
                        putString("usuario_id", "local_${usuario.id}")
                        putString("nombre", usuario.name)
                        putString("email", usuario.email)
                    }

                    Toast.makeText(this, "Bienvenido ${usuario.name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PanelPrincipalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
