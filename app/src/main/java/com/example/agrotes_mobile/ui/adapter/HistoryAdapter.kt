package com.example.agrotes_mobile.ui.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.databinding.ItemHistoryBinding
import com.example.agrotes_mobile.ui.activities.detailHistory.DetailHistoryActivity
import com.example.agrotes_mobile.ui.activities.detailHistory.DetailHistoryActivity.Companion.EXTRA_HISTORY
import com.example.agrotes_mobile.ui.fragment.history.HistoryViewModel

class HistoryAdapter(private val historyViewModel: HistoryViewModel) : ListAdapter<DiseaseEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiseaseEntity) {
            with(binding) {
                tvPlantName.text = data.plantName
                tvDiseaseName.text = data.diseaseName
                tvDate.text = data.date
                ivPhoto.setImageURI(data.imageUri?.toUri())

                btnDelete.setOnClickListener {
                    AlertDialog.Builder(itemView.context)
                        .setTitle(R.string.alert_title_delete)
                        .setMessage(R.string.alert_message_delete)
                        .setPositiveButton(R.string.alert_positive_button) { _, _ ->
                            historyViewModel.delete(data)
                        }
                        .setNegativeButton(R.string.alert_negative_button) { dialog, _ ->
                            dialog.cancel()
                        } .show()
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailHistoryActivity::class.java)
                    intent.putExtra(EXTRA_HISTORY, data)
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