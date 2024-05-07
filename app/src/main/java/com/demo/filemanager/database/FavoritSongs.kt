package com.demo.filemanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.demo.filemanager.DTO.File_DTO


class FavoritSongs(context: Context) {
    private val TABLE_NAME = "fav"
    private val COLUMN_NAME_ID = "id"
    private val TITLE = "title"
    private val PATH = "path"
    private val SIZE = "size"
    private val ALBUM = "album"
    private val DATE = "date"
    private val NAMETYPE = "nametype"
    private val ALL_KEYS = arrayOf("id", "title", "path", "album", "size", "date", "nametype")

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE $TABLE_NAME ($COLUMN_NAME_ID INTEGER PRIMARY KEY, $TITLE TEXT, $PATH TEXT, $SIZE TEXT, $ALBUM TEXT, $DATE TEXT, $NAMETYPE TEXT);"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    private var db: SQLiteDatabase? = null
    private val myDBHelper: ReaderDB by lazy { ReaderDB(context) }

    init {
        db = myDBHelper.writableDatabase
    }

    fun open(): FavoritSongs {
        db = myDBHelper.writableDatabase
        return this
    }

    fun close(): FavoritSongs {
        myDBHelper.close()
        return this
    }

    fun addRow(file_DTO: File_DTO): Long {
        val contentValues = ContentValues().apply {
            put(TITLE, file_DTO.name)
            put(PATH, file_DTO.path)
            put(SIZE, file_DTO.size)
            put(ALBUM, if (file_DTO.path?.endsWith(".mp3") == true) file_DTO.abumid else file_DTO.abumname)
            put(COLUMN_NAME_ID, file_DTO.id)
            put(DATE, file_DTO.date)
            put(NAMETYPE, file_DTO.abumid)
        }
        return db?.insert(TABLE_NAME, null, contentValues) ?: -1
    }

//    val allRows: ArrayList<File_DTO>
//        get() {
//            val query = db?.query(
//                TABLE_NAME,
//                ALL_KEYS,
//                null,
//                null,
//                null,
//                null,
//                "$COLUMN_NAME_ID DESC"
//            )
//            val arrayList = ArrayList<File_DTO>()
//
//            query?.use {
//                if (it.moveToFirst()) {
//                    do {
//                        val file_DTO = File_DTO().apply {
//                            name = it.getString(1)
//                            path = it.getString(2)
//                            size = it.getString(4)
//                            abumname = it.getString(4)
//                            id = it.getString(0)
//                            date = it.getString(5)
//                            artistid = it.getString(6)
//                        }
//                        arrayList.add(file_DTO)
//                    } while (it.moveToNext())
//                }
//            }
//            query?.close()
//            return arrayList
//        }

//    fun getRow(id: Long): File_DTO {
//        val query = db?.query(
//            TABLE_NAME,
//            ALL_KEYS,
//            "$COLUMN_NAME_ID=$id",
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        var file_DTO = File_DTO()
//
//        query?.use {
//            if (it.moveToFirst()) {
//                file_DTO = File_DTO().apply {
//                    title = it.getString(2)
//                    path = it.getString(3)
//                    artistid = it.getString(7)
//                    abumname = it.getString(4)
//                    size = it.getString(5)
//                    date = it.getString(6)
//                    this.id = it.getString(1)
//                }
//            }
//        }
//        query?.close()
//        return file_DTO
//    }

