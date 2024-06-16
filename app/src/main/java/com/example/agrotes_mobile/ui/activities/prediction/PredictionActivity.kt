package com.example.agrotes_mobile.ui.activities.prediction

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.agrotes_mobile.utils.ViewModelFactory
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
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let {
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            Log.d("OUTPUT MODEL", it.toString())
                            val categories = it[0].categories[0]
                            val label = categories.label
                            val displayName = categories.displayName
                            val score = categories.score
                            val time = inferenceTime.toString()
                            predictionResult = DiseaseEntity(
                                plantName = displayName, // masih menunggu model dari machine learning
                                diseaseName = label,
                                date = DateHelper.getCurrentDate(),
                                imageUri = currentImageUri.toString()
                            )

                            with(binding) {
                                tvDiseaseName.text = label
                                tvPlantName.text = displayName
                                tvDate.text= time
                                tvAlternativeDiseaseName.text = score.toString()
                            }

                        } else {
                            showToast(getString(R.string.error_model_result))
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyImage(uri)
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

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_CAMERAX_IMAGE = "extra_camerax_image"
        const val TAG = "PredictionActivity"
    }
}