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

    //    private void getdatafromDevice() {
//        new String[]{""};
//        String[] strArr = {"_size", "_data", "_id", "date_added", "duration", "album", "_display_name", "_data", "title", "artist", "album", "date_modified", "bucket_display_name"};
//        Cursor query = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "is_download != 0ANDis_drm != 0", null, null);
//        if (query != null) {
//            if (query.moveToFirst()) {
//                do {
//                    if (!query.getString(query.getColumnIndex("album")).equals("WhatsApp Audio")) {
//                        String string = query.getString(query.getColumnIndex("_data"));
//                        String replaceAll = query.getString(query.getColumnIndex("title")).replace("_", " ").trim().replaceAll(" +", " ");
//                        query.getString(query.getColumnIndex("artist"));
//                        String string2 = query.getString(query.getColumnIndex("_id"));
//                        String string3 = query.getString(query.getColumnIndex("_size"));
//                        String string4 = query.getString(query.getColumnIndex("date_added"));
//                        query.getLong(query.getColumnIndex("date_added"));
//                        File_DTO file_DTO = new File_DTO();
//                        file_DTO.setTitle(replaceAll);
//                        file_DTO.setPath(string);
//                        file_DTO.setDate(string4);
//                        file_DTO.setId(string2);
//                        file_DTO.setSize(new Ultil(this.context).bytesToHuman(Long.parseLong(string3) * 1000));
//                        file_dtos.add(file_DTO);
//                    }
//                } while (query.moveToNext());
//                query.close();
//            }
//            query.close();
//        }
//    }
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


//    private ArrayList getImageButket(Context var1) {
//        ArrayList var7 = new ArrayList();
//        Uri var9 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String[] var8 = new String[]{"bucket_display_name", "_data", "date_modified"};
//        Cursor var11 = var1.getContentResolver().query(var9, var8, (String)null, (String[])null, "date_added ASC");
//        ArrayList var18 = new ArrayList();
//        SimpleDateFormat var10 = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
//        if (var11 != null) {
//            while(true) {
//                if (!var11.moveToNext()) {
//                    var11.close();
//                    break;
//                }
//                int var2 = 0;
//                String var17;
//                label45: {
//                    label44: {
//                        boolean var4;
//                        try {
//                            StringBuilder var16 = new StringBuilder();
//                            var16.append("");
//                            var16.append(var11.getString((int)var11.getColumnIndex(var8[0])));
//                            var17 = var16.toString();
//                            var4 = var17.equals(null);
//                        } catch (Exception var15) {
//                            break label44;
//                        }
//
//                        if (!var4) {
//                            break label45;
//                        }
//                    }
//
//                    var17 = "Unknown";
//                }
//
//                String var12 = var11.getString((int)var11.getColumnIndex(var8[1]));
//                long var5 = var11.getLong((int)var11.getColumnIndex(var8[2])) * 1000L;
//                String var13 = (new File(var12)).getName();
//                StringBuilder var14 = new StringBuilder();
//                var14.append("/");
//                var14.append(var13);
//                String var20 = var12.replace(var14.toString(), "");
//                if ((new File(var12)).exists()) {
//                    File_DTO var19;
//                    if (var18.contains(var17)) {
//                        while(var2 < var7.size()) {
//                            if (((File_DTO)var7.get(var2)).getAbumname() != null && ((File_DTO)var7.get(var2)).getAbumname().equals(var17)) {
//                                int var3 = ((File_DTO)var7.get(var2)).getTotalitem();
//                                var7.remove(var2);
//                                var19 = new File_DTO();
//                                var19.setAbumname(var17);
//                                var19.setTotalitem(var3 + 1);
//                                var19.setPath(var12);
//                                var19.setRealpath(var20);
//                                var19.setDate(var10.format(var5));
//                                var7.add(var19);
//                                break;
//                            }
//
//                            ++var2;
//                        }
//                    } else {
//                        var19 = new File_DTO();
//                        var19.setAbumname(var17);
//                        var19.setTotalitem(1);
//                        var19.setDate(String.valueOf(var5));
//                        var19.setPath(var12);
//                        var7.add(var19);
//                        var18.add(var17);
//                    }
//                }
//            }
//        }
//        if (var7.isEmpty()) {
//            Log.d("TAG", "savedInstanceState is null");
//        }
//        return var7;
//    }


//    @SuppressLint("Range")
//    private ArrayList<File_DTO> getImageButket(Context context) {
//        int i = 0;
//        String str;
//        ArrayList<File_DTO> arrayList = new ArrayList<>();
//        String[] strArr = {"bucket_display_name", "_data", "date_modified"};
//        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "date_added ASC");
//        ArrayList arrayList2 = new ArrayList();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
//        if (query != null) {
//            while (query.moveToNext()) {
//                i = 0;
//                try {
//                    str = "" + query.getString(query.getColumnIndex(strArr[0]));
//                } catch (Exception unused) {
//                }
//            }
//            query.close();
//        }
//        if (arrayList.isEmpty()) {
//            Log.d("TAG", "savedInstanceState is null");
//        }
////        return arrayList;
//        str = "Unknown";
//        String string = query.getString(query.getColumnIndex(strArr[1]));
//        long j = query.getLong(query.getColumnIndex(strArr[2])) * 1000;
//        String replace = string.replace("/" + new File(string).getName(), "");
//        if (new File(string).exists()) {
//            if (arrayList2.contains(str)) {
//                while (true) {
//                    if (i < arrayList.size()) {
//                        if (arrayList.get(i).getAbumname() != null && arrayList.get(i).getAbumname().equals(str)) {
//                            int totalitem = arrayList.get(i).getTotalitem();
//                            arrayList.remove(i);
//                            File_DTO file_DTO = new File_DTO();
//                            file_DTO.setAbumname(str);
//                            file_DTO.setTotalitem(totalitem + 1);
//                            file_DTO.setPath(string);
//                            file_DTO.setRealpath(replace);
//                            file_DTO.setDate(simpleDateFormat.format(Long.valueOf(j)));
//                            arrayList.add(file_DTO);
//                            break;
//                        }
//                        i++;
//                    } else {
//                        break;
//                    }
//                }
//            } else {
//                File_DTO file_DTO2 = new File_DTO();
//                file_DTO2.setAbumname(str);
//                file_DTO2.setTotalitem(1);
//                file_DTO2.setDate(String.valueOf(j));
//                file_DTO2.setPath(string);
//                arrayList.add(file_DTO2);
//                arrayList2.add(str);
//            }
//        }
//        return arrayList;
//    }

    public ArrayList<File_DTO> allImage() {
        ArrayList<File_DTO> allShownImagesPath = getAllShownImagesPath();
        image_list = allShownImagesPath;
        return allShownImagesPath;
    }

    public ArrayList<File_DTO> updatedata() {
        image_list = new ArrayList<>();
        ArrayList<File_DTO> allShownImagesPath = getAllShownImagesPath();
        image_list = allShownImagesPath;
        return allShownImagesPath;
    }

    public ArrayList<File_DTO> updatealbumimage() {
        Album_iimage = new ArrayList<>();
        ArrayList<File_DTO> imageButket = getImageButket(this.context);
        Album_iimage = imageButket;
        return imageButket;
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
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
