package com.demo.filemanager.adapter.viewpagerAdapter

import android.view.View
import androidx.viewpager2.widget.ViewPager2


class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, f: Float) {
        val width = view.width
        val height = view.height
        if (f < -1.0f) {
            view.alpha = 0.0f
        } else if (f <= 1.0f) {
            val max = Math.max(MIN_SCALE, 1.0f - Math.abs(f))
            val f2 = 1.0f - max
            val f3 = height * f2 / 2.0f
            val f4 = width * f2 / 2.0f
            if (f < 0.0f) {
                view.translationX = f4 - f3 / 2.0f
            } else {
                view.translationX = -f4 + f3 / 2.0f
            }
            view.scaleX = max
            view.scaleY = max
            view.alpha = (max - MIN_SCALE) / 0.14999998f * 0.5f + 0.5f
        } else {
            view.alpha = 0.0f
        }
    }

    companion object {
        private const val MIN_ALPHA = 0.5f
        private const val MIN_SCALE = 0.85f
    }
}
