package com.demo.filemanager.Ultil

import com.demo.filemanager.BuildConfig

class FileUltils {
    companion object{
        val AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
        private val DEBUG = false
        val DOCUMENTS_DIR = "documents"
        val HIDDEN_PREFIX = "."
        val TAG = "FileUtils"
    }

}