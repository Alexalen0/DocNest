package com.example.ocr_cam

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_detail)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.textView)
        val btnShare = findViewById<Button>(R.id.btnShare)

        val scanId = intent.getIntExtra("scan_id", -1)
        if (scanId == -1) {
            Toast.makeText(this, "Error: Invalid scan ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            try {
                val scan = withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(applicationContext).scanDao().getById(scanId)
                }

                scan?.let {
                    val bitmap = BitmapFactory.decodeFile(it.imagePath)
                    imageView.setImageBitmap(bitmap)
                    textView.text = it.ocrText
                    btnShare.setOnClickListener { _ ->
                        shareTextViaWhatsApp(it.ocrText)
                    }
                } ?: run {
                    Toast.makeText(this@ScanDetailActivity, "Error: Scan not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ScanDetailActivity, "Error loading scan: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun shareTextViaWhatsApp(text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            setPackage("com.whatsapp")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // WhatsApp not installed, fallback to generic share
            val fallback = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(fallback, "Share via"))
        }
    }
}

