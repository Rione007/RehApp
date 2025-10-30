package com.dam.rehapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dam.rehapp.R
import com.dam.rehapp.databinding.FragmentProfileBinding
import com.dam.rehapp.ui.viewmodel.UserViewModel
import androidx.fragment.app.viewModels

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.loadUserFromFirebase()

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvName.text = user.name
            binding.subtitleEmail.text = user.email
            binding.tvRole.text = user.role
            binding.tvAge.text = user.age?.toString() ?: "N/A"
            binding.subtitleFono.text = user.phone ?: "No registrado"
            binding.subtitleCalendar.text = user.birthDate ?: "No definido"
            binding.subtitleSex.text = user.sex ?: "Sin especificar"
            binding.subtitleEmerg.text = user.emergencyContact ?: "No configurado"
            binding.subtitleDiagn.text = user.diagnosis ?: "Sin diagnóstico"
            binding.subtitleLimit.text = user.limitations ?: "Sin limitaciones"

            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_cat)
                .circleCrop()
                .into(binding.imgAvatar)
        }
    }

    companion object {
        fun newInstance():
                ProfileFragment = ProfileFragment()
    }

}