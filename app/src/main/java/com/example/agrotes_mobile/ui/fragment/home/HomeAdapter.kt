package com.example.agrotes_mobile.ui.fragment.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.data.remote.responses.ListStoryItem
import com.example.agrotes_mobile.databinding.ItemDiseaseBinding
import com.example.agrotes_mobile.dummy.Model

class HomeAdapter : ListAdapter<ListStoryItem, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
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
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}