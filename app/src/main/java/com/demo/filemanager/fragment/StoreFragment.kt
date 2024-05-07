package com.demo.filemanager.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.filemanager.Activity.DocumnetActivity
import com.demo.filemanager.Activity.LookActivity
import com.demo.filemanager.Activity.MainActivity
import com.demo.filemanager.Activity.ResulfActivity
import com.demo.filemanager.Activity.ResulfActivity2
import com.demo.filemanager.Activity.customview.CircularProgressIndicator
import com.demo.filemanager.DTO.File_DTO
import com.demo.filemanager.Iinterface.CallbackFav
import com.demo.filemanager.R
import com.demo.filemanager.Ultil.Data_Manager
import com.demo.filemanager.Ultil.Image_Ultil
import com.demo.filemanager.Ultil.MusicUltil
import com.demo.filemanager.Ultil.Ultil
import com.demo.filemanager.Ultil.VideoUtil
import com.demo.filemanager.adapter.Recent_Adapter
import com.demo.filemanager.database.FavoritSongs
import kotlinx.android.synthetic.main.fragment_store.circleIndicator
import kotlinx.android.synthetic.main.fragment_store.l_favourite
import kotlinx.android.synthetic.main.fragment_store.l_hide
import kotlinx.android.synthetic.main.fragment_store.l_recycle
import kotlinx.android.synthetic.main.fragment_store.linearLayout_Document
import kotlinx.android.synthetic.main.fragment_store.linearLayout_apk
import kotlinx.android.synthetic.main.fragment_store.linearLayout_app
import kotlinx.android.synthetic.main.fragment_store.linearLayout_dowload
import kotlinx.android.synthetic.main.fragment_store.linearLayout_img
import kotlinx.android.synthetic.main.fragment_store.linearLayout_music
import kotlinx.android.synthetic.main.fragment_store.linearLayout_newfiles
import kotlinx.android.synthetic.main.fragment_store.linearLayout_video
import kotlinx.android.synthetic.main.fragment_store.linearLayout_zip
import kotlinx.android.synthetic.main.fragment_store.recycleview_recent
import kotlinx.android.synthetic.main.fragment_store.spinner_storage
import kotlinx.android.synthetic.main.fragment_store.t_count_document
import kotlinx.android.synthetic.main.fragment_store.t_count_dowload
import kotlinx.android.synthetic.main.fragment_store.t_count_fav
import kotlinx.android.synthetic.main.fragment_store.t_countapp
import kotlinx.android.synthetic.main.fragment_store.t_countimg
import kotlinx.android.synthetic.main.fragment_store.t_countmusic
import kotlinx.android.synthetic.main.fragment_store.t_countrecycle
import kotlinx.android.synthetic.main.fragment_store.t_countzip
import kotlinx.android.synthetic.main.fragment_store.t_coutvidep
import kotlinx.android.synthetic.main.fragment_store.textView_space
import kotlinx.android.synthetic.main.fragment_store.textView_used
import kotlinx.android.synthetic.main.fragment_store.txt_countapkfile
import kotlinx.android.synthetic.main.fragment_store.txt_countnewfile
import kotlinx.android.synthetic.main.fragment_store.txt_viewall_recycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class StoreFragment : Fragment(), Recent_Adapter.OnClickItem {
    private lateinit var ultil: Ultil
    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null
    private var recent_adapter: Recent_Adapter? = null
    private var file_dtos_recent: ArrayList<File_DTO>? = null
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        arguments
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_store, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.e("MMMM", "1:$circleIndicator");

        Log.e("MMMM", "2:" + dpToPx(50.0f).toInt());
        circleIndicator?.setTextSize1(dpToPx(50.0f).toInt())
        circleIndicator!!.setAnimationEnabled(true)

        this.ultil = Ultil(requireContext())
        file_dtos_recent = ultil.getallfileRecent()
        Log.e("MMMM", "2:${file_dtos_recent!!.size}");
        CreateSpiner()
        setmemory(curent)
        spinner_storage!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, j: Long) {
                curent = i
                val unused = curent
                setmemory(i)
            }
        }
        launcher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == -1) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
            }
        }
        linearLayout_music!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity2::class.java)
            intent.putExtra("nameitem", "music")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_img!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity2::class.java)
            intent.putExtra("nameitem", "images")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_video!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity2::class.java)
            intent.putExtra("nameitem", "video")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_Document!!.setOnClickListener {
            requireActivity()!!.startActivity(
                Intent(
                    context,
                    DocumnetActivity::class.java
                )
            )
        }

        externalMemoryAvailable(requireActivity())
        linearLayout_app!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "app")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_dowload!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "dowload")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_zip!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "zip")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_apk!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "apk")
            requireActivity()!!.startActivity(intent)
        }
        linearLayout_newfiles!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "newfile")
            requireActivity()!!.startActivity(intent)
        }
        l_favourite!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "fav")
            requireActivity()!!.startActivity(intent)
        }
        l_recycle!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "recycle")
            requireActivity()!!.startActivity(intent)
        }
        l_hide!!.setOnClickListener {
            requireActivity()!!.startActivity(
                Intent(
                    context,
                    LookActivity::class.java
                )
            )
        }
        txt_viewall_recycle!!.setOnClickListener {
            val intent = Intent(context, ResulfActivity::class.java)
            intent.putExtra("nameitem", "recent")
            requireActivity()!!.startActivity(intent)
        }
        RecentView()
        CallbackFav.instance?.setListener(object : CallbackFav.OnCustomStateListener {
            override fun stateChanged() {

            }
        })


    }


    private fun RecentView() {
        Ultil(requireContext()).checkrecent_fileExist()
        recycleview_recent.setLayoutManager(
            LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                true
            )
        )
        recent_adapter = Recent_Adapter(requireContext(), this@StoreFragment)
        recent_adapter!!.setList(file_dtos_recent)
