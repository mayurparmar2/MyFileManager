package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Image_Ultil {
    private Context context;
    private static ArrayList<File_DTO> file_dtos = new ArrayList<>();
    public static ArrayList<File_DTO> Album_iimage = new ArrayList<>();
    public static ArrayList<File_DTO> image_list = new ArrayList<>();
    public static long totolsize = 0;

    public Image_Ultil(Context context) {
        this.context = context;
    }
    @SuppressLint("Range")
    private ArrayList<File_DTO> getAllShownImagesPath() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        Cursor query = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"bucket_display_name", "_size", "_data", "_id", "date_added", "title", "_display_name", "date_modified"}, null, null, "date_added ASC");
        while (query.moveToNext()) {
            try {
                long j = query.getLong(query.getColumnIndexOrThrow("_size"));
                @SuppressLint("Range") String format = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(query.getLong(query.getColumnIndex("date_modified")) * 1000));
                File_DTO file_DTO = new File_DTO();
                String string = query.getString(query.getColumnIndexOrThrow("_data"));
                String str = "" + query.getString(query.getColumnIndexOrThrow("_display_name"));
                file_DTO.setPath(string);
                if (str.equals("null")) {
                    file_DTO.setName(new File(string).getName());
                } else {
                    file_DTO.setName(str);
                }
                file_DTO.setSize(new Ultil(this.context).bytesToHuman(j));
                String str2 = "" + query.getString(query.getColumnIndexOrThrow("bucket_display_name"));
                if (str2.equals("null")) {
                    str2 = "Unknow";
                }
                file_DTO.setAbumname(str2);
                file_DTO.setDate(format);
                file_DTO.setId(query.getString(query.getColumnIndexOrThrow("_id")));
                totolsize += query.getLong(query.getColumnIndexOrThrow("_size"));
                arrayList.add(file_DTO);
            } catch (Exception unused) {
                Log.d("AAAA", "getAllShownImagesPath: ");
            }
        }
        return arrayList;
    }


    private ArrayList<File_DTO> getImageButket(Context context) {
        ArrayList<File_DTO> fileDTOList = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {"bucket_display_name", "_data", "date_modified"};

        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "date_added ASC")) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String albumName = cursor.getString((int)cursor.getColumnIndex(projection[0]));
                    String filePath = cursor.getString((int)cursor.getColumnIndex(projection[1]));
                    long dateModified = cursor.getLong((int)cursor.getColumnIndex(projection[2])) * 1000L;

                    String fileName = new File(filePath).getName();
                    String folderPath = filePath.replace("/" + fileName, "");

                    if (new File(filePath).exists()) {
                        File_DTO fileDTO = createFileDTO(fileDTOList, albumName, filePath, folderPath, dateModified);
                        fileDTOList.add(fileDTO);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "Exception is:  "+e);
            e.printStackTrace();
        }
        if (fileDTOList.isEmpty()) {
            Log.e("TAG", "fileDTOList is empty");
        }
        return fileDTOList;
    }

    private File_DTO createFileDTO(ArrayList<File_DTO> fileDTOList, String albumName, String filePath, String folderPath, long dateModified) {
        for (File_DTO fileDTO : fileDTOList) {
            if (fileDTO.getAbumname() != null && fileDTO.getAbumname().equals(albumName)) {
                int totalItems = fileDTO.getTotalitem();
                fileDTOList.remove(fileDTO);

                File_DTO updatedFileDTO = new File_DTO();
                updatedFileDTO.setAbumname(albumName);
                updatedFileDTO.setTotalitem(totalItems + 1);
                updatedFileDTO.setPath(filePath);
                updatedFileDTO.setRealpath(folderPath);
                updatedFileDTO.setDate(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(dateModified));
                return updatedFileDTO;
            }
        }
        File_DTO newFileDTO = new File_DTO();
        newFileDTO.setAbumname(albumName);
        newFileDTO.setTotalitem(1);
        newFileDTO.setDate(String.valueOf(dateModified));
        newFileDTO.setPath(filePath);
        return newFileDTO;
    }
    public ArrayList<File_DTO> allImage() {
        image_list = getAllShownImagesPath();
        return image_list;
    }

    public ArrayList<File_DTO> updatedata() {
        image_list = new ArrayList<>();
        image_list = getAllShownImagesPath();
        return image_list;
    }

    public ArrayList<File_DTO> updatealbumimage() {
        Album_iimage = getImageButket(this.context);
        return Album_iimage;
    }

    public ArrayList<File_DTO> getallalbumImage() {
        if (Album_iimage.size() == 0 || Album_iimage.isEmpty()) {
            Album_iimage = getImageButket(this.context);
        }
        return Album_iimage;
    }

    public Bitmap getBitmap(String str, ImageView imageView) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDirectory + str, ".png");
        return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options()), imageView.getWidth(), imageView.getHeight(), true);
    }

    public ArrayList<File_DTO> delteabum(String str) {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        allImage();
        for (int i = 0; i < image_list.size(); i++) {
            if (image_list.get(i).getAbumname().equals(str)) {
                arrayList.add(image_list.get(i));
            }
        }
        return arrayList;
    }

    private void deletewithandroidversion(ArrayList<File_DTO> arrayList, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        AndroidXI androidXI = new AndroidXI(this.context);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        if (Build.VERSION.SDK_INT < 30) {
            for (int i = 0; i < arrayList.size(); i++) {
                Uri withAppendedPath = Uri.withAppendedPath(uri, arrayList.get(i).getId());
                copyFile(arrayList.get(i).getPath());
                this.context.getContentResolver().delete(withAppendedPath, "_id=?", new String[]{arrayList.get(i).getId()});
            }
            return;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            Uri withAppendedPath2 = Uri.withAppendedPath(uri, arrayList.get(i2).getId());
            copyFile(arrayList.get(i2).getPath());
            androidXI.delete(activityResultLauncher, withAppendedPath2);
        }
    }

    private void copyFile(String str) {
        String replaceAll = str.replaceAll("/", "%");
        File file = new File(MainActivity.getStore(this.context) + "/Bin", replaceAll);
        try {
            InputStream openInputStream = this.context.getContentResolver().openInputStream(Uri.fromFile(new File(str)));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MainActivity.copyStream(openInputStream, fileOutputStream);
            fileOutputStream.close();
            openInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("MTAG", "Image_Ultil.class,copyFile():" + e);
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("MTAG", "Image_Ultil.class,copyFile():" +e2);

        }
    }
}
