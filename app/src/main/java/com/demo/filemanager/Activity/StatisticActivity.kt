package com.demo.filemanager.Activity

import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.EnvironmentCompat
import com.demo.filemanager.AdsGoogle
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.Image_Ultil
import com.demo.filemanager.Ultil.MusicUltil
import com.demo.filemanager.Ultil.VideoUtil
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class StatisticActivity : AppCompatActivity() {
    var audioSize: Long = 0
    var data_manager: Data_Manager? = null
    var docsSize: Long = 0
    var image_ultil: Image_Ultil? = null
    var imagesSize: Long = 0
    var img_back: ImageView? = null
    var musicUltil: MusicUltil? = null
    var pieChart: PieChart? = null
    var sdPieChart: PieChart? = null
    var sdaudioSize: Long = 0
    var sddocsSize: Long = 0
    var sdimagesSize: Long = 0
    var sdvideoSize: Long = 0
    var videoSize: Long = 0
    var video_ultil: VideoUtil? = null
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_statistic)
        val adsGoogle = AdsGoogle(this)
        adsGoogle.Banner_Show((findViewById<View>(R.id.banner) as RelativeLayout), this)
        adsGoogle.Interstitial_Show_Counter(this)
        img_back = findViewById<View>(R.id.img_back) as ImageView
        musicUltil = MusicUltil(this)
        video_ultil = VideoUtil(this)
        image_ultil = Image_Ultil(this)
        data_manager = Data_Manager(this)
        pieChart = findViewById<View>(R.id.pie_chart) as PieChart
        sdPieChart = findViewById<View>(R.id.sd_pie_chart) as PieChart
        musicUltil!!.allsong()
        video_ultil!!.getallvideo()


        CoroutineScope(Dispatchers.IO).launch {
            image_ultil!!.allImage()
            data_manager!!.setDocs(applicationContext)
        }
        imagesSize = Image_Ultil.totolsize
        videoSize = VideoUtil.totolsize
        audioSize = MusicUltil.totolsize
        docsSize = Data_Manager.sizedoccument




        setPieData()
        if (externalMemoryAvailable()) {
            BackgroundSizeCalculation().start()
            sdPieChart!!.visibility = View.VISIBLE
            setPieChart()
        } else {
            sdPieChart!!.visibility = View.GONE
        }
        img_back!!.setOnClickListener { view -> m63xa77a9205(view) }
    }

    fun m63xa77a9205(view: View?) {
        finish()
    }

    fun externalMemoryAvailable(): Boolean {
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(this, null)
        return if (externalFilesDirs.size <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) false else true
    }

    fun setPieData() {
        val arrayList: ArrayList<PieEntry> = ArrayList()
        val statFs = StatFs(Environment.getExternalStorageDirectory().path)
        arrayList.add(PieEntry(statFs.freeBytes.toFloat(), "Free"))
        arrayList.add(PieEntry(videoSize.toFloat(), "Videos"))
        arrayList.add(PieEntry(imagesSize.toFloat(), "Images"))
        arrayList.add(PieEntry(audioSize.toFloat(), "Audio"))
        arrayList.add(PieEntry(docsSize.toFloat(), "Documents"))
        arrayList.add(
            PieEntry(
                (statFs.totalBytes - audioSize - imagesSize - docsSize - videoSize - statFs.freeBytes).toFloat(),
                "Other"
            )
        )
        val pieDataSet = PieDataSet(arrayList, ":File Types")
        pieDataSet.colors = ColorTemplate.createColors(
            intArrayOf(
                getResources().getColor(R.color.piechart_red),
                getResources().getColor(R.color.piechart_blue),
                getResources().getColor(R.color.piechart_origin),
                getResources().getColor(R.color.piechart_violet),
                getResources().getColor(R.color.piechart_yl),
                getResources().getColor(R.color.piechart_green)
            )
        )
        pieDataSet.valueTextSize = 15.0f
        pieDataSet.isUsingSliceColorAsValueLineColor = true
        pieChart!!.data = PieData(pieDataSet)
        pieChart!!.animateY(1500)
        pieChart!!.setUsePercentValues(true)
        pieChart!!.setCenterTextSize(15.0f)
        pieChart!!.setDrawCenterText(true)
        pieChart!!.isDrawHoleEnabled = true
        pieChart!!.invalidate()
        val pieChart = pieChart
        pieChart!!.centerText = Formatter.formatFileSize(this, statFs.freeBytes) + " Free"
        this.pieChart!!.setDrawEntryLabels(false)
        val description = Description()
        description.text = "Internal Storage"
        description.textColor = -0x10000
        description.textSize = 15.0f
        this.pieChart!!.description = description
        this.pieChart!!.legend.orientation = Legend.LegendOrientation.HORIZONTAL
    }

    fun setPieChart() {
        val arrayList: ArrayList<PieEntry> = ArrayList()
        val statFs = StatFs(externalStorageDirectories[0])
        arrayList.add(PieEntry(sdvideoSize.toFloat(), "Video"))
        arrayList.add(PieEntry(statFs.freeBytes.toFloat(), "Free"))
        arrayList.add(PieEntry(sdimagesSize.toFloat(), "Images"))
        arrayList.add(PieEntry(sdaudioSize.toFloat(), "Audio"))
        arrayList.add(PieEntry(sddocsSize.toFloat(), "Documents"))
        arrayList.add(
            PieEntry(
                (statFs.totalBytes - sdaudioSize - sdimagesSize - sddocsSize - sdvideoSize - statFs.freeBytes).toFloat(),
                "Other"
            )
        )
        val pieDataSet = PieDataSet(arrayList, "File Type")
        pieDataSet.colors = ColorTemplate.createColors(
            intArrayOf(
                getResources().getColor(R.color.piechart_red),
                getResources().getColor(R.color.piechart_blue),
                getResources().getColor(R.color.piechart_origin),
                getResources().getColor(R.color.piechart_violet),
                getResources().getColor(R.color.piechart_yl),
                getResources().getColor(R.color.piechart_green)
            )
        )
        sdPieChart!!.data = PieData(pieDataSet)
        sdPieChart!!.animateY(1000)
        sdPieChart!!.setUsePercentValues(true)
        sdPieChart!!.invalidate()
        val pieChart = sdPieChart
        pieChart!!.centerText =
            Formatter.formatFileSize(this, statFs.freeBytes) + " Free"
        sdPieChart!!.setDrawEntryLabels(false)
        val description = Description()
        description.text = "External Storage"
        description.textSize = 16.0f
        sdPieChart!!.description = description
        sdPieChart!!.legend.orientation = Legend.LegendOrientation.VERTICAL
    }

    val externalStorageDirectories: Array<String?>
        get() {
            val LOG_TAG = "SDCARD"
            val results: MutableList<String> = ArrayList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val externalDirs = getExternalFilesDirs(null)
                for (file in externalDirs) {
                    val path = file.path.split("/Android".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[0]
                    var addPath = false
                    addPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Environment.isExternalStorageRemovable(file)
                    } else {
                        Environment.MEDIA_MOUNTED == EnvironmentCompat.getStorageState(file)
                    }
                    if (addPath) {
                        results.add(path)
                    }
                }
            }
            if (results.isEmpty()) {
                var output = ""
                try {
                    val process = ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start()
                    process.waitFor()
                    val `is` = process.inputStream
                    val buffer = ByteArray(1024)
                    while (`is`.read(buffer) != -1) {
                        output = output + String(buffer)
                    }
                    `is`.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (!output.trim { it <= ' ' }.isEmpty()) {
                    val devicePoints =
                        output.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (voldPoint in devicePoints) {
                        results.add(voldPoint.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[2])
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var i = 0
                while (i < results.size) {
                    if (!results[i].lowercase(Locale.getDefault())
                            .matches(".*[0-9a-f]{4}[-][0-9a-f]{4}".toRegex())
                    ) {
                        Log.d(LOG_TAG, results[i] + " might not be extSDcard")
                        results.removeAt(i--)
                    }
                    i++
                }
            } else {
                var i = 0
                while (i < results.size) {
                    if (!results[i].lowercase(Locale.getDefault())
                            .contains("ext") && !results[i].lowercase(
                            Locale.getDefault()
                        ).contains("sdcard")
                    ) {
                        Log.d(LOG_TAG, results[i] + " might not be extSDcard")
                        results.removeAt(i--)
                    }
                    i++
                }
            }
            val storageDirectories = arrayOfNulls<String>(results.size)
            for (i in results.indices) storageDirectories[i] = results[i]
            return storageDirectories
        }

    internal inner class BackgroundSizeCalculation : Thread() {
        override fun run() {
            var cursor: Cursor
            val proj = arrayOf(
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA
            )
            cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj,
                null,
                null,
                null
            )!!
            cursor.moveToFirst()
            do {
                if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        .contains(
                            Environment.getExternalStorageDirectory().path
                        )
                ) imagesSize += cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media.SIZE
                    )
                ).toLong() else sdimagesSize += cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Images.Media.SIZE
                    )
                ).toLong()
            } while (cursor.moveToNext())
            val proj_audio = arrayOf(
                MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA
            )
            cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                proj_audio,
                null,
                null,
                null
            )!!
            cursor.moveToFirst()
            if (cursor.getCount() != 0) {
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                            .contains(
                                Environment.getExternalStorageDirectory().path
                            )
                    ) audioSize += cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.SIZE
                        )
                    ).toLong() else sdaudioSize += cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.SIZE
                        )
                    ).toLong()
                } while (cursor.moveToNext())
            }
            val proj_video = arrayOf(
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATA
            )
            cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj_video,
                null,
                null,
                null
            )!!
            cursor.moveToFirst()
            if (cursor.getCount() != 0) {
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                            .contains(
                                Environment.getExternalStorageDirectory().path
                            )
                    ) videoSize += cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Video.Media.SIZE
                        )
                    ).toLong() else sdvideoSize += cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Video.Media.SIZE
                        )
                    ).toLong()
                } while (cursor.moveToNext())
            }
            val pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
            val doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc")
            val docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx")
            val xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls")
            val xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx")
            val ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt")
            val pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx")
            val txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
            val rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx")
            val rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf")
            val html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html")
            val table = MediaStore.Files.getContentUri("external")
            val column =
                arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE)
            val where = (MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?")
            val args = arrayOf(pdf, doc, docx, xls, xlsx, ppt, pptx, txt, rtx, rtf, html)
            cursor = contentResolver.query(table, column, where, args, null)!!
            cursor.moveToFirst()
            do {
                if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                        .contains(
                            Environment.getExternalStorageDirectory().path
                        )
                ) docsSize += cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Files.FileColumns.SIZE
                    )
                ).toLong() else sddocsSize += cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Files.FileColumns.SIZE
                    )
                ).toLong()
            } while (cursor.moveToNext())
        }
    }
}
