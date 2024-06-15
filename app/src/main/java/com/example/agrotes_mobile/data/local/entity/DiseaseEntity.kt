package com.example.agrotes_mobile.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "disease_table")
data class DiseaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "plantName")
    var plantName: String? = null,

    @ColumnInfo(name = "diseaseName")
    var diseaseName: String? = null,

    @ColumnInfo(name = "imageUri")
    var imageUri: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,
)