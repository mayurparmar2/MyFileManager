package com.demo.filemanager.adapter.GirlAdapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GridAdapter(private val dataManager: Data_Manager, private val context: Context) : RecyclerView.Adapter<GridViewHolder>() {
    private val selectionState = SparseBooleanArray()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GridViewHolder {
        return GridViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.card_girl, viewGroup, false))
    }

    override fun onBindViewHolder(gridViewHolder: GridViewHolder, i: Int) {
        gridViewHolder.textView.text = if (dataManager.getName(i)!!.length > 12) dataManager!!.getName(i)!!.substring(0, 11) else dataManager.getName(i)
        gridViewHolder.imageView.setImageResource(dataManager.getIconId(i))
        gridViewHolder.itemView.isActivated = selectionState.get(i, false)
        BackImageLoading(i, gridViewHolder).load()
        gridViewHolder.img_check.visibility = if (selectionState.get(i, false)) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return dataManager.name!!.size
    }

    fun toggleSelection(i: Int) {
        if (selectionState.get(i, false)) {
            selectionState.delete(i)
        } else {
            selectionState.put(i, true)
        }
        notifyItemChanged(i)
    }

    fun clearSelection() {
        selectionState.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItemCount(): Int {
        return selectionState.size()
    }

    fun getSelectedItemsFile(): List<Unit> {
        return List(selectionState.size()) { i ->
            dataManager.getFiles(selectionState.keyAt(i))
        }
    }

    inner class BackImageLoading(private val i: Int, private val gridViewHolder: GridViewHolder) {
        private var bitmap: Bitmap? = null

        fun load() {
            GlobalScope.launch(Dispatchers.IO) {
                val lowerCase = dataManager.getFiles(i)!!.path.lowercase()
                if (listOf(".jpg", ".png", ".jpeg", ".gif", ".webp").any { lowerCase.contains(it) }) {
                    if (dataManager.getFiles(i)!!.length() > 120000) {
                        var id: Long = 0
                        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        val cursor = context.contentResolver.query(uri, arrayOf("_data", "_id"), null, null, null)
                        cursor?.moveToFirst()
                        while (cursor != null && !cursor.isAfterLast) {
                            if (cursor.getString(cursor.getColumnIndexOrThrow("_data")) == dataManager.getFiles(i)?.path) {
                                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
                                break
                            } else {
                                cursor.moveToNext()
                            }
                        }
                        cursor?.close()
                        bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
                    }
                    if (bitmap == null) {
                        bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(
                            dataManager.getFiles(i)!!.path), 192, 192)
                    }
                    withContext(Dispatchers.Main) {
                        gridViewHolder.imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}


