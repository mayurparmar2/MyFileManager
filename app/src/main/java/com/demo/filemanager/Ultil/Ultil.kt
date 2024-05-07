package com.demo.filemanager.Ultil

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.os.StrictMode
import android.os.storage.StorageManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.demo.filemanager.Activity.MainActivity.Companion.getStore
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.CallbackUpdateMusic
import com.demo.filemanager.R
import com.demo.filemanager.database.FavoritSongs
import com.demo.filemanager.database.RecentAdd
import com.google.android.gms.common.util.IOUtils.copyStream
import kotlinx.android.synthetic.main.dialogrename.edt_filename
import kotlinx.android.synthetic.main.dialogrename.img_delete
import kotlinx.android.synthetic.main.dialogrename.txt_Cancel
import kotlinx.android.synthetic.main.dialogrename.txt_OK
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

class Ultil(val context: Context) {

    companion object{
//        val context: Context = mContext!!
    }

    fun addFav(file_DTO: File_DTO) {
        val favoritSongs = FavoritSongs(context)
        favoritSongs.open()
        if (check(file_DTO.path)) {
            Toast.makeText(context, "File already exists !", Toast.LENGTH_SHORT).show()
        } else {
            favoritSongs.addRow(file_DTO)
            Toast.makeText(context, "File was added !", Toast.LENGTH_SHORT).show()
        }
        favoritSongs.close()
    }
    private fun check(str: String): Boolean {
        val favoritSongs = FavoritSongs(context)
        favoritSongs.open()
        ArrayList<Any?>()
        val allRows = favoritSongs.getAllRows()
        var z = false
        if (allRows != null) {
            var z2 = false
            for (i in allRows.indices) {
                if (allRows[i].path == str) {
                    z2 = true
                }
            }
            z = z2
        }
        favoritSongs.close()
        return z
    }
    fun dialogRename(file_DTO: File_DTO, str: String?) {
        val dialog = Dialog(context)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        dialog.setContentView(R.layout.dialogrename)
        dialog.setCancelable(false)
        dialog.edt_filename.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(2, 0)
        val path = file_DTO.path
        File(path)
        var name = File(file_DTO.path).name
        dialog.img_delete.setOnClickListener { dialog.edt_filename.setText("") }
        dialog.txt_OK.setOnClickListener(View.OnClickListener {
            if (dialog.edt_filename.text.toString() == "" || dialog.edt_filename.text.length == 0) {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.cannot_be_left_be_blank),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            AndroidXI(context).renamewithpathAPI29(path, dialog.edt_filename.text.toString())
            context.sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"))
            try {
                Thread.sleep(1000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            CallbackUpdateMusic.instance?.change()
            inputMethodManager.toggleSoftInput(1, 0)
            dialog.cancel()
        })
        try {
            name = name.substring(0, name.lastIndexOf(FileUltils.HIDDEN_PREFIX))
        } catch (unused: java.lang.Exception) {
        }
        dialog.edt_filename.setText("" + name)
        dialog.edt_filename.setSelection(dialog.edt_filename.text.length)
        dialog.txt_Cancel.setOnClickListener {
            inputMethodManager.toggleSoftInput(1, 0)
            dialog.cancel()
        }
        dialog.edt_filename.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                if (dialog.edt_filename.text.toString().length > 5) {
                    dialog.img_delete.visibility = View.VISIBLE
                } else {
                    dialog.img_delete.visibility = View.GONE
                }
            }
        })
        dialog.show()
    }
    fun getallfileRecent(): java.util.ArrayList<File_DTO>? {
        val recentAdd = RecentAdd(context)
        recentAdd.open()
        val allRows: java.util.ArrayList<File_DTO>? = recentAdd.getAllRows()
        recentAdd.close()
        return allRows
    }

    fun checkrecent_fileExist() {
        val recentAdd = RecentAdd(context)
        recentAdd.open()
        java.util.ArrayList<Any?>()
        val allRows = recentAdd.getAllRows()
        for (i in allRows!!.indices) {
            if (!File(allRows[i].path).exists()) {
                recentAdd.deleteRowByPath(allRows[i].path!!)
            }
        }
        recentAdd.close()
    }

    fun getDate(j: Long): String? {
        return SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(java.lang.Long.valueOf(j))
    }


    fun showdiloginfo(file_DTO: File_DTO, str: String) {
        val dialog = Dialog(context)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
//        dialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        dialog.setContentView(R.layout.dialog_info)
        val textView = dialog.findViewById<View>(R.id.file_name) as TextView
        val textView2 = dialog.findViewById<View>(R.id.size) as TextView
        val textView3 = dialog.findViewById<View>(R.id.location) as TextView
        val textView4 = dialog.findViewById<View>(R.id.modifiled) as TextView
        val textView5 = dialog.findViewById<View>(R.id.dissmis) as TextView
        val path = file_DTO.path
        val name = File(path).name
        val replace = path!!.replace("/$name", "")
        if (str == "almumimage") {
            textView.text = "File Name: " + file_DTO.abumname
            textView2.text = "Total: " + file_DTO.totalitem
            textView3.text = "Location: $replace"
            textView4.text = "Last Modified: " + file_DTO.date
        } else if (str == "Videoalbum") {
            textView.text = "File Name: " + file_DTO.abumname
            textView2.text = "Toltal: " + file_DTO.totalitem
            textView3.text = "Location: $replace"
            textView4.text = "Last Modified: " + file_DTO.date
        } else if (str == "Video") {
            textView.text = "File Name: " + file_DTO.name
            textView2.setText(context.getString(R.string.size1) + file_DTO.size)
            textView3.text = "Location: " + file_DTO.path
            textView4.text = "Last Modified: " + file_DTO.date
        } else if (str == "app") {
            textView.text = "App Name: " + file_DTO.name
            textView2.text = "Size: " + file_DTO.size
            textView3.text = "Package Name: " + file_DTO.packagename
            textView4.text = "Version: " + file_DTO.abumname
        } else {
            textView.text = "File Name: " + file_DTO.name
            textView2.text = "Size: " + file_DTO.size
            textView3.text = "Location: " + file_DTO.path
            textView4.text = "Last Modified: " + file_DTO.date
        }
        textView5.setOnClickListener { dialog.cancel() }
        dialog.show()
    }


    fun openFile(file: File) {
        Uri.fromFile(file)
        val uriForFile =
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        val intent = Intent("android.intent.action.VIEW")
        if (file.toString().lowercase(Locale.getDefault()).contains(".apk") || file.toString()
                .lowercase(
                    Locale.getDefault()
                ).contains(".apks")
        ) {
            installAPK(file.path)
            return
        }
        if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
            intent.setDataAndType(uriForFile, "application/msword")
        } else if (file.toString().contains(".pdf")) {
            intent.setDataAndType(uriForFile, "application/pdf")
        } else if (file.toString().contains(".ppt") || file.toString().contains(".pptx")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint")
        } else if (file.toString().contains(".xls") || file.toString().contains(".xlsx")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel")
        } else if (file.toString().contains(".zip") || file.toString().contains(".rar")) {
            intent.setDataAndType(uriForFile, "application/x-wav")
        } else if (file.toString().contains(".rtf")) {
            intent.setDataAndType(uriForFile, "application/rtf")
        } else if (file.toString().contains(".gif") || file.toString()
                .contains(".jpg") || file.toString().contains(".png") || file.toString()
                .contains(".tiff")
        ) {
            intent.setDataAndType(uriForFile, "image/*")
        } else if (file.toString().contains(".txt")) {
            intent.setDataAndType(uriForFile, "text/plain")
        } else if (file.toString().endsWith(".mp3") || file.toString()
                .endsWith(".wav") || file.toString().endsWith(".flac") || file.toString()
                .endsWith(".wma") || file.toString().endsWith(".mp4a")
        ) {
            intent.setDataAndType(uriForFile, "audio/*")
        } else if (file.toString().contains(".mp4") || file.toString()
                .contains(".avi") || file.toString().contains(".mkv") || file.toString()
                .contains(".vob") || file.toString().contains(".mov")
        ) {
            openVideo(uriForFile)
            return
        } else {
            intent.setDataAndType(uriForFile, "*/*")
        }
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(intent)
        } catch (unused: Exception) {
            Toast.makeText(context, "No activity for action", Toast.LENGTH_SHORT).show()
        }
    }

    fun installAPK(str: String?) {
        if (Build.VERSION.SDK_INT >= 26 && !context.getPackageManager()
                .canRequestPackageInstalls()
        ) {
            context.startActivity(
                Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES").setData(
                    Uri.parse(String.format("package:%s", context.getPackageName()))
                )
            )
        }
        val file = File(str)
        val uriForFile =
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        if (file.exists()) {
            val intent = Intent("android.intent.action.INSTALL_PACKAGE")
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.getApplicationContext().startActivity(intent)
                return
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Log.e("TAG", "Error in opening the file!")
                return
            }
        }
        Toast.makeText(context, "installing", Toast.LENGTH_LONG).show()
    }
    private fun openVideo(uri: Uri) {
        uri.path
        val intent = Intent()
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
        intent.setAction("android.intent.action.VIEW")
        intent.setDataAndType(uri, "video/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
       context.startActivity(intent)
    }

    fun bytesToHuman(size: Long): String? {
        if (size <= 0) {
            return "0 B"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }
    fun copyFile(str: String) {
        val replaceAll = str.replace("/".toRegex(), "%")
        val file: File = File(getStore(context) + "/Bin", replaceAll)
        try {
            val openInputStream: InputStream? = context.getContentResolver().openInputStream(Uri.fromFile(File(str)))
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.let {
                copyStream(it, fileOutputStream)
                fileOutputStream.close()
                openInputStream.close()
                context.sendBroadcast(
                    Intent(
                        "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                        Uri.fromFile(file)
                    )
                )
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e2: IOException) {
            e2.printStackTrace()
        }
    }
    fun floatForm(d: Double): String? {
        return DecimalFormat("#.##").format(d)
    }
    fun sharefile(file_DTO: File_DTO) {
        val path = file_DTO.path
        val uriForFile = FileProvider.getUriForFile(
            context,
            context.getPackageName() + ".provider",
            File(file_DTO.path)
        )
        val intent = Intent()
        intent.setAction("android.intent.action.SEND")
        intent.putExtra("android.intent.extra.STREAM", uriForFile)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(
                ".tiff"
            )
        ) {
            intent.setDataAndType(uriForFile, "image/*")
        } else if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac") || path.endsWith(
                ".wma"
            ) || path.endsWith(".mp4a")
        ) {
            intent.setDataAndType(uriForFile, "audio/*")
        } else if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".mkv") || path.endsWith(
                ".vob"
            ) || path.endsWith(".mov")
        ) {
            intent.setDataAndType(uriForFile, "video/*")
        } else {
            intent.setDataAndType(uriForFile, "document/*")
        }
        context.startActivity(Intent.createChooser(intent, "Remi_FileManger"))
    }
    fun intent_tree() {
        if (Build.VERSION.SDK_INT >= 29) {
            val createOpenDocumentTreeIntent =
                (context.getSystemService(Context.STORAGE_SERVICE) as StorageManager).primaryStorageVolume.createOpenDocumentTreeIntent()
            val uri =
                (createOpenDocumentTreeIntent.getParcelableExtra<Parcelable>("android.provider.extra.INITIAL_URI") as Uri?).toString()
            Log.e("UStats.TAG", "INITIAL_URI scheme: $uri")
            val replace = uri.replace("/root/", "/document/")
            val replace2 = "Android/data".replace("/", "%2F")
            createOpenDocumentTreeIntent.putExtra(
                "android.provider.extra.INITIAL_URI", Uri.parse(
                    "$replace%3A$replace2"
                )
            )
            createOpenDocumentTreeIntent.putExtra("android.content.extra.SHOW_ADVANCED", true)
            (context as Activity).startActivityForResult(createOpenDocumentTreeIntent, 111)
        }
    }
}
