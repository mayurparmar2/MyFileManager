package com.demo.filemanager.Ultil

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.loader.content.CursorLoader
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.DocsFetchCallback
import com.demo.filemanager.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Locale

class Data_Manager(val mContext: Context) {
    //    var name: java.util.ArrayList<String>? = null
    var name: ArrayList<String>? = null

    companion object {
        private var date_and_time: ArrayList<String>? = null

        //        private var files: Array<File>? = null
        lateinit var files: Array<File>
        var mContext: Context? = null
        private var sizelist: java.util.ArrayList<String>? = null
        var totalItem: java.util.ArrayList<Int>? = null
        private var file_dtos = java.util.ArrayList<File_DTO>()
        private var fileall = java.util.ArrayList<File_DTO>()
        private var file_apk = java.util.ArrayList<File_DTO>()
        private var drawables = java.util.ArrayList<Drawable>()
        var sizedoccument: Long = 0


    }

    fun setImagesData() {
        date_and_time = ArrayList()
        name = ArrayList()

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf("_id", "_data")
        val cursor: Cursor? =
            CursorLoader(mContext, uri, projection, null, null, null).loadInBackground()

        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndexOrThrow = it.getColumnIndexOrThrow("_data")
                val tempFiles = arrayOfNulls<File?>(it.count)

                var i = 0
                do {
                    tempFiles[i] = File(it.getString(columnIndexOrThrow))
                    date_and_time?.add(SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(tempFiles[i]?.lastModified()))
                    tempFiles[i]?.name?.let { it1 -> name?.add(it1) }
                    i++
                } while (it.moveToNext())
                files = (tempFiles as Array<File>?)!!
            }
            it.close()
        }
    }


    fun setAudio(context: Context) {
        date_and_time = ArrayList()
        name = ArrayList()

        val query: Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf("_data"),
            null,
            null,
            null
        )

        query?.let {
//            files = arrayOfNulls(it.count)
            val tempFiles = arrayOfNulls<File?>(it.count)
            if (it.moveToFirst()) {
                val columnIndexOrThrow = it.getColumnIndexOrThrow("_data")

                var i = 0
                do {
                    tempFiles!![i] = File(it.getString(columnIndexOrThrow))
                    date_and_time!!.add(SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(tempFiles!![i]?.lastModified()))
                    tempFiles!![i]?.name?.let { it1 -> name!!.add(it1) }
                    i++
                } while (it.moveToNext())
                files = (tempFiles as Array<File>?)!!
            } else {
                Toast.makeText(context, "No Audio Files Present", Toast.LENGTH_LONG).show()
            }

            it.close()
        }
    }