    fun deleteRow(id: Long): Boolean {
        return try {
            db?.execSQL("DELETE FROM $TABLE_NAME WHERE $COLUMN_NAME_ID='$id'")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun deleteRowall(): Boolean {
        return db?.delete(TABLE_NAME, null, null) != 0
    }

    fun deleteRowByPath(path: String): Boolean {
        return try {
            db?.delete(TABLE_NAME, "$PATH=?", arrayOf(path)) != 0
        } catch (e: Exception) {
            false
        }
    }

    fun deleteAll(): Boolean {
        return try {
            allRowsCursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(COLUMN_NAME_ID).toLong()
                if (it.moveToFirst()) {
                    do {
                        deleteRow(it.getLong(columnIndex.toInt()))
                    } while (it.moveToNext())
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private val allRowsCursor: Cursor?
        get() {
            return db?.query(true, TABLE_NAME, ALL_KEYS, null, null, null, null, null, null)
        }

    inner class ReaderDB(context: Context) : SQLiteOpenHelper(context, "Favs.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
    }

    fun getAllRows(): ArrayList<File_DTO>? {
        val query = db?.query(
            TABLE_NAME,
            ALL_KEYS,
            null,
            null,
            null,
            null,
            "$COLUMN_NAME_ID DESC"
        )
        val arrayList = ArrayList<File_DTO>()

        query?.use {
            if (it.moveToFirst()) {
                do {
                    val file_DTO = File_DTO().apply {
                        name = it.getString(1)
                        path = it.getString(2)
                        size = it.getString(4)
                        abumname = it.getString(4)
                        id = it.getString(0)
                        date = it.getString(5)
                        artistid = it.getString(6)
                    }
                    arrayList.add(file_DTO)
                } while (it.moveToNext())
            }
        }
        query?.close()
        return arrayList
    }


    // Renamed property to avoid clash
    val allRowsList: ArrayList<File_DTO>
        get() {
            val query = db?.query(
                TABLE_NAME,
                ALL_KEYS,
                null,
                null,
                null,
                null,
                "$COLUMN_NAME_ID DESC"
            )
            val arrayList = ArrayList<File_DTO>()

            query?.use {
                if (it.moveToFirst()) {
                    do {
                        val file_DTO = File_DTO().apply {
                            name = it.getString(1)
                            path = it.getString(2)
                            size = it.getString(4)
                            abumname = it.getString(4)
                            id = it.getString(0)
                            date = it.getString(5)
                            artistid = it.getString(6)
                        }
                        arrayList.add(file_DTO)
                    } while (it.moveToNext())
                }
            }
            query?.close()
            return arrayList
        }
}

//
//class FavoritSongs(context: Context) {
//    private val SQL_DELETE_ENTRIES: String
//    var context: Context
//    private var db: SQLiteDatabase? = null
//    private val myDBHelper: ReaderDB
//    private val TEXT_TYPE = " TEXT"
//    private val COMMA_SEP = ","
//    private val TABLE_NAME = "fav"
//    private val COLUMN_NAME_ID = "id"
//    private val TITLE = "title"
//    private val PATH = "path"
//    private val SIZE = "size"
//    private val ALBUM = "album"
//    private val DATE = "date"
//    private val NAMETYPE = "nametype"
//    private val ALL_KEYS = arrayOf("id", "title", "path", "album", "size", "date", "nametype")
//    private val SQL_CREATE_ENTRIES =
//        "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME_ID + COMMA_SEP + TITLE + TEXT_TYPE + COMMA_SEP + PATH + TEXT_TYPE + COMMA_SEP + SIZE + TEXT_TYPE + COMMA_SEP + ALBUM + TEXT_TYPE + COMMA_SEP + DATE + TEXT_TYPE + COMMA_SEP + NAMETYPE + TEXT_TYPE + ");"
//
//    init {
//        val sb = StringBuilder()
//        sb.append("DROP TABLE IF EXISTS ")
//        sb.append(TABLE_NAME)
//        SQL_DELETE_ENTRIES = sb.toString()
//        this.context = context
//        myDBHelper = ReaderDB(this.context)
//    }
//
//    fun open(): FavoritSongs {
//        db = myDBHelper.writableDatabase
//        return this
//    }
//
//    fun close(): FavoritSongs {
//        myDBHelper.close()
//        return this
//    }
//
//    fun addRow(file_DTO: File_DTO): Long {
//        val path = file_DTO.path
//        val contentValues = ContentValues()
//        contentValues.put(TITLE, file_DTO.name)
//        contentValues.put(PATH, file_DTO.path)
//        contentValues.put(SIZE, file_DTO.size)
//        if (path!!.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac") || path.endsWith(
//                ".wma"
//            ) || path.endsWith(".m4a")
//        ) {
//            contentValues.put(ALBUM, file_DTO.abumid)
//        } else {
//            contentValues.put(ALBUM, file_DTO.abumname)
//        }
//        contentValues.put(COLUMN_NAME_ID, file_DTO.id)
//        contentValues.put(DATE, file_DTO.date)
//        contentValues.put(NAMETYPE, file_DTO.abumid)
//        return db!!.insert(TABLE_NAME, "NULL", contentValues)
//    }
//
//    val allRows: ArrayList<File_DTO>
//        get() {
//            val sQLiteDatabase = db
//            val str = TABLE_NAME
//            val strArr = ALL_KEYS
//            Log.e("MTAG", "db" + db)
//            Log.e("MTAG", "TABLE_NAME" + TABLE_NAME)
//            Log.e("MTAG", "ALL_KEYS" + ALL_KEYS)
//            Log.e("MTAG", "ALL_KEYS" + ALL_KEYS)
//            val query = sQLiteDatabase!!.query(
//                str,
//                strArr,
//                null,
//                null,
//                null,
//                null,
//                COLUMN_NAME_ID + " DESC"
//            )
//            val arrayList = ArrayList<File_DTO>()
//            if (query.moveToFirst()) {
//                do {
//                    val file_DTO = File_DTO()
//                    file_DTO.name = query.getString(1)
//                    file_DTO.path = query.getString(2)
//                    file_DTO.size = query.getString(4)
//                    file_DTO.abumname = query.getString(4)
//                    file_DTO.id = query.getString(0)
//                    file_DTO.date = query.getString(5)
//                    file_DTO.artistid = query.getString(6)
//                    arrayList.add(file_DTO)
//                } while (query.moveToNext())
//                query.close()
//                return arrayList
//            }
//            query.close()
//            return arrayList
//        }
//
//    fun getRow(j: Long): File_DTO {
//        val str: String?
//        val str2: String?
//        val str3: String?
//        val str4: String?
//        val str5: String?
//        val str6: String?
//        val query = db!!.query(
//            true,
//            TABLE_NAME,
//            ALL_KEYS,
//            COLUMN_NAME_ID + "=" + j,
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        var str7: String? = null
//        if (query != null) {
//            query.moveToFirst()
//            str7 = query.getString(2)
//            str = query.getString(3)
//            str2 = query.getString(4)
//            str3 = query.getString(5)
//            str4 = query.getString(1)
//            str5 = query.getString(6)
//            str6 = query.getString(7)
//        } else {
//            str = null
//            str2 = null
//            str3 = null
//            str4 = null
//            str5 = null
//            str6 = null
//        }
//        query!!.close()
//        val file_DTO = File_DTO()
//        file_DTO.title = str7
//        file_DTO.path = str
//        file_DTO.artistid = str6
//        file_DTO.abumname = str3
//        file_DTO.size = str2
//        file_DTO.date = str5
//        file_DTO.id = str4
//        return file_DTO
//    }
//
//    fun deleteRow(j: Long): Boolean {
//        return try {
//            val sQLiteDatabase = db
//            sQLiteDatabase!!.execSQL("DELETE FROM fav WHERE id ='$j'")
//            true
//        } catch (unused: Exception) {
//            false
//        }
//    }
//
//    fun deleteRowall(): Boolean {
//        return db!!.delete(TABLE_NAME, null, null) != 0
//    }
//
//    fun deleteRowByPath(str: String): Boolean {
//        return try {
//            Log.d("TAG!!", "deleteRow: $str")
//            val sQLiteDatabase = db
//            val str2 = TABLE_NAME
//            val sb = StringBuilder()
//            sb.append(PATH)
//            sb.append("=?")
//            sQLiteDatabase!!.delete(str2, sb.toString(), arrayOf(str)) != 0
//        } catch (e: Exception) {
//            Log.d("TAG!!", "deleteRowByPath: " + e.message)
//            false
//        }
//    }
//
//    fun deleteAll(): Boolean {
//        return try {
//            val allRowsCursor = allRowsCursor
//            val columnIndexOrThrow = allRowsCursor!!.getColumnIndexOrThrow(COLUMN_NAME_ID).toLong()
//            if (allRowsCursor.moveToFirst()) {
//                do {
//                    deleteRow(allRowsCursor.getLong(columnIndexOrThrow.toInt()))
//                } while (allRowsCursor.moveToNext())
//                allRowsCursor.close()
//                return true
//            }
//            allRowsCursor.close()
//            true
//        } catch (unused: Exception) {
//            false
//        }
//    }
//
//    private val allRowsCursor: Cursor?
//        private get() {
//            val query = db!!.query(true, TABLE_NAME, ALL_KEYS, null, null, null, null, null, null)
//            query?.moveToFirst()
//            return query
//        }
//
//    inner class ReaderDB internal constructor(context: Context?) :
//        SQLiteOpenHelper(context, "Favs.db", null as SQLiteDatabase.CursorFactory?, 1) {
//        override fun onCreate(sQLiteDatabase: SQLiteDatabase) {
//            sQLiteDatabase.execSQL(SQL_CREATE_ENTRIES)
//        }
//
//        override fun onUpgrade(sQLiteDatabase: SQLiteDatabase, i: Int, i2: Int) {
//            sQLiteDatabase.execSQL(SQL_DELETE_ENTRIES)
//            onCreate(sQLiteDatabase)
//        }
//
//        override fun onDowngrade(sQLiteDatabase: SQLiteDatabase, i: Int, i2: Int) {
//            onUpgrade(sQLiteDatabase, i, i2)
//        }
//    }
//
//    fun getAllRows(): java.util.ArrayList<File_DTO>? {
//        val sQLiteDatabase = db
//        val str = TABLE_NAME
//        val strArr = ALL_KEYS
//        Log.e("MTAG", "db" + db)
//        Log.e("MTAG", "TABLE_NAME" + TABLE_NAME)
//        Log.e("MTAG", "ALL_KEYS" + ALL_KEYS)
//        Log.e("MTAG", "ALL_KEYS" + ALL_KEYS)
//        val query =
//            sQLiteDatabase!!.query(str, strArr, null, null, null, null, COLUMN_NAME_ID + " DESC")
//        val arrayList = java.util.ArrayList<File_DTO>()
//        if (query.moveToFirst()) {
//            do {
//                val file_DTO = File_DTO()
//                file_DTO.name = query.getString(1)
//                file_DTO.path = query.getString(2)
//                file_DTO.size = query.getString(4)
//                file_DTO.abumname = query.getString(4)
//                file_DTO.id = query.getString(0)
//                file_DTO.date = query.getString(5)
//                file_DTO.artistid = query.getString(6)
//                arrayList.add(file_DTO)
//            } while (query.moveToNext())
//            query.close()
//            return arrayList
//        }
//        query.close()
//        return arrayList
//    }
//
//}