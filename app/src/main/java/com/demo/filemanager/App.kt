package com.demo.filemanager

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        fun checkPermission(context: Activity): Boolean {
            return if (Build.VERSION.SDK_INT >= 30) {
                Environment.isExternalStorageManager()
            } else ContextCompat.checkSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            ) == 0 && ContextCompat.checkSelfPermission(
                context,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            ) == 0
        }

        fun requestPermission(context: Activity,launcher:ActivityResultLauncher<Intent>) {
            if (Build.VERSION.SDK_INT >= 30) {
                try {
                    val intent = Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION")
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.setData(
                        Uri.parse(
                            String.format(
                                "package:%s",
                                context.getPackageName()
                            )
                        )
                    )
                    launcher.launch(intent)
                    return
                } catch (unused: Exception) {
                    val intent2 = Intent()
                    intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION")
                    launcher.launch(intent2)
                    return
                }
            }
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE"
                ),
                111
            )
        }
    }
}