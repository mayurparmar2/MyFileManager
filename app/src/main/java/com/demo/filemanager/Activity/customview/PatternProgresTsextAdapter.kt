package com.demo.filemanager.Activity.customview

import com.demo.filemanager.Activity.customview.CircularProgressIndicator.ProgressTextAdapter

class PatternProgresTsextAdapter(private val pattern: String) : CircularProgressIndicator.ProgressTextAdapter {

    override fun formatText(d: Double): String? {
        return String.format(this.pattern, java.lang.Double.valueOf(d))
    }
}
