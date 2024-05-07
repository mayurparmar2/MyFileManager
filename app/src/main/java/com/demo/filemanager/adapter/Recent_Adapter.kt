package com.demo.filemanager.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.FileProvider

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.demo.filemanager.Activity.Edit_MediaActivity
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.UpdateSearch
import com.demo.filemanager.LoadIimage.LoadImage_piscaso_glide
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Ultil
import com.demo.filemanager.Ultil.VideoUtil
import com.demo.filemanager.database.FavoritSongs
import java.io.File
import java.util.Locale

public class Recent_Adapter(override var context: Context, private val onClickItem: OnClickItem) :
    BaseQuickAdapter<File_DTO?, BaseViewHolder>(
        R.layout.custom_ittem_recent
    ) {
    private var updateSearch: UpdateSearch? = null

    interface OnClickItem {
        fun itemclickFr(i: Int)
        fun menuclickFr(i: Int)
    }

    fun getUpdateSearch(): UpdateSearch? {
        return updateSearch
    }

    fun setUpdateSearch(updateSearch: UpdateSearch?) {
        this.updateSearch = updateSearch
    }
    override fun convert(baseViewHolder: BaseViewHolder, file_DTO: File_DTO?) {
//    override fun convert(baseViewHolder: BaseViewHolder, file_DTO: File_DTO) {
        val imageView = baseViewHolder.itemView.findViewById<View>(R.id.image) as ImageView
        val relativeLayout =
            baseViewHolder.itemView.findViewById<View>(R.id.rela_songitem) as RelativeLayout
        val imageView2 = baseViewHolder.itemView.findViewById<View>(R.id.menu_button) as ImageView
        val textView = baseViewHolder.itemView.findViewById<View>(R.id.title) as TextView
        val textView2 = baseViewHolder.itemView.findViewById<View>(R.id.band) as TextView
        val textView3 = baseViewHolder.itemView.findViewById<View>(R.id.txt_durian) as TextView
        if (file_DTO != null) {
            textView2.setText(file_DTO.size + "-" + Ultil(context).getDate(file_DTO.lastmodified!!.toLong()))
            textView.setText(file_DTO.name)
            textView3.visibility = View.GONE
            val lowerCase: String = file_DTO.path!!.lowercase(Locale.getDefault())
            if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(
                    ".tiff"
                ) || lowerCase.contains(".jpeg") || lowerCase.contains("webp")
            ) {
                LoadImage_piscaso_glide(context).LoadImageAlbum(file_DTO.path, imageView)
            } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(
                    ".mkv"
                ) || lowerCase.contains(".vob") || lowerCase.contains(".mov")
            ) {
                try {
                    imageView.setImageBitmap(VideoUtil(context).getBitmap(file_DTO.path))
                } catch (unused: Exception) {
                    imageView.setImageResource(R.drawable.button_video)
                }
            } else if (lowerCase.contains(".txt")) {
                imageView.setImageResource(R.drawable.icon_txt)
            } else if (lowerCase.lowercase(Locale.getDefault()).contains(".pdf")) {
                imageView.setImageResource(R.drawable.icon_pdf)
            } else if (lowerCase.lowercase(Locale.getDefault()).contains(".ppt")) {
                imageView.setImageResource(R.drawable.icon_ppt)
            } else if (lowerCase.lowercase(Locale.getDefault())
                    .contains(".csv") || lowerCase.contains(".csvs")
            ) {
                imageView.setImageResource(R.drawable.icon_xls)
            } else if (lowerCase.lowercase(Locale.getDefault())
                    .contains(".docx") || lowerCase.contains(".doc")
            ) {
                imageView.setImageResource(R.drawable.icon_doc)
            } else if (lowerCase.lowercase(Locale.getDefault())
                    .contains(".apk") || lowerCase.contains(".apks")
            ) {
                imageView.setImageResource(R.drawable.icon_apk)
            } else if (lowerCase.lowercase(Locale.getDefault())
                    .contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(
                    ".wma"
                ) || lowerCase.contains(".m4a")
            ) {
                if (file_DTO.artistid == null || file_DTO.artistid == "null") {
                    imageView.setImageResource(R.drawable.button_music)
                } else {
                    LoadImage_piscaso_glide(context).setAlbumArt(file_DTO.artistid!!, imageView)
                }
            } else {
                imageView.setImageResource(R.drawable.unknowfile)
            }
        }
        relativeLayout.setOnClickListener {
            checkintent(
                file_DTO!!,
                baseViewHolder.getLayoutPosition()
            )
        }
        imageView2.setOnClickListener {
            onClickItem.menuclickFr(baseViewHolder.getLayoutPosition())
            val popupMenu = PopupMenu(ContextThemeWrapper(context, R.style.AppTheme), imageView2)
            popupMenu.menuInflater.inflate(R.menu.popupmenufav, popupMenu.menu)
            popupMenu.menu.findItem(R.id.remove).setVisible(false)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                val itemId = menuItem.itemId
                if (itemId == R.id.info) {
                    Log.d("TAG!", "onMenuItemClick: " + file_DTO!!.size)
                    Ultil(context).showdiloginfo(file_DTO, "def")
                    true
                } else if (itemId == R.id.remove) {
                    if (file_DTO != null) {
                        delete(file_DTO)
                    }
                    true
                } else if (itemId != R.id.share) {
                    true
                } else {
                    if (file_DTO != null) {
                        sharefile(file_DTO)
                    }
                    true
                }
            }
            popupMenu.show()
        }
    }

    fun checkintent(file_DTO: File_DTO, i: Int) {
        val path: String = file_DTO.path.toString()
        val lowerCase: String = file_DTO.path!!.lowercase(Locale.getDefault())
        if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(
                ".tiff"
            ) || lowerCase.contains(".webp") || lowerCase.contains(".jpeg")
        ) {
            val intent = Intent(context, Edit_MediaActivity::class.java)
            intent.putExtra("path", path)
            intent.putExtra("file_dto", file_DTO)
            intent.putExtra("hidefile", "recent")
            intent.putExtra("pos", i)
            context.startActivity(intent)
        } else if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(
                ".wma"
            ) || lowerCase.contains(".m4a")
        ) {
            val withAppendedPath =
                Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, file_DTO.id)
            val intent2 = Intent("android.intent.action.VIEW")
            intent2.setData(withAppendedPath)
            context.startActivity(intent2)
        } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(
                ".vob"
            ) || lowerCase.contains(".mov")
        ) {
            val withAppendedPath2 =
                Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, file_DTO.id)
            val intent3 = Intent("android.intent.action.VIEW")
            intent3.setData(withAppendedPath2)
            context.startActivity(intent3)
        } else {
            Ultil(context).openFile(File(file_DTO.path))
        }
    }

    fun sharefile(file_DTO: File_DTO) {
        val path: String = file_DTO.path.toString()
        val uriForFile = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            File(file_DTO.path)
        )
        val intent = Intent()
        intent.setAction("android.intent.action.SEND")
        intent.putExtra("android.intent.extra.STREAM", uriForFile)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (path.lowercase(Locale.getDefault())
                .contains(".jpg") || path.lowercase(Locale.getDefault())
                .contains(".png") || path.lowercase(
                Locale.getDefault()
            ).contains(".gif") || path.lowercase(Locale.getDefault()).contains(".tiff")
        ) {
            intent.setDataAndType(uriForFile, "image/*")
        } else if (path.lowercase(Locale.getDefault())
                .contains(".mp3") || path.contains(".wav") || path.contains(".flac") || path.contains(
                ".wma"
            ) || path.contains(".mp4a")
        ) {
            intent.setDataAndType(uriForFile, "audio/*")
        } else if (path.lowercase(Locale.getDefault())
                .contains(".mp4") || path.contains(".avi") || path.contains(".mkv") || path.contains(
                ".vob"
            ) || path.contains(".mov")
        ) {
            intent.setDataAndType(uriForFile, "video/*")
        } else {
            intent.setDataAndType(uriForFile, "document/*")
        }
        context.startActivity(Intent.createChooser(intent, "Remi_FileManger"))
    }

    fun delete(file_DTO: File_DTO) {
        val favoritSongs = FavoritSongs(context)
        favoritSongs.open()
        file_DTO.path?.let { favoritSongs.deleteRowByPath(it) }
        setList(ArrayList<File_DTO>())
        setList(favoritSongs.allRowsList)
        notifyDataSetChanged()
        favoritSongs.close()
    }

    fun search(str: String) {
        SearchBackground(str).execute(*arrayOfNulls<String>(0))
    }

    inner class SearchBackground(private val key: String) :
        AsyncTask<String?, Int?, ArrayList<File_DTO>?>() {
        private var listResulft: ArrayList<File_DTO>? = null
        private var savelist: ArrayList<File_DTO>? = null


        override fun onPreExecute() {
            super.onPreExecute()
            listResulft = ArrayList<File_DTO>()
            savelist = Ultil(context).getallfileRecent()
        }
        override fun doInBackground(vararg params: String?): ArrayList<File_DTO>? {
//        override fun doInBackground(vararg strArr: String): ArrayList<File_DTO>? {
            val it: Iterator<File_DTO> = savelist!!.iterator()
            while (it.hasNext()) {
                val next: File_DTO = it.next()
                if (next.name!!.lowercase(Locale.getDefault())
                        .contains(key.lowercase(Locale.getDefault()))
                ) {
                    listResulft!!.add(next)
                }
            }
            return listResulft
        }

        public override fun onPostExecute(arrayList: ArrayList<File_DTO>?) {
            super.onPostExecute(arrayList)
            if (key.length == 0) {
                this@Recent_Adapter.setList(savelist)
            } else {
                this@Recent_Adapter.setList(listResulft)
            }
            this@Recent_Adapter.notifyDataSetChanged()
            updateSearch?.search()
        }
    }
}
