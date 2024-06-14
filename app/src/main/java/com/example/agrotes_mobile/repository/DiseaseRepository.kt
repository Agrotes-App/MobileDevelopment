package com.example.agrotes_mobile.repository

import androidx.lifecycle.LiveData
import com.example.agrotes_mobile.data.local.room.DiseaseDao
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity

class DiseaseRepository(private val diseaseDao: DiseaseDao) {

    fun getAllHistory(): LiveData<List<DiseaseEntity>> = diseaseDao.getAllHistory()

    suspend fun insert(entity: DiseaseEntity) = diseaseDao.insert(entity)

    companion object {
        @Volatile
        private var instance: DiseaseRepository? = null
        fun getInstance(diseaseDao: DiseaseDao): DiseaseRepository =
            instance ?: synchronized(this) {
                instance ?: DiseaseRepository(diseaseDao)
            }.also { instance = it }
    }
}