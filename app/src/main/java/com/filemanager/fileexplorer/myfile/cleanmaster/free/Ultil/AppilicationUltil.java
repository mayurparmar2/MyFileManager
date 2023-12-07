package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;

import java.io.File;
import java.util.ArrayList;


public class AppilicationUltil {
    private Context context;
    protected ArrayList<File_DTO> file_dtos = new ArrayList<>();

    public AppilicationUltil(Context context) {
        this.context = context;
    }
    @SuppressLint("Range")
    private void getAllAlbumVideo() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"bucket_display_name", "_data", "date_added", "duration", "_size", "_id"};
        Cursor query = this.context.getContentResolver().query(uri, strArr, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                int i = 0;
                 String string = query.getString(query.getColumnIndex(strArr[0]));
                String string2 = query.getString(query.getColumnIndex(strArr[1]));
                long j = query.getLong(query.getColumnIndex(strArr[2]));
                long j2 = query.getLong(query.getColumnIndex(strArr[4]));
                String string3 = query.getString(query.getColumnIndex(strArr[5]));
                Long.valueOf(query.getLong(query.getColumnIndex(strArr[3])));
                if (new File(string2).exists()) {
                    if (arrayList.contains(string)) {
                        while (true) {
                            if (i < this.file_dtos.size()) {
                                if (this.file_dtos.get(i).getAbumname() != null && this.file_dtos.get(i).getAbumname().equals(string)) {
                                    int totalitem = this.file_dtos.get(i).getTotalitem();
                                    this.file_dtos.remove(i);
                                    File_DTO file_DTO = new File_DTO();
                                    file_DTO.setSize(new Ultil(this.context).bytesToHuman(j2));
                                    file_DTO.setId(string3);
                                    file_DTO.setDate(new Ultil(this.context).getDate(j));
                                    file_DTO.setTotalitem(totalitem + 1);
                                    file_DTO.setPath(string2);
                                    file_DTO.setAbumname(string);
                                    this.file_dtos.add(file_DTO);
                                    break;
                                }
                                i++;
                            } else {
                                break;
                            }
                        }
                    } else {
                        File_DTO file_DTO2 = new File_DTO();
                        file_DTO2.setSize(new Ultil(this.context).bytesToHuman(j2));
                        file_DTO2.setId(string3);
                        file_DTO2.setDate(new Ultil(this.context).getDate(j));
                        file_DTO2.setTotalitem(1);
                        file_DTO2.setAbumname(string);
                        file_DTO2.setPath(string2);
                        this.file_dtos.add(file_DTO2);
                        arrayList.add(file_DTO2);
                    }
                }
            }
            query.close();
        }
    }
}
