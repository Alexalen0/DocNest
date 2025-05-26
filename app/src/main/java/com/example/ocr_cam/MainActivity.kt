package com.example.ocr_cam

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var tvOcrText: TextView
    private var capturedBitmap: Bitmap? = null
    private var recognizedText: String = ""

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                capturedBitmap = it
                imageView.setImageBitmap(it)
                runTextRecognition(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.imageView)
        tvOcrText = findViewById(R.id.tvOcrText)
        val btnCapture: Button = findViewById(R.id.btnCapture)
        btnCapture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }

        val btnSave: Button = findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            saveScan()
        }

        val btnViewScans: Button = findViewById(R.id.btnViewScans)
        btnViewScans.setOnClickListener {
            val intent = Intent(this, ScanListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun runTextRecognition(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                recognizedText = visionText.text
                tvOcrText.text = recognizedText
            }
            .addOnFailureListener { e ->
                recognizedText = ""
                tvOcrText.text = "OCR failed: ${e.localizedMessage}"
            }
    }

    private fun saveScan() {
        val bitmap = capturedBitmap
        val text = recognizedText
        if (bitmap == null || text.isBlank()) {
            tvOcrText.text = "Capture an image and run OCR first."
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val imagePath = saveImageToInternalStorage(bitmap)
            val scan = Scan(imagePath = imagePath, ocrText = text)
            AppDatabase.getDatabase(applicationContext).scanDao().insert(scan)
            launch(Dispatchers.Main) {
                tvOcrText.text = "Scan saved!"
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val filename = "scan_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.png"
        val file = File(filesDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file.absolutePath
    }
}
