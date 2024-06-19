package com.example.agrotes_mobile.ui.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.agrotes_mobile.data.local.entity.UserEntity
import com.example.agrotes_mobile.repository.app.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getUserById(id: String?) = userRepository.getUserById(id)
    fun getSession(): LiveData<UserEntity> = userRepository.getSession().asLiveData()
    fun logOut() = viewModelScope.launch{ userRepository.logout() }
}