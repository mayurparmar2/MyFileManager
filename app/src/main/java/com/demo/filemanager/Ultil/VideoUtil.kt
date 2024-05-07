package com.demo.filemanager.Ultil

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class VideoUtil(private val context: Context) {

    companion object{
        var totolsize: Long = 0
        private var file_dtos = java.util.ArrayList<File_DTO>()

    }
    fun getBitmap(str: String?): Bitmap? {
        return try {
            Bitmap.createBitmap(
                ThumbnailUtils.createVideoThumbnail(
                    str!!, 1
                )!!, 0, 0, 100, 100, Matrix(), true
            )
        } catch (unused: Exception) {
            BitmapFactory.decodeResource(context.resources, R.drawable.button_video)
        }
    }
    fun updateAllVidepAlbum(): java.util.ArrayList<File_DTO> {
        file_dtos = java.util.ArrayList<File_DTO>()
        file_dtos = getAllVidepAlbum()
        return file_dtos
    }
    fun getAllVidepAlbum(): java.util.ArrayList<File_DTO> {
        if (file_dtos.isEmpty()) {
            file_dtos = getAllAlbumVideo()
        }
        return file_dtos
    }
    @SuppressLint("Range")
    fun getAllAlbumVideo(): ArrayList<File_DTO> {
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val arrayList = ArrayList<File_DTO>()
        val arrayList2 = ArrayList<String>()
        val strArr = arrayOf(
            "bucket_display_name", "_data", "date_modified", "duration", "_size", "_id"
        )
        val query: Cursor? = context.contentResolver.query(uri, strArr, null, null, null)
        query?.use { cursor ->
            while (cursor.moveToNext()) {
                val sb = StringBuilder()
                sb.append("")
                var albumName = cursor.getString(cursor.getColumnIndex(strArr[0]))
                var path = cursor.getString(cursor.getColumnIndex(strArr[1]))
                var dateModified = cursor.getLong(cursor.getColumnIndex(strArr[2])) * 1000
                val size = cursor.getLong(cursor.getColumnIndex(strArr[4]))
                val id = cursor.getString(cursor.getColumnIndex(strArr[5]))
                val duration = cursor.getLong(cursor.getColumnIndex(strArr[3]))

                if (albumName == "null") {
                    albumName = "Unknown"
                }

                if (File(path).exists()) {
                    if (arrayList2.contains(albumName)) {
                        var i = 0
                        while (i < arrayList.size) {
                            if (arrayList[i].abumname != null && arrayList[i].abumname == albumName) {
                                val totalItem = arrayList[i].totalitem
                                arrayList.removeAt(i)
                                val fileDTO = File_DTO()
                                fileDTO.duration = getDuration(duration)
                                fileDTO.size = Ultil(context).bytesToHuman(size)
                                fileDTO.id = id
                                fileDTO.date = Ultil(context).getDate(dateModified)
                                fileDTO.totalitem = totalItem + 1
                                fileDTO.path = path
                                fileDTO.abumname = albumName
                                totolsize += size
                                arrayList.add(fileDTO)
                                break
                            }
                            i++
                        }
                    } else {
                        val fileDTO = File_DTO()
                        fileDTO.duration = getDuration(duration)
                        fileDTO.size = Ultil(context).bytesToHuman(size)
                        fileDTO.id = id
                        fileDTO.date = Ultil(context).getDate(dateModified)
                        fileDTO.totalitem = 1
                        fileDTO.abumname = albumName
                        fileDTO.path = path
                        arrayList2.add(albumName)
                        arrayList.add(fileDTO)
                    }
                }
            }
        }
        Log.d("TAGX", "onCreate: $arrayList")
        query?.close()
        return arrayList
    }


//    @SuppressLint("Range")
//    fun getAllAlbumVideo(): java.util.ArrayList<File_DTO> {
//        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//        val arrayList = java.util.ArrayList<File_DTO>()
//        val arrayList2: java.util.ArrayList<File_DTO> = java.util.ArrayList()
//        val strArr =
//            arrayOf("bucket_display_name", "_data", "date_modified", "duration", "_size", "_id")
//        val query = context.contentResolver.query(uri, strArr, null, null, null)
//        if (query != null) {
//            while (query.moveToNext()) {
//                val sb = StringBuilder()
//                sb.append("")
//                var i = 0
//                sb.append(query.getString(query.getColumnIndex(strArr[0])))
//                var sb2 = sb.toString()
//                if (sb2 == "null") {
//                    sb2 = "Unknow"
//                }
//                var string = query.getString(query.getColumnIndex(strArr[1]))
//                val j = query.getLong(query.getColumnIndex(strArr[2])) * 1000
//                val j2 = query.getLong(query.getColumnIndex(strArr[4]))
//                val string2 = query.getString(query.getColumnIndex(strArr[5]))
//                val valueOf = query.getLong(query.getColumnIndex(strArr[3]))
//                if (File(string).exists()) {
//                    if (arrayList2.contains(sb2)) {
//                        while (true) {
//                            if (i < arrayList.size) {
//                                if (arrayList[i].abumname != null && arrayList[i].abumname == sb2) {
//                                    val totalitem = arrayList[i].totalitem
//                                    arrayList.remove(arrayList[i])
//                                    val file_DTO = File_DTO()
//                                    file_DTO.duration = getDuration(valueOf)
//                                    file_DTO.size = Ultil(context).bytesToHuman(j2)
//                                    file_DTO.id = string2
//                                    file_DTO.date = Ultil(context).getDate(j)
//                                    file_DTO.totalitem = totalitem + 1
//                                    file_DTO.path = string!!
//                                    file_DTO.abumname = sb2
//                                    totolsize += j2
//                                    arrayList.add(file_DTO)
//                                    break
//                                }
//                                i++
//                                string = string
//                            } else {
//                                break
//                            }
//                        }
//                    } else {
//                        val file_DTO2 = File_DTO()
//                        file_DTO2.duration = getDuration(valueOf)
//                        file_DTO2.size = Ultil(context).bytesToHuman(j2)
//                        file_DTO2.id = string2
//                        file_DTO2.date = Ultil(context).getDate(j)
//                        file_DTO2.totalitem = 1
//                        file_DTO2.abumname = sb2
//                        file_DTO2.path = string!!
//                        arrayList2.add(sb2)
//                        arrayList.add(file_DTO2)
//                    }
//                }
//            }
//            Log.d("TAGX", "onCreate: $arrayList")
//        }
//        query!!.close()
//        return arrayList
//    }



    @SuppressLint("Range")
    fun getallvideo(): ArrayList<File_DTO> {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val arrayList = ArrayList<File_DTO>()
        val strArr = arrayOf(
            "bucket_display_name",
            "_data",
            "date_modified",
            "duration",
            "_size",
            "_id",
            "_display_name",
            "date_added"
        )
        val query = context.contentResolver.query(uri, strArr, null, null, "date_added ASC")
        if (query != null) {
            while (query.moveToNext()) {
                try {
                    val string = query.getString(query.getColumnIndex(strArr[0]))
                    val string2 = query.getString(query.getColumnIndex(strArr[1]))
                    val j = query.getLong(query.getColumnIndex(strArr[2])) * 1000
                    val j2 = query.getLong(query.getColumnIndex(strArr[4]))
                    val string3 = query.getString(query.getColumnIndex(strArr[5]))
                    val valueOf =
                        java.lang.Long.valueOf(query.getLong(query.getColumnIndex(strArr[3])))
                    val string4 = query.getString(query.getColumnIndexOrThrow(strArr[6]))
                    if (File(string2).exists()) {
                        val file_DTO = File_DTO()
                        file_DTO.duration = getDuration(valueOf)
                        file_DTO.size = Ultil(context).bytesToHuman(j2)
                        file_DTO.id = string3
                        file_DTO.date = Ultil(context).getDate(j)
                        file_DTO.path = string2
                        file_DTO.abumname = string
                        file_DTO.name = string4
                        arrayList.add(file_DTO)
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("TAG", "getallvideo: "+e)
                }
            }
        }
        query!!.close()
        return arrayList
    }

    private fun getDuration(j: Long): String? {
        val round = Math.round(j.toString().toInt().toDouble()).toInt()
        val timeZone = TimeZone.getTimeZone("UTC")
        val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(Integer.valueOf(round)).toString()
    }
}
