package com.example.agrotes_mobile.ui.activities.detailHistory

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        val data = intent.getParcelableExtra<DiseaseEntity>("extra_history") as DiseaseEntity
        with(binding){
            tvDiseaseName.text = data.diseaseName
            tvPlantName.text = data.plantName
            tvDate.text = data.date
            tvAlternativeDiseaseName.text = data.diseaseName
            tvOverview.text = data.overview
            tvCauses.text = data.causes
            tvPrevention.text = data.prevention
            ivPhoto.setImageURI(data.imageUri?.toUri())
        }
    }
}