package com.example.agrotes_mobile.ui.activities.analyze

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityAnalyzeBinding
import com.example.agrotes_mobile.ui.activities.camera.CameraActivity
import com.example.agrotes_mobile.ui.activities.camera.CameraActivity.Companion.CAMERAX_RESULT

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showImage()
    }

    private fun showImage() {
        val imageUri = intent.getStringExtra(EXTRA_CAMERAX_IMAGE)
        currentImageUri = imageUri?.toUri()

        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreviewImage.setImageURI(it)
        }
    }

    companion object{
        const val EXTRA_CAMERAX_IMAGE = "extra_camerax_image"
    }
}