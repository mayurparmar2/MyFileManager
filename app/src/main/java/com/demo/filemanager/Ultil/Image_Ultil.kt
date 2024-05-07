package com.demo.filemanager.Ultil

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.demo.filemanager.DTO.File_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class Image_Ultil(private val context: Activity) {
    var Album_iimage = java.util.ArrayList<File_DTO>()
    companion object{
        var image_list = java.util.ArrayList<File_DTO>()
        var totolsize: Long = 0
    }
    suspend fun allImage(): ArrayList<File_DTO> = suspendCoroutine { continuation ->
        CoroutineScope(Dispatchers.Main).launch {
            // Call the function
            val result = getAllShownImagesPath()

            // Return the result
            continuation.resume(result as ArrayList<File_DTO>)
        }
    }

//    fun allImage(): ArrayList<File_DTO> {
//        image_list = getAllShownImagesPath()
//        return image_list
//    }


    suspend fun getAllShownImagesPath(): List<File_DTO> = withContext(Dispatchers.IO) {
        val arrayList = ArrayList<File_DTO>()

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                "bucket_display_name",
                "_size",
                "_data",
                "_id",
                "date_added",
                "title",
                "_display_name",
                "date_modified"
            ),
            null,
            null,
            "date_added ASC"
        )?.use { query ->
            while (query.moveToNext()) {
                try {
                    val size = query.getLong(query.getColumnIndexOrThrow("_size"))
                    val modifiedDate = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(
                        query.getLong(query.getColumnIndexOrThrow("date_modified")) * 1000
                    )

                    val file_DTO = File_DTO().apply {
                        path = query.getString(query.getColumnIndexOrThrow("_data"))

                        val displayName =
                            query.getString(query.getColumnIndexOrThrow("_display_name"))
                        name = if (displayName == "null") File(path).name else displayName

                        val bucketName =
                            query.getString(query.getColumnIndexOrThrow("bucket_display_name"))
                        abumname = if (bucketName == "null") "Unknown" else bucketName

                        date = modifiedDate
                        id = query.getString(query.getColumnIndexOrThrow("_id"))
                    }

                    Image_Ultil.totolsize += size
                    arrayList.add(file_DTO)
                } catch (unused: Exception) {
                    Log.d("AAAA", "getAllShownImagesPath: ")
                }
            }
        }

        return@withContext arrayList
    }
//    suspend fun getAllShownImagesPath(): ArrayList<File_DTO> = withContext(Dispatchers.IO) {
//        val arrayList = ArrayList<File_DTO>()
//        val query = context.contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            arrayOf(
//                "bucket_display_name",
//                "_size",
//                "_data",
//                "_id",
//                "date_added",
//                "title",
//                "_display_name",
//                "date_modified"
//            ),
//            null,
//            null,
//            "date_added ASC"
//        )
//
//        while (query!!.moveToNext()) {
//            try {
//                var size = query.getLong(query.getColumnIndexOrThrow("_size"))
//                @SuppressLint("Range") val format =
//                    SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(
//                        java.lang.Long.valueOf(
//                            query.getLong(
//                                query.getColumnIndex("date_modified")
//                            ) * 1000
//                        )
//                    )
//
//                val file_DTO = File_DTO().apply {
//                    path = query.getString(query.getColumnIndexOrThrow("_data"))
//
//                    val displayName = query.getString(query.getColumnIndexOrThrow("_display_name"))
//                    name = if (displayName == "null") {
//                        File(path).name
//                    } else {
//                        displayName
//                    }
//
//                    val size = Ultil(context).bytesToHuman(size)
//
//                    var bucketName =
//                        query.getString(query.getColumnIndexOrThrow("bucket_display_name"))
//                    if (bucketName == "null") {
//                        bucketName = "Unknown"
//                    }
//                    abumname = bucketName
//
//                    date = format
//                    id = query.getString(query.getColumnIndexOrThrow("_id"))
//                }
//
//                Image_Ultil.totolsize += size
//                arrayList.add(file_DTO)
//            } catch (unused: Exception) {
//                Log.d("AAAA", "getAllShownImagesPath: ")
//            }
//        }
//
//        return@withContext arrayList
//    }

