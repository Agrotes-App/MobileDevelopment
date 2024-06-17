package com.example.agrotes_mobile.ui.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.pref.UserModel
import com.example.agrotes_mobile.repository.user.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)
    fun saveSession(userModel: UserModel) = viewModelScope.launch { userRepository.saveSession(userModel) }
}