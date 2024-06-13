package com.example.agrotes_mobile.ui.activities.prediction

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityPredictionBinding
import com.example.agrotes_mobile.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null

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

        // imageUri from CameraActivity
        val imageUri = intent.getStringExtra(EXTRA_CAMERAX_IMAGE)
        currentImageUri = imageUri?.toUri()

        showImage()
        currentImageUri?.let { startAnalyze(it) }

    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPhoto.setImageURI(it)
        }
    }

    private fun startAnalyze(uri: Uri){
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(this@PredictionActivity, error, Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let {
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val categories = it[0].categories[0]
                            val label = categories.displayName
                            val score = categories.score
                            val time = inferenceTime.toString()

                            binding.tvDiseaseName.text = label
                        } else {
                           Toast.makeText(this@PredictionActivity, "Tidak ada hasil", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        )
        imageClassifierHelper.classifyImage(uri)
    }

    companion object{
        const val EXTRA_CAMERAX_IMAGE = "extra_camerax_image"
    }
}