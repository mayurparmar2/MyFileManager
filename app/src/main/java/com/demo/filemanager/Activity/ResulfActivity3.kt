package com.demo.filemanager.Activity

//
//class ResulfActivity3 : AppCompatActivity() {
//
//
//    fun Contructer() {
//        this.musicUltil = MusicUltil(this)
//        this.image_ultil = Image_Ultil(this)
//        this.video_ultil = Video_Ultil(this)
//        data_manager = Data_Manager(this)
//        data_manager.setDocs()
//    }
//    private fun data() {
//
//        this.file_dtos_image = image_ultil.getallalbumImage()
//        this.file_dtos_video = video_ultil.updateAllVidepAlbum()
//
//        Log.e("TAG", "data:file_dtos_image ${file_dtos_image.size} ")
//        Log.e("TAG", "data:file_dtos_video ${file_dtos_video.size} ")
//
//    }
////    private val imageAdapter: ImageAdapter? = null
//    private val favourite_adapter: Favourite_Adapter? = null
//
//    lateinit var video_ultil: Video_Ultil
//    lateinit var musicUltil: MusicUltil
//    lateinit var image_ultil: Image_Ultil
//
//    lateinit var file_dtos_image: ArrayList<File_DTO>
//    lateinit var file_dtos_video: ArrayList<File_DTO>
//
//    lateinit var data_manager: Data_Manager
//    lateinit var adapter: GridAdapter
//
//    lateinit var myRecyclerAdapter: MyRecyclerAdapter
//    var gridView = false
//
//    private var name_key: String = ""
//    private val dowloadApdapter: DowloadApdapter? = null
//
//    private var check = false
//    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null
//    public override fun onCreate(bundle: Bundle?) {
//        super.onCreate(bundle)
//        setContentView(R.layout.activity_resulf)
//
//
//        this.name_key = intent.extras?.getString("nameitem").toString()
//
//        val popupMenu = PopupMenu(ContextThemeWrapper(this,R.style.AppTheme), this.img_menu
//        )
//        popupMenu.menuInflater.inflate(R.menu.menu_filestorage, popupMenu.menu)
//        this.img_back.setOnClickListener(View.OnClickListener { Eventback() })
//        this.launcher = registerForActivityResult<IntentSenderRequest, ActivityResult>(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
//            if (activityResult.resultCode == -1) {
//                Toast.makeText(
//                    this@ResulfActivity3,
//                    "Delete",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
////        val handleLooper: HandleLooper = object : HandleLooper {
////            override fun update() {
////                musicAdapter.setList(data_manager.readAllAppssss(this@ResulfActivity))
////                musicAdapter.setDrawables(data_manager.drawables())
////                musicAdapter.notifyDataSetChanged()
////            }
////        }
////
////        this.activityResultLauncher = registerForActivityResult<android.content.Intent, androidx.activity.result.ActivityResult>(StartActivityForResult(), object : ActivityResultCallback<androidx.activity.result.ActivityResult> {
////            open fun onActivityResult(activityResult: androidx.activity.result.ActivityResult) {
////                if (activityResult.getResultCode() == -1) {
////                    android.widget.Toast.makeText(this@ResulfActivity , "can't uninstall", android.widget.Toast.LENGTH_SHORT).show()
////                } else {
////                    BackgroundTask(this@ResulfActivity ).Handleloop(handleLooper)
////                }
////            }
////        })
//        Contructer()
//
//        data()
//        name_key = intent.extras!!.getString("nameitem")!!
////        val updatelistVar: updatelist = object : updatelist {
////            override fun update(i: Int) {
////                checkEmpty(i)
////            }
////        }
//        when (name_key) {
//            "SDcard" -> {
//                this.img_menu.visibility = View.VISIBLE
//                this.textView_title.setText(getString(R.string.sd_card))
//                switchToSD()
//            }
//
////            "images" -> {
////                this.textView_title.setText(getString(R.string.images))
////                this.txt_checklist.setText("No  Albums found!")
////                this.img_checklist.setImageResource(R.drawable.icon_imgcheck)
////                ImageAlbum()
////            }
////            "recent" -> {
////                this.textView_title.setText("Recent")
////                this.txt_checklist.setText("No files found!")
////                recentfile()
////            }
////
////            "imagehide" -> {
////                this.textView_title.setText("Images Hide")
////                this.txt_checklist.setText("No images found!")
////                hidefile()
////            }
////
////            "apk" -> {
////                this.textView_title.setText(R.string.apks)
////                this.txt_checklist.setText("No  apks found!")
////                this.img_checklist.setImageResource(R.drawable.apkcheck)
////                apkUltil()
////            }
////
////            "app" -> {
////                this.textView_title.setText(getString(R.string.Application))
////                this.txt_checklist.setText("No apps found!")
////                this.img_checklist.setImageResource(R.drawable.appcheck)
////                app()
////            }
////
////            "doc" -> {
////                this.textView_title.setText("DOC")
////                DocUi()
////            }
////
////            "fav" -> {
////                this.textView_title.setText("Favourite")
////                this.txt_checklist.setText("No favourite files found!")
////                this.img_checklist.setImageResource(R.drawable.favcheck)
////                Favourites()
////            }
////
////            "pdf" -> {
////                this.textView_title.setText("PDF")
////                PdfUi()
////            }
////
////            "ppt" -> {
////                this.textView_title.setText("PPT")
////                PptUi()
////            }
////
////            "txt" -> {
////                this.textView_title.setText("TXT")
////                txtUi()
////            }
////
////            "xls" -> {
////                this.textView_title.setText("XLS")
////                XlstUi()
////            }
////
////            "zip" -> {
////                this.textView_title.setText(R.string.zipped)
////                this.txt_checklist.setText("No  zips found!")
////                this.img_checklist.setImageResource(R.drawable.zipcheck)
////                zipUI()
////            }
////
////            "music" -> {
////                this.textView_title.setText(R.string.audios)
////                this.txt_checklist.setText("No audios found!")
////                this.img_checklist.setImageResource(R.drawable.iconmusic_check)
////                MusicUi()
////            }
////
////            "video" -> {
////                this.textView_title.setText(getString(R.string.video))
////                this.txt_checklist.setText("No  Albums found!")
////                this.img_checklist.setImageResource(R.drawable.icon_videocheck)
////                VideoAlbum()
////            }
////
////            "recycle" -> {
////                this.textView_title.setText(R.string.recycle)
////                this.txt_checklist.setText("No files found!")
////                this.img_checklist.setImageResource(R.drawable.recyclecheck)
////                Recycledta()
////            }
////
////            "videohide" -> {
////                this.textView_title.setText("Videos Hide")
////                this.txt_checklist.setText("No videos found!")
////                hidefile()
////            }
////
////            "newfile" -> {
////                this.textView_title.setText("New File")
////                this.txt_checklist.setText("No files found!")
////                try {
////                    NewFile()
////                    break
////                } catch (e: IOException) {
////                    e.printStackTrace()
////                    break
////                }
////                this.textView_title.setText(getString(R.string.dowdnloads))
////                dowlaod()
////            }
////
////            "dowload" -> {
////                this.textView_title.setText(getString(R.string.dowdnloads))
////                dowlaod()
////            }
////
////            "Instorage" -> {
////                this.img_menu.visibility = View.VISIBLE
////                this.textView_title.setText(getString(R.string.instorage))
////                switchToInternal()
////                this.search =
////                    BackGroundSearch(this.data_manager, this.adapter, this.myRecyclerAdapter)
////            }
//        }
//
//    }
//    fun refresh() {
//        path?.let { this.data_manager?.setRecycler(it, ResulfActivity.sortFlag) }
//    }
//    fun switchToSD() {
//        setExternalSD_root()
//        path = File(getExternalStorageDirectories().get(0))
//        refresh()
//        Log.e("TAGW", "switchToSD: $path")
//        UItabfile()
//    }
//    private fun UItabfile() {
//        this.adapter = GridAdapter(data_manager!!, this)
//        this.myRecyclerAdapter = MyRecyclerAdapter(data_manager!!, this)
//        data_manager!!.setRecycler(path!!, sortFlag)
//        if (ResulfActivity.gridView) {
//            this.recyclerview_result.setLayoutManager(
//                GridLayoutManager(
//                    this as Context,
//                    4,
//                    RecyclerView.VERTICAL,
//                    false
//                )
//            )
//            this.recyclerview_result.setAdapter(this.adapter)
//        } else {
//            this.recyclerview_result.setAdapter(this.myRecyclerAdapter)
//            this.recyclerview_result.setLayoutManager(LinearLayoutManager(this))
//        }
//        registerForContextMenu(this.recyclerview_result)
//        this.recyclerview_result.addOnItemTouchListener(
//            Listener_for_Recycler(
//                applicationContext,
//                this.recyclerview_result,
//                Listener_for_Recycler())
//        )
//    }
//    inner class Listener_for_Recycler :com.demo.filemanager.adapter.Callback.Listener_for_Recycler.ClickListener{
//        override fun onClick(view: View?, i: Int) {
//            var intent: Intent? = null
//            if (!ResulfActivity.isSelection) {
//                path = data_manager!!.getFiles(i)
//                if (ResulfActivity.favourites) {
//                    ResulfActivity.favourites = false
//                }
//                if (ResulfActivity.isPasteMode) {
//                    actionMode?.setTitle(path!!.getName())
//                }
//                if (path!!.isDirectory()) {
//                    if (ResulfActivity.collections) {
//                        ResulfActivity.collections = false
//                    }
//                    if (ResulfActivity.sdCardmode) {
//                        ResulfActivity.documentFile =
//                            data_manager!!.getName(i)?.let {
//                                ResulfActivity.documentFile?.findFile(it)
//                            }
//                    }
//                    if (path!!.getName() == "data" && Build.VERSION.SDK_INT >= 30) {
//                        restrict_dialog()
//                    }
//                    data_manager!!.setRecycler(path!!, sortFlag)
//                    recyclerview_result.scrollToPosition(0)
//                    if (!ResulfActivity.gridView) {
//                        myRecyclerAdapter?.notifyDataSetChanged()
//                    } else {
//                        adapter?.notifyDataSetChanged()
//                    }
//                } else {
//                    try {
//                        intent = Intent()
//                    } catch (unused: java.lang.Exception) {
//                        Toast.makeText(
//                            this@ResulfActivity3,
//                            "Couldn`t open the specified file",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    if (!path.toString().lowercase(Locale.getDefault())
//                            .contains("jpg") && !path.toString().lowercase(
//                            Locale.getDefault()
//                        ).contains("jpeg") && !path.toString().lowercase(
//                            Locale.getDefault()
//                        ).contains("png") && !path.toString().lowercase(
//                            Locale.getDefault()
//                        ).contains("giff")
//                    ) {
//                        if (!path.toString().lowercase(Locale.getDefault())
//                                .contains(".apks") && !path.toString().lowercase(
//                                Locale.getDefault()
//                            ).contains(".apk")
//                        ) {
//                            var contentType: String? = null
//                            contentType = try {
//                                URL("file://" + path!!.path).openConnection().contentType
//                            } catch (e: IOException) {
//                                throw RuntimeException(e)
//                            }
//                            StrictMode.setVmPolicy(
//                                StrictMode.VmPolicy.Builder().build()
//                            )
//                            val fromFile = Uri.fromFile(path)
//                            intent!!.setAction("android.intent.action.VIEW")
//                            intent!!.setDataAndType(fromFile, contentType)
//                            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            startActivity(intent)
//                            if (!ResulfActivity.collections) {
//                                path = path!!.getParentFile()
//                            }
//                        }
//                        Ultil(this@ResulfActivity3).installAPK(path!!.path)
//                        startActivity(intent)
//                        if (!ResulfActivity.collections) {
//                        }
//                    }
//                    val file_DTO = File_DTO()
//                    file_DTO.name = path!!.getName()
//                    file_DTO.path = path!!.path
//                    file_DTO.size = Ultil(this@ResulfActivity3).bytesToHuman(path!!.length())
//                    file_DTO.date =
//                        Ultil(this@ResulfActivity3).getDate(path!!.lastModified())
//                    val intent2 = Intent(
//                        this@ResulfActivity3,
//                        Edit_MediaActivity::class.java
//                    )
//                    intent2.putExtra("path", "" + path!!.path)
//                    intent2.putExtra("file_dto", file_DTO)
//                    intent2.putExtra("hidefile", "internal")
//                    intent2.putExtra("pos", i)
//                    intent = intent2
//                    startActivity(intent)
//                    if (!ResulfActivity.collections) {
//                    }
//                }
//            } else if (!ResulfActivity.gridView) {
//                myRecyclerAdapter!!.toggleSelection(i)
//                if (MyRecyclerAdapter.getSelectedItemCount(myRecyclerAdapter) > 1) {
//                    actionMode.getMenu().findItem(R.id.rename).setEnabled(false)
//                    actionMode.getMenu().findItem(R.id.properties).setEnabled(false)
//                }
//                if (MyRecyclerAdapter.getSelectedItemCount(myRecyclerAdapter) === 1) {
//                    actionMode.getMenu().findItem(R.id.rename).setEnabled(true)
//                    actionMode.getMenu().findItem(R.id.properties).setEnabled(true)
//                }
//                actionMode.setTitle("${
//                    MyRecyclerAdapter.getSelectedItemCount(
//                        myRecyclerAdapter
//                    )} Selected")
//                if (MyRecyclerAdapter.getSelectedItemCount(myRecyclerAdapter) === 0) {
//                    myRecyclerAdapter.clearSelection()
//                    ResulfActivity.isSelection = false
//                    actionMode.finish()
//                }
//            } else {
//                adapter.toggleSelection(i)
//                if (adapter.getSelectedItemCount() > 1) {
//                    actionMode.getMenu().findItem(R.id.rename).setEnabled(false)
//                    actionMode.getMenu().findItem(R.id.properties).setEnabled(false)
//                }
//                if (adapter.getSelectedItemCount() == 1) {
//                    actionMode.getMenu().findItem(R.id.rename).setEnabled(true)
//                    actionMode.getMenu().findItem(R.id.properties).setEnabled(true)
//                }
//                val actionMode2: ActionMode = actionMode
//                actionMode2.setTitle(
//                    adapter.getSelectedItemCount().toString() + " Selected"
//                )
//                if (adapter.getSelectedItemCount() == 0) {
//                    adapter.clearSelection()
//                    ResulfActivity.isSelection = false
//                    actionMode.finish()
//                }
//            }
//            try {
//                checkEmpty(path!!.listFiles().size)
//            } catch (unused2: java.lang.Exception) {
//                checkEmpty(0)
//            }
//            textView_title.text = path.toString()
//        }
//
//        override fun onLongClick(view: View?, i: Int) {
//            if (ResulfActivity.isSelection) {
//                return
//            }
//            if (ResulfActivity.gridView) {
//                adapter.toggleSelection(i)
//                val gridAdapter: GridAdapter = this@ResulfActivity3.adapter
//                actionMode = this@ResulfActivity3.startActionMode(ActionModeCallBack(gridAdapter, this@ResulfActivity3, this@ResulfActivity3.data_manager, sortFlag))!!
//                actionMode.setTitle("1 Selected")
//                ResulfActivity.isSelection = true
//                return
//            }
//            myRecyclerAdapter.toggleSelection(i)
//            actionMode = this@ResulfActivity3.startActionMode(
//                ActionModeCallBack(this@ResulfActivity3.myRecyclerAdapter!!, this@ResulfActivity3, this@ResulfActivity3.data_manager, sortFlag)
//            )!!
//            actionMode.setTitle("1 Selected")
//            ResulfActivity.isSelection = true
//        }
//    }
//    fun checkEmpty(i: Int) {
//        if (i == 0) {
//            this.check_layout_empty.setVisibility(View.VISIBLE)
//            this.recyclerview_result.visibility = View.GONE
//            return
//        }
//        this.check_layout_empty.setVisibility(View.GONE)
//        this.recyclerview_result.visibility = View.VISIBLE
//    }
//
//    fun getExternalStorageDirectories(): Array<String> {
//        val arrayList = mutableListOf<String>()
//
//        if (Build.VERSION.SDK_INT >= 19) {
//            val externalFilesDirs = getExternalFilesDirs(null)
//            val lowerCase = Environment.getExternalStorageDirectory().absolutePath.toLowerCase()
//
//            arrayList.addAll(externalFilesDirs
//                .filterNotNull()
//                .mapNotNull { file ->
//                    val path = file.path.split("/Android")[0]
//                    if (!path.toLowerCase().startsWith(lowerCase) &&
//                        (Build.VERSION.SDK_INT >= 21 && Environment.isExternalStorageRemovable(file) ||
//                                Build.VERSION.SDK_INT < 21 && "mounted" == EnvironmentCompat.getStorageState(file))
//                    ) path else null
//                }
//            )
//        }
//
//        if (arrayList.isEmpty()) {
//            try {
//                val process = ProcessBuilder().command("mount | grep /dev/block/vold").redirectErrorStream(true).start()
//                process.waitFor()
//                val inputStream: InputStream = process.inputStream
//                val output = inputStream.bufferedReader().readText()
//                inputStream.close()
//
//                arrayList.addAll(output.split("\n").mapNotNull { line ->
//                    line.split(" ")[2].takeIf { it.isNotBlank() }
//                })
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            arrayList.removeIf { !it.toLowerCase().matches(Regex(".*[0-9a-f]{4}[-][0-9a-f]{4}")) }
//        } else {
//            arrayList.removeIf { !it.toLowerCase().contains("ext") && !it.toLowerCase().contains("sdcard") }
//        }
//
//        return arrayList.toTypedArray()
//    }
//    fun setExternalSD_root() {
//        var absolutePath: String?
//        if (Build.DEVICE.contains("samsung") || Build.MANUFACTURER.contains("samsung")) {
//            absolutePath = Environment.getExternalStorageDirectory().absolutePath
//            val file =
//                File(Environment.getExternalStorageDirectory().parent + "/extSdCard/myDirectory")
//            if (file.exists() && file.isDirectory) {
//                absolutePath = Environment.getExternalStorageDirectory().parent + "/extSdCard"
//            } else {
//                val file2 =
//                    File(Environment.getExternalStorageDirectory().absolutePath + "/external_sd/myDirectory")
//                if (file2.exists() && file2.isDirectory) {
//                    absolutePath =
//                        Environment.getExternalStorageDirectory().absolutePath + "/external_sd"
//                }
//            }
//        } else {
//            absolutePath = Environment.getExternalStorageDirectory().path
//        }
//        ResulfActivity.externalSD_root = File(absolutePath)
//    }
//    fun restrict_dialog() {
//        val customDeleteDialog = CustomDeleteDialog(this, object : DeleteCallback {
//            override fun update() {
//                Ultil(this@ResulfActivity3).intent_tree()
//            }
//        })
//        customDeleteDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
//        customDeleteDialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
//        customDeleteDialog.show()
//        customDeleteDialog.titile_retrict_access()
//    }
//    fun Eventback() {
////        if (this.check) {
////            if (this.name_key == "dowload") {
////                this.dowloadApdapter!!.setIscheck(false)
////                    this.dowloadApdapter!!.clearListchose()
////                this.dowloadApdapter!!.notifyDataSetChanged()
////            }
////            else if (this.name_key == "fav" || this.name_key == "recycle") {
////                this.favourite_adapter.setCheckview(false)
////                this.favourite_adapter.clearlistchose()
////                this.favourite_adapter.notifyDataSetChanged()
////            } else if (this.name_key == "imagehide" || this.name_key == "videohide") {
////                this.imageAdapter.setIscheck(false)
////                this.imageAdapter.cleanfilechose()
////                this.imageAdapter.notifyDataSetChanged()
////            } else if (this.name_key == "Instorage" || this.name_key == "SDcard") {
////                ResulfActivity.relativeLayout_head.setVisibility(View.VISIBLE)
////            } else {
////                this.musicAdapter.setCheck(false)
////                this.musicAdapter.clearlistchose()
////                this.musicAdapter.notifyDataSetChanged()
////            }
////            this.txt_count_file.setText("Select all ()")
////            updateview(false)
////            this.img_chose_all.setImageResource(R.drawable.esclip)
////            this.check = false
////            return
////        }
//        if (collections) {
//            collections = false
//            finish()
//        }
//        if (isSelection) {
//            contentPerent()
//        }
//        try {
//            if (sdCardmode && path?.getPath() == externalSD_root?.getPath()) {
//                sdCardmode = false
//                finish()
//            } else if (path?.getPath() == Environment.getExternalStorageDirectory().path) {
//                finish()
//            } else {
//                contentPerent()
//            }
//        } catch (unused: Exception) {
//            super.onBackPressed()
//        }
//    }
//
//    private fun contentPerent() {
//        path = path!!.getParent()?.let { File(it) }
//        this.textView_title.text = path.toString()
//        path!!.listFiles()?.let { checkEmpty(it.size) }
//        data_manager.setRecycler(path!!, sortFlag)
//        if (sdCardmode) {
//            documentFile = documentFile!!.parentFile
//        }
//        if (isPasteMode) {
//            actionMode.setTitle(path!!.getName())
//        }
//        if (ResulfActivity.gridView) {
//            adapter.notifyDataSetChanged()
//        } else {
//            myRecyclerAdapter.notifyDataSetChanged()
//        }
//    }
//
//    companion object{
//        var permadDocumentFile: DocumentFile? = null
//
//        fun getCurrentPath(): File? {
//            return path
//        }
//        var whichCollection = 0
//        const val CHANNEL_ID = "default"
//
//        var path: File? = null
//        var externalSD_root: File? = null
//        var sortFlag = 1
//        var gridView = false
//        var isSelection = false
//        var favourites = false
//        var isPasteMode = false
//        lateinit var actionMode: ActionMode
//        var collections = false
//        var sdCardmode = false
//        var documentFile: DocumentFile? = null
//
//    }
//
//
//}