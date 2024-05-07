package com.demo.filemanager.fragment.slide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.filemanager.R

class Slide2Fragment : Fragment() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_slide2, viewGroup, false)
    }

}
