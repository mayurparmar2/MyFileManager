package com.demo.filemanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.demo.filemanager.DTO.File_DTO

class RecentAdd(context: Context) {
    private val SQL_DELETE_ENTRIES: String
    var context: Context
    private var db: SQLiteDatabase? = null
    private val myDBHelper: ReaderDB
    private val TEXT_TYPE = " TEXT"
    private val COMMA_SEP = ","
    private val TABLE_NAME = "recent"
    private val COLUMN_NAME_ID = "id"
    private val TITLE = "title"
    private val PATH = "path"
    private val SIZE = "size"
    private val ALBUM = "album"
    private val DATE = "date"
    private val NAMETYPE = "nametype"
    private val DATEADED = "dateadded"
    private val ALL_KEYS = arrayOf("id", "title", "path", "album", "size", "date", "nametype", "dateadded")
    private val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME ($COLUMN_NAME_ID$COMMA_SEP$TITLE$TEXT_TYPE$COMMA_SEP$PATH$TEXT_TYPE$COMMA_SEP$SIZE$TEXT_TYPE$COMMA_SEP$ALBUM$TEXT_TYPE$COMMA_SEP$DATE$TEXT_TYPE$COMMA_SEP$NAMETYPE$TEXT_TYPE$COMMA_SEP$DATEADED$TEXT_TYPE);"

    init {
        val sb = StringBuilder()
        sb.append("DROP TABLE IF EXISTS ")
        sb.append(TABLE_NAME)
        SQL_DELETE_ENTRIES = sb.toString()
        this.context = context
        myDBHelper = ReaderDB(this.context)
    }

    fun open(): RecentAdd {
        db = myDBHelper.writableDatabase
        return this
    }

    fun close(): RecentAdd {
        myDBHelper.close()
        return this
    }

    fun addRow(file_DTO: File_DTO): Long {
        val path: String? = file_DTO.path
        val contentValues = ContentValues()
        contentValues.put(TITLE, file_DTO.name)
        contentValues.put(PATH, file_DTO.path)
        contentValues.put(SIZE, file_DTO.size)
        if (path != null) {
            if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac") || path.endsWith(
                    ".wma"
                ) || path.endsWith(".m4a")
            ) {
                contentValues.put(ALBUM, file_DTO.abumid)
            } else {
                contentValues.put(ALBUM, file_DTO.abumname)
            }
        }
        contentValues.put(COLUMN_NAME_ID, file_DTO.id)
        contentValues.put(DATE, file_DTO.date)
        contentValues.put(NAMETYPE, file_DTO.abumid)
        contentValues.put(DATEADED, file_DTO.lastmodified)
        return db!!.insert(TABLE_NAME, "NULL", contentValues)
    }

    fun deleteRow(j: Long): Boolean {
        return try {
            val sQLiteDatabase = db
            sQLiteDatabase!!.execSQL("DELETE FROM fav WHERE id ='$j'")
            true
        } catch (unused: Exception) {
            false
        }
    }

    fun deleteRowall(): Boolean {
        return db!!.delete(TABLE_NAME, null, null) != 0
    }

    fun deleteRowByPath(str: String): Boolean {
        return try {
            Log.d("TAG!!", "deleteRow: $str")
            val sQLiteDatabase = db
            val str2 = TABLE_NAME
            val sb = StringBuilder()
            sb.append(PATH)
            sb.append("=?")
            sQLiteDatabase!!.delete(str2, sb.toString(), arrayOf(str)) != 0
        } catch (e: Exception) {
            Log.d("TAG!!", "deleteRowByPath: " + e.message)
            false
        }
    }

    fun deleteAll(): Boolean {
        return try {
            val allRowsCursor = allRowsCursor
            val columnIndexOrThrow = allRowsCursor!!.getColumnIndexOrThrow(COLUMN_NAME_ID).toLong()
            if (allRowsCursor.moveToFirst()) {
                do {
                    deleteRow(allRowsCursor.getLong(columnIndexOrThrow.toInt()))
                } while (allRowsCursor.moveToNext())
                allRowsCursor.close()
                return true
            }
            allRowsCursor.close()
            true
        } catch (unused: Exception) {
            false
        }
    }

    fun getAllRows(): java.util.ArrayList<File_DTO>? {
        val sQLiteDatabase = db
        val str = TABLE_NAME
        val strArr = ALL_KEYS
        val query = sQLiteDatabase!!.query(str, strArr, null, null, null, null, "$DATE ASC")
        val arrayList = java.util.ArrayList<File_DTO>()
        if (query.moveToFirst()) {
            do {
                val file_DTO = File_DTO()
                file_DTO.name = query.getString(1)
                file_DTO.path = query.getString(2)
                file_DTO.size = query.getString(4)
                file_DTO.abumname = query.getString(4)
                file_DTO.id = query.getString(0)
                file_DTO.date = query.getString(5)
                file_DTO.artistid = query.getString(6)
                file_DTO.lastmodified = query.getString(7)
                arrayList.add(file_DTO)
            } while (query.moveToNext())
            query.close()
            return arrayList
        }
        query.close()
        return arrayList
    }

    private val allRowsCursor: Cursor?
        private get() {
            val query = db!!.query(true, TABLE_NAME, ALL_KEYS, null, null, null, null, null, null)
            query?.moveToFirst()
            return query
        }

    private inner class ReaderDB internal constructor(context: Context?) :
        SQLiteOpenHelper(context, "Recent.db", null as SQLiteDatabase.CursorFactory?, 1) {
        override fun onCreate(sQLiteDatabase: SQLiteDatabase) {
            sQLiteDatabase.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(sQLiteDatabase: SQLiteDatabase, i: Int, i2: Int) {
            sQLiteDatabase.execSQL(SQL_DELETE_ENTRIES)
            onCreate(sQLiteDatabase)
        }

        override fun onDowngrade(sQLiteDatabase: SQLiteDatabase, i: Int, i2: Int) {
            onUpgrade(sQLiteDatabase, i, i2)
        }
    }
}