//    private fun getAllShownImagesPath(): ArrayList<File_DTO> {
//        val arrayList = ArrayList<File_DTO>()
//        val query = context.contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            arrayOf(
//                "bucket_display_name",
//                "_size",
//                "_data",
//                "_id",
//                "date_added",
//                "title",
//                "_display_name",
//                "date_modified"
//            ),
//            null,
//            null,
//            "date_added ASC"
//        )
//        while (query!!.moveToNext()) {
//            try {
//                val j = query.getLong(query.getColumnIndexOrThrow("_size"))
//                @SuppressLint("Range") val format =
//                    SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(
//                        java.lang.Long.valueOf(
//                            query.getLong(
//                                query.getColumnIndex("date_modified")
//                            ) * 1000
//                        )
//                    )
//                val file_DTO = File_DTO()
//                val string = query.getString(query.getColumnIndexOrThrow("_data"))
//                val str = "" + query.getString(query.getColumnIndexOrThrow("_display_name"))
//                file_DTO.path = string
//                if (str == "null") {
//                    file_DTO.name = File(string).name
//                } else {
//                    file_DTO.name = str
//                }
//                file_DTO.size = Ultil(context).bytesToHuman(j)
//                var str2 = "" + query.getString(query.getColumnIndexOrThrow("bucket_display_name"))
//                if (str2 == "null") {
//                    str2 = "Unknow"
//                }
//                file_DTO.abumname = str2
//                file_DTO.date = format
//                file_DTO.id = query.getString(query.getColumnIndexOrThrow("_id"))
//                Image_Ultil.totolsize += query.getLong(query.getColumnIndexOrThrow("_size"))
//                arrayList.add(file_DTO)
//            } catch (unused: Exception) {
//                Log.d("AAAA", "getAllShownImagesPath: ")
//            }
//        }
//        return arrayList
//    }

    suspend fun getallalbumImage(): java.util.ArrayList<File_DTO> {
        if (Album_iimage.size == 0 || Album_iimage.isEmpty()) {
            Album_iimage = getImageBucket(context) as java.util.ArrayList<File_DTO>
        }
        return Album_iimage
    }


    suspend fun getImageBucket(context: Context): List<File_DTO> = withContext(Dispatchers.IO) {
        val fileDTOList = mutableListOf<File_DTO>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf("bucket_display_name", "_data", "date_modified")
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_MODIFIED)
//        try {
            context.contentResolver.query(uri, projection, null, null, "date_added ASC")?.use { cursor ->
                while (cursor.moveToNext()) {
                    val albumName:String? = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)as Int)
                    val filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)as Int)
                    val dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)as Int)

//                    val albumName = cursor.getString(cursor.getColumnIndex(projection[0]) as Int)
//                    val filePath = cursor.getString(cursor.getColumnIndex(projection[1])as Int)
//                    val dateModified = cursor.getLong(cursor.getColumnIndex(projection[2])as Int) * 1000L



                    val fileName = filePath.substringAfterLast('/')
                    val folderPath = filePath.replace("/$fileName", "")

                    if (java.io.File(filePath).exists()) {
                        val fileDTO = createFileDTO(fileDTOList,albumName?:"", filePath, folderPath, dateModified)
                        fileDTOList.add(fileDTO)
                    }
                }
            }
//        } catch (e: Exception) {
//            Log.e("TAG", "Exception is: $e")
//            e.printStackTrace()
//        }

        if (fileDTOList.isEmpty()) {
            Log.e("TAG", "fileDTOList is empty")
        }

        fileDTOList
    }




//    private fun createFileDTO(
//        fileDTOList: MutableList<File_DTO>,
//        albumName: String,
//        filePath: String,
//        folderPath: String,
//        dateModified: Long
//    ): File_DTO {
//        fileDTOList.find { it.abumname == albumName }?.let {
//            fileDTOList.remove(it)
//            return it.copy(totalitem = it.totalitem + 1, path = filePath, realpath = folderPath,
//                date = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss", Locale.getDefault())
//                    .format(Date(dateModified)))
//        }
//        return File_DTO().apply {
//            this.abumname = albumName
//            this.totalitem = 1
//            this.date = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss", Locale.getDefault())
//                .format(Date(dateModified))
//            this.path = filePath
//        }
//    }

    private fun createFileDTO(
        fileDTOList: MutableList<File_DTO>,
        albumName: String,
        filePath: String,
        folderPath: String,
        dateModified: Long
    ): File_DTO {
        fileDTOList.find { it.abumname == albumName }?.apply {
            val totalItems = this.totalitem
            fileDTOList.remove(this)
            return File_DTO().apply {
                this.abumname = albumName
                this.totalitem = totalItems + 1
                this.path = filePath
                this.realpath = folderPath
                this.date = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss", Locale.getDefault())
                    .format(Date(dateModified))

            }
        }
        return File_DTO().apply {
            this.abumname = albumName
            this.totalitem = 1
            this.date = SimpleDateFormat("dd/MM/yyyy | HH:mm:ss", Locale.getDefault())
                .format(Date(dateModified))
            this.path = filePath
        }
    }

}