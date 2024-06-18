package com.example.agrotes_mobile.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrotes_mobile.data.local.entity.PlantEntity
import com.example.agrotes_mobile.databinding.ItemPlantBinding
import com.example.agrotes_mobile.ui.activities.camera.CameraActivity
import com.example.agrotes_mobile.ui.activities.prediction.PredictionActivity

class PlantAdapter: ListAdapter<PlantEntity, PlantAdapter.ViewModel>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val plant = getItem(position)
        holder.bind(plant)
    }

    class ViewModel(val binding: ItemPlantBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: PlantEntity) {
            with(binding) {
                ivIconPlant.setImageResource(plant.icon)
                tvNamePlant.text = plant.name
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CameraActivity::class.java)
                intent.putExtra("plant", plant.name)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlantEntity>() {
            override fun areItemsTheSame(oldItem: PlantEntity, newItem: PlantEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: PlantEntity, newItem: PlantEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}