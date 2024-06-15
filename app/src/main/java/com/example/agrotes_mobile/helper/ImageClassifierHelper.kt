package com.example.agrotes_mobile.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.example.agrotes_mobile.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private var threshold: Float = 0.1f,
    private var maxResults: Int = 1,
    private val modelName: String = "agripredict.tflite",
    val context: Context,
    private val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // Initialize Image Classifier.
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        // Initialize TF Lite Image Classifier.
        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(context, modelName, optionsBuilder.build())
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.error_tflite))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyImage(uri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        // Create preprocessor for the image.
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        val contentResolver = context.contentResolver

        // Convert the input image to a TensorImage object.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
            .copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
                var inferenceTime = SystemClock.uptimeMillis()
                val results = imageClassifier?.classify(tensorImage)

                inferenceTime = SystemClock.uptimeMillis() - inferenceTime
                classifierListener?.onResults(results, inferenceTime)
            }
    }

    // Listener for TF Lite based models.
    interface ClassifierListener {
        fun onResults(results: List<Classifications>?, inferenceTime: Long)
        fun onError(error: String)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}