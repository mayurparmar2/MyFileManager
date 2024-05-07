package com.demo.filemanager.Activity.customview

import com.demo.filemanager.Activity.customview.CircularProgressIndicator.ProgressTextAdapter

class DefaultProgressTextAdapter : ProgressTextAdapter {
    override fun formatText(d: Double): String? {
        return d.toInt().toString()
    }
}
