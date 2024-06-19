package com.example.agrotes_mobile.utils.modelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agrotes_mobile.di.app.Injection
import com.example.agrotes_mobile.repository.app.UserRepository
import com.example.agrotes_mobile.ui.activities.detailDisease.DetailDiseaseViewModel
import com.example.agrotes_mobile.ui.activities.editProfile.EditProfileViewModel
import com.example.agrotes_mobile.ui.activities.login.LoginViewModel
import com.example.agrotes_mobile.ui.activities.main.MainViewModel
import com.example.agrotes_mobile.ui.activities.password.PasswordViewModel
import com.example.agrotes_mobile.ui.activities.prediction.PredictionViewModel
import com.example.agrotes_mobile.ui.activities.signup.SignupViewModel
import com.example.agrotes_mobile.ui.fragment.history.HistoryViewModel
import com.example.agrotes_mobile.ui.fragment.home.HomeViewModel
import com.example.agrotes_mobile.ui.fragment.profile.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(DetailDiseaseViewModel::class.java) -> {
                DetailDiseaseViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(PredictionViewModel::class.java) -> {
                PredictionViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(PasswordViewModel::class.java) -> {
                PasswordViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideUserRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}