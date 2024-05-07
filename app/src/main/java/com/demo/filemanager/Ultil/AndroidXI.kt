package com.demo.filemanager.Ultil

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.File

class AndroidXI(val context: Context) {

    fun renamewithpathAPI29(path: String, text: String) {
        val file = File(path)
        val parent = file.parent
        val absolutePath = file.absolutePath
        val substring: String =
            absolutePath.substring(absolutePath.lastIndexOf(FileUltils.HIDDEN_PREFIX))
        val file2 = File("$parent/$text$substring")
        if (file2.path != file.path) {
            if (file.renameTo(file2)) {
                this.context.getApplicationContext().getContentResolver().delete(
                    MediaStore.Files.getContentUri("external"),
                    "_data=?",
                    arrayOf<String>(file.absolutePath)
                )
                val intent =
                    Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2))
                intent.setData(Uri.fromFile(file2))
                this.context.getApplicationContext().sendBroadcast(intent)
                this.context.sendBroadcast(intent)
                Toast.makeText(this.context, "Done", Toast.LENGTH_SHORT).show()
                return
            }
            val contentValues = ContentValues()
            contentValues.put("_display_name", text)
            this.context.getContentResolver().update(
                MediaStore.Files.getContentUri("external"),
                contentValues,
                "_data=?",
                arrayOf<String>(path)
            )
            this.context.sendBroadcast(
                Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2))
            )
            Toast.makeText(this.context, "Done", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this.context, "No change", Toast.LENGTH_SHORT).show()
    }

}