package com.example.agrotes_mobile.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.databinding.ItemHistoryBinding
import com.example.agrotes_mobile.ui.activities.detailHistory.DetailHistoryActivity

class HistoryAdapter: ListAdapter<DiseaseEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class ViewHolder(val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiseaseEntity) {
            with(binding){
                tvPlantName.text = data.plantName
                tvDiseaseName.text = data.diseaseName
                tvDate.text = data.date
                ivPhoto.setImageURI(data.imageUri?.toUri())

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailHistoryActivity::class.java)
                    intent.putExtra("extra_history",data)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(tvPlantName, "plant_name"),
                            Pair(tvDiseaseName, "disease_name"),
                            Pair(ivPhoto, "photo"),
                            Pair(tvDate, "date"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiseaseEntity>() {
            override fun areItemsTheSame(oldItem: DiseaseEntity, newItem: DiseaseEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DiseaseEntity, newItem: DiseaseEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

}