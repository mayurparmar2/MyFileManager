package com.demo.filemanager.Iinterface

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.demo.filemanager.Activity.customview.Dialog_thread

class BackgroundTask(private val activity: Context) {
    fun Handleloop(handleLooper: HandleLooper) {
        val dialog_thread = Dialog_thread(activity)
        dialog_thread.show()
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                handleLooper.update()
                dialog_thread.dissmiss()
            }
        }.obtainMessage(111, "parameter").sendToTarget()
    }
}
