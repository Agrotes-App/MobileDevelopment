package com.example.agrotes_mobile.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.repository.user.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    fun updateProfile(username: String, email: String, password: String) = userRepository.updateProfile(username, email, password)
    fun logOut() = viewModelScope.launch{ userRepository.logout() }
}