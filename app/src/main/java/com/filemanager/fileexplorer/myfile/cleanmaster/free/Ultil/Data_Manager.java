package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public class Data_Manager {
    private List<String> date_and_time;
    private File[] files;
    Context mContext;
    public List<String> name;
    private ArrayList<String> sizelist;
    public ArrayList<Integer> totalItem;
    private ArrayList<File_DTO> file_dtos = new ArrayList<>();
    private ArrayList<File_DTO> fileall = new ArrayList<>();
    private ArrayList<File_DTO> file_apk = new ArrayList<>();
    private ArrayList<Drawable> drawables = new ArrayList<>();
    public long sizedoccument = 0;

    public Data_Manager(Context context) {
        this.mContext = context;
    }

    public ArrayList<File_DTO> getAPkfile() {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.INFO");
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (ResolveInfo resolveInfo : this.mContext.getPackageManager().queryIntentActivities(intent, 0)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
            File file = new File(String.valueOf(resolveInfo.activityInfo.applicationInfo.name));
            Log.d("TAGQ", "getAPkfile: " + file.getName());
            File_DTO file_DTO = new File_DTO();
            file_DTO.setSize(new Ultil(this.mContext).bytesToHuman(file.length()));
            file_DTO.setDate(simpleDateFormat.format(Long.valueOf(file.lastModified())));
            file_DTO.setName(file.getName());
            file_DTO.setPath(file.getPath());
            arrayList.add(file_DTO);
        }
        return arrayList;
    }

    public void setRecycler(File file, int i) {
        File[] fileArr;
        this.date_and_time = new ArrayList();
        this.sizelist = new ArrayList<>();
        this.name = new ArrayList();
        this.totalItem = new ArrayList<>();
        File[] listFiles = file.getAbsoluteFile().listFiles();
        this.files = listFiles;
        if (listFiles == null) {
            this.files = new File[0];
        }
        if (i == 1) {
            sortByName(this.files);
        } else if (i == 2) {
            sortByDate(this.files);
        } else if (i == 3) {
            sortBySize(this.files);
        } else if (i == -1) {
            sortByNameReverse(this.files);
        } else if (i == -2) {
            sortByDateReverse(this.files);
        } else if (i == -3) {
            sortBySizeReverse(this.files);
        }
        for (File file2 : this.files) {
            this.name.add(file2.getName());
            this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(file2.lastModified())));
            this.sizelist.add(new Ultil(this.mContext).bytesToHuman(file2.length()));
            try {
                this.totalItem.add(Integer.valueOf(file2.listFiles().length));
            } catch (Exception unused) {
                this.totalItem.add(0);
            }
        }
    }

    public File[] listpath() {
        return new File[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS), Environment.getExternalStorageDirectory(), Environment.getDataDirectory()};
    }

    public ArrayList<File> getallpath() {
        ArrayList<File> arrayList = new ArrayList<>();
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));
        arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        arrayList.add(Environment.getExternalStorageDirectory());
        arrayList.add(new File("/storage/emulated/0/Android/data"));
        return arrayList;
    }

    public void getallfile() {
        for (int i = 0; i < getallpath().size(); i++) {
            setRecycler(getallpath().get(i), 1);
        }
    }

    public ArrayList<File_DTO> getallfilewithpath(File file) {
        File[] fileArr;
        this.files = new File[0];
        File[] listFiles = file.listFiles();
        this.files = listFiles;
        if (listFiles == null) {
            this.files = new File[0];
        }
        for (File file2 : this.files) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
            if (!file2.isDirectory()) {
                File_DTO file_DTO = new File_DTO();
                file_DTO.setSize(new Ultil(this.mContext).bytesToHuman(file2.length()));
                file_DTO.setDate(simpleDateFormat.format(Long.valueOf(file2.lastModified())));
                file_DTO.setLastmodified(String.valueOf(file2.lastModified()));
                file_DTO.setName(file2.getName());
                file_DTO.setPath(file2.getPath());
                this.fileall.add(file_DTO);
            }
        }
        return this.fileall;
    }

    public ArrayList<File_DTO> filesDowload() {
        this.fileall = new ArrayList<>();
        ArrayList<File_DTO> arrayList = getallfilewithpath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        this.fileall = arrayList;
        return arrayList;
    }

    public ArrayList<File_DTO> getallfileindecive() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        ArrayList<File> arrayList2 = getallpath();
        this.fileall = new ArrayList<>();
        for (int i = 0; i < arrayList2.size(); i++) {
            arrayList = getallfilewithpath(arrayList2.get(i));
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getfileApkdowload() {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        new ArrayList();
        ArrayList<File_DTO> arrayList2 = getallfilewithpath(externalStoragePublicDirectory);
        if (!arrayList2.isEmpty()) {
            for (int i = 0; i < arrayList2.size(); i++) {
                if (arrayList2.get(i).getName().endsWith(".apk") || arrayList2.get(i).getName().endsWith(".apks")) {
                    arrayList.add(arrayList2.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getzipdowload() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        new ArrayList();
        ArrayList<File_DTO> arrayList2 = getallfileindecive();
        if (!arrayList2.isEmpty()) {
            for (int i = 0; i < arrayList2.size(); i++) {
                if (arrayList2.get(i).getName().endsWith(".zip") || arrayList2.get(i).getName().endsWith(".rar")) {
                    arrayList.add(arrayList2.get(i));
                }
            }
        }
        return arrayList;
    }

    public void setSearchResults(List<File> list) {
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        this.files = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            this.files[i] = list.get(i);
            this.name.add(this.files[i].getName());
            this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
        }
    }

    public File getFiles(int i) {
        return this.files[i];
    }

    public String getName(int i) {
        return this.name.get(i);
    }

    public String getDate_and_time(int i) {
        return this.date_and_time.get(i);
    }

    public String getsize(int i) {
        return this.sizelist.get(i);
    }

    public int getTotalItem(int i) {
        return this.totalItem.get(i).intValue();
    }

    public int getIconId(int i) {
        String str;
        try {
            str = this.files[i].getAbsolutePath().toLowerCase();
        } catch (Exception unused) {
            str = "";
        }
        return this.files[i].isDirectory() ? R.drawable.image_folder : str.contains(".apk") ? R.drawable.icon_apk : (str.contains(".docx") || str.contains(".txt")) ? R.drawable.icon_doc : (str.contains(".mp3") || str.contains(".wav") || str.contains(".flac") || str.contains(".wma") || str.contains(".m4a")) ? R.drawable.button_music : (str.contains(".mp4") || str.contains(".avi") || str.contains(".mkv") || str.contains(".vob") || str.contains(".mov")) ? R.drawable.videoico : (str.contains(".jpg") || str.contains(".png") || str.contains(".gif") || str.contains(".tiff") || str.contains(".jpeg")) ? R.drawable.imageicon : str.contains(".pdf") ? R.drawable.icon_pdf : str.contains(".ppt") ? R.drawable.icon_ppt : (str.contains(".xls") || str.contains(".csv")) ? R.drawable.icon_xls : (str.contains(".zip") || str.contains(".rar")) ? R.drawable.icon_zip : R.drawable.unknowfile;
    }

    public void sortByName(File[] fileArr) {
        Arrays.sort(fileArr, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return file.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
            }
        });
    }

    public void sortByNameReverse(File[] fileArr) {
        Arrays.sort(fileArr, Collections.reverseOrder(new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return file.getName().compareTo(file2.getName());
            }
        }));
    }

    public void sortByDate(File[] fileArr) {
        Arrays.sort(fileArr, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return Long.valueOf(file.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
            }
        });
    }

    void sortByDateReverse(File[] fileArr) {
        Arrays.sort(fileArr, Collections.reverseOrder(new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return Long.valueOf(file.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
            }
        }));
    }

    void sortBySize(File[] fileArr) {
        Arrays.sort(fileArr, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return Long.valueOf(file.length()).compareTo(Long.valueOf(file2.length()));
            }
        });
    }

    void sortBySizeReverse(File[] fileArr) {
        Arrays.sort(fileArr, Collections.reverseOrder(new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return Long.valueOf(file.length()).compareTo(Long.valueOf(file2.length()));
            }
        }));
    }

    public void setImagesData() {
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor loadInBackground = new CursorLoader(this.mContext, uri, new String[]{"_id", "_data"}, null, null, null).loadInBackground();
        loadInBackground.moveToFirst();
        int columnIndexOrThrow = loadInBackground.getColumnIndexOrThrow("_data");
        if (loadInBackground != null) {
            this.files = new File[loadInBackground.getCount()];
        }
        int i = 0;
        do {
            this.files[i] = new File(loadInBackground.getString(columnIndexOrThrow));
            this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
            this.name.add(this.files[i].getName());
            i++;
        } while (loadInBackground.moveToNext());
    }

    public ArrayList<File> testgetfile() {
        ArrayList<File> arrayList = new ArrayList<>();
        Cursor query = this.mContext.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data"}, null, null, null);
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    arrayList.add(new File(query.getString(0)));
                }
            } finally {
                query.close();
            }
        }
        return arrayList;
    }

    public void setAudio(Context context) {
        this.mContext = context;
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_data"}, null, null, null);
        this.files = new File[query.getCount()];
        query.moveToFirst();
        if (query.getCount() != 0) {
            int i = 0;
            do {
                this.files[i] = new File(query.getString(query.getColumnIndexOrThrow("_data")));
                this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
                this.name.add(this.files[i].getName());
                i++;
            } while (query.moveToNext());
            return;
        }
        Toast.makeText(context, "No Audio Files Present", Toast.LENGTH_LONG).show();
    }

    public ArrayList<File_DTO> getfiledownloadwithMEdiastore() {
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        this.file_dtos = new ArrayList<>();
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Downloads.INTERNAL_CONTENT_URI, new String[]{"_data", "mime_type", "_id"}, null, null, null);
        this.files = new File[query.getCount()];
        query.moveToFirst();
        if (query.getCount() != 0) {
            int i = 0;
            do {
                this.files[i] = new File(query.getString(query.getColumnIndexOrThrow("_data")));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
                this.date_and_time.add(simpleDateFormat.format(Long.valueOf(this.files[i].lastModified())));
                this.name.add(this.files[i].getName());
                File_DTO file_DTO = new File_DTO();
                file_DTO.setPath(this.files[i].getPath());
                file_DTO.setDate(simpleDateFormat.format(Long.valueOf(this.files[i].lastModified())));
                file_DTO.setName(this.files[i].getName());
                file_DTO.setId(query.getString(query.getColumnIndexOrThrow("_id")));
                try {
                    file_DTO.setVolumname(query.getString(query.getColumnIndex("volume_name")));
                } catch (Exception unused) {
                    file_DTO.setVolumname("");
                }
                file_DTO.setSize(new Ultil(this.mContext).bytesToHuman(this.files[i].length()));
                file_DTO.setMinetype(query.getString(query.getColumnIndexOrThrow("mime_type")));
                this.file_dtos.add(file_DTO);
                i++;
            } while (query.moveToNext());
        } else {
            Toast.makeText(this.mContext, "No Document Files Present", Toast.LENGTH_LONG).show();
        }
        return this.file_dtos;
    }

    public void setDocs() {
        String[] extensions = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "html"};
        this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"));
        this.date_and_time = new ArrayList<>();
        this.name = new ArrayList<>();
        this.file_dtos = new ArrayList<>();
        String[] projection = (Build.VERSION.SDK_INT <= 28) ? new String[]{"_data", "mime_type", "_id"} : new String[]{"_data", "mime_type", "volume_name", "_id"};
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), projection, "mime_type IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", extensions, null);
        this.files = new File[query.getCount()];
        int i = 0;
        if (query.moveToFirst()) {
            do {
                this.files[i] = new File(query.getString(query.getColumnIndexOrThrow("_data")));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
                this.date_and_time.add(simpleDateFormat.format(this.files[i].lastModified()));
                this.name.add(this.files[i].getName());
                File_DTO file_DTO = new File_DTO();
                file_DTO.setPath(this.files[i].getPath());
                file_DTO.setDate(simpleDateFormat.format(this.files[i].lastModified()));
                file_DTO.setName(this.files[i].getName());
                file_DTO.setId(query.getString(query.getColumnIndexOrThrow("_id")));
                try {
                    file_DTO.setVolumname(query.getString((int)query.getColumnIndex("volume_name")));
                } catch (Exception unused) {
                    file_DTO.setVolumname("");
                }
                this.sizedoccument += this.files[i].length();
                file_DTO.setSize(new Ultil(this.mContext).bytesToHuman(this.files[i].length()));
                file_DTO.setMinetype(query.getString(query.getColumnIndexOrThrow("mime_type")));
                this.file_dtos.add(file_DTO);
                i++;
            } while (query.moveToNext());
        } else {
            Log.e("TAG", "setDocs: ");
        }
        query.close();
    }

