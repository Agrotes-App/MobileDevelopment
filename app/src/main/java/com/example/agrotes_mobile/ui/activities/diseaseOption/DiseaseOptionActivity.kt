package com.example.agrotes_mobile.ui.activities.diseaseOption

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.local.entity.PlantEntity
import com.example.agrotes_mobile.databinding.ActivityDiseaseOptionBinding
import com.example.agrotes_mobile.ui.adapter.PlantAdapter

class DiseaseOptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseOptionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getData()
        setupView()
    }

    private fun getData(){
        val plantName = resources.getStringArray(R.array.array_plant)
        val icon = resources.obtainTypedArray(R.array.array_icon_plant)

        val listPlant = ArrayList<PlantEntity>()
        for (i in plantName.indices) {
            val plant = PlantEntity(
                name = plantName[i],
                icon = icon.getResourceId(i, -1)
            )
            listPlant.add(plant)
        }
        icon.recycle()

        setupAdapter(listPlant)
    }

    private fun setupAdapter(result: List<PlantEntity>) {
        val adapter = PlantAdapter()
        adapter.submitList(result)
        binding.rvPlants.adapter = adapter
    }

    private fun setupView() {
        val layoutManager = GridLayoutManager(this, 3)
        with(binding) {
            rvPlants.layoutManager = layoutManager
            rvPlants.setHasFixedSize(true)
        }
    }
}