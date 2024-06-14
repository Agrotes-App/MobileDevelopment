package com.example.agrotes_mobile.ui.fragment.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.dummy.Model

class HistoryAdapter(private val history: ArrayList<DiseaseEntity>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantName: TextView = itemView.findViewById(R.id.tv_plant_name)
        val diseaseName: TextView = itemView.findViewById(R.id.tv_disease_name)
        val photo: ImageView = itemView.findViewById(R.id.iv_photo)
        val plantDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = history[position]
        holder.plantName.text = data.plantName
        holder.diseaseName.text = data.diseaseName
        holder.plantDate.text = data.date.toString()
        holder.photo.setImageURI(Uri.parse(data.imageUri))
    }

    override fun getItemCount(): Int = history.size

}