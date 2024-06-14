package com.example.agrotes_mobile.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity

@Dao
interface DiseaseDao {
    @Query("SELECT * FROM disease_table ORDER BY id ASC")
    fun getAllHistory(): LiveData<List<DiseaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DiseaseEntity)

    @Delete
    suspend fun delete(entity: DiseaseEntity)
}