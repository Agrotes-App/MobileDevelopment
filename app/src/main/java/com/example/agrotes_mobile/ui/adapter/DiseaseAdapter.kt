package com.example.agrotes_mobile.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.databinding.ItemDiseaseBinding
import com.example.agrotes_mobile.ui.activities.detailDisease.DetailDiseaseActivity
import com.example.agrotes_mobile.ui.activities.detailDisease.DetailDiseaseActivity.Companion.EXTRA_ID

class DiseaseAdapter : ListAdapter<DiseaseResponses, DiseaseAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    class ViewHolder(val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: DiseaseResponses) {
            with(binding) {
                tvDiseaseName.text = result.diseaseName
                tvPlantName.text = result.plantNames
                Glide
                    .with(itemView.context)
                    .load("https://th.bing.com/th/id/R.c226737a808cdbf3dafd25da793209ca?rik=284TtDMHKDbLPw&riu=http%3a%2f%2fwww.bio.brandeis.edu%2ffieldbio%2fmedicinal_plants%2fimages%2fpitcher_plant_close_full.jpg&ehk=6l3O%2f8BHLhC2udyF9tHFgvgZ7k9Y%2fTeLm6aF1sk8JSk%3d&risl=&pid=ImgRaw&r=0")
                    .into(ivPhoto)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailDiseaseActivity::class.java)
                    intent.putExtra(EXTRA_ID, result.id)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivPhoto, "photo"),
                            Pair(tvDiseaseName, "disease_name"),
                            Pair(tvPlantName, "plant_name")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiseaseResponses>() {
            override fun areItemsTheSame(oldItem: DiseaseResponses, newItem: DiseaseResponses): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DiseaseResponses, newItem: DiseaseResponses): Boolean {
                return oldItem == newItem
            }
        }
    }
}