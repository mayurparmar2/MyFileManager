package com.demo.filemanager.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import java.io.File
import java.util.Locale

class MyRecyclerAdapter(private val dataManager: Data_Manager, private val context: Context) :
    RecyclerView.Adapter<MyViewHolder>() {
    private var n = 0
    private val selectionState = SparseBooleanArray()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.custom_itemfolder, viewGroup, false)
        )
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val str: String
        myViewHolder.imageView.setImageResource(dataManager.getIconId(i))
        myViewHolder.title.setText(dataManager.getName(i))
        n = dataManager.getTotalItem(i)!!
        str = if (n > 1) {
            n.toString() + " files"
        } else {
            n.toString() + " file"
        }
        try {
            myViewHolder.date.setText((dataManager.getsize(i) + "-" + dataManager.getDate_and_time(i)).toString() + "-" + str)
        } catch (unused: Exception) {
            Log.e("AAAA", "onBindViewHolder: ")
        }
        myViewHolder.itemView.setActivated(selectionState[i, false])
        BackImageLoading(i).execute(myViewHolder)
        if (selectionState[i, false]) {
            myViewHolder.img_check.setVisibility(View.VISIBLE)
        } else {
            myViewHolder.img_check.setVisibility(View.GONE)
        }
    }

    override fun getItemCount(): Int {
        return dataManager.name!!.size
    }

    fun toggleSelection(i: Int) {
        if (selectionState[i, false]) {
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

    val selectedItemCount: Int
        get() = selectionState.size()
    val selectedItemsFile: List<File>
        get() {
            val arrayList: ArrayList<File> = ArrayList()
            for (i in 0 until selectionState.size()) {
                dataManager.getFiles(selectionState.keyAt(i))?.let { arrayList.add(it) }
            }
            return arrayList
        }

    inner class BackImageLoading internal constructor(private val i: Int) :
        AsyncTask<MyViewHolder?, MyViewHolder?, Any?>() {
        var bitmap: Bitmap? = null
        override fun doInBackground(vararg myViewHolderArr: MyViewHolder?): Any? {
//        public override fun doInBackground(vararg myViewHolderArr: MyViewHolder): Any? {
            if (dataManager.getFiles(i)!!.path.lowercase(Locale.getDefault())
                    .contains(".jpg") || dataManager.getFiles(
                    i
                )!!
                    .path.lowercase(Locale.getDefault()).endsWith(".png") || dataManager.getFiles(
                    i
                )!!
                    .path.lowercase(Locale.getDefault()).contains(".jpeg") || dataManager.getFiles(
                    i
                )!!
                    .path.endsWith(".JPEG")
            ) {
                if (dataManager.getFiles(i)!!.length() > 120000) {
                    var j: Long = 0
                    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    val query = context.contentResolver.query(
                        uri,
                        arrayOf("_data", "_id"),
                        null,
                        null,
                        null
                    )
                    query!!.moveToFirst()
                    while (true) {
                        if (query.getString(query.getColumnIndexOrThrow("_data")) == dataManager.getFiles(
                                i
                            )!!
                                .path
                        ) {
                            j = query.getLong(query.getColumnIndexOrThrow("_id"))
                            break
                        } else if (!query.moveToNext()) {
                            break
                        }
                    }
                    query.close()
                    bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                        context.contentResolver,
                        j,
                        1,
                        null
                    )
                }
                if (bitmap == null) {
                    bitmap = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(
                            dataManager.getFiles(i)!!.path
                        ), 192, 192
                    )
                }
                publishProgress(*myViewHolderArr)
            }
            return null
        }

        public override fun onProgressUpdate(vararg values: MyViewHolder?) {
            values[0]?.imageView?.setImageBitmap(bitmap)
        }

    }

    companion object {
        @JvmStatic
        fun getSelectedItemCount(myRecyclerAdapter: MyRecyclerAdapter): Int {
            return myRecyclerAdapter.selectionState.size()
        }
    }
}
