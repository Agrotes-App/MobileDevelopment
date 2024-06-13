package com.example.agrotes_mobile.ui.fragment.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.dummy.Model

class HistoryAdapter(private val DataList: ArrayList<Model>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantName: TextView = itemView.findViewById(R.id.tv_plant_name)
        val diseaseName: TextView = itemView.findViewById(R.id.tv_disease_name)
        val photo: ImageView = itemView.findViewById(R.id.iv_photo)
        val plantDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val data = DataList[position]

        Glide.with(holder.itemView.context)
            .load(data.photoUrl) // URL Gambar
            .into(holder.photo) // imageView mana yang akan diterapkan
    }

    override fun getItemCount(): Int = DataList.size

}