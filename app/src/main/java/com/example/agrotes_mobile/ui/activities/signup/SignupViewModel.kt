package com.example.agrotes_mobile.ui.activities.signup

import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.repository.user.UserRepository

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun signup(username: String, email: String, password: String) = userRepository.signup(username, email, password)
}