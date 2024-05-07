package com.demo.filemanager.Activity.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.demo.filemanager.Iinterface.DeleteCallback
import com.demo.filemanager.R

class CustomDeleteDialog(context: Context?, var deleteCallback: DeleteCallback) : Dialog(
    context!!
) {
    var textView_titile: TextView? = null
    var txt_yes: TextView? = null
    override fun onCreate(bundle: Bundle) {
        super.onCreate(bundle)
        requestWindowFeature(1)
        setContentView(R.layout.dialog_recycle)
        textView_titile = findViewById<View>(R.id.titile) as TextView
        txt_yes = findViewById<View>(R.id.txt_yes) as TextView
        findViewById<View>(R.id.txt_Cancel).setOnClickListener { view ->
            dismiss()
        }
        txt_yes!!.setOnClickListener { view ->
            deleteCallback.update()
            dismiss()
        }
    }
    fun setview() {
        textView_titile!!.setText(R.string.hide_titile_dialog_delete)
    }

    fun setTextdelete() {
        textView_titile!!.setText(R.string.delete_dialog_title)
    }

    fun setTextdeleteMultil() {
        textView_titile!!.setText(R.string.delete_therefiles)
    }

    fun settextdeleteone_file() {
        textView_titile!!.setText(R.string.delete_one_img)
    }

    fun setTextmul_file() {
        textView_titile!!.setText(R.string.delete_files)
    }

    fun set_titile_restore_oneVideo() {
        txt_yes!!.setText(R.string.restore_im)
        textView_titile!!.setText(R.string.delete_one_video)
    }

    fun set_tiltile_restore_mulvideo() {
        txt_yes!!.setText(R.string.restore_im)
        textView_titile!!.setText(R.string.delete_multil_video)
    }

    fun set_title_hide() {
        textView_titile!!.setText(R.string.titile_hide)
        txt_yes!!.setText(R.string.hide)
    }

    fun delete_video() {
        textView_titile!!.setText(R.string.delete_video)
        txt_yes!!.setText(R.string.delete)
    }

    fun delete_videos() {
        textView_titile!!.setText(R.string.delete_videos)
        txt_yes!!.setText(R.string.delete)
    }

    fun set_title_exit() {
        textView_titile!!.setText(R.string.exits_titile)
        txt_yes!!.setText(R.string.exit)
    }

    fun set_titile_permission_Usage() {
        txt_yes!!.setText(R.string.agree)
        textView_titile!!.setText(R.string.permision_titile_usage)
    }

    fun set_titile_dialog_exit_antivirus() {
        txt_yes!!.setText(R.string.exit)
    }

    fun set_titile_button_hide() {
        txt_yes!!.setText(R.string.hide)
    }

    fun settitle_Music() {
        textView_titile!!.setText(R.string.delete_audio_file)
    }

    fun set_titile_multil_audio() {
        textView_titile!!.setText(R.string.delete_multil_files)
    }

    fun delete_permanerly() {
        textView_titile!!.setText(R.string.delete_permanely)
    }

    fun delete_onefile_permanenly() {
        textView_titile!!.setText(R.string.delete_one_file_permanerly)
    }

    fun restore_img() {
        txt_yes!!.setText(R.string.restore_im)
        textView_titile!!.setText(R.string.tititle_restore)
    }

    fun restore_imgs() {
        txt_yes!!.setText(R.string.restore_im)
        textView_titile!!.setText(R.string.titile_restore_imgs)
    }

    fun set_title_trustapp() {
        textView_titile!!.setText(R.string.trust_this_app)
        txt_yes!!.setText(R.string.trust)
    }

    fun titile_retrict_access() {
        textView_titile!!.setText(R.string.titile_retrict)
        txt_yes!!.setText(R.string.ok)
    }
}
