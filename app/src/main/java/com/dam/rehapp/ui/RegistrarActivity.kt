package com.dam.rehapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dam.rehapp.R

class RegistrarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val txtLogin = findViewById<TextView>(R.id.txtLogin)

        // Botón de registro (más adelante implementarás Firebase o tu lógica)
        btnRegistrar.setOnClickListener {
            // Aquí puedes registrar al usuario o mostrar un mensaje
        }

        // Texto para volver al Login
        txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
