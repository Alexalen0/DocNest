package com.example.ocr_cam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.BaseAdapter
import android.graphics.BitmapFactory

class ScanListAdapter(private val context: Context, private val scans: List<Scan>) : BaseAdapter() {
    override fun getCount(): Int = scans.size
    override fun getItem(position: Int): Any = scans[position]
    override fun getItemId(position: Int): Long = scans[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        val scan = scans[position]
        val text1 = view.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)
        text1.text = scan.ocrText.take(40)
        text2.text = scan.timestamp.toString()
        return view
    }
}

