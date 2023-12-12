package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateAlbumVideo;
import com.demo.example.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;


public class Video_Ultil {
    private static ArrayList<File_DTO> file_dtos = new ArrayList<>();
    public static long totolsize;
    private Context context;

    public Video_Ultil(Context context) {
        this.context = context;
    }

    public ArrayList<File_DTO> getAllAlbumVideo() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        String[] strArr = {"bucket_display_name", "_data", "date_modified", "duration", "_size", "_id"};
        Cursor query = this.context.getContentResolver().query(uri, strArr, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append("");
                int i = 0;
                sb.append(query.getString((int)query.getColumnIndex(strArr[0])));
                String sb2 = sb.toString();
                if (sb2.equals("null")) {
                    sb2 = "Unknow";
                }
                String string = query.getString((int)query.getColumnIndex(strArr[1]));
                long j = query.getLong((int)query.getColumnIndex(strArr[2])) * 1000;
                long j2 = query.getLong((int)query.getColumnIndex(strArr[4]));
                String string2 = query.getString((int)query.getColumnIndex(strArr[5]));
                Long valueOf = Long.valueOf(query.getLong((int)query.getColumnIndex(strArr[3])));
                if (new File(string).exists()) {
                    if (arrayList2.contains(sb2)) {
                        while (true) {
                            if (i < arrayList.size()) {
                                if (arrayList.get(i).getAbumname() != null && arrayList.get(i).getAbumname().equals(sb2)) {
                                    int totalitem = arrayList.get(i).getTotalitem();
                                    arrayList.remove(arrayList.get(i));
                                    File_DTO file_DTO = new File_DTO();
                                    file_DTO.setDuration(getDuration(valueOf.longValue()));
                                    file_DTO.setSize(new Ultil(this.context).bytesToHuman(j2));
                                    file_DTO.setId(string2);
                                    file_DTO.setDate(new Ultil(this.context).getDate(j));
                                    file_DTO.setTotalitem(totalitem + 1);
                                    file_DTO.setPath(string);
                                    file_DTO.setAbumname(sb2);
                                    totolsize += j2;
                                    arrayList.add(file_DTO);
                                    break;
                                }
                                i++;
                                string = string;
                            } else {
                                break;
                            }
                        }
                    } else {
                        File_DTO file_DTO2 = new File_DTO();
                        file_DTO2.setDuration(getDuration(valueOf.longValue()));
                        file_DTO2.setSize(new Ultil(this.context).bytesToHuman(j2));
                        file_DTO2.setId(string2);
                        file_DTO2.setDate(new Ultil(this.context).getDate(j));
                        file_DTO2.setTotalitem(1);
                        file_DTO2.setAbumname(sb2);
                        file_DTO2.setPath(string);
                        arrayList2.add(sb2);
                        arrayList.add(file_DTO2);
                    }
                }
            }
            Log.d("TAGX", "onCreate: " + arrayList);
        }
        query.close();
        return arrayList;
    }

    public ArrayList<File_DTO> getallvideo() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        String[] strArr = {"bucket_display_name", "_data", "date_modified", "duration", "_size", "_id", "_display_name", "date_added"};
        Cursor query = this.context.getContentResolver().query(uri, strArr, null, null, "date_added ASC");
        if (query != null) {
            while (query.moveToNext()) {
                try {
                    String string = query.getString((int)query.getColumnIndex(strArr[0]));
                    String string2 = query.getString((int)query.getColumnIndex(strArr[1]));
                    long j = query.getLong((int)query.getColumnIndex(strArr[2])) * 1000;
                    long j2 = query.getLong((int)query.getColumnIndex(strArr[4]));
                    String string3 = query.getString((int)query.getColumnIndex(strArr[5]));
                    Long valueOf = Long.valueOf(query.getLong((int)query.getColumnIndex(strArr[3])));
                    String string4 = query.getString(query.getColumnIndexOrThrow(strArr[6]));
                    if (new File(string2).exists()) {
                        File_DTO file_DTO = new File_DTO();
                        file_DTO.setDuration(getDuration(valueOf.longValue()));
                        file_DTO.setSize(new Ultil(this.context).bytesToHuman(j2));
                        file_DTO.setId(string3);
                        file_DTO.setDate(new Ultil(this.context).getDate(j));
                        file_DTO.setPath(string2);
                        file_DTO.setAbumname(string);
                        file_DTO.setName(string4);
                        arrayList.add(file_DTO);
                    }
                } catch (Exception unused) {
                    Log.d(UStats.TAG, "getallvideo: ");
                }
            }
        }
        query.close();
        return arrayList;
    }

    public ArrayList<File_DTO> getAllVidepAlbum() {
        if (file_dtos.isEmpty()) {
            file_dtos = getAllAlbumVideo();
        }
        return file_dtos;
    }

    public ArrayList<File_DTO> updateAllVidepAlbum() {
        file_dtos = new ArrayList<>();
        ArrayList<File_DTO> allVidepAlbum = getAllVidepAlbum();
        file_dtos = allVidepAlbum;
        return allVidepAlbum;
    }

    private String getDuration(long j) {
        int round = Math.round(Integer.parseInt(String.valueOf(j)));
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(timeZone);
        return String.valueOf(simpleDateFormat.format(Integer.valueOf(round)));
    }

    public Bitmap getbitmap(String str) {
        try {
            return Bitmap.createBitmap(ThumbnailUtils.createVideoThumbnail(str, 1), 0, 0, 100, 100, new Matrix(), true);
        } catch (Exception unused) {
            return BitmapFactory.decodeResource(this.context.getResources(), R.drawable.button_video);
        }
    }

    public ArrayList<Bitmap> bitmaps(ArrayList<File_DTO> arrayList) {
        ArrayList<Bitmap> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(getbitmap(arrayList.get(i).getPath()));
        }
        return arrayList2;
    }

    public void delteabum(ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, File_DTO file_DTO) {
        String abumname = file_DTO.getAbumname();
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        ArrayList<File_DTO> arrayList2 = getallvideo();
        for (int i = 0; i < getallvideo().size(); i++) {
            if (arrayList2.get(i).getAbumname().equals(abumname)) {
                arrayList.add(arrayList2.get(i));
            }
        }
        deletewithandroidversion(arrayList, activityResultLauncher);
    }

    private void deletewithandroidversion(ArrayList<File_DTO> arrayList, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        AndroidXI androidXI = new AndroidXI(this.context);
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        if (Build.VERSION.SDK_INT < 30) {
            for (int i = 0; i < arrayList.size(); i++) {
                new Ultil(this.context).copyFile(arrayList.get(i).getPath());
                Uri withAppendedPath = Uri.withAppendedPath(uri, arrayList.get(i).getId());
                new Ultil(this.context).copyFile(arrayList.get(i).getPath());
                this.context.getContentResolver().delete(withAppendedPath, "_id=?", new String[]{arrayList.get(i).getId()});
            }
            return;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            new Ultil(this.context).copyFile(arrayList.get(i2).getPath());
            androidXI.delete(activityResultLauncher, Uri.withAppendedPath(uri, arrayList.get(i2).getId()));
        }
    }

    public void dialogdelete(final File_DTO file_DTO, final ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        final Dialog dialog = new Dialog(this.context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_setwallpaper);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_yes);
        ((TextView) dialog.findViewById(R.id.titile)).setText(R.string.titile_delete_video);
        textView.setText(R.string.delete);
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog_thread dialog_thread = new Dialog_thread(Video_Ultil.this.context);
                dialog_thread.show();
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Video_Ultil.this.delteabum(activityResultLauncher, file_DTO);
                        dialog_thread.dissmiss();
                        dialog.dismiss();
                        CallbackUpdateAlbumVideo.getInstance().change();
                    }
                }.obtainMessage(111, "parameter").sendToTarget();
            }
        });
        dialog.show();
    }

    public ArrayList<File_DTO> getVideoAlbum(String str) {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < getallvideo().size(); i++) {
            if (getallvideo().get(i).getAbumname().equals(str)) {
                arrayList.add(getallvideo().get(i));
            }
        }
        return arrayList;
    }
}
