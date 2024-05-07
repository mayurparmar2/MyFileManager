package com.demo.filemanager.Iinterface

import com.demo.filemanager.DTO.File_DTO

interface ResultListener {
    fun onResultReceived(result: ArrayList<File_DTO>)
}