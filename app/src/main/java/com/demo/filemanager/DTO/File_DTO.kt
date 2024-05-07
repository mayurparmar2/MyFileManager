package com.demo.filemanager.DTO

import java.io.Serializable

class File_DTO : Serializable {
    var abumid: String? = null
    var abumname: String? = null
    var artistid: String? = null
    var artistname: String? = null
    var date: String? = null
    var duration: String? = null
    var file_dtos: ArrayList<File_DTO>? = null
    var id: String? = null
    var lastmodified: String? = null
    var minetype: String? = null
    var name: String? = null
    var packagename: String? = null
    var path: String = ""
    var realpath: String? = null
    var size: String? = null
    var title: String? = null
    var totalitem = 0
    var volumname: String? = null
}
