package com.demo.filemanager.Activity.customview

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.demo.filemanager.R

class Dialog_thread(private val context: Context) {
    private val aDialog: Dialog
    private val aDialogBuilder: AlertDialog.Builder? = null
    private val imageView: ImageView

    init {
        val dialog = Dialog(context)
        aDialog = dialog
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        aDialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        aDialog.setContentView(R.layout.dialog_process)
        imageView = aDialog.findViewById<View>(R.id.gif) as ImageView
        Glide.with(context).load(R.raw.rocket).into(imageView)
    }

    fun show() {
        aDialog.show()
    }

    fun dissmiss() {
        aDialog.cancel()
    }
}
