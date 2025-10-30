package com.dam.rehapp.ui.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dam.rehapp.data.model.User

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun loadUserFromLocal(user: User) {
        _user.value = user
    }

    fun updateUser(newData: User) {
        _user.value = newData
    }
}