package com.demo.filemanager.LoadIimage

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.R
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File

class LoadImage_piscaso_glide(private val context: Context) {
    fun LoadImageAlbumwithBitmap(bitmap: Bitmap?, imageView: ImageView?) {}
    fun setAlbumArt(str: String, imageView: ImageView?) {
        try {
            Picasso.get().load(getSongUri(java.lang.Long.valueOf(str.toLong()))).placeholder(
                context.getDrawable(
                    R.drawable.button_music
                )!!
            ).centerCrop().resize(300, 300).onlyScaleDown().into(imageView)
        } catch (unused: Exception) {
        }
    }

    private fun getSongUri(l: Long): Uri {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), l)
    }

    fun setAlbumArt(arrayList: ArrayList<File_DTO>, imageView: ImageView) {
        if (arrayList.size > 0) {
            val arrayList2: ArrayList<*> = ArrayList<Any?>()
            for (i in arrayList.indices) {
                arrayList2.add(arrayList[i].abumid as Nothing)
                if (i == 20) {
                    break
                }
            }
            setAlbumArt(arrayList2, imageView, 0, arrayList2.size - 1)
            return
        }
        imageView.setImageDrawable(context.getDrawable(R.drawable.button_music))
    }

    fun setAlbumArt(list: List<*>, imageView: ImageView?, i: Int, i2: Int) {
        try {
            if (i < i2) {
                Picasso.get().load(getSongUri(java.lang.Long.valueOf(list[i].toString().toLong())))
                    .placeholder(
                        context.getDrawable(R.drawable.button_music)!!
                    ).centerCrop().resize(300, 300).onlyScaleDown()
                    .into(imageView, object : Callback {
                        override fun onSuccess() {}
                        override fun onError(exc: Exception) {
                            this@LoadImage_piscaso_glide.setAlbumArt(list, imageView, i + 1, i2)
                        }
                    })
            } else if (i != i2) {
            } else {
                Picasso.get().load(getSongUri(java.lang.Long.valueOf(list[i].toString().toLong())))
                    .placeholder(
                        context.getDrawable(R.drawable.button_music)!!
                    ).into(imageView)
            }
        } catch (unused: Exception) {
        }
    }

    fun LoadImageAlbum(str: String?, imageView: ImageView?) {
        try {
            Picasso.get().load(Uri.fromFile(File(str)))
                .placeholder(context.getDrawable(R.drawable.imageicon)!!).centerCrop()
                .resize(300, 300).onlyScaleDown().into(imageView)
        } catch (unused: Exception) {
        }
    }

    fun LoadZoomView(str: String?, photoView: PhotoView?) {
        try {
            Picasso.get().load(Uri.fromFile(File(str)))
                .placeholder(context.getDrawable(R.drawable.button_music)!!).centerCrop()
                .resize(1000, 1000).onlyScaleDown().into(photoView)
        } catch (unused: Exception) {
        }
    }
}
