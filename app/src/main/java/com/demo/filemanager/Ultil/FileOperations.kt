package com.demo.filemanager.Ultil

import java.io.File


class FileOperations {
//    var file: File? = null
//    fun pasteDoc(source: File, destination_Path: File?, context: Context) {
//        if (source.isFile) {
//            copy(source, destination_Path, context)
//        } else {
//            ActionModeCallBack.getDocumentFile(destination_Path).createDirectory(source.name)
//            val createdDir = File(destination_Path, source.name)
//            val content = source.listFiles()
//            for (i in content.indices) {
//                try {
//                    if (content[i].isFile) copy(content[i], createdDir, context) else pasteDoc(
//                        content[i], createdDir, context
//                    )
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    fun copy(copy: File, directory: File?, con: Context): Boolean {
//        var inStream: FileInputStream? = null
//        var outStream: OutputStream? = null
//        val dir: DocumentFile = ActionModeCallBack.getDocumentFile(directory)
//        val mime = mime(copy.toURI().toString())
//        val copy1: DocumentFile = dir.createFile(mime, copy.name)
//        try {
//            inStream = FileInputStream(copy)
//            outStream = con.contentResolver.openOutputStream(copy1.getUri())
//            val buffer = ByteArray(inStream.available())
//            var bytesRead: Int
//            while (inStream.read(buffer).also { bytesRead = it } != -1) {
//                outStream!!.write(buffer, 0, bytesRead)
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            try {
//                inStream!!.close()
//                outStream!!.close()
//                return true
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return false
//    }
//
    companion object {
        @Throws(Exception::class)
        fun delete(file: File) {
            if (file.isFile) {
                val able = file.delete()
                if (!able) throw Exception()
            } else {
                val files = file.listFiles()
                if (files.size == 0) {
                    val able = file.delete()
                    if (!able) throw Exception()
                }
                for (file1 in files) {
                    if (file1.isDirectory) delete(file1) else {
                        file1.delete()
                    }
                }
            }
        }
//
//        fun mime(URI: String?): String? {
//            var type: String? = null
//            val extention = MimeTypeMap.getFileExtensionFromUrl(URI)
//            if (extention != null) {
//                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
//            }
//            return type
//        }
//
//        @Throws(IOException::class)
//        fun copyFolder(src: File, dest: File) {
//            var dest = dest
//            if (src.isDirectory) {
//                dest = File(dest, src.name)
//                if (!dest.exists()) {
//                    dest.mkdirs()
//                }
//                val files = src.list()
//                for (file in files) {
//                    val srcFile = File(src, file)
//                    copyFolder(srcFile, dest)
//                }
//            } else {
//                val `in`: InputStream = FileInputStream(src)
//                val out: OutputStream = FileOutputStream(File(dest, src.name))
//                val buffer = ByteArray(1024)
//                var length: Int
//                while (`in`.read(buffer).also { length = it } > 0) {
//                    out.write(buffer, 0, length)
//                }
//                `in`.close()
//                out.close()
//            }
//        }
    }
}
