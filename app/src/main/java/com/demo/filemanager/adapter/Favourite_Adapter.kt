package com.demo.filemanager.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Ultil

class Favourite_Adapter(
    val mcontext: Context,
    private val onClickItem: OnClickItem,
    private val check: String
) : BaseQuickAdapter<File_DTO?, BaseViewHolder>(R.layout.custom_ittemsong) {
    var isCheckview = false
    var pos: Int
    var posadd: ArrayList<String>
    var savelisrrecycle: ArrayList<File_DTO>? = null
    lateinit var savelist: ArrayList<File_DTO>

    interface OnClickItem {
        fun itemclickFr(i: Int)
        fun menuclickFr(i: Int)
        fun onlongclick()
    }

    init {
        pos = -1
        posadd = ArrayList()
//        savelist = Ultil(context).getListfav()
    }

    //    override fun convert(holder: BaseViewHolder, item: File_DTO?) {
    override fun convert(baseViewHolder: BaseViewHolder, file_DTO: File_DTO?) {
    }

}
