package com.dam.rehapp.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dam.rehapp.R
import com.dam.rehapp.bd.BDHelper
import com.dam.rehapp.dao.UserDao
import com.dam.rehapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 Inicializar base de datos
        val dbHelper = BDHelper(requireContext())
        userDao = UserDao(dbHelper)

        // 🔹 Obtener datos del usuario logueado desde SharedPreferences
        val prefs = requireContext().getSharedPreferences("rehapp_user", Context.MODE_PRIVATE)
        val nombre = prefs.getString("nombre", "Usuario no encontrado")
        val email = prefs.getString("email", "Correo no disponible")

        // 🔹 Mostrar solo nombre y correo
        binding.tvName.text = nombre
        binding.subtitleEmail.text = email

        // 🔹 Ocultar los demás campos
        binding.tvRole.visibility = View.GONE


        // 🔹 Imagen por defecto
        Glide.with(this)
            .load(R.drawable.ic_cat)
            .circleCrop()
            .into(binding.imgAvatar)

        // 🔹 Acción: cambiar contraseña
        binding.iconPassword.setOnClickListener {
            mostrarDialogoCambioContrasena(email ?: "")
        }

        // 🔹 Acción: cerrar sesión
        binding.iconLogout.setOnClickListener {
            prefs.edit().clear().apply()
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    /**
     * Muestra un diálogo para cambiar la contraseña
     */
    private fun mostrarDialogoCambioContrasena(email: String) {
        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)
        val inputOld = layout.findViewById<EditText>(R.id.txtOldPassword)
        val inputNew = layout.findViewById<EditText>(R.id.txtNewPassword)

        AlertDialog.Builder(requireContext())
            .setTitle("Cambiar contraseña")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val oldPass = inputOld.text.toString().trim()
                val newPass = inputNew.text.toString().trim()

                if (oldPass.isNotEmpty() && newPass.isNotEmpty()) {
                    val exito = userDao.updatePassword(email, oldPass, newPass)
                    if (exito) {
                        Toast.makeText(requireContext(), "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Contraseña anterior incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Complete ambos campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
