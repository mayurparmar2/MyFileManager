package com.demo.filemanager.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Environment
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.demo.filemanager.Activity.Edit_MediaActivity
import com.demo.filemanager.Activity.customview.CustomDeleteDialog
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.CallbackUpdateMusic
import com.demo.filemanager.Iinterface.DeleteCallback
import com.demo.filemanager.Iinterface.UpdateSearch
import com.demo.filemanager.LoadIimage.LoadImage_piscaso_glide
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.FileOperations
import com.demo.filemanager.Ultil.Ultil
import com.demo.filemanager.Ultil.VideoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale


class DowloadApdapter(val mContext: Context,
                      val activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
                      val onLongclick:OnLongclick
) : BaseQuickAdapter<File_DTO?, BaseViewHolder>((R.layout.custom_ittemsong)) {
    interface OnLongclick {
        fun onclick_item(i: Int)
        fun onlongClickRecent()
    }

    private var check: String? = null
    private var ischeck = false
    var pos = 0
    var posadd: java.util.ArrayList<String>? = null
    private val type: String? = null
    private var updateSearch: UpdateSearch? = null
    init {
        this.check = ""
        this.pos = -1
        this.posadd = ArrayList<String>()
    }
    override fun convert(baseViewHolder: BaseViewHolder, file_DTO: File_DTO?) {
        val image = baseViewHolder.itemView.findViewById<View>(R.id.image) as ImageView
        val rela_songitem = baseViewHolder.itemView.findViewById<View>(R.id.rela_songitem) as RelativeLayout
        val menu_button = baseViewHolder.itemView.findViewById<View>(R.id.menu_button) as ImageView
        val title = baseViewHolder.itemView.findViewById<View>(R.id.title) as TextView
        val band = baseViewHolder.itemView.findViewById<View>(R.id.band) as TextView
        val txt_durian = baseViewHolder.itemView.findViewById<View>(R.id.txt_durian) as TextView
        val img_check = baseViewHolder.itemView.findViewById<View>(R.id.img_check) as ImageView
        val popupMenu = PopupMenu(
            ContextThemeWrapper(
                context,
                R.style.AppTheme
            ), menu_button
        )
        popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
        if (file_DTO != null) {
            val lowerCase: String = file_DTO.path.lowercase(Locale.getDefault())
            if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(
                    ".wma"
                ) || lowerCase.contains(".mp4a")
            ) {
                image.setImageResource(R.drawable.button_music)
            } else if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(
                    ".gif"
                ) || lowerCase.contains(".tiff") || lowerCase.contains(".jpeg") || lowerCase.contains(
                    "webp"
                )
            ) {
                LoadImage_piscaso_glide(context).LoadImageAlbum(file_DTO.path, image)
            } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(
                    ".mkv"
                ) || lowerCase.contains(".vob") || lowerCase.contains(".mov")
            ) {
                image.setImageBitmap(VideoUtil(context).getBitmap(file_DTO.path))
            } else if (lowerCase.contains(".txt")) {
                image.setImageResource(R.drawable.icon_txt)
            } else if (lowerCase.contains(".doc") || lowerCase.contains(".docx")) {
                image.setImageResource(R.drawable.icon_doc)
            } else if (lowerCase.contains(".ppt") || lowerCase.contains(".pptx")) {
                image.setImageResource(R.drawable.icon_ppt)
            } else if (lowerCase.contains(".pdf")) {
                image.setImageResource(R.drawable.icon_pdf)
            } else if (lowerCase.contains(".xls") || lowerCase.contains(".xlsx") || lowerCase.contains(
                    ".csv"
                ) || lowerCase.contains(".csvx")
            ) {
                image.setImageResource(R.drawable.icon_xls)
            } else if (lowerCase.contains(".zip") || lowerCase.contains(".rar")) {
                image.setImageResource(R.drawable.icon_zip)
            } else if (lowerCase.contains(".apk") || lowerCase.contains(".apks")) {
                image.setImageResource(R.drawable.icon_apk)
            } else {
                image.setImageResource(R.drawable.unknowfile)
            }
            band.setText(file_DTO.size + ":" + file_DTO.date)
            title.setText(file_DTO.name)
            txt_durian.setText(file_DTO.duration)
            val deleteCallback: DeleteCallback = object : DeleteCallback {
                override fun update() {
                    Ultil(context).copyFile(file_DTO.path)
                    try {
                        FileOperations.delete(File(file_DTO.path))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    setList(
                        Data_Manager(context).getallfilewithpath(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS
                            )
                        )
                    )
                }
            }
            menu_button.setOnClickListener {
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.addfv -> {
                            Log.e("TAG", "onMenuItemClick: " + file_DTO.id + file_DTO.path)
                            Ultil(context).addFav(file_DTO)
                            true
                        }

                        R.id.delete -> {
                            val customDeleteDialog = CustomDeleteDialog(context, deleteCallback)
                            customDeleteDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(0))
                            customDeleteDialog.getWindow()?.getAttributes()?.windowAnimations = R.style.PauseDialogAnimation
                            customDeleteDialog.show()
                            true
                        }

                        R.id.info -> {
                            Ultil(context).showdiloginfo(file_DTO, "")
                            true
                        }

                        R.id.rename -> {
                            Ultil(context).dialogRename(file_DTO, "def")
                            Toast.makeText(
                                context,
                                "rename",
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }

                        R.id.share -> {
                            Ultil(context).sharefile(file_DTO)
                            true
                        }

                        else -> true
                    }
                }
                popupMenu.show()
            }
            if (posadd!!.size == 0) {
                img_check.setImageResource(R.drawable.esclip)
            }
            for (i in posadd!!.indices) {
                val arrayList = posadd
                if (arrayList!!.contains("" + baseViewHolder.getLayoutPosition())) {
                    img_check.setImageResource(R.drawable.blackcheck)
                } else {
                    img_check.setImageResource(R.drawable.esclip)
                }
            }
            img_check.setOnClickListener {
                pos = baseViewHolder.getAdapterPosition()
                if (posadd!!.size == 0) {
                    val arrayList2 = posadd
                    arrayList2!!.add("" + pos)
                } else {
                    val arrayList3 = posadd
                    if (arrayList3!!.contains("" + pos)) {
                        val arrayList4 = posadd
                        arrayList4!!.remove("" + pos)
                    } else {
                        val arrayList5 = posadd
                        arrayList5!!.add("" + pos)
                    }
                }
                notifyDataSetChanged()
                onLongclick.onclick_item(baseViewHolder.getLayoutPosition())
            }
            rela_songitem.setOnClickListener(View.OnClickListener {
                val file: File = File(file_DTO.path)
                if (file_DTO.path.lowercase(Locale.getDefault())
                        .contains(".jpg") || file_DTO.path.lowercase(
                        Locale.getDefault()
                    ).contains(".png") || file_DTO.path.lowercase(Locale.getDefault())
                        .contains(".gif") || file_DTO.path.lowercase(
                        Locale.getDefault()
                    ).contains(".tiff") || file_DTO.path.lowercase(Locale.getDefault())
                        .contains("webp")
                ) {
                    val intent = Intent(
                        context,
                        Edit_MediaActivity::class.java
                    )
                    intent.putExtra("path", file)
                    intent.putExtra("file_dto", file_DTO)
                    intent.putExtra("hidefile", "download")
                    intent.putExtra("pos", pos)
                    context.startActivity(intent)
                    return@OnClickListener
                }
                Ultil(context).openFile(file)
            })
            CallbackUpdateMusic.instance?.setStateListen(object : CallbackUpdateMusic.OncustomStateListen {
                override fun statechange() {
                    setList(
                        Data_Manager(context).getallfilewithpath(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS
                            )
                        )
                    )
                }
            })
            rela_songitem.setOnLongClickListener {
                onLongclick.onlongClickRecent()
                false
            }
            if (isIscheck()) {
                img_check.visibility = View.VISIBLE
            } else {
                img_check.visibility = View.GONE
            }
        }

    }
    fun isIscheck(): Boolean {
        return ischeck
    }
     suspend fun search(key: String?) {
        key?.let {
            SearchBackground(mContext,key,this@DowloadApdapter).executeAsync()
        }
    }


    internal class SearchBackground(private val context: Context, private val key: String,val adapter:DowloadApdapter) {
        private var listResult: ArrayList<File_DTO> = ArrayList()
        private var saveList: ArrayList<File_DTO> = ArrayList()

        suspend fun executeAsync(): ArrayList<File_DTO> = coroutineScope {
            listResult = ArrayList()
            async(Dispatchers.IO) {
                saveList = Data_Manager(context).filesDowload()
                for (file in saveList) {
                    if (file.name?.toLowerCase()?.contains(key.toLowerCase()) == true) {
                        listResult.add(file)
                    }
                }
                withContext(Dispatchers.Main) {
                    if (key.isEmpty()) {
                        showToast("List empty")
                    } else {
                        showToast("Key is not empty")
                    }
                    if (key.length == 0) {
                        adapter.setList(saveList)
                    } else {
                        adapter.setList(listResult)
                    }
                    adapter.notifyDataSetChanged()
//                    onupdate.updateUI(listResult)
                }
            }
            listResult
        }
        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setIscheck(z: Boolean) {
        ischeck = z
    }
    fun clearListchose() {
        posadd!!.clear()
        notifyDataSetChanged()
    }


}

