package com.demo.filemanager.Activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.DocsFetchCallback
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.Image_Ultil
import com.demo.filemanager.Ultil.MusicUltil
import com.demo.filemanager.Ultil.VideoUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ResulfActivity : AppCompatActivity() {


    //
////    private val imageAdapter: ImageAdapter? = null
//    private val favourite_adapter: Favourite_Adapter? = null
//
    lateinit var video_ultil: VideoUtil
    lateinit var musicUltil: MusicUltil
    lateinit var image_ultil: Image_Ultil
    lateinit var file_dtos_image: ArrayList<File_DTO>
    lateinit var file_dtos_video: ArrayList<File_DTO>
//
    lateinit var data_manager: Data_Manager
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
//    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

//    private val musicAdapter: MusicAdapter? = null

//    private val job = Job()
//    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_resulf)


        Contructer()
        data()

    }

    fun Contructer() {

        CoroutineScope(Dispatchers.Main).launch {
            musicUltil = MusicUltil(this@ResulfActivity)
            image_ultil = Image_Ultil(this@ResulfActivity)
            video_ultil = VideoUtil(this@ResulfActivity)
            data_manager = Data_Manager(this@ResulfActivity)

            data_manager.setDocs(applicationContext, object : DocsFetchCallback<List<File_DTO>> {
                override fun onSuccess(data: List<File_DTO>) {
                    Log.e("TAG", "Contructer onSuccess: "+data.size)
                }
                override fun onFailure(it: Exception) {
                    // Handle failure
                    Log.e("TAG", "Contructer onFailure: "+it.message)
                }
            })


        }
    }
    private fun data() {
        CoroutineScope(Dispatchers.Main).launch {
            file_dtos_image = image_ultil.getallalbumImage()
            file_dtos_video = video_ultil.updateAllVidepAlbum()

            Log.e("TAG", "Contructer file_dtos_image: "+file_dtos_image.size)
            Log.e("TAG", "Contructer file_dtos_video: "+file_dtos_video.size)
        }
    }






//
//
//
//
//        val popupMenu = PopupMenu(
//            ContextThemeWrapper(
//                this,
//                R.style.AppTheme
//            ), img_menu
//        )
//        popupMenu.menuInflater.inflate(R.menu.menu_filestorage, popupMenu.menu)
//        this.img_back.setOnClickListener { Eventback() }
//
//        launcher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
//            ActivityResultContracts.StartIntentSenderForResult()
//        ) { activityResult ->
//            if (activityResult.resultCode == -1) {
//                Toast.makeText(this@ResulfActivity, "Delete", Toast.LENGTH_SHORT).show()
//            }
//        }
//
////        val handleLooper: HandleLooper = object : HandleLooper {
////            override fun update() {
////                musicAdapter.setList(data_manager.readAllAppssss(this@ResulfActivity))
////                musicAdapter.setDrawables(data_manager.drawables())
////                musicAdapter.notifyDataSetChanged()
////            }
////        }
//
//        this.activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult()
//        ) { activityResult ->
//            if (activityResult.resultCode == -1) {
//                Toast.makeText(this@ResulfActivity, "can't uninstall", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
////                BackgroundTask(this@ResulfActivity).Handleloop(handleLooper)
//            }
//        }
//
//        Contructer()
//        data()
//
//
//    }
//    fun Contructer() {
//        this.musicUltil = MusicUltil(this)
//        this.image_ultil = Image_Ultil(this)
//        this.video_ultil = Video_Ultil(this)
//         data_manager = Data_Manager(this)
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
//
//
//    fun Eventback() {
//    }

}