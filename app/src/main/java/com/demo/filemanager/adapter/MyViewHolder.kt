package com.demo.filemanager.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.filemanager.R

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var date: TextView
    var imageView: ImageView
    var img_check: ImageView
    var time: TextView
    var title: TextView

    init {
        date = view.findViewById<View>(R.id.date) as TextView
        time = view.findViewById<View>(R.id.time) as TextView
        title = view.findViewById<View>(R.id.title) as TextView
        imageView = view.findViewById<View>(R.id.icon_file) as ImageView
        img_check = view.findViewById<View>(R.id.img_check) as ImageView
    }
}
