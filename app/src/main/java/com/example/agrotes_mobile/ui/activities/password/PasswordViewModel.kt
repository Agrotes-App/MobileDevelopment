package com.example.agrotes_mobile.ui.activities.password

import androidx.lifecycle.ViewModel
import com.example.agrotes_mobile.repository.user.UserRepository

class PasswordViewModel(private val userRepository: UserRepository): ViewModel() {
    fun updateProfile(password: String) = userRepository.updatePassword(password)
}