//    // Declare these variables in the class scope
//    var mContext: Context? = null
//    var dateAndTime: ArrayList<String>? = null
//    var name: ArrayList<String>? = null
//    var files: Array<File?>? = null


    fun getDate_and_time(i: Int): String? {
        return date_and_time?.get(i)
    }

    fun getsize(i: Int): String {
        return sizelist!!.get(i)
    }

    fun getTotalItem(i: Int): Int? {
        return totalItem?.get(i)
    }

    fun getFiles(i: Int): File? {
        return files?.get(i)
    }

    fun getIconId(i: Int): Int {
        val str: String
        str = try {
            files!!.get(i).getAbsolutePath().lowercase(Locale.getDefault())
        } catch (unused: java.lang.Exception) {
            ""
        }
        return if (files!!.get(i)
                .isDirectory()
        ) R.drawable.image_folder else if (str.contains(".apk")) R.drawable.icon_apk else if (str.contains(
                ".docx"
            ) || str.contains(".txt")
        ) R.drawable.icon_doc else if (str.contains(".mp3") || str.contains(".wav") || str.contains(
                ".flac"
            ) || str.contains(".wma") || str.contains(".m4a")
        ) R.drawable.button_music else if (str.contains(".mp4") || str.contains(".avi") || str.contains(
                ".mkv"
            ) || str.contains(".vob") || str.contains(".mov")
        ) R.drawable.videoico else if (str.contains(".jpg") || str.contains(".png") || str.contains(
                ".gif"
            ) || str.contains(".tiff") || str.contains(".jpeg")
        ) R.drawable.imageicon else if (str.contains(".pdf")) R.drawable.icon_pdf else if (str.contains(
                ".ppt"
            )
        ) R.drawable.icon_ppt else if (str.contains(".xls") || str.contains(".csv")) R.drawable.icon_xls else if (str.contains(
                ".zip"
            ) || str.contains(".rar")
        ) R.drawable.icon_zip else R.drawable.unknowfile
    }

    fun getName(i: Int): String? {
        return name?.get(i)
    }

    fun filesDowload(): java.util.ArrayList<File_DTO> {
        fileall = java.util.ArrayList<File_DTO>()
        fileall =
            getallfilewithpath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        return fileall
    }

    fun getfilepdf(): java.util.ArrayList<File_DTO>? {
        val arrayList = java.util.ArrayList<File_DTO>()
        for (i in file_dtos.indices) {
            if (file_dtos.get(i).path?.lowercase(Locale.getDefault())?.contains(".pdf") == true) {
                arrayList.add(file_dtos.get(i))
            }
        }
        return arrayList
    }


    fun getfidocx(): ArrayList<File_DTO> {
        val arrayList = ArrayList<File_DTO>()

        if (file_dtos.isNotEmpty()) {
            for (fileDTO in file_dtos) {
                val lowerCasePath = fileDTO.path?.toLowerCase()
                if (lowerCasePath != null && (lowerCasePath.contains(".docx") || lowerCasePath.contains(
                        ".doc"
                    ))
                ) {
                    arrayList.add(fileDTO)
                }
            }
        }

        return arrayList
    }

    fun getfileppt(): ArrayList<File_DTO> {
        val arrayList = ArrayList<File_DTO>()

        if (file_dtos.isNotEmpty()) {
            for (fileDTO in file_dtos) {
                val lowerCasePath = fileDTO.path?.toLowerCase()
                if (lowerCasePath != null && (lowerCasePath.contains(".pptx") || lowerCasePath.contains(
                        ".ppt"
                    ))
                ) {
                    arrayList.add(fileDTO)
                }
            }
        }

        return arrayList
    }

    fun getfiletxt(): ArrayList<File_DTO> {
        val arrayList = ArrayList<File_DTO>()

        if (file_dtos.isNotEmpty()) {
            for (fileDTO in file_dtos) {
                val lowerCasePath = fileDTO.path?.toLowerCase()
                if (lowerCasePath != null && lowerCasePath.contains(".txt")) {
                    arrayList.add(fileDTO)
                }
            }
        }

        return arrayList
    }

    fun getfilexls(): ArrayList<File_DTO> {
        val arrayList = ArrayList<File_DTO>()

        if (file_dtos.isNotEmpty()) {
            for (fileDTO in file_dtos) {
                val lowerCasePath = fileDTO.path?.toLowerCase()
                if (lowerCasePath != null && (lowerCasePath.contains(".xlsx") || lowerCasePath.contains(
                        ".xls"
                    ) || lowerCasePath.contains(".csv"))
                ) {
                    arrayList.add(fileDTO)
                }
            }
        }

        return arrayList
    }

    suspend fun setDocs(context: Context, callback: DocsFetchCallback<List<File_DTO>>? = null) = withContext(Dispatchers.IO) {
        return@withContext try {
            val extensions = arrayOf("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "html")
            context.sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"))
            val dateAndTime = mutableListOf<String>()
            val name = mutableListOf<String>()
            val fileDtos = mutableListOf<File_DTO>()
            val projection = if (Build.VERSION.SDK_INT <= 28) {
                arrayOf("_data", "mime_type", "_id")
            } else {
                arrayOf("_data", "mime_type", "volume_name", "_id")
            }

            context.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    projection,
                    "mime_type IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    extensions,
                    null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val filePath = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
                    val file = File(filePath)
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss")
                    dateAndTime.add(simpleDateFormat.format(file.lastModified()))
                    name.add(file.name)
                    val fileDto = File_DTO().apply {
                        this.path = file.path
                        this.date = simpleDateFormat.format(file.lastModified())
                        this.name = file.name
                        id = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                        try {
                            volumname = cursor.getString(cursor.getColumnIndexOrThrow("volume_name"))
                        } catch (e: Exception) {
                            volumname = ""
                        }
                        sizedoccument += file.length()
                        size = Ultil(context).bytesToHuman(file.length())
                        minetype = cursor.getString(cursor.getColumnIndexOrThrow("mime_type"))
                    }
                    fileDtos.add(fileDto)
                }
            } ?: throw Exception("Cursor is null")
            callback?.onSuccess(fileDtos)
        } catch (e: Exception) {
            callback?.onFailure(e)
        }
    }
