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
import com.example.agrotes_mobile.data.remote.test.ListStoryItem
import com.example.agrotes_mobile.databinding.ItemDiseaseBinding
import com.example.agrotes_mobile.ui.activities.detailDisease.DetailDiseaseActivity
import com.example.agrotes_mobile.ui.activities.detailDisease.DetailDiseaseActivity.Companion.EXTRA_ID

class DiseaseAdapter : ListAdapter<ListStoryItem, DiseaseAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    class ViewHolder(val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ListStoryItem) {
            with(binding) {
                tvDiseaseName.text = result.name
                tvPlantName.text = result.id
                Glide
                    .with(itemView.context)
                    .load(result.photoUrl)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}