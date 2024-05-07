package com.demo.filemanager.Activity

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.Image_Ultil
import com.demo.filemanager.Ultil.MusicUltil
import com.demo.filemanager.Ultil.VideoUtil
import com.demo.filemanager.adapter.ImageAlbumAdapter
import kotlinx.android.synthetic.main.activity_resulf.img_back
import kotlinx.android.synthetic.main.activity_resulf.img_menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//

class ResulfActivity2 : AppCompatActivity(), ImageAlbumAdapter.OnClickImageAlbum {
    override fun itemclickimg_album(i: Int) {}
    override fun menuclick_album(i: Int) {}
    private lateinit var musicUltil: MusicUltil
    private lateinit var image_ultil: Image_Ultil
    private lateinit var video_ultil: VideoUtil
    private lateinit var data_manager: Data_Manager
    private var file_dtos_image: ArrayList<File_DTO>? = null
    private var file_dtos_video: ArrayList<File_DTO>? = null
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_resulf)
        val popupMenu = PopupMenu(ContextThemeWrapper(this, R.style.AppTheme), this.img_menu)
        popupMenu.menuInflater.inflate(R.menu.menu_filestorage, popupMenu.menu)
        this.img_back.setOnClickListener(View.OnClickListener { finish() })

        Contructer()
        data()
    }


    private fun Contructer() {
//        this.musicUltil = MusicUltil(this)
        this.image_ultil = Image_Ultil(this)
//        runBlocking {
//
//
////
////            try {
//////                Log.e("TAG", "Contructer musicUltil: ${musicUltil.allsong().size}")
//                Log.e("TAG", "Contructer musicUltil: ${image_ultil.allImage().size}")
////            }catch (e:Exception){
////                Log.e("MTAG", "Contructer: ", e)
////
////            }


        lifecycleScope.launch {



            Log.e("TAG", "Contructer musicUltil: ${image_ultil.getAllShownImagesPath().size}")

        }


//        this.video_ultil = Video_Ultil(this)
//        this.data_manager = Data_Manager(this)
//        this.data_manager.setDocs()
    }

    private fun data() {
//        this.file_dtos_image = image_ultil.getallalbumImage()
//        this.file_dtos_video = video_ultil.updateAllVidepAlbum()
    }


    data class ImageData(val id: Long, val displayName: String, val contentUri: String)

    suspend fun getAllImages(context: Context): List<ImageData> = withContext(Dispatchers.IO) {

        val imageList = mutableListOf<ImageData>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val displayName = it.getString(displayNameColumn)
                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
                    .appendPath(id.toString())
                    .build()
                    .toString()

                val imageData = ImageData(id, displayName, contentUri)
                imageList.add(imageData)
            }
        }
        return@withContext imageList
    }


}