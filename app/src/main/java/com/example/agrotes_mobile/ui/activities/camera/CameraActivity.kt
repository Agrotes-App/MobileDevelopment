package com.example.agrotes_mobile.ui.activities.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityCameraBinding
import com.example.agrotes_mobile.ui.activities.prediction.PredictionActivity
import com.example.agrotes_mobile.utils.helper.createCustomTempFile

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var model: String? = null

    // request permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showToast(getString(R.string.permission_granted))
                startCamera()
            } else {
                showToast(getString(R.string.permission_denied))
                finish()
            }
        }

    // Permission check
    private fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        model = intent.getStringExtra(EXTRA_PLANT) // get model from intent
        setupAction()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }


    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        with(binding) {
            fabCapture.setOnClickListener { takePhoto() }
            fabGallery.setOnClickListener { startGallery() }
            fabHelp.setOnClickListener { showDialog() }
        }
    }

    private fun startCamera() {
        if (checkPermission(CAMERAX_PERMISSION)) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }
                imageCapture = ImageCapture.Builder().build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch (exc: Exception) {
                    showToast(getString(R.string.error_camera))
                }
            }, ContextCompat.getMainExecutor(this))
        } else {
            requestPermissionLauncher.launch(CAMERAX_PERMISSION)
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) = showImage(output.savedUri)
                override fun onError(exc: ImageCaptureException) = showToast(getString(R.string.error_camera))
            })
    }

    private fun startGallery() {
        if (checkPermission(STORAGE_PERMISSION)) {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            requestPermissionLauncher.launch(STORAGE_PERMISSION)
        }
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, flag)
                showImage(uri)
            } else {
                showToast(getString(R.string.error_gallery))
            }
        }

    // set orientation
    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setIcon(R.drawable.ic_camera)
        dialogBuilder.setTitle(getString(R.string.alert_title_tips))
        dialogBuilder.setMessage(getString(R.string.tips_message))
        dialogBuilder.setPositiveButton(getString(R.string.alert_positive_button)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showImage(uri: Uri?) {
        with(binding) {
            ivCapturePreview.visibility = android.view.View.VISIBLE
            fabCaptureResult.visibility = android.view.View.VISIBLE
            ivCapturePreview.setImageURI(uri)
            fabCaptureResult.setOnClickListener {
                cardScannerOverlay.visibility = android.view.View.VISIBLE
                fabCaptureResult.isEnabled = false

                val intent = Intent(this@CameraActivity, PredictionActivity::class.java)
                intent.putExtra(PredictionActivity.EXTRA_CAMERAX_IMAGE, uri.toString())
                intent.putExtra(PredictionActivity.EXTRA_MODEL, model)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    companion object {
        const val EXTRA_PLANT = "extra_plant"
        private const val CAMERAX_PERMISSION = Manifest.permission.CAMERA
        private const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
}