//    @SuppressLint("Range")
//    suspend fun setDocs(context: Context, callback: (List<FileDTO>) -> Unit) = withContext(Dispatchers.IO){
////        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val extensions = arrayOf("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "html")
//                mContext.sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"))
//
//                date_and_time = ArrayList()
//                name = ArrayList()
//                file_dtos = ArrayList()
//
//                val projection = if (Build.VERSION.SDK_INT <= 28) {
//                    arrayOf("_data", "mime_type", "_id")
//                } else {
//                    arrayOf("_data", "mime_type", "volume_name", "_id")
//                }
//
//                val query = mContext.contentResolver.query(
//                    MediaStore.Files.getContentUri("external"),
//                    projection,
//                    "mime_type IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                    extensions,
//                    null
//                )
//
//                val files = Array<File?>(query!!.count) { null }
//                var i = 0
//
//                if (query.moveToFirst()) {
//                    do {
//                        files[i] = File(query.getString(query.getColumnIndexOrThrow("_data")))
//                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss")
//                        date_and_time!!.add(simpleDateFormat.format(files[i]!!.lastModified()))
//                        name?.add(files[i]!!.name)
//
//                        val file_DTO = File_DTO().apply {
//                            path = files[i]!!.path
//                            date = simpleDateFormat.format(files[i]!!.lastModified())
//                            name = files[i]!!.name
//                            id = query.getString(query.getColumnIndexOrThrow("_id"))
//
//                            try {
//                                volumname = query.getString(query.getColumnIndex("volume_name")) ?: ""
//                            } catch (unused: Exception) {
//                                volumname = ""
//                            }
//
//                            sizedoccument += files[i]!!.length()
//                            size = Ultil(mContext).bytesToHuman(files[i]!!.length())
//                            minetype = query.getString(query.getColumnIndexOrThrow("mime_type"))
//                        }
//
//                        file_dtos.add(file_DTO)
//                        i++
//                    } while (query.moveToNext())
//                } else {
//                    Log.e("TAG", "setDocs: ")
//                }
//                query.close()
//            } catch (e: Exception) {
//                // Signal failure
//            }
//        }



    fun readAllAppssss(context: Context): ArrayList<File_DTO> {
        var packageInfo: PackageInfo?
        drawables = ArrayList()
        val arrayList = ArrayList<File_DTO>()

        if (Build.VERSION.SDK_INT >= 26) {
            val launcherApps =
                context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            for (userHandle in launcherApps.profiles) {
                for (launcherActivityInfo in launcherApps.getActivityList(null, userHandle)) {
                    val packageName = launcherActivityInfo.componentName.packageName
                    val flags = launcherActivityInfo.applicationInfo.flags
                    val label = launcherActivityInfo.label.toString()
                    try {
                        packageInfo = context.packageManager.getPackageInfo(packageName, 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        packageInfo = null
                    }
                    val versionName = packageInfo?.versionName ?: ""
                    val lastUpdateTime = packageInfo?.lastUpdateTime ?: 0L
                    val size = getsizeappInstalled1(packageName, context)
                    val fileDTO = File_DTO().apply {
                        name = label
                        packagename = packageName
                        this.size = size
                        date = getdateInstalled(packageName, context)
                        abumname = versionName
                        path = ""
                    }
                    arrayList.add(fileDTO)
                    drawables.add(launcherActivityInfo.getIcon(200))
                }
            }
        } else {
            val intent = Intent("android.intent.action.MAIN").apply {
                addCategory("android.intent.category.LAUNCHER")
            }
            for (resolveInfo in context.packageManager.queryIntentActivities(intent, 0)) {
                val packageName = resolveInfo.activityInfo.packageName
                val activityName = resolveInfo.activityInfo.name
                val flags = resolveInfo.activityInfo.applicationInfo.flags
                val size = getsizeappInstalled1(packageName, context)
                val fileDTO = File_DTO().apply {
                    name = activityName
                    packagename = packageName
                    abumname = resolveInfo.providerInfo?.toString() ?: ""
                    this.size = size
                    date = getdateInstalled(packageName, context)
                }
                arrayList.add(fileDTO)
            }
        }
        return arrayList
    }

    private fun getsizeappInstalled1(packageName: String, context: Context): String? {
        var applicationInfo: ApplicationInfo?
        try {
            applicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            applicationInfo = null
        }

        val file = File(applicationInfo?.publicSourceDir ?: "")
        val bytesToHuman = Ultil(context).bytesToHuman(file.length())
        Log.d("TAG!", "getsizeappInstalled1: ${file.name}")
        return bytesToHuman
    }

    private fun getdateInstalled(packageName: String, context: Context): String {
        var applicationInfo: ApplicationInfo?
        try {
            applicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            applicationInfo = null
        }

        val lastModified = applicationInfo?.let {
            File(it.publicSourceDir).lastModified()
        } ?: 0L

        return SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(lastModified)
    }

    fun getallapp(): java.util.ArrayList<File_DTO> {
        file_apk = java.util.ArrayList<File_DTO>()
        val arrayList: java.util.ArrayList<File_DTO> = getallfileindecive()
        for (i in arrayList.indices) {
            if (arrayList[i].path!!.lowercase(Locale.getDefault()).contains(".apk")) {
                file_apk.add(arrayList[i])
            }
        }
        return file_apk
    }

    fun getallfileindecive(): java.util.ArrayList<File_DTO> {
        var arrayList: java.util.ArrayList<File_DTO> = java.util.ArrayList()
        val arrayList2: java.util.ArrayList<File> = getallpath()
        fileall = java.util.ArrayList<File_DTO>()
        for (i in arrayList2.indices) {
            arrayList = getallfilewithpath(arrayList2[i])
        }
        return arrayList
    }

    fun getallfilewithpath(file: File): java.util.ArrayList<File_DTO> {
        files = file.listFiles() ?: emptyArray()
        if (files == null) {
            files = emptyArray()
        }

        val fileall = ArrayList<File_DTO>()

        files?.apply {
            for (file2 in this) {
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss")
                if (file2 != null) {
                    if (!file2.isDirectory) {
                        val fileDTO = File_DTO().apply {
                            size = Ultil(mContext).bytesToHuman(file2.length())
                            date = simpleDateFormat.format(file2.lastModified())
                            lastmodified = file2.lastModified().toString()
                            name = file2.name
                            path = file2.path
                        }
                        fileall.add(fileDTO)
                    }
                }
            }

        }
        return fileall
    }

    fun getzipwithMediastore(): List<File_DTO> {
        val arrayList = mutableListOf<File_DTO>()

        mContext.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf("_data"),
            "mime_type=? OR mime_type=?",
            arrayOf(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("rar")
            ),
            null
        )?.use { query ->
            val files = mutableListOf<File>()

            while (query.moveToNext()) {
                val filePath = query.getString(query.getColumnIndexOrThrow("_data"))
                val file = File(filePath)

                if (!filePath.contains("%Bin")) {
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss")
                    val fileDTO = File_DTO().apply {
                        path = file.path
                        date = simpleDateFormat.format(file.lastModified())
                        name = file.name
                        size = Ultil(mContext).bytesToHuman(file.length())
                    }
                    arrayList.add(fileDTO)
                }
            }
        }

        return arrayList
    }

    fun setRecycler(file: File, i: Int) {
        date_and_time = java.util.ArrayList()
        sizelist = java.util.ArrayList<String>()
        name = java.util.ArrayList()
        totalItem = java.util.ArrayList<Int>()
        files = file.getAbsoluteFile().listFiles()
        if (files == null) {
            files = arrayOf()
        }

        if (i == 1) {
            sortByName(files)
        } else if (i == 2) {
            sortByDate(files!!)
        } else if (i == 3) {
            sortBySize(files!!)
        } else if (i == -1) {
            sortByNameReverse(files!!)
        } else if (i == -2) {
            sortByDateReverse(files!!)
        } else if (i == -3) {
            sortBySizeReverse(files!!)
        }
        for (file2 in files!!) {
            name?.add(file2.getName())
            date_and_time?.add(SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(file2.lastModified()))

            Ultil(this.mContext).bytesToHuman(file2.length())?.let { sizelist?.add(it) }
            try {
                totalItem?.add(file2.listFiles().size)
            } catch (unused: java.lang.Exception) {
                totalItem?.add(0)
            }
        }
    }

    fun sortBySize(fileArr: Array<File>) {
        fileArr.sortWith(Comparator { file, file2 ->
            file.length().compareTo(file2.length())
        })
    }

    fun sortByName(fileArr: Array<File>) {
        fileArr.sortWith(Comparator { file, file2 ->
            file.name.toLowerCase().compareTo(file2.name.toLowerCase())
        })
    }

    fun sortByDate(fileArr: Array<File>) {
        fileArr.sortWith(Comparator { file, file2 ->
            file.lastModified().compareTo(file2.lastModified())
        })
    }

    fun sortByDateReverse(fileArr: Array<File>) {
        fileArr.sortWith(Collections.reverseOrder(Comparator { file, file2 ->
            file.lastModified().compareTo(file2.lastModified())
        }))
    }

    fun sortByNameReverse(fileArr: Array<File>) {
        fileArr.sortWith(Collections.reverseOrder(Comparator { file, file2 ->
            file.getName().compareTo(file2.getName());
        }))
    }

    fun sortBySizeReverse(fileArr: Array<File>) {
        fileArr.sortWith(Collections.reverseOrder(Comparator { file, file2 ->
            file.length().compareTo(file2.length())
        }))
    }


