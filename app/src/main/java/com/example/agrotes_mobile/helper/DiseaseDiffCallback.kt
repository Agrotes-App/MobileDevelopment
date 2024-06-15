package com.example.agrotes_mobile.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity

class DiseaseDiffCallback(private val oldDiseaseList: List<DiseaseEntity>, private val newDiseaseList: List<DiseaseEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldDiseaseList.size
    override fun getNewListSize(): Int = newDiseaseList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDiseaseList[oldItemPosition].id == newDiseaseList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDiseaseList[oldItemPosition] == newDiseaseList[newItemPosition]
    }
}