//    public void setDocs() {
//        String[] strArr;
//        this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"));
//        this.date_and_time = new ArrayList();
//        this.name = new ArrayList();
//        this.file_dtos = new ArrayList<>();
//        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
//        String mimeTypeFromExtension2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
//        String mimeTypeFromExtension3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
//        String mimeTypeFromExtension4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
//        String mimeTypeFromExtension5 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
//        String mimeTypeFromExtension6 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
//        String mimeTypeFromExtension7 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
//        String mimeTypeFromExtension8 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
//        String mimeTypeFromExtension9 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");
//        if (Build.VERSION.SDK_INT <= 28) {
//            strArr = new String[]{"_data", "mime_type", "_id"};
//        } else {
//            strArr = new String[]{"_data", "mime_type", "volume_name", "_id"};
//        }
//        int i = 0;
//        Cursor query = this.mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), strArr, "mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=?", new String[]{mimeTypeFromExtension, mimeTypeFromExtension2, mimeTypeFromExtension3, mimeTypeFromExtension4, mimeTypeFromExtension5, mimeTypeFromExtension6, mimeTypeFromExtension7, mimeTypeFromExtension8, mimeTypeFromExtension9}, null);
//        this.files = new File[query.getCount()];
//        query.moveToFirst();
//        if (query.getCount() != 0) {
//            do {
//                this.files[i] = new File(query.getString(query.getColumnIndexOrThrow("_data")));
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
//                this.date_and_time.add(simpleDateFormat.format(Long.valueOf(this.files[i].lastModified())));
//                this.name.add(this.files[i].getName());
//                File_DTO file_DTO = new File_DTO();
//                file_DTO.setPath(this.files[i].getPath());
//                file_DTO.setDate(simpleDateFormat.format(Long.valueOf(this.files[i].lastModified())));
//                file_DTO.setName(this.files[i].getName());
//                file_DTO.setId(query.getString(query.getColumnIndexOrThrow("_id")));
//                try {
//                    file_DTO.setVolumname(query.getString(query.getColumnIndex("volume_name")));
//                } catch (Exception unused) {
//                    file_DTO.setVolumname("");
//                }
//                this.sizedoccument += this.files[i].length();
//                file_DTO.setSize(new Ultil(this.mContext).bytesToHuman(this.files[i].length()));
//                file_DTO.setMinetype(query.getString(query.getColumnIndexOrThrow("mime_type")));
//                this.file_dtos.add(file_DTO);
//                i++;
//            } while (query.moveToNext());
//            return;
//        }
//        Log.d("TAG", "setDocs: ");
//    }

    public ArrayList<File_DTO> getzipwithMediastore() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        int i = 0;
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data"}, "mime_type=? OR mime_type=?", new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rar")}, null);
        this.files = new File[query.getCount()];
        query.moveToFirst();
        if (query.getCount() != 0) {
            do {
                this.files[i] = new File(query.getString(query.getColumnIndexOrThrow("_data")));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
                if (this.files[i].getPath().contains("%Bin")) {
                    Log.d(UStats.TAG, "getzipwithMediastore: ");
                } else {
                    File_DTO file_DTO = new File_DTO();
                    file_DTO.setPath(this.files[i].getPath());
                    file_DTO.setDate(simpleDateFormat.format(Long.valueOf(this.files[i].lastModified())));
                    file_DTO.setName(this.files[i].getName());
                    file_DTO.setSize(new Ultil(this.mContext).bytesToHuman(this.files[i].length()));
                    arrayList.add(file_DTO);
                }
                i++;
            } while (query.moveToNext());
        } else {
            Log.e("TAG", "getzipwithMediastore: ");
        }
        return arrayList;
    }

    public ArrayList<File_DTO> datadocs() {
        if (this.file_dtos.isEmpty()) {
            setDocs();
            return this.file_dtos;
        }
        return this.file_dtos;
    }

    public void sysn() {
        setDocs();
    }

    public void sortCollectionsBySize() {
        sortBySize(this.files);
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        int i = 0;
        while (true) {
            File[] fileArr = this.files;
            if (i >= fileArr.length) {
                return;
            }
            this.name.add(fileArr[i].getName());
            this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
            i++;
        }
    }

    public void sortCollectionsByName() {
        sortByName(this.files);
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        int i = 0;
        while (true) {
            File[] fileArr = this.files;
            if (i >= fileArr.length) {
                return;
            }
            this.name.add(fileArr[i].getName());
            this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
            i++;
        }
    }

    public void sortCollectionsByDate() {
        sortByDate(this.files);
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        int i = 0;
        while (true) {
            File[] fileArr = this.files;
            if (i >= fileArr.length) {
                return;
            }
            this.name.add(fileArr[i].getName());
            this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
            i++;
        }
    }

    public void setFavourites(Context context) {
        int i = 0;
        Set<String> stringSet = context.getSharedPreferences("favourites", 0).getStringSet("key", null);
        if (stringSet != null) {
            this.files = new File[stringSet.size()];
        } else {
            this.files = new File[0];
        }
        this.date_and_time = new ArrayList();
        this.name = new ArrayList();
        if (stringSet != null) {
            for (String str : stringSet) {
                this.files[i] = new File(str);
                this.date_and_time.add(new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(this.files[i].lastModified())));
                this.name.add(this.files[i].getName());
                i++;
            }
        }
    }

    public ArrayList<File_DTO> getfiletxt() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (!this.file_dtos.isEmpty()) {
            for (int i = 0; i < this.file_dtos.size(); i++) {
                if (this.file_dtos.get(i).getPath().toLowerCase().contains(".txt")) {
                    arrayList.add(this.file_dtos.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getfidocx() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (!this.file_dtos.isEmpty()) {
            for (int i = 0; i < this.file_dtos.size(); i++) {
                if (this.file_dtos.get(i).getPath().toLowerCase().contains(".docx") || this.file_dtos.get(i).getPath().toLowerCase().contains(".doc")) {
                    arrayList.add(this.file_dtos.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getfileppt() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (!this.file_dtos.isEmpty()) {
            for (int i = 0; i < this.file_dtos.size(); i++) {
                if (this.file_dtos.get(i).getPath().toLowerCase().contains(".pptx") || this.file_dtos.get(i).getPath().toLowerCase().contains(".ppt")) {
                    arrayList.add(this.file_dtos.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getfilexls() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (!this.file_dtos.isEmpty()) {
            for (int i = 0; i < this.file_dtos.size(); i++) {
                if (this.file_dtos.get(i).getPath().toLowerCase().contains(".xlsx") || this.file_dtos.get(i).getPath().toLowerCase().contains(".xls") || this.file_dtos.get(i).getPath().toLowerCase().contains(".csv")) {
                    arrayList.add(this.file_dtos.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getaps() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (!this.file_dtos.isEmpty()) {
            for (int i = 0; i < this.file_dtos.size(); i++) {
                if (this.file_dtos.get(i).getPath().endsWith(".apk") || this.file_dtos.get(i).getPath().endsWith(".apks")) {
                    arrayList.add(this.file_dtos.get(i));
                }
            }
        }
        Log.d(UStats.TAG, "getaps: " + arrayList.size());
        return arrayList;
    }

    public ArrayList<File_DTO> getfilepdf() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < this.file_dtos.size(); i++) {
            if (this.file_dtos.get(i).getPath().toLowerCase().contains(".pdf")) {
                arrayList.add(this.file_dtos.get(i));
            }
        }
        return arrayList;
    }

    public ArrayList<Drawable> drawables() {
        if (this.drawables.isEmpty()) {
            readAllAppssss(this.mContext);
        }
        return this.drawables;
    }

    public ArrayList<File_DTO> readAllAppssss(Context context) {
        PackageInfo packageInfo;
        this.drawables = new ArrayList<>();
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 26) {
            LauncherApps launcherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
            for (UserHandle userHandle : launcherApps.getProfiles()) {
                for (LauncherActivityInfo launcherActivityInfo : launcherApps.getActivityList(null, userHandle)) {
                    String packageName = launcherActivityInfo.getComponentName().getPackageName();
                    launcherActivityInfo.getName();
                    int i = launcherActivityInfo.getApplicationInfo().flags;
                    String charSequence = launcherActivityInfo.getLabel().toString();
                    try {
                        packageInfo = this.mContext.getPackageManager().getPackageInfo(packageName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        packageInfo = null;
                    }
                    String str = packageInfo.versionName;
                    long j = packageInfo.lastUpdateTime;
                    String str2 = getsizeappInstalled1(packageName, context);
                    File_DTO file_DTO = new File_DTO();
                    file_DTO.setName(charSequence);
                    file_DTO.setPackagename(packageName);
                    file_DTO.setSize(str2);
                    file_DTO.setDate(getdateInstalled(packageName, context));
                    file_DTO.setAbumname(str);
                    file_DTO.setPath("");
                    arrayList.add(file_DTO);
                    this.drawables.add(launcherActivityInfo.getIcon(200));
                }
            }
        } else {
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            intent.addCategory("android.intent.category.LAUNCHER");
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
                String str3 = resolveInfo.activityInfo.packageName;
                String str4 = resolveInfo.activityInfo.name;
                int i2 = resolveInfo.activityInfo.applicationInfo.flags;
                resolveInfo.loadLabel(context.getPackageManager()).toString();
                String str5 = getsizeappInstalled1(str3, context);
                File_DTO file_DTO2 = new File_DTO();
                file_DTO2.setName(str4);
                file_DTO2.setPackagename(str3);
                file_DTO2.setAbumname(String.valueOf(resolveInfo.providerInfo));
                file_DTO2.setSize(str5);
                file_DTO2.setDate(getdateInstalled(str3, context));
                arrayList.add(file_DTO2);
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getallapp() {
        this.file_apk = new ArrayList<>();
        ArrayList<File_DTO> arrayList = getallfileindecive();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getPath().toLowerCase().contains(".apk")) {
                this.file_apk.add(arrayList.get(i));
            }
        }
        return this.file_apk;
    }

    public ArrayList<File_DTO> getzips2() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        ArrayList<File_DTO> arrayList2 = getallfileindecive();
        for (int i = 0; i < arrayList2.size(); i++) {
            if (arrayList2.get(i).getPath().contains(".zip") || arrayList2.get(i).getPath().contains(".rar") || arrayList2.get(i).getPath().contains(".jar") || arrayList2.get(i).getPath().contains(".bin")) {
                arrayList.add(arrayList2.get(i));
            }
        }
        return arrayList;
    }

    private static String getsizeappInstalled1(String str, Context context) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        File file = new File(applicationInfo.publicSourceDir);
        String bytesToHuman = new Ultil(context).bytesToHuman(file.length());
        Log.d("TAG!", "getsizeappInstalled1: " + file.getName());
        return bytesToHuman;
    }

    private static String getdateInstalled(String str, Context context) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        return new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(new File(applicationInfo.publicSourceDir).lastModified()));
    }
}
