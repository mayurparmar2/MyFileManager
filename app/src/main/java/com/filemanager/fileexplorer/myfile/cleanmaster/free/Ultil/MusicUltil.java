package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.TimeZone;


public class MusicUltil {
    public static long totolsize;
    private Context context;
    protected ArrayList<File_DTO> file_dtos = new ArrayList<>();

    public MusicUltil(Context context) {
        this.context = context;
    }
    @SuppressLint("Range")
    public ArrayList<File_DTO> getdatafromDevice() {
        int i;
        this.file_dtos = new ArrayList<>();
        Cursor query = this.context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_size", "_id", "date_modified", "date_added", "duration", "album", "_display_name", "_data", "title", "artist", "album", "album_id"}, "is_music != 0", null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                do {
                    try {
                        i = Math.round(Integer.parseInt(query.getString(query.getColumnIndex("duration"))));
                    } catch (Exception unused) {
                        i = 1000;
                    }
                    if (!query.getString(query.getColumnIndex("album")).equals("WhatsApp Audio")) {
                        String string = query.getString(query.getColumnIndex("_display_name"));
                        String string2 = query.getString(query.getColumnIndex("_data"));
                        String replaceAll = query.getString(query.getColumnIndex("title")).replace("_", " ").trim().replaceAll(" +", " ");
                        query.getString(query.getColumnIndex("artist"));
                        query.getString(query.getColumnIndex("album"));
                        String string3 = query.getString(query.getColumnIndex("_id"));
                        String string4 = query.getString(query.getColumnIndex("album_id"));
                        String string5 = query.getString(query.getColumnIndex("_size"));
                        String string6 = query.getString(query.getColumnIndex("artist"));
                        String string7 = query.getString(query.getColumnIndex("date_added"));
                        TimeZone timeZone = TimeZone.getTimeZone("UTC");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
                        simpleDateFormat.setTimeZone(timeZone);
                        String valueOf = String.valueOf(simpleDateFormat.format(Integer.valueOf(i)));
                        query.getLong(query.getColumnIndex("date_modified"));
                        File_DTO file_DTO = new File_DTO();
                        file_DTO.setName(string);
                        file_DTO.setTitle(replaceAll);
                        file_DTO.setPath(string2);
                        file_DTO.setDuration(valueOf);
                        file_DTO.setDate(getFormattedDateFromTimestamp(Long.parseLong(string7) * 1000));
                        file_DTO.setId(string3);
                        file_DTO.setArtistname(string6);
                        file_DTO.setAbumid(string4);
                        file_DTO.setSize(new Ultil(this.context).bytesToHuman(Long.parseLong(string5)));
                        totolsize += query.getLong(query.getColumnIndex("_size"));
                        this.file_dtos.add(file_DTO);
                    }
                } while (query.moveToNext());
                query.close();
            } else {
                query.close();
            }
        }
        return this.file_dtos;
    }

    public ArrayList<File_DTO> allsong() {
        if (this.file_dtos.size() == 0) {
            getdatafromDevice();
        }
        Collections.sort(this.file_dtos, new Comparator<File_DTO>() {
            @Override
            public int compare(File_DTO file_DTO, File_DTO file_DTO2) {
                return file_DTO.getName().toLowerCase().compareTo(file_DTO2.getName().toLowerCase());
            }
        });
        return this.file_dtos;
    }

    public ArrayList<File_DTO> updateData() {
        this.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"));
        getdatafromDevice();
        return this.file_dtos;
    }

    public static String getDate(long j, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getFormattedDateFromTimestamp(long j) {
        return new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(j));
    }
}
