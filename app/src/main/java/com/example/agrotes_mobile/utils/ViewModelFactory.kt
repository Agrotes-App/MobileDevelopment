package com.example.agrotes_mobile.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agrotes_mobile.di.Injection
import com.example.agrotes_mobile.repository.DiseaseRepository
import com.example.agrotes_mobile.ui.activities.prediction.PredictionViewModel
import com.example.agrotes_mobile.ui.fragment.history.HistoryViewModel

class ViewModelFactory private constructor(private val diseaseRepository: DiseaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    // Adding context when calling the ViewModel class
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PredictionViewModel::class.java) -> {
                PredictionViewModel(diseaseRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(diseaseRepository) as T
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
                        INSTANCE = ViewModelFactory(Injection.provideDiseaseRepository(context))
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }
    }