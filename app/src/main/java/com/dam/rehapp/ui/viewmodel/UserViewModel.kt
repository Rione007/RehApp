package com.dam.rehapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dam.rehapp.data.model.User
import com.google.firebase.auth.FirebaseAuth

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun loadUserFromFirebase() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val newUser = User(
                uid = it.uid,
                name = it.displayName ?: "",
                email = it.email ?: "",
                photoUrl = it.photoUrl?.toString(),
                role = "Paciente"
            )
            _user.value = newUser
        }
    }

    fun updateUser(newData: User) {
        _user.value = newData
    }
}