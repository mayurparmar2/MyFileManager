package com.filemanager.fileexplorer.myfile.cleanmaster.free.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;

import java.util.ArrayList;


public class RecentAdd {
    private String SQL_DELETE_ENTRIES;
    Context context;
    private SQLiteDatabase db;
    private ReaderDB myDBHelper;
    private String TEXT_TYPE = " TEXT";
    private String COMMA_SEP = ",";
    private String TABLE_NAME = "recent";
    private String COLUMN_NAME_ID = "id";
    private String TITLE = "title";
    private String PATH = "path";
    private String SIZE = "size";
    private String ALBUM = "album";
    private String DATE = "date";
    private String NAMETYPE = "nametype";
    private String DATEADED = "dateadded";
    private String[] ALL_KEYS = {"id", "title", "path", "album", "size", "date", "nametype", "dateadded"};
    private String SQL_CREATE_ENTRIES = "CREATE TABLE " + this.TABLE_NAME + " (" + this.COLUMN_NAME_ID + this.COMMA_SEP + this.TITLE + this.TEXT_TYPE + this.COMMA_SEP + this.PATH + this.TEXT_TYPE + this.COMMA_SEP + this.SIZE + this.TEXT_TYPE + this.COMMA_SEP + this.ALBUM + this.TEXT_TYPE + this.COMMA_SEP + this.DATE + this.TEXT_TYPE + this.COMMA_SEP + this.NAMETYPE + this.TEXT_TYPE + this.COMMA_SEP + this.DATEADED + this.TEXT_TYPE + ");";

    public RecentAdd(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS ");
        sb.append(this.TABLE_NAME);
        this.SQL_DELETE_ENTRIES = sb.toString();
        this.context = context;
        this.myDBHelper = new ReaderDB(this.context);
    }

    public RecentAdd open() {
        this.db = this.myDBHelper.getWritableDatabase();
        return this;
    }

    public RecentAdd close() {
        this.myDBHelper.close();
        return this;
    }

    public long addRow(File_DTO file_DTO) {
        String path = file_DTO.getPath();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.TITLE, file_DTO.getName());
        contentValues.put(this.PATH, file_DTO.getPath());
        contentValues.put(this.SIZE, file_DTO.getSize());
        if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac") || path.endsWith(".wma") || path.endsWith(".m4a")) {
            contentValues.put(this.ALBUM, file_DTO.getAbumid());
        } else {
            contentValues.put(this.ALBUM, file_DTO.getAbumname());
        }
        contentValues.put(this.COLUMN_NAME_ID, file_DTO.getId());
        contentValues.put(this.DATE, file_DTO.getDate());
        contentValues.put(this.NAMETYPE, file_DTO.getAbumid());
        contentValues.put(this.DATEADED, file_DTO.getLastmodified());
        return this.db.insert(this.TABLE_NAME, "NULL", contentValues);
    }

    public ArrayList<File_DTO> getAllRows() {
        SQLiteDatabase sQLiteDatabase = this.db;
        String str = this.TABLE_NAME;
        String[] strArr = this.ALL_KEYS;
        Cursor query = sQLiteDatabase.query(str, strArr, null, null, null, null, this.DATE + " ASC");
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (query.moveToFirst()) {
            do {
                File_DTO file_DTO = new File_DTO();
                file_DTO.setName(query.getString(1));
                file_DTO.setPath(query.getString(2));
                file_DTO.setSize(query.getString(4));
                file_DTO.setAbumname(query.getString(4));
                file_DTO.setId(query.getString(0));
                file_DTO.setDate(query.getString(5));
                file_DTO.setArtistid(query.getString(6));
                file_DTO.setLastmodified(query.getString(7));
                arrayList.add(file_DTO);
            } while (query.moveToNext());
            query.close();
            return arrayList;
        }
        query.close();
        return arrayList;
    }

    public File_DTO getRow(long j) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        Cursor query = this.db.query(true, this.TABLE_NAME, this.ALL_KEYS, this.COLUMN_NAME_ID + "=" + j, null, null, null, null, null);
        String str7 = null;
        if (query != null) {
            query.moveToFirst();
            str7 = query.getString(2);
            str = query.getString(3);
            str2 = query.getString(4);
            str3 = query.getString(5);
            str4 = query.getString(1);
            str5 = query.getString(6);
            str6 = query.getString(7);
        } else {
            str = null;
            str2 = null;
            str3 = null;
            str4 = null;
            str5 = null;
            str6 = null;
        }
        query.close();
        File_DTO file_DTO = new File_DTO();
        file_DTO.setTitle(str7);
        file_DTO.setPath(str);
        file_DTO.setArtistid(str6);
        file_DTO.setAbumname(str3);
        file_DTO.setSize(str2);
        file_DTO.setDate(str5);
        file_DTO.setId(str4);
        return file_DTO;
    }

    public boolean deleteRow(long j) {
        try {
            SQLiteDatabase sQLiteDatabase = this.db;
            sQLiteDatabase.execSQL("DELETE FROM fav WHERE id ='" + j + "'");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean deleteRowall() {
        return this.db.delete(this.TABLE_NAME, null, null) != 0;
    }

    public boolean deleteRowByPath(String str) {
        try {
            Log.d("TAG!!", "deleteRow: " + str);
            SQLiteDatabase sQLiteDatabase = this.db;
            String str2 = this.TABLE_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append(this.PATH);
            sb.append("=?");
            return sQLiteDatabase.delete(str2, sb.toString(), new String[]{str}) != 0;
        } catch (Exception e) {
            Log.d("TAG!!", "deleteRowByPath: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAll() {
        try {
            Cursor allRowsCursor = getAllRowsCursor();
            long columnIndexOrThrow = allRowsCursor.getColumnIndexOrThrow(this.COLUMN_NAME_ID);
            if (allRowsCursor.moveToFirst()) {
                do {
                    deleteRow(allRowsCursor.getLong((int) columnIndexOrThrow));
                } while (allRowsCursor.moveToNext());
                allRowsCursor.close();
                return true;
            }
            allRowsCursor.close();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private Cursor getAllRowsCursor() {
        Cursor query = this.db.query(true, this.TABLE_NAME, this.ALL_KEYS, null, null, null, null, null, null);
        if (query != null) {
            query.moveToFirst();
        }
        return query;
    }


    private class ReaderDB extends SQLiteOpenHelper {
        ReaderDB(Context context) {
            super(context, "Recent.db", (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(RecentAdd.this.SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            sQLiteDatabase.execSQL(RecentAdd.this.SQL_DELETE_ENTRIES);
            onCreate(sQLiteDatabase);
        }

        @Override
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            onUpgrade(sQLiteDatabase, i, i2);
        }
    }
}
