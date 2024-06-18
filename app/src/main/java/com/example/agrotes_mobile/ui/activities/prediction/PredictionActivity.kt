package com.example.agrotes_mobile.ui.activities.prediction

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.databinding.ActivityPredictionBinding
import com.example.agrotes_mobile.helper.DateHelper
import com.example.agrotes_mobile.helper.ImageClassifierHelper
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications

class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private var predictionResult: DiseaseEntity? = null

    private val viewModel: PredictionViewModel by viewModels<PredictionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // get image uri from CameraActivity
        val imageUri = intent.getStringExtra(EXTRA_CAMERAX_IMAGE)
        currentImageUri = imageUri?.toUri()

        // start image classifier
        currentImageUri?.let { startAnalyze(it) }

        showImage()
        setupAction()

    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d(TAG, "showImage: $it")
            binding.ivPhoto.setImageURI(it)
        }
    }

    private fun startAnalyze(uri: Uri) {
        val model: String = intent.getStringExtra(EXTRA_MODEL).toString()
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            modelName = model,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let {
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            val categories = it[0].categories[0]
                            val label = categories.label
                            setupPrediction("Blight")
                        } else {
                            showToast(getString(R.string.error_model_result))
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyImage(uri)
    }

    private fun setupPrediction(label: String) {
        viewModel.getDiseaseByName(label).observe(this@PredictionActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    val data = result.data
                    // insert data to database
                    predictionResult = DiseaseEntity(
                        imageUri = currentImageUri.toString(),
                        diseaseName = label,
                        plantName = data.plantNames,
                        date = DateHelper.getCurrentDate(),
                        overview = data.description,
                        causes = data.causes,
                        prevention = data.prevention
                    )

                    with(binding) {
                        tvDiseaseName.text = label
                        tvPlantName.text = data.plantNames
                        tvDate.text = DateHelper.getCurrentDate()
                        tvAlternativeDiseaseName.text = data.diseaseName
                        tvOverview.text = data.description
                        tvCauses.text = data.causes
                        tvPrevention.text = data.prevention
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

    private fun setupAction() {
        binding.btnSave.setOnClickListener {
            predictionResult.let {
                if (it != null) {
                    viewModel.insert(it)
                }
                showToast(getString(R.string.data_saved))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_CAMERAX_IMAGE = "extra_camerax_image"
        const val EXTRA_MODEL = "extra_model"
        const val TAG = "PredictionActivity"
    }
}