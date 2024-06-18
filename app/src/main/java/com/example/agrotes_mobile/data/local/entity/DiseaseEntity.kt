package com.example.agrotes_mobile.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "disease_table")
data class DiseaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "diseaseName")
    var diseaseName: String? = null,

    @ColumnInfo(name = "plantName")
    var plantName: String? = null,

    @ColumnInfo(name = "alternativeName")
    var alternativeName: String? = null,

    @ColumnInfo(name = "overview")
    var overview: String? = null,

    @ColumnInfo(name = "causes")
    var causes: String? = null,

    @ColumnInfo(name = "prevention")
    var prevention: String? = null,

    @ColumnInfo(name = "imageUri")
    var imageUri: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,
) : Parcelable