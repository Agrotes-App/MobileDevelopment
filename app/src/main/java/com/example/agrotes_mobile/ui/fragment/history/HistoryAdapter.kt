package com.example.agrotes_mobile.ui.fragment.history

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.dummy.Model
import com.example.agrotes_mobile.ui.activities.detailHistory.DetailHistoryActivity

class HistoryAdapter(private val history: ArrayList<DiseaseEntity>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = history[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = history.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var plantName: TextView = itemView.findViewById(R.id.tv_plant_name)
        private var diseaseName: TextView = itemView.findViewById(R.id.tv_disease_name)
        private var plantDate: TextView = itemView.findViewById(R.id.tv_date)
        private var photo: ImageView = itemView.findViewById(R.id.iv_photo)

        fun bind(data: DiseaseEntity) {
            plantName.text = data.plantName
            diseaseName.text = data.diseaseName
            plantDate.text = data.date
            photo.setImageURI(data.imageUri?.toUri())

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailHistoryActivity::class.java)
                intent.putExtra("extra_history",data)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(plantName, "plant_name"),
                        Pair(diseaseName, "disease_name"),
                        Pair(photo, "photo"),
                        Pair(plantDate, "date"),
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}