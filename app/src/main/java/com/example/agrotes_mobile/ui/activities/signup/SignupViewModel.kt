package com.example.agrotes_mobile.ui.activities.signup

import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.repository.UserRepository

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun signup(name: String, email: String, password: String) = userRepository.signup(name, email, password)
}