package com.demo.filemanager.Activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

import com.demo.filemanager.Activity.customview.tablayout.TabView
import com.demo.filemanager.BuildConfig
import com.demo.filemanager.R
import com.demo.filemanager.adapter.viewpagerAdapter.FragmentAdapter
import com.demo.filemanager.adapter.viewpagerAdapter.ZoomOutPageTransformer
import com.demo.filemanager.fragment.FilesFragment
import com.demo.filemanager.fragment.StoreFragment
import kotlinx.android.synthetic.main.activity_main.activity_main_drawer
import kotlinx.android.synthetic.main.activity_main.lauchericon
import kotlinx.android.synthetic.main.activity_main.main_toolbar
import kotlinx.android.synthetic.main.activity_main.opend_drawer
import kotlinx.android.synthetic.main.activity_main.r_lan
import kotlinx.android.synthetic.main.activity_main.r_rate
import kotlinx.android.synthetic.main.activity_main.r_recyclerbin
import kotlinx.android.synthetic.main.activity_main.r_share
import kotlinx.android.synthetic.main.activity_main.txt_file
import kotlinx.android.synthetic.main.activity_main.txt_storages
import kotlinx.android.synthetic.main.activity_main.viewpager_activity
import java.io.File

class MainActivity : AppCompatActivity() {
    private var isopen = false
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var fragmentAdapter: FragmentAdapter? = null
    private var filesFragment: FilesFragment? = null
    private var storeFragment: StoreFragment? = null
    private val tabViews = arrayOfNulls<TabView>(2)

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)
//        val adsGoogle = AdsGoogle(this)
//        adsGoogle.Banner_Show((findViewById<View>(R.id.banner) as RelativeLayout)!!, this)
//        adsGoogle.Interstitial_Show_Counter(this)

        //Add DrawerToggle
        drawerToggle = ActionBarDrawerToggle(
            this,
            this.activity_main_drawer,
            this.main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerToggle!!.getDrawerArrowDrawable().setColor(resources.getColor(R.color.black))
        (resources.getDrawable(R.drawable.icon_setting) as BitmapDrawable).bitmap
        drawerToggle!!.setHomeAsUpIndicator(R.drawable.drawer_icon)
        drawerToggle!!.setDrawerIndicatorEnabled(false)
        drawerToggle!!.setToolbarNavigationClickListener(View.OnClickListener { view ->
            if (this.activity_main_drawer.isDrawerVisible(GravityCompat.START)) {
                this.activity_main_drawer.closeDrawer(GravityCompat.START)
            } else {
                this.activity_main_drawer.openDrawer(GravityCompat.START)
            }
        })
        this.opend_drawer.setOnClickListener(View.OnClickListener {
            if (isopen) {
                this@MainActivity.activity_main_drawer.openDrawer(GravityCompat.START)
            } else {
                this@MainActivity.activity_main_drawer.closeDrawer(GravityCompat.START)
            }
        })
        this.activity_main_drawer.addDrawerListener(drawerToggle!!)
        drawerToggle!!.syncState()


        // add Tabs
        val arrayList: List<String> = listOf<String>("Storage","File")
        this.fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        this.storeFragment = StoreFragment()
        this.filesFragment = FilesFragment()
        this.fragmentAdapter!!.addFrag(this.storeFragment!!)
        this.fragmentAdapter!!.addFrag(this.filesFragment!!)
        this.viewpager_activity.setAdapter(this.fragmentAdapter)
        this.viewpager_activity.setPageTransformer(ZoomOutPageTransformer())

        tabViews[0] = findViewById<View>(R.id.tabv_tab0) as TabView
        this.tabViews.get(0)?.setOnTabSelectedListener(object : TabView.OnTabSelectedListener {
            override fun onTabSelected(i: Int) {
                try {
                    this@MainActivity.viewpager_activity.setCurrentItem(i)
                } catch (unused: Exception) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                }
            }
        })
        this.txt_storages.setOnClickListener(View.OnClickListener {
            this@MainActivity.viewpager_activity.setCurrentItem(
                0
            )
        })
        this.txt_file.setOnClickListener(View.OnClickListener {
            this@MainActivity.viewpager_activity.setCurrentItem(1)
        })
        this.viewpager_activity.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(i: Int) {
                super.onPageSelected(i)
                if (i == 0) {
                    this@MainActivity.tabViews.get(0)?.select(0, true)
                } else {
                    this@MainActivity.tabViews.get(0)?.select(i, true)
                }
            }

            override fun onPageScrolled(i: Int, f: Float, i2: Int) {
                super.onPageScrolled(i, f, i2)
            }
        })
        this.lauchericon.setOnClickListener(View.OnClickListener {
            object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(message: Message) {
                    this@MainActivity.startActivity(
                        Intent(
                            this@MainActivity,
                            LaucherActivity::class.java
                        )
                    )
                }
            }.obtainMessage(111, "parameter").sendToTarget()
        })



        createhide()
        createBin()
        createVideoFolder()
        this.r_recyclerbin.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@MainActivity,
                ResulfActivity::class.java
            )
            intent.putExtra("nameitem", "recycle")
            this@MainActivity.startActivity(intent)
        })
        this.r_lan.setOnClickListener(View.OnClickListener {
            val intent = Intent("android.intent.action.VIEW")
            intent.setData(Uri.parse("https://www.google.com"))
            this@MainActivity.startActivity(intent)
        })
        this.r_share.setOnClickListener(View.OnClickListener {
            try {
                val intent = Intent("android.intent.action.SEND")
                intent.setType("text/plain")
                intent.putExtra("android.intent.extra.SUBJECT", "Recommend")
                intent.putExtra("android.intent.extra.TEXT", """ Let me recommend you this application https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}""".trimIndent()
                )
                this@MainActivity.startActivity(Intent.createChooser(intent, "choose one"))
            } catch (unused: java.lang.Exception) {
            }
        })
        this.r_rate.setOnClickListener(View.OnClickListener { rateApp() })
    }
    private fun createVideoFolder() {
        val store: String? = this@MainActivity.getStore(this@MainActivity)
//        val store: String? = this@MainActivity.getStore(this)
        val file = File(store)
        val file2 = File("$store/Video")
        if (!file.isDirectory) {
            file.mkdir()
        }
        if (file2.isDirectory) {
            return
        }
        file2.mkdir()
    }
    private fun createBin() {
        val store: String? = this@MainActivity.getStore(this@MainActivity)
//        val store: String = MainActivity.getStore(this)
        val file = File(store)
        val file2 = File("$store/Bin")
        if (!file.isDirectory) {
            file.mkdir()
        }
        if (file2.isDirectory) {
            return
        }
        file2.mkdir()
    }
    private fun createhide() {
        val store: String? = this@MainActivity.getStore(this@MainActivity)
        val file = File(store)
        val file2 = File("$store/remiImagehide")
        if (!file.isDirectory) {
            file.mkdir()
        }
        if (file2.isDirectory) {
            return
        }
        file2.mkdir()
    }
    fun getStore(context: Context): String {
        if (Build.VERSION.SDK_INT >= 29) {
            val externalFilesDir = context.getExternalFilesDir(null)
            return if (externalFilesDir != null) {
                externalFilesDir.absolutePath
            } else "/storage/emulated/0/Android/data/" + context.packageName
        }
        return Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context.packageName
    }
    private fun rateApp() {
        try {
            startActivity(rateIntentForUrl("market://details"))
        } catch (unused: ActivityNotFoundException) {
            startActivity(rateIntentForUrl("https://play.google.com/store/apps/details"))
        }
    }

    @SuppressLint("WrongConstant")
    private fun rateIntentForUrl(str: String): Intent? {
        val intent = Intent(
            "android.intent.action.VIEW", Uri.parse(
                String.format(
                    "%s?id=%s", str,
                    packageName
                )
            )
        )
        val i = Build.VERSION.SDK_INT
        intent.addFlags(1208483840)
        return intent
    }
    companion object{
        fun getStore(context: Context): String? {
            if (Build.VERSION.SDK_INT >= 29) {
                val externalFilesDir = context.getExternalFilesDir(null)
                return if (externalFilesDir != null) {
                    externalFilesDir.absolutePath
                } else "/storage/emulated/0/Android/data/" + context.packageName
            }
            return Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + context.packageName
        }
    }
}