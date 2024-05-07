package com.demo.filemanager.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.demo.filemanager.Activity.ResulfActivity
import com.demo.filemanager.R
import kotlinx.android.synthetic.main.fragment_files.l_internal
import kotlinx.android.synthetic.main.fragment_files.l_sdcard


class FilesFragment : Fragment() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        arguments
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_files, viewGroup, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!externalMemoryAvailable()) {
            l_internal!!.visibility = View.GONE
        } else {
            l_internal!!.visibility = View.VISIBLE
        }
        l_sdcard!!.setOnClickListener {
            val intent = Intent(
                this@FilesFragment.context,
                ResulfActivity::class.java
            )
            intent.putExtra("nameitem", "Instorage")
            requireActivity().startActivity(intent)
        }
        l_internal!!.setOnClickListener {
            val intent = Intent(
                this@FilesFragment.context,
                ResulfActivity::class.java
            )
            intent.putExtra("nameitem", "SDcard")
            requireActivity().startActivity(intent)
        }
    }
    fun externalMemoryAvailable(): Boolean {
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(
            requireActivity(), null
        )
        if (externalFilesDirs.size <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) {
            Log.d("TAG", "externalMemoryAvailable: fal")
            return false
        }
        return true
    }
    companion object {
        const val APP_STORAGE_ACCESS_REQUEST_CODE = 501
    }
}