//        recent_adapter.setAdapterAnimation(ScaleInAnimation())
        recycleview_recent.setAdapter(recent_adapter)
    }

    //
    @SuppressLint("ResourceType")
    private fun CreateSpiner() {
        val strArr: Array<String?> = if (externalMemoryAvailable(activity)) {
            arrayOf("Internal", "External Storage")
        } else {
            arrayOf("Internal")
        }
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity(), R.layout.customspiner, strArr
        )
        arrayAdapter.setDropDownViewResource(17367049)
        spinner_storage!!.adapter = arrayAdapter
        spinner_storage!!.setSelection(0)
    }

    fun setmemory(i: Int) {
        if (i == 0) {
            textView_space!!.text =
                bytesToHuman(totalInternalMemorySize - availableInternalMemorySize)
            textView_used!!.text =
                bytesToHuman(totalInternalMemorySize)
            circleIndicator!!.setProgress(
                (totalInternalMemorySize - availableInternalMemorySize).toDouble(),
                totalInternalMemorySize.toDouble()
            )
            circleIndicator!!.setAnimationEnabled(true)
            circleIndicator!!.setProgressTextAdapter(TIME_TEXT_ADAPTER)
        } else if (i == 1) {
            textView_space!!.text =
                bytesToHuman(totalspace - totalfree)
            textView_used!!.text = bytesToHuman(totalspace)
            val circleIndicator = circleIndicator
            val j = totalspace
            circleIndicator!!.setProgress((j - totalfree).toDouble(), j.toDouble())
            this.circleIndicator!!.setAnimationEnabled(true)
            this.circleIndicator!!.setProgressTextAdapter(TIME_TEXT_ADAPTER)
        }
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            updateRecentData(requireContext())
        }
