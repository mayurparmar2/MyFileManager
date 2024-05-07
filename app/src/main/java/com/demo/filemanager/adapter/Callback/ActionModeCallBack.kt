package com.demo.filemanager.adapter.Callback

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.system.Os.remove
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.demo.filemanager.Activity.ResulfActivity
import com.demo.filemanager.Activity.customview.CustomDeleteDialog
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.DeleteCallback
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.FileOperations
import com.demo.filemanager.Ultil.Ultil
import com.demo.filemanager.adapter.GirlAdapter.GridAdapter
import com.demo.filemanager.adapter.MyRecyclerAdapter
import java.io.File
import java.io.IOException

class ActionModeCallBack : ActionMode.Callback {

    //
//    private var adapter: MyRecyclerAdapter? = null
//    private var alertDialog1: AlertDialog? = null
//    private var b1: TextView? = null
//    private var b2: TextView? = null
//    private var context: Context
//    var cut = false
//    private var data_manager: Data_Manager
//    private var editText: EditText? = null
//    var fileOperations = FileOperations()
//    var gadapter: GridAdapter? = null
//    var handler: Handler
//    private var sortFlags: Int
//    private var source_doc: List<DocumentFile?>? = null
//    private var sources: List<File>? = null
//    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
//        return false
//    }
//
//    constructor(
//        myRecyclerAdapter: MyRecyclerAdapter?,
//        context: Context,
//        data_Manager: Data_Manager,
//        i: Int
//    ) {
//        adapter = myRecyclerAdapter
//        this.context = context
//        data_manager = data_Manager
//        sortFlags = i
//        handler = Handler(context.mainLooper)
//    }
//
//    constructor(gridAdapter: GridAdapter?, context: Context, data_Manager: Data_Manager, i: Int) {
//        gadapter = gridAdapter
//        this.context = context
//        data_manager = data_Manager
//        sortFlags = i
//        handler = Handler(context.mainLooper)
//    }
//
//    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
//        actionMode.menuInflater.inflate(R.menu.contextual_menu, menu)
//        if (ResulfActivity.favourites) {
//            menu.findItem(R.id.remove).setVisible(true)
//        } else {
//            menu.findItem(R.id.remove).setVisible(false)
//        }
//        source_doc = ArrayList()
//        return true
//    }
//
//    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
//        val file: File
//        var z: Boolean
////        when (menuItem.itemId) {
////            R.id.copy -> {
////                ResulfActivity.isPasteMode = true
////                actionMode.menu.clear()
////                actionMode.title = Environment.getExternalStorageDirectory().path
////                if (ResulfActivity.gridView) {
////                    sources = gadapter!!.getSelectedItemsFile()
////                    gadapter!!.clearSelection()
////                } else {
////                    sources = adapter!!.selectedItemsFile
////                    adapter!!.clearSelection()
////                }
////                ResulfActivity.isSelection = false
////                if (!ResulfActivity.sdCardmode) {
////                    ResulfActivity.path = Environment.getExternalStorageDirectory()
////                    ResulfActivity.getCurrentPath()?.let { data_manager.setRecycler(it, sortFlags) }
////                    if (ResulfActivity.gridView) {
////                        gadapter!!.notifyDataSetChanged()
////                    } else {
////                        adapter!!.notifyDataSetChanged()
////                    }
////                } else {
////                    ResulfActivity.path = ResulfActivity.externalSD_root
////                    ResulfActivity.getCurrentPath()?.let { data_manager.setRecycler(it, sortFlags) }
////                    if (ResulfActivity.gridView) {
////                        gadapter!!.notifyDataSetChanged()
////                    } else {
////                        adapter!!.notifyDataSetChanged()
////                    }
////                    ResulfActivity.documentFile = ResulfActivity.permadDocumentFile
////                }
////                ResulfActivity.collections = false
////                actionMode.menuInflater.inflate(R.menu.paste_menu, actionMode.menu)
////            }
////
////            R.id.cut -> {
////                cut = true
////                ResulfActivity.isPasteMode = true
////                actionMode.menu.clear()
////                actionMode.title = Environment.getExternalStorageDirectory().path
////                if (ResulfActivity.gridView) {
////                    sources = gadapter!!.getSelectedItemsFile()
////                    gadapter!!.clearSelection()
////                } else {
////                    sources = adapter!!.selectedItemsFile
////                    adapter!!.clearSelection()
////                }
////                ResulfActivity.isSelection = false
////                if (!ResulfActivity.sdCardmode) {
////                    ResulfActivity.path = Environment.getExternalStorageDirectory()
////                    ResulfActivity.getCurrentPath()?.let { data_manager.setRecycler(it, sortFlags) }
////                    if (ResulfActivity.gridView) {
////                        gadapter!!.notifyDataSetChanged()
////                    } else {
////                        adapter!!.notifyDataSetChanged()
////                    }
////                } else {
////                    ResulfActivity.path = ResulfActivity.externalSD_root
////                }
////                ResulfActivity.getCurrentPath()?.let { data_manager.setRecycler(it, sortFlags) }
////                if (ResulfActivity.gridView) {
////                    gadapter!!.notifyDataSetChanged()
////                } else {
////                    adapter!!.notifyDataSetChanged()
////                }
////                ResulfActivity.documentFile = ResulfActivity.permadDocumentFile
////                ResulfActivity.collections = false
////                actionMode.menuInflater.inflate(R.menu.paste_menu, actionMode.menu)
////            }
////
////            R.id.delete -> {
////                AlertDialog.Builder(context)
////                if (ResulfActivity.gridView) {
////                    gadapter!!.getSelectedItemCount()
////                } else {
////                    adapter!!.getSelectedItemCount()
////                }
////                val customDeleteDialog = CustomDeleteDialog(context, object : DeleteCallback {
////                    override fun update() {
////                        val selectedItemsFile: List<File>
////                        if (!ResulfActivity.gridView) {
////                            selectedItemsFile = adapter!!.selectedItemsFile
////                        } else {
////                            selectedItemsFile = gadapter!!.getSelectedItemsFile()
////                        }
////                        var i = 0
////                        while (i < selectedItemsFile.size) {
////                            Ultil(context).copyFile(selectedItemsFile[i].path)
////                            if (!ResulfActivity.sdCardmode && !ResulfActivity.collections) {
////                                while (selectedItemsFile[i].exists()) {
////                                    try {
////                                        FileOperations.delete(selectedItemsFile[i])
////                                    } catch (unused: Exception) {
////                                        Toast.makeText(
////                                            context,
////                                            "Sorry, unable to delete the file, don`t have permission",
////                                            Toast.LENGTH_SHORT
////                                        ).show()
////                                    }
////                                }
////                            } else if (ResulfActivity.collections) {
////                                if (ResulfActivity.whichCollection === 1) {
////                                    context.contentResolver.delete(
////                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                                        "_data=?",
////                                        arrayOf(
////                                            selectedItemsFile[i].path
////                                        )
////                                    )
////                                }
////                                if (ResulfActivity.whichCollection === 2) {
////                                    context.contentResolver.delete(
////                                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
////                                        "_data=?",
////                                        arrayOf(
////                                            selectedItemsFile[i].path
////                                        )
////                                    )
////                                }
////                                if (ResulfActivity.whichCollection === 3) {
////                                    context.contentResolver.delete(
////                                        MediaStore.Files.getContentUri("external"),
////                                        "_data=?",
////                                        arrayOf(
////                                            selectedItemsFile[i].path
////                                        )
////                                    )
////                                }
////                            } else {
////                                getDocumentFile(selectedItemsFile[i])?.delete()
////                            }
////                            i++
////                        }
////                        if (ResulfActivity.collections) {
////                            if (ResulfActivity.whichCollection === 1) {
////                                data_manager.setImagesData()
////                            }
////                            if (ResulfActivity.whichCollection === 2) {
////                                data_manager.setAudio(context)
////                            }
////                            if (ResulfActivity.whichCollection === 3) {
////                                data_manager.setDocs()
////                            }
////                        }
////                        if (!ResulfActivity.collections && !ResulfActivity.favourites) {
////                            ResulfActivity.getCurrentPath()
////                                ?.let { data_manager.setRecycler(it, sortFlags) }
////                        }
////                        if (ResulfActivity.favourites) {
////                            this@ActionModeCallBack.remove(actionMode)
////                        }
////                        if (!ResulfActivity.gridView) {
////                            adapter!!.notifyDataSetChanged()
////                        } else {
////                            gadapter!!.notifyDataSetChanged()
////                        }
////                        actionMode.finish()
////                    }
////                })
////                customDeleteDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
////                customDeleteDialog.window!!.attributes.windowAnimations =
////                    R.style.PauseDialogAnimation
////                customDeleteDialog.show()
////            }
////
////            R.id.paste -> {
////                ResulfActivity.collections = false
////                val builder: NotificationCompat.Builder = NotificationCompat.Builder(
////                    context, ResulfActivity.CHANNEL_ID
////                )
////                builder.setContentTitle("Copying Files").setSmallIcon(R.drawable.image_folder)
////                    .setAutoCancel(true).setPriority(111)
////                builder.setContentText(
////                    "Copying files from " + sources!![0].getName() + " to " + ResulfActivity.getCurrentPath()?.getName()
////                )
////                builder.setLargeIcon(
////                    BitmapFactory.decodeResource(
////                        context.resources,
////                        R.drawable.image_folder
////                    )
////                )
////                val from = NotificationManagerCompat.from(context)
////                builder.setContentTitle("Files copied sucessfully")
////                builder.setContentText("100% completed")
////                from.notify(1, builder.build())
////                backGroundCopy(actionMode)
////                if (cut) {
////                    Thread(object : Runnable {
////                        override fun run() {
////                            var i = 0
////                            while (i < sources!!.size) {
////                                if (!ResulfActivity.sdCardmode) {
////                                    while (sources!![i].exists()) {
////                                        try {
////                                            FileOperations.delete(sources!![i])
////                                            context.sendBroadcast(
////                                                Intent(
////                                                    "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
////                                                    Uri.fromFile(
////                                                        sources!![i]
////                                                    )
////                                                )
////                                            )
////                                            MediaScannerConnection.scanFile(context,
////                                                arrayOf(
////                                                    sources!![i].path
////                                                ),
////                                                null,
////                                                MediaScannerConnection.OnScanCompletedListener { str, uri ->
////                                                    try {
////                                                        context.contentResolver.delete(
////                                                            uri,
////                                                            null,
////                                                            null
////                                                        )
////                                                    } catch (unused: Exception) {
////                                                        Toast.makeText(
////                                                            context,
////                                                            context.getString(R.string.paste_tiitle),
////                                                            Toast.LENGTH_SHORT
////                                                        ).show()
////                                                    }
////                                                })
////                                        } catch (unused: Exception) {
////                                            if (!getDocumentFile(
////                                                    sources!![i]
////                                                )?.delete()!!
////                                            ) {
////                                                Toast.makeText(
////                                                    context,
////                                                    "Sorry, Could not delete the selected file",
////                                                    Toast.LENGTH_SHORT
////                                                ).show()
////                                            }
////                                        }
////                                    }
////                                } else {
////                                    getDocumentFile(sources!![i])?.delete()
////                                }
////                                i++
////                            }
////                            handler.post(object : Runnable {
////                                override fun run() {
////                                    actionMode.finish()
////                                    ResulfActivity.getCurrentPath()
////                                        ?.let { data_manager.setRecycler(it, sortFlags) }
////                                    if (!ResulfActivity.gridView) {
////                                        adapter!!.notifyDataSetChanged()
////                                    } else {
////                                        gadapter!!.notifyDataSetChanged()
////                                    }
////                                }
////                            })
////                        }
////                    }).start()
////                }
////                actionMode.finish()
////                if (ResulfActivity.sdCardmode) {
////                    ResulfActivity.getCurrentPath()?.let { data_manager.setRecycler(it, sortFlags) }
////                    if (ResulfActivity.gridView) {
////                        gadapter!!.notifyDataSetChanged()
////                    } else {
////                        adapter!!.notifyDataSetChanged()
////                    }
////                }
////                cut = false
////            }
////
////            R.id.properties -> {
////                val ultil = Ultil(context)
////                val file_DTO = File_DTO()
////                if (ResulfActivity.gridView) {
////                    file = gadapter!!.getSelectedItemsFile()[0]
////                } else {
////                    file = adapter!!.selectedItemsFile[0]
////                }
////                file_DTO.name = file.getName()
////                file_DTO.path = file.path
////                file_DTO.size = ultil.bytesToHuman(file.length())
////                file_DTO.date = ultil.getDate(file.lastModified())
////                Ultil(context).showdiloginfo(file_DTO, "")
////            }
////
////            R.id.remove -> remove(actionMode)
////            R.id.rename -> {
////                val builder2 = AlertDialog.Builder(context)
////                builder2.setView(R.layout.dialogrename).setCancelable(false)
////                val create = builder2.create()
////                alertDialog1 = create
////                create.window!!.setBackgroundDrawable(ColorDrawable(0))
////                alertDialog1!!.show()
////                editText = alertDialog1!!.findViewById<View>(R.id.edt_filename) as EditText
////                if (ResulfActivity.gridView) {
////                    editText.setText(gadapter!!.getSelectedItemsFile()[0].getName())
////                } else {
////                    editText!!.setText(adapter!!.selectedItemsFile[0].getName())
////                }
////                editText!!.selectAll()
////                b1 = alertDialog1!!.findViewById<View>(R.id.txt_Cancel) as TextView
////                val textView = alertDialog1!!.findViewById<View>(R.id.txt_OK) as TextView
////                b2 = textView
////                textView.setOnClickListener(object : View.OnClickListener {
////                    override fun onClick(view: View) {
////                        val renameTo: Boolean
////                        val obj = editText!!.getText().toString()
////                        if (!ResulfActivity.sdCardmode && !ResulfActivity.collections) {
////                            if (!ResulfActivity.gridView) {
////                                if (!adapter!!.selectedItemsFile[0].renameTo(
////                                        File(
////                                            ResulfActivity.getCurrentPath()?.getPath() + "/" + obj
////                                        )
////                                    )
////                                ) {
////                                    Toast.makeText(context, "Invalid FileName", Toast.LENGTH_SHORT)
////                                        .show()
////                                }
////                            } else {
////                                if (!gadapter!!.getSelectedItemsFile()[0].renameTo(File(ResulfActivity.getCurrentPath()?.getPath() + "/" + obj))) {
////                                    Toast.makeText(context, "Invalid FileName", Toast.LENGTH_SHORT).show()
////                                }
////                            }
////                        } else if (ResulfActivity.collections) {
////                            val contentValues = ContentValues()
////                            contentValues.put("_display_name", obj)
////                            val strArr = if (ResulfActivity.gridView) arrayOf<String>(
////                                gadapter!!.getSelectedItemsFile()[0].getPath()
////                            ) else arrayOf<String>(
////                                adapter!!.selectedItemsFile[0].path
////                            )
////                            if (ResulfActivity.whichCollection === 1) {
////                                context.contentResolver.update(
////                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                                    contentValues,
////                                    "_data=?",
////                                    strArr
////                                )
////                            }
////                            if (ResulfActivity.whichCollection === 2) {
////                                context.contentResolver.update(
////                                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
////                                    contentValues,
////                                    "_data=?",
////                                    strArr
////                                )
////                            }
////                            if (ResulfActivity.whichCollection === 3) {
////                                context.contentResolver.update(
////                                    MediaStore.Files.getContentUri("external"),
////                                    contentValues,
////                                    "_data=?",
////                                    strArr
////                                )
////                            }
////                        } else {
////                            if (!ResulfActivity.gridView) {
////                                renameTo =
////                                    getDocumentFile(adapter!!.selectedItemsFile[0]).renameTo(obj)
////                            } else {
////                                renameTo =
////                                    getDocumentFile(gadapter!!.getSelectedItemsFile()[0]).renameTo(
////                                        obj
////                                    )
////                            }
////                            if (!renameTo) {
////                                Toast.makeText(context, "Invalid FileName", Toast.LENGTH_LONG)
////                                    .show()
////                            }
////                        }
////                        if (ResulfActivity.favourites) {
////                            this@ActionModeCallBack.remove(actionMode)
////                        }
////                        alertDialog1!!.cancel()
////                        if (!ResulfActivity.gridView) {
////                            adapter!!.clearSelection()
////                        } else {
////                            gadapter!!.clearSelection()
////                        }
////                        ResulfActivity.isPasteMode = false
////                        ResulfActivity.isSelection = false
////                        if (ResulfActivity.collections) {
////                            if (ResulfActivity.whichCollection === 1) {
////                                data_manager.setImagesData()
////                            }
////                            if (ResulfActivity.whichCollection === 2) {
////                                data_manager.setAudio(context)
////                            }
////                            if (ResulfActivity.whichCollection === 3) {
////                                data_manager.setDocs()
////                            }
////                        }
////                        if (!ResulfActivity.collections && !ResulfActivity.favourites) {
////                            ResulfActivity.getCurrentPath()
////                                ?.let { data_manager.setRecycler(it, sortFlags) }
////                        }
////                        if (!ResulfActivity.gridView) {
////                            adapter!!.notifyDataSetChanged()
////                        } else {
////                            gadapter!!.notifyDataSetChanged()
////                        }
////                        actionMode.finish()
////                    }
////                })
////                b1!!.setOnClickListener(object : View.OnClickListener {
////                    override fun onClick(view: View) {
////                        alertDialog1!!.cancel()
////                    }
////                })
////            }
////
////            R.id.share -> {
////                val arrayList:ArrayList<Uri> = ArrayList<Uri>()
////                if (ResulfActivity.gridView) {
////                    var i = 0
////                    while (i < gadapter!!.getSelectedItemsFile().size) {
////                        if (gadapter!!.getSelectedItemsFile()[i].isDirectory()) {
////                            Toast.makeText(
////                                context,
////                                "Sorry couldn`t share directories",
////                                Toast.LENGTH_SHORT
////                            ).show()
////                            z = false
////                        } else {
////                            val add = arrayList.add(
////                                FileProvider.getUriForFile(
////                                    context,
////                                    context.packageName + ".provider",
////                                    gadapter!!.getSelectedItemsFile()[i]
////                                )
////                            )
////                        }
////                        i++
////                    }
////                    z = true
////                } else {
////                    var i2 = 0
////                    while (i2 < adapter!!.selectedItemsFile.size) {
////                        if (adapter!!.selectedItemsFile[i2].isDirectory()) {
////                            Toast.makeText(
////                                context,
////                                "Sorry couldn`t share directories",
////                                Toast.LENGTH_SHORT
////                            ).show()
////                            z = false
////                        } else {
////                            arrayList.add(
////                                FileProvider.getUriForFile(
////                                    context,
////                                    context.packageName + ".provider",
////                                    adapter!!.selectedItemsFile[i2]
////                                )
////                            )
////                        }
////                        i2++
////                    }
////                    z = true
////                }
////                if (z) {
////                    try {
////                        val intent = Intent("android.intent.action.SEND_MULTIPLE")
////                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
////                        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList)
////                        intent.setType("images/*")
////                        context.startActivity(Intent.createChooser(intent, "Send Via"))
////                        break
////                    } catch (unused: Exception) {
////                        break
////                    }
////                }
////            }
////        }
//        return false
//    }
//
//    override fun onDestroyActionMode(actionMode: ActionMode) {
//        if (ResulfActivity.gridView) {
//            gadapter!!.clearSelection()
//        } else {
//            adapter!!.clearSelection()
//        }
//        ResulfActivity.isPasteMode = false
//        ResulfActivity.isSelection = false
//        cut = false
//    }
//
////    fun remove(actionMode: ActionMode) {
////        var i = 0
////        val sharedPreferences = context.getSharedPreferences("favourites", 0)
////        val hashSet: HashSet<*> = HashSet<Any?>(sharedPreferences.getStringSet("key", null))
////        if (!ResulfActivity.gridView) {
////            while (i < adapter!!.getSelectedItemCount()) {
////                hashSet.remove(adapter!!.selectedItemsFile[i].path)
////                i++
////            }
////        } else {
////            while (i < gadapter!!.getSelectedItemCount()) {
////                hashSet.remove(gadapter!!.getSelectedItemsFile()[i].getPath())
////                i++
////            }
////        }
////        val edit = sharedPreferences.edit()
////        edit.putStringSet("key", hashSet)
////        edit.apply()
////        actionMode.finish()
////        data_manager.setFavourites(context)
////        if (ResulfActivity.gridView) {
////            gadapter!!.notifyDataSetChanged()
////        } else {
////            adapter!!.notifyDataSetChanged()
////        }
////    }
////
////    private fun backGroundCopy(actionMode: ActionMode) {
////        Thread(object : Runnable {
////            override fun run() {
////                for (i in sources!!.indices) {
////                    if (!ResulfActivity.sdCardmode) {
////                        try {
////                            FileOperations.copyFolder(sources!![i], ResulfActivity.getCurrentPath())
////                        } catch (e: IOException) {
////                            if (ResulfActivity.isExternalSD_available) {
////                                fileOperations.pasteDoc(
////                                    sources!![i],
////                                    ResulfActivity.getCurrentPath(),
////                                    context
////                                )
////                            }
////                            e.printStackTrace()
////                        }
////                    } else if (ResulfActivity.sdCardmode) {
////                        fileOperations.pasteDoc(
////                            sources!![i],
////                            ResulfActivity.getCurrentPath(),
////                            context
////                        )
////                    }
////                }
////                handler.post(object : Runnable {
////                    override fun run() {
////                        actionMode.finish()
////                        data_manager.setRecycler(ResulfActivity.getCurrentPath(), sortFlags)
////                        if (!ResulfActivity.gridView) {
////                            adapter!!.notifyDataSetChanged()
////                        } else {
////                            gadapter!!.notifyDataSetChanged()
////                        }
////                    }
////                })
////            }
////        }).start()
////    }
//
//    companion object {
//        private val PRIORITY_DEFAULT = 111
//        fun getDocumentFile(file: File): DocumentFile? {
//            val split = file.path.substring(ResulfActivity.externalSD_root!!.path.length + 1)
//                .split("/".toRegex()).dropLastWhile { it.isEmpty() }
//                .toTypedArray()
//            var documentFile: DocumentFile? = ResulfActivity.permadDocumentFile
//            for (str: String? in split) {
//                val findFile = documentFile?.findFile((str)!!)
//                if (findFile != null) {
//                    documentFile = findFile
//                }
//            }
//            return documentFile
//        }
//    }
//
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        TODO("Not yet implemented")
    }

}
