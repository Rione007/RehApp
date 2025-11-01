package com.dam.rehapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit
import com.dam.rehapp.R
import com.dam.rehapp.bd.BDHelper
import com.dam.rehapp.dao.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
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
        //val btnGoogle = findViewById<LinearLayout>(R.id.btnGoogle)
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

//        // 🔹 LOGIN CON GOOGLE
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        auth = FirebaseAuth.getInstance()
//
//        btnGoogle.setOnClickListener {
//            signInWithGoogle()
//        }

        // Ajuste visual
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    val user = auth.currentUser

                    // ✅ Guardar datos de usuario con ID único
                    val prefs = getSharedPreferences("rehapp_user", MODE_PRIVATE)
                    prefs.edit {
                        putString("usuario_id", "google_${user?.uid}")
                        putString("nombre", user?.displayName)
                        putString("email", user?.email)
                        putString("photo", user?.photoUrl.toString())
                    }

                    Toast.makeText(this, "Bienvenido ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, PanelPrincipalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
