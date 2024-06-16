package com.example.agrotes_mobile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.data.remote.test.ListStoryItem
import com.example.agrotes_mobile.databinding.ItemArticleBinding

class ArticleAdapter: ListAdapter<ListStoryItem, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }

    class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ListStoryItem) {
            with(binding) {
                tvArticleHeadline.text = result.name
                Glide
                    .with(itemView.context)
                    .load(result.photoUrl)
                    .into(ivArticleImage)
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