//        Updaterecent().execute(*arrayOfNulls<Void>(0))
    }

    suspend fun updateRecentData(context: Context) {
        var data_manager: Data_Manager? = Data_Manager(requireContext())
        var tt_apk_ = 0
        var tt_app = 0
        var tt_doc = 0
        var tt_dowload = 0
        var tt_image = 0
        var tt_music = 0
        var tt_re = 0
        var tt_video = 0
        var size = 0
        CoroutineScope(Dispatchers.IO).launch {
            tt_image = Image_Ultil(requireActivity()).allImage().size
            tt_video = VideoUtil(requireActivity()).getallvideo().size
            tt_music = MusicUltil(requireActivity()).allsong().size
            data_manager?.setDocs(requireContext())
            tt_doc = data_manager!!.getfilepdf()!!.size + data_manager!!.getfidocx().size + data_manager!!.getfileppt().size + data_manager!!.getfiletxt().size + data_manager!!.getfilexls().size
            tt_app = data_manager!!.readAllAppssss(requireContext()).size
            tt_apk_ = data_manager!!.getallapp().size
            tt_dowload = data_manager!!.filesDowload().size
            tt_re = setRecylerView().size
            tt_zip = data_manager!!.getzipwithMediastore().size
            if (recent_adapter != null) {
                file_dtos_recent = ultil.getallfileRecent()
            }
            val favoritSongs = FavoritSongs(requireContext())
            favoritSongs.open()
            size = favoritSongs.getAllRows()!!.size
            favoritSongs.close()

            withContext(Dispatchers.Main){
                Log.e(
                    "TAG",
                    "doInBackground:image:$tt_image ,tt_video:$tt_video ,tt_music:$tt_music , "
                )
                t_countimg!!.text = "" + tt_image
                t_coutvidep!!.text = "" + tt_video
                t_countmusic!!.text = "" + tt_music
                t_count_document!!.text = "" + tt_doc
                t_countzip!!.text = "" + tt_zip
                t_countapp!!.text = "" + tt_app
                txt_countapkfile!!.text = "" + tt_apk_
                t_count_dowload!!.text = "" + tt_dowload
                t_count_fav!!.text = "" + size
                t_countrecycle!!.text = "" + tt_re
                txt_countnewfile!!.text = "" + data_manager!!.getallfileindecive().size
                Ultil(requireContext()).checkrecent_fileExist()
                recent_adapter?.setList(file_dtos_recent)
                recent_adapter?.notifyDataSetChanged()
            }
        }
    }
    companion object {
        private val TIME_TEXT_ADAPTER: CircularProgressIndicator.ProgressTextAdapter =
            object : CircularProgressIndicator.ProgressTextAdapter {
                override fun formatText(d: Double): String? {
                    val j: Long
                    val j2: Long
                    if (curent != 0) {
                        if (curent == 1) {
                            j2 = totalspace
                        } else {
                            j = 0
                            return "$j%"
                        }
                    } else {
                        j2 = totalInternalMemorySize
                    }
                    j = (d / j2 * 100.0).toLong()
                    return "$j%"
                }
            }
        private var tt_zip = 0
        private var curent = 0
        private var totalfree: Long = 0
        private var totalspace: Long = 0
        fun dpToPx(f: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                f,
                Resources.getSystem().displayMetrics
            )
        }

        fun externalMemoryAvailable(activity: Activity?): Boolean {
            val externalFilesDirs = ContextCompat.getExternalFilesDirs(
                activity!!, null
            )
            if (externalFilesDirs.size > 1 && externalFilesDirs[0] != null && externalFilesDirs[1] != null) {
                totalspace = externalFilesDirs[1]!!.totalSpace
                totalfree = externalFilesDirs[1]!!.freeSpace
                return true
            }
            Log.d("TAG", "externalMemoryAvailable: fal")
            return false
        }

        val availableInternalMemorySize: Long
            get() {
                val statFs = StatFs(Environment.getExternalStorageDirectory().path)
                return java.lang.Long.valueOf(statFs.availableBlocksLong * statFs.blockSizeLong)
            }
        val totalInternalMemorySize: Long
            get() {
                val statFs = StatFs(Environment.getDataDirectory().path)
                return java.lang.Long.valueOf(statFs.blockCountLong * statFs.blockSizeLong)
            }
        fun bytesToHuman(size: Long): String? {
            if (size <= 0) {
                return "0 B"
            }
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
        }
    }

    override fun itemclickFr(i: Int) {
        //
    }

    override fun menuclickFr(i: Int) {
        //
    }

    fun setRecylerView(): java.util.ArrayList<File_DTO> {
        val arrayList = java.util.ArrayList<File_DTO>()
        val file: File = File(MainActivity.getStore(requireContext()) + "/Bin")
        if (file.isDirectory) {
            val listFiles = file.listFiles()
            for (i in listFiles.indices) {
                val absolutePath = listFiles[i].absolutePath
                listFiles[i].lastModified()
                listFiles[i].name.substring(listFiles[i].name.lastIndexOf("%") + 1)
                listFiles[i].length()
                val file_DTO = File_DTO()
                file_DTO.path = absolutePath
                arrayList.add(file_DTO)
            }
        }
        return arrayList
    }

}
