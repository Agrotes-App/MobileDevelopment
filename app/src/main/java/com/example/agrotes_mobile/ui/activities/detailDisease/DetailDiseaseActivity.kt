package com.example.agrotes_mobile.ui.activities.detailDisease

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityDetailDiseaseBinding
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory

class DetailDiseaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiseaseBinding
    private val viewModel: DetailDiseaseViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(EXTRA_ID) // get id from intent
        getDiseaseById(id)
    }

    private fun getDiseaseById(id: String?) {
        viewModel.getDiseaseById(id).observe(this@DetailDiseaseActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    val data = result.data
                    with(binding) {
                        tvDiseaseName.text = data.diseaseName
                        tvPlantName.text = data.plantNames
                        tvAlternativeDiseaseName.text = data.otherNames
                        tvOverview.text = data.description
                        tvCauses.text = data.causes
                        tvPrevention.text = data.prevention
                        Glide
                            .with(this@DetailDiseaseActivity)
                            .load(data.photo)
                            .into(ivPhoto)
                    }
                    showLoading(false)
                }

                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun showToast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}