package com.demo.filemanager.adapter.GirlAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.filemanager.R


class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageView: ImageView
    var img_check: ImageView
    var textView: TextView

    init {
        textView = view.findViewById<View>(R.id.text_grid) as TextView
        imageView = view.findViewById<View>(R.id.icon_grid) as ImageView
        img_check = view.findViewById<View>(R.id.img_check) as ImageView
    }
}

