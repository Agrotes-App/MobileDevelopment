package com.example.agrotes_mobile.ui.fragment.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.databinding.ItemDiseaseBinding
import com.example.agrotes_mobile.dummy.Model

class MainAdapter(private val data: ArrayList<Model>): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    class MainViewHolder(val binding: ItemDiseaseBinding): RecyclerView.ViewHolder(binding.root) {
        val photo = binding.ivPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
        val data = data[position]
        Glide
            .with(holder.itemView.context)
            .load(data.photoUrl)
            .into(holder.photo)
    }

    override fun getItemCount(): Int = data.size

}