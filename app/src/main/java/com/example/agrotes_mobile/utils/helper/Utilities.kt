package com.example.agrotes_mobile.utils.helper

import android.content.Context
import android.widget.EditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

// for creating temporary file
fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

// error without icon
fun error(editText: EditText, messageId: String) {
    editText.run {
        setError(messageId, null)
        requestFocus()
    }
}

// error with icon
fun errorIcon(editText: EditText, messageId: String) {
    editText.run {
        error = messageId
        requestFocus()
    }
}