//    fun getzipwithMediastore(): ArrayList<File_DTO> {
//        val arrayList = ArrayList<File_DTO>()
//        var i = 0
//
//        val query = mContext.contentResolver.query(
//            MediaStore.Files.getContentUri("external"),
//            arrayOf("_data"),
//            "mime_type=? OR mime_type=?",
//            arrayOf(
//                MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip"),
//                MimeTypeMap.getSingleton().getMimeTypeFromExtension("rar")
//            ),
//            null
//        )
//
//        files = Array(query?.count ?: 0) { null }
//        query?.moveToFirst()
//
//        if (query?.count != 0) {
//            do {
//                files[i] = File(query!!.getString(query.getColumnIndexOrThrow("_data")))
//                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss")
//
//                if (files[i]?.path?.contains("%Bin") == true) {
//                    Log.d(UStats.TAG, "getzipwithMediastore: ")
//                } else {
//                    val fileDTO = File_DTO().apply {
//                        path = files!![i]?.path ?: ""
//                        date = simpleDateFormat.format(files[i]?.lastModified() ?: 0)
//                        name = files!![i]?.name ?: ""
//                        size = Ultil(mContext).bytesToHuman(files[i]?.length() ?: 0)
//                    }
//                    arrayList.add(fileDTO)
//                }
//                i++
//            } while (query.moveToNext())
//        } else {
//            Log.e("TAG", "getzipwithMediastore: ")
//        }
//        query?.close()
//
//        return arrayList
//    }

    fun getallpath(): java.util.ArrayList<File> {
        val arrayList = java.util.ArrayList<File>()
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES))
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
        arrayList.add(Environment.getExternalStorageDirectory())
        arrayList.add(File("/storage/emulated/0/Android/data"))
        return arrayList
    }
}
