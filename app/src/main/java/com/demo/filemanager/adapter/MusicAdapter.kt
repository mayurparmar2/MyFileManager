package com.demo.filemanager.adapter

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.CallbackUpdateMusic
import com.demo.filemanager.Iinterface.UpdateSearch
import com.demo.filemanager.LoadIimage.LoadImage_piscaso_glide
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.MusicUltil
import com.demo.filemanager.Ultil.Ultil
import java.io.File
import java.util.Locale
//
//class MusicAdapter(
//    protected var onClick: OnClick,
//    private val context: Context,
//    private val ckeckicon: String,
//    activityResultLauncher: ActivityResultLauncher<IntentSenderRequest?>
//) : BaseQuickAdapter<File_DTO?, BaseViewHolder?>(R.layout.custom_ittemsong) {
//    var isCheck = false
//    private var drawables: ArrayList<Drawable>
//    private val launcher: ActivityResultLauncher<IntentSenderRequest>
//    var pos: Int
//    var posadd: ArrayList<String>
//    var updateApp: UpdateApp? = null
//    private var updateSearch: UpdateSearch? = null
//
//    interface OnClick {
//        fun itemclick(i: Int)
//        fun menuclick(i: Int)
//        fun onlongclick()
//    }
//
//    fun getUpdateApp(): UpdateApp? {
//        return updateApp
//    }
//
//    fun setUpdateApp(updateApp: UpdateApp?) {
//        this.updateApp = updateApp
//    }
//
//    fun getUpdateSearch(): UpdateSearch? {
//        return updateSearch
//    }
//
//    fun setUpdateSearch(updateSearch: UpdateSearch?) {
//        this.updateSearch = updateSearch
//    }
//
//    fun getDrawables(): ArrayList<Drawable> {
//        return drawables
//    }
//
//    fun setDrawables(arrayList: ArrayList<Drawable>) {
//        drawables = arrayList
//    }
//
//    init {
//        pos = -1
//        posadd = ArrayList()
//        launcher = activityResultLauncher
//        drawables = Data_Manager(context).drawables()
//    }
//
//    override fun convert(baseViewHolder: BaseViewHolder, file_DTO: File_DTO) {
//        val imageView = baseViewHolder.itemView.findViewById<View>(R.id.image) as ImageView
//        val relativeLayout: RelativeLayout =
//            baseViewHolder.itemView.findViewById<View>(R.id.rela_songitem) as RelativeLayout
//        val imageView2 = baseViewHolder.itemView.findViewById<View>(R.id.menu_button) as ImageView
//        val textView: TextView = baseViewHolder.itemView.findViewById<View>(R.id.title) as TextView
//        val textView2: TextView = baseViewHolder.itemView.findViewById<View>(R.id.band) as TextView
//        val imageView3 = R.id.img_check.findView<View>() as ImageView
//        val textView3: TextView =
//            baseViewHolder.itemView.findViewById<View>(R.id.txt_durian) as TextView
//        val popupMenu =
//            PopupMenu(ContextThemeWrapper(context, R.style.AppTheme as Int), imageView2, 5)
//        if (file_DTO != null) {
//            if (ckeckicon == "music") {
//                LoadImage_piscaso_glide(context).setAlbumArt(file_DTO.abumid, imageView)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "txt") {
//                imageView.setImageResource(R.drawable.icon_txt)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "docx") {
//                imageView.setImageResource(R.drawable.icon_doc)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "xls") {
//                imageView.setImageResource(R.drawable.icon_xls)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "ppt") {
//                imageView.setImageResource(R.drawable.icon_ppt)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "pdf") {
//                imageView.setImageResource(R.drawable.icon_pdf)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "app") {
//                imageView.setImageDrawable(drawables[baseViewHolder.getLayoutPosition()])
//                popupMenu.menuInflater.inflate(R.menu.popmenuapp, popupMenu.menu)
//            } else if (ckeckicon == "dowload") {
//                imageView.setColorFilter(Color.parseColor("#09F169"))
//                imageView.setImageResource(R.drawable.icon_dowload)
//                popupMenu.menuInflater.inflate(R.menu.popmenuapp, popupMenu.menu)
//            } else if (ckeckicon == "apk") {
//                imageView.setImageResource(R.drawable.icon_apk)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else if (ckeckicon == "zip") {
//                imageView.setImageResource(R.drawable.icon_zip)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            } else {
//                imageView.setImageResource(R.drawable.unknowfile)
//                popupMenu.menuInflater.inflate(R.menu.popmenumusic, popupMenu.menu)
//            }
//            textView2.setText(file_DTO.size + "-" + file_DTO.date)
//            textView.setText(file_DTO.name)
//            textView3.setText(file_DTO.duration)
//        }
//        imageView2.setOnClickListener {
//            onClick.menuclick(baseViewHolder.getLayoutPosition())
//            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
//                when (menuItem.itemId) {
//                    R.id.addfv -> {
//                        Ultil(context).addFav(file_DTO)
//                        true
//                    }
//
//                    R.id.appinfo -> {
//                        Ultil(context).showdiloginfo(file_DTO, "app")
//                        true
//                    }
//
//                    R.id.delete -> {
//                        Ultil(context).dialogdeletewihtpath(file_DTO)
//                        true
//                    }
//
//                    R.id.info -> {
//                        Ultil(context).showdiloginfo(file_DTO, "")
//                        true
//                    }
//
//                    R.id.open -> {
//                        try {
//                            context.startActivity(
//                                context.packageManager.getLaunchIntentForPackage(
//                                    file_DTO.packagename
//                                )
//                            )
//                            return@OnMenuItemClickListener true
//                        } catch (e: Exception) {
//                            Log.d("TAG!", "onMenuItemClick: " + e.message)
//                            return@OnMenuItemClickListener true
//                        }
//                        Ultil(context).dialogRename(file_DTO, "")
//                        true
//                    }
//
//                    R.id.rename -> {
//                        Ultil(context).dialogRename(file_DTO, "")
//                        true
//                    }
//
//                    R.id.share -> {
//                        Ultil(context).sharefile(file_DTO)
//                        true
//                    }
//
//                    R.id.stop -> {
//                        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
//                        intent.setData(Uri.parse("package:" + file_DTO.packagename))
//                        context.startActivity(intent)
//                        true
//                    }
//
//                    R.id.uninstall -> {
//                        updateApp.update(file_DTO.packagename)
//                        true
//                    }
//
//                    else -> true
//                }
//            })
//            popupMenu.show()
//        }
//        relativeLayout.setOnClickListener(View.OnClickListener {
//            onClick.itemclick(baseViewHolder.getLayoutPosition())
//            if (ckeckicon != "music") {
//                if (ckeckicon == "app") {
//                    try {
//                        context.startActivity(
//                            context.packageManager.getLaunchIntentForPackage(
//                                file_DTO.packagename
//                            )
//                        )
//                    } catch (e: Exception) {
//                        Log.d("TAG!", "onMenuItemClick: " + e.message)
//                    }
//                } else {
//                    try {
//                        Ultil(context).openFile(File(file_DTO.path))
//                    } catch (unused: Exception) {
//                        Toast.makeText(context, "No activity for this action", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            } else {
//                playMedia(
//                    Uri.withAppendedPath(
//                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                        file_DTO.id
//                    )
//                )
//            }
//            Ultil(context).addRecent(file_DTO)
//        })
//        relativeLayout.setOnLongClickListener(object : OnLongClickListener {
//            override fun onLongClick(view: View): Boolean {
//                onClick.onlongclick()
//                val musicAdapter = this@MusicAdapter
//                musicAdapter.isCheck = !musicAdapter.isCheck
//                this@MusicAdapter.notifyDataSetChanged()
//                return false
//            }
//        })
//        if (posadd.size == 0) {
//            imageView3.setImageResource(R.drawable.esclip)
//        }
//        for (i in posadd.indices) {
//            val arrayList = posadd
//            if (arrayList.contains("" + baseViewHolder.getLayoutPosition())) {
//                imageView3.setImageResource(R.drawable.blackcheck)
//            } else {
//                imageView3.setImageResource(R.drawable.esclip)
//            }
//        }
//        imageView3.setOnClickListener {
//            pos = baseViewHolder.getLayoutPosition()
//            if (posadd.size == 0) {
//                val arrayList2: ArrayList<*> = posadd
//                arrayList2.add("" + pos)
//            } else {
//                val arrayList3: ArrayList<*> = posadd
//                if (arrayList3.contains("" + pos)) {
//                    val arrayList4: ArrayList<*> = posadd
//                    arrayList4.remove("" + pos)
//                } else {
//                    val arrayList5: ArrayList<*> = posadd
//                    arrayList5.add("" + pos)
//                }
//            }
//            this@MusicAdapter.notifyDataSetChanged()
//            onClick.itemclick(baseViewHolder.getLayoutPosition())
//        }
//        if (isCheck) {
//            imageView3.setVisibility(View.VISIBLE)
//        } else {
//            imageView3.setVisibility(View.GONE)
//        }
//        CallbackUpdateMusic.getInstance().setStateListen(object : OncustomStateListen {
//            override fun statechange() {
//                if (ckeckicon == "music") {
//                    this@MusicAdapter.setList(MusicUltil(context).getdatafromDevice())
//                } else {
//                    val data_Manager = Data_Manager(context)
//                    data_Manager.sysn()
//                    if (ckeckicon != "txt") {
//                        if (ckeckicon != "docx") {
//                            if (ckeckicon != "xls") {
//                                if (ckeckicon != "ppt") {
//                                    if (ckeckicon != "pdf") {
//                                        if (ckeckicon != "apk") {
//                                            if (ckeckicon == "zip") {
//                                                this@MusicAdapter.setList(data_Manager.getzipwithMediastore())
//                                            }
//                                        } else {
//                                            this@MusicAdapter.setList(data_Manager.getallapp())
//                                        }
//                                    } else {
//                                        this@MusicAdapter.setList(data_Manager.getfilepdf())
//                                    }
//                                } else {
//                                    data_Manager.setDocs()
//                                    this@MusicAdapter.setList(data_Manager.getfileppt())
//                                }
//                            } else {
//                                this@MusicAdapter.setList(data_Manager.getfilexls())
//                            }
//                        } else {
//                            this@MusicAdapter.setList(data_Manager.getfidocx())
//                        }
//                    } else {
//                        this@MusicAdapter.setList(data_Manager.getfiletxt())
//                    }
//                }
//                this@MusicAdapter.notifyDataSetChanged()
//            }
//        })
//    }
//
//    fun playMedia(uri: Uri?) {
//        val intent = Intent("android.intent.action.VIEW")
//        intent.setData(uri)
//        context.startActivity(intent)
//    }
//
//    fun getlistchose(): ArrayList<File_DTO> {
//        val arrayList: ArrayList<File_DTO> = ArrayList<File_DTO>()
//        for (i in posadd.indices) {
//            arrayList.add(data.get(posadd[i].toInt()))
//        }
//        return arrayList
//    }
//
//    fun choseall() {
//        posadd.clear()
//        for (i in data.indices) {
//            val arrayList = posadd
//            arrayList.add("" + i)
//        }
//        notifyDataSetChanged()
//    }
//
//    fun clearlistchose() {
//        posadd.clear()
//        notifyDataSetChanged()
//    }
//
//    fun search(str: String) {
//        Log.e("~~~", "_$str:rs")
//        searchbackground(str).execute(*arrayOfNulls<String>(0))
//    }
//
//    inner class searchbackground(private val key: String) :
//        AsyncTask<String?, Int?, ArrayList<File_DTO?>?>() {
//        private var listResulf: ArrayList<File_DTO>? = null
//        private var save_list: ArrayList<File_DTO>? = null
//        override fun doInBackground(vararg strArr: String): ArrayList<File_DTO> {
//            data()
//            if (!save_list!!.isEmpty()) {
//                val it: Iterator<File_DTO> = save_list!!.iterator()
//                while (it.hasNext()) {
//                    val next: File_DTO = it.next()
//                    if (next.name.lowercase(Locale.getDefault()).contains(key)) {
//                        listResulf!!.add(next)
//                    } else {
//                        listResulf!!.isEmpty()
//                    }
//                }
//            }
//            return listResulf!!
//        }
//
//        protected override fun onPreExecute() {
//            listResulf = ArrayList<File_DTO>()
//            super.onPreExecute()
//        }
//
//        override fun onPostExecute(arrayList: ArrayList<File_DTO?>) {
//            super.onPostExecute(arrayList)
//            if (key.length == 0) {
//                this@MusicAdapter.setList(save_list)
//            } else {
//                this@MusicAdapter.setList(listResulf)
//            }
//            updateSearch.search()
//        }
//
//        fun data() {
//            val data_Manager = Data_Manager(context)
//            data_Manager.sysn()
//            if (ckeckicon != "music") {
//                if (ckeckicon != "txt") {
//                    if (ckeckicon != "docx") {
//                        if (ckeckicon != "xls") {
//                            if (ckeckicon != "ppt") {
//                                if (ckeckicon != "pdf") {
//                                    if (ckeckicon == "app") {
//                                        save_list = data_Manager.readAllAppssss(context)
//                                        return
//                                    } else if (ckeckicon != "dowload") {
//                                        if (ckeckicon != "apk") {
//                                            if (ckeckicon == "zip") {
//                                                save_list = data_Manager.getzipwithMediastore()
//                                                return
//                                            }
//                                            return
//                                        }
//                                        save_list = data_Manager.getallapp()
//                                        return
//                                    } else {
//                                        save_list = data_Manager.filesDowload()
//                                        return
//                                    }
//                                }
//                                save_list = data_Manager.getfilepdf()
//                                return
//                            }
//                            save_list = data_Manager.getfileppt()
//                            return
//                        }
//                        save_list = data_Manager.getfilexls()
//                        return
//                    }
//                    save_list = data_Manager.getfidocx()
//                    return
//                }
//                save_list = data_Manager.getfiletxt()
//                return
//            }
//            save_list = MusicUltil(context).getdatafromDevice()
//        }
//    }
//
//    companion object {
//        fun ShareAudiofileWith_idfile(context: Context, j: Long) {
//            val withAppendedId: Uri =
//                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, j)
//            val intent = Intent("android.intent.action.SEND")
//            intent.setType("audio/*")
//            intent.putExtra("android.intent.extra.STREAM", withAppendedId)
//            context.startActivity(Intent.createChooser(intent, "Share Sound File"))
//        }
//    }
//}
