package com.example.ocr_cam

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val scans = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext).scanDao().getAll()
            }
            recyclerView.adapter = ScanAdapter(scans) { scan ->
                val intent = Intent(this@ScanListActivity, ScanDetailActivity::class.java)
                intent.putExtra("scan_id", scan.id)
                startActivity(intent)
            }
        }
    }
}

class ScanAdapter(
    private val scans: List<Scan>,
    private val onItemClick: (Scan) -> Unit
) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {

    class ScanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val scan = scans[position]
        val bitmap = BitmapFactory.decodeFile(scan.imagePath)
        holder.imageView.setImageBitmap(bitmap)
        holder.textView.text = scan.ocrText.take(100) // Show first 100 chars
        holder.itemView.setOnClickListener { onItemClick(scan) }
    }

    override fun getItemCount() = scans.size
}
