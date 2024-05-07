package com.demo.filemanager.Ultil

import android.annotation.SuppressLint
import android.app.Activity
import android.provider.MediaStore
import com.demo.filemanager.DTO.File_DTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Locale
import java.util.TimeZone


class MusicUltil(private val context: Activity) {
    companion object{
        var totolsize: Long = 0

        fun getFormattedDateFromTimestamp(j: Long): String? {
            return SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(java.lang.Long.valueOf(j))
        }
    }
    protected var file_dtos = java.util.ArrayList<File_DTO>()
    fun allsong(): ArrayList<File_DTO> {
        if (this.file_dtos.size == 0) {

            GlobalScope.launch(Dispatchers.IO){
                getdatafromDevice()
                withContext(Dispatchers.Main){
                    Collections.sort<File_DTO>(file_dtos, object : Comparator<File_DTO?> {
                        override fun compare(file_DTO: File_DTO?, file_DTO2: File_DTO?): Int {
//            fun compare(file_DTO: File_DTO, file_DTO2: File_DTO): Int {
                            return file_DTO!!.name!!.lowercase(Locale.getDefault()).compareTo(
                                file_DTO2!!.name!!.lowercase(
                                    Locale.getDefault()
                                )
                            )
                        }

                    })
                }
            }
        }

        return this.file_dtos
    }

    @SuppressLint("Range")
    suspend fun getdatafromDevice(): ArrayList<File_DTO> = withContext(Dispatchers.IO) {
        var i: Int
        file_dtos = ArrayList()

        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                "_size",
                "_id",
                "date_modified",
                "date_added",
                "duration",
                "album",
                "_display_name",
                "_data",
                "title",
                "artist",
                "album",
                "album_id"
            ),
            "is_music != 0",
            null,
            null
        )

        if (query != null) {
            if (query.moveToFirst()) {
                do {
                    i = try {
                        Math.round(query.getString(query.getColumnIndex("duration")).toInt().toDouble()).toInt()
                    } catch (unused: Exception) {
                        1000
                    }

                    if (query.getString(query.getColumnIndex("album")) != "WhatsApp Audio") {
                        val string = query.getString(query.getColumnIndex("_display_name"))
                        val string2 = query.getString(query.getColumnIndex("_data"))
                        val replaceAll =
                            query.getString(query.getColumnIndex("title")).replace("_", " ")
                                .trim { it <= ' ' }
                                .replace(" +".toRegex(), " ")
                        query.getString(query.getColumnIndex("artist"))
                        query.getString(query.getColumnIndex("album"))
                        val string3 = query.getString(query.getColumnIndex("_id"))
                        val string4 = query.getString(query.getColumnIndex("album_id"))
                        val string5 = query.getString(query.getColumnIndex("_size"))
                        val string6 = query.getString(query.getColumnIndex("artist"))
                        val string7 = query.getString(query.getColumnIndex("date_added"))

                        val timeZone = TimeZone.getTimeZone("UTC")
                        val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
                        simpleDateFormat.timeZone = timeZone
                        val valueOf = simpleDateFormat.format(Integer.valueOf(i)).toString()
                        query.getLong(query.getColumnIndex("date_modified"))

                        val file_DTO = File_DTO().apply {
                            name = string
                            title = replaceAll
                            path = string2
                            duration = valueOf
                            date = MusicUltil.getFormattedDateFromTimestamp(string7.toLong() * 1000)
                            id = string3
                            artistname = string6
                            abumid = string4
                            size = Ultil(context).bytesToHuman(string5.toLong())
                        }

                        MusicUltil.totolsize += query.getLong(query.getColumnIndex("_size"))
                        file_dtos.add(file_DTO)
                    }
                } while (query.moveToNext())
                query.close()
            } else {
                query.close()
            }
        }

        file_dtos
    }

//    fun getdatafromDevice(): java.util.ArrayList<File_DTO> {
//        var i: Int
//        file_dtos = java.util.ArrayList()
//        val query = context.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            arrayOf(
//                "_size",
//                "_id",
//                "date_modified",
//                "date_added",
//                "duration",
//                "album",
//                "_display_name",
//                "_data",
//                "title",
//                "artist",
//                "album",
//                "album_id"
//            ),
//            "is_music != 0",
//            null,
//            null
//        )
//        if (query != null) {
//            if (query.moveToFirst()) {
//                do {
//                    i = try {
//                        Math.round(
//                            query.getString(query.getColumnIndex("duration")).toInt()
//                                .toDouble()
//                        ).toInt()
//                    } catch (unused: Exception) {
//                        1000
//                    }
//                    if (query.getString(query.getColumnIndex("album")) != "WhatsApp Audio") {
//                        val string = query.getString(query.getColumnIndex("_display_name"))
//                        val string2 = query.getString(query.getColumnIndex("_data"))
//                        val replaceAll =
//                            query.getString(query.getColumnIndex("title")).replace("_", " ")
//                                .trim { it <= ' ' }
//                                .replace(" +".toRegex(), " ")
//                        query.getString(query.getColumnIndex("artist"))
//                        query.getString(query.getColumnIndex("album"))
//                        val string3 = query.getString(query.getColumnIndex("_id"))
//                        val string4 = query.getString(query.getColumnIndex("album_id"))
//                        val string5 = query.getString(query.getColumnIndex("_size"))
//                        val string6 = query.getString(query.getColumnIndex("artist"))
//                        val string7 = query.getString(query.getColumnIndex("date_added"))
//                        val timeZone = TimeZone.getTimeZone("UTC")
//                        val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
//                        simpleDateFormat.timeZone = timeZone
//                        val valueOf = simpleDateFormat.format(Integer.valueOf(i)).toString()
//                        query.getLong(query.getColumnIndex("date_modified"))
//                        val file_DTO = File_DTO()
//                        file_DTO.name = string
//                        file_DTO.title = replaceAll
//                        file_DTO.path = string2
//                        file_DTO.duration = valueOf
//                        file_DTO.date =
//                            MusicUltil.getFormattedDateFromTimestamp(string7.toLong() * 1000)
//                        file_DTO.id = string3
//                        file_DTO.artistname = string6
//                        file_DTO.abumid = string4
//                        file_DTO.size = Ultil(context).bytesToHuman(string5.toLong())
//                        MusicUltil.totolsize += query.getLong(query.getColumnIndex("_size"))
//                        file_dtos.add(file_DTO)
//                    }
//                } while (query.moveToNext())
//                query.close()
//            } else {
//                query.close()
//            }
//        }
//        return file_dtos
//    }
}