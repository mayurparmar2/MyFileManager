package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;

import java.util.ArrayList;


public class DocumentUltil {
    public static long docsSize;
    private static ArrayList<File_DTO> file_dtos = new ArrayList<>();
    public static long sddocsSize;
    private Context context;

    public DocumentUltil(Context context) {
        this.context = context;
    }

    public void getdata() {
        Cursor query = this.context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "_size"}, "mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=?", new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("html")}, null);
        query.moveToFirst();
        do {
            if (query.getString(query.getColumnIndexOrThrow("_data")).contains(Environment.getExternalStorageDirectory().getPath())) {
                docsSize += Long.parseLong(query.getString(query.getColumnIndexOrThrow("_size")));
            } else {
                sddocsSize += Long.parseLong(query.getString(query.getColumnIndexOrThrow("_size")));
            }
        } while (query.moveToNext());
    }
}
