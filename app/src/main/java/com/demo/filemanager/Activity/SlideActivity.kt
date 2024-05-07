package com.demo.filemanager.Activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.demo.filemanager.R
import com.demo.filemanager.adapter.viewpagerAdapter.FragmentAdapter
import com.demo.filemanager.adapter.viewpagerAdapter.ZoomOutPageTransformer
import com.demo.filemanager.fragment.slide.Slide2Fragment
import com.demo.filemanager.fragment.slide.Slide3Fragment
import com.demo.filemanager.fragment.slide.SlideFragment
import kotlinx.android.synthetic.main.activity_slide.img_next
import kotlinx.android.synthetic.main.activity_slide.img_skip
import kotlinx.android.synthetic.main.activity_slide.indicator
import kotlinx.android.synthetic.main.activity_slide.tvReadMore
import kotlinx.android.synthetic.main.activity_slide.txt_bold
import kotlinx.android.synthetic.main.activity_slide.txt_thin
import kotlinx.android.synthetic.main.activity_slide.viewPager1


class SlideActivity : AppCompatActivity() {
    private var fragmentAdapter: FragmentAdapter? = null
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_slide)
        if (Companion.checkPermission(this)) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        fragmentAdapter!!.addFrag(SlideFragment())
        fragmentAdapter!!.addFrag(Slide2Fragment())
        fragmentAdapter!!.addFrag(Slide3Fragment())
        viewPager1.setPageTransformer(ZoomOutPageTransformer())
        viewPager1.adapter = fragmentAdapter
        viewPager1.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(i: Int, f: Float, i2: Int) {
                super.onPageScrolled(i, f, i2)
                if (i == 0) {
                    img_skip!!.setBackgroundResource(R.drawable.bgrskip)
                    txt_bold.setText(R.string.txtslide1)
                    txt_thin.setText(R.string.txtthinslide1)
                    img_next!!.setBackgroundResource(R.drawable.imgnextred)
                    img_skip!!.visibility = View.VISIBLE
                    tvReadMore!!.visibility = View.GONE
                    img_next!!.text = ""
                } else if (i == 1) {
                    img_skip!!.setBackgroundResource(R.drawable.bgrskip)
                    img_skip!!.visibility = View.VISIBLE
                    txt_bold.setText(R.string.txtslide2)
                    txt_thin.setText(R.string.txtthinslide2)
                    img_next!!.setBackgroundResource(R.drawable.imgnextred)
                    tvReadMore!!.visibility = View.GONE
                    img_next!!.text = ""
                } else {
                    tvReadMore!!.visibility = View.VISIBLE
                    img_skip!!.setBackgroundColor(0)
                    txt_bold.setText(R.string.txtslide3)
                    txt_thin.setText(R.string.txtthinslide3)
                    img_next!!.text = "Allow"
                    img_next!!.setBackgroundResource(R.drawable.slide_icon_bgr)
                }
            }
        })
        img_next!!.setOnClickListener {
            if (viewPager1.currentItem == 0) {
                viewPager1.currentItem = 1
            } else if (viewPager1.currentItem == 1) {
                viewPager1.currentItem = 2
            } else {
                this@SlideActivity.startActivity(
                    Intent(
                        this@SlideActivity,
                        SplashActivity::class.java
                    )
                )
                finish()
            }
        }
        img_skip!!.setOnClickListener { viewPager1.currentItem = 2 }
        indicator.setSliderColor(
            resources.getColor(R.color.white),
            resources.getColor(R.color.indiacator)
        )
        indicator.setSliderHeight(resources.getDimension(R.dimen.dp_2))
        indicator.setSliderWidth(resources.getDimension(R.dimen.dp_8))
        indicator.setSlideMode(0)
        indicator.setIndicatorStyle(0)
        indicator.setupWithViewPager(viewPager1)
        val spannableString = SpannableString(getString(R.string.read_more))
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        tvReadMore!!.text = spannableString
        tvReadMore!!.setOnClickListener {
            val intent = Intent("android.intent.action.VIEW")
            intent.setData(Uri.parse("https://www.google.com"))
            this@SlideActivity.startActivity(intent)
        }
    }

    companion object {
        private fun checkPermission(slideActivity: SlideActivity): Boolean {
            return if (Build.VERSION.SDK_INT >= 30) {
                Environment.isExternalStorageManager()
            } else ContextCompat.checkSelfPermission(
                slideActivity,
                "android.permission.READ_EXTERNAL_STORAGE"
            ) == 0 && ContextCompat.checkSelfPermission(
                slideActivity,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            ) == 0
        }
    }
}

