package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
 
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Pattern;


public class DeviceMemoryInfo {
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");
    private static Context context;

    public DeviceMemoryInfo(Context context2) {
        context = context2;
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getAvailableBlocks() * statFs.getBlockSize();
    }

    public static long getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return statFs.getBlockCount() * statFs.getBlockSize();
    }

    public static String getFreeInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return Formatter.formatFileSize(context, Math.abs((statFs.getBlockCount() * statFs.getBlockSize()) - (statFs.getAvailableBlocks() * statFs.getBlockSize())));
    }

    public static long getAvailableExternalMemorySize(File file) {
        if (externalMemoryAvailable()) {
            StatFs statFs = new StatFs(file.getPath());
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        }
        return 0L;
    }

    public static long getTotalExternalMemorySize(File file) {
        if (externalMemoryAvailable()) {
            StatFs statFs = new StatFs(file.getPath());
            return statFs.getBlockCount() * statFs.getBlockSize();
        }
        return 0L;
    }

    public static String getFreeExternalMemorySize(File file) {
        if (externalMemoryAvailable()) {
            StatFs statFs = new StatFs(file.getPath());
            return Formatter.formatFileSize(context, Math.abs((statFs.getBlockCount() * statFs.getBlockSize()) - (statFs.getAvailableBlocks() * statFs.getBlockSize())));
        }
        return "Error";
    }

    public static String[] getStorageDirectories() {
        HashSet hashSet = new HashSet();
        String str = System.getenv("EXTERNAL_STORAGE");
        String str2 = System.getenv("SECONDARY_STORAGE");
        if (TextUtils.isEmpty(System.getenv("EMULATED_STORAGE_TARGET"))) {
            TextUtils.isEmpty(str);
        } else {
            String str3 = "";
            if (Build.VERSION.SDK_INT >= 17) {
                String[] split = DIR_SEPORATOR.split(Environment.getExternalStorageDirectory().getAbsolutePath());
                boolean z = true;
                String str4 = split[split.length - 1];
                try {
                    Integer.valueOf(str4);
                } catch (NumberFormatException unused) {
                    z = false;
                }
                if (z) {
                    str3 = str4;
                }
            }
            TextUtils.isEmpty(str3);
        }
        if (!TextUtils.isEmpty(str2)) {
            Collections.addAll(hashSet, str2.split(File.pathSeparator));
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    public static String formatMemSize(long j, int i) {
        if (1024 > j) {
            return String.valueOf(j) + " B";
        } else if (1048576 > j) {
            return String.valueOf(String.format("%." + i + "f", Float.valueOf(((float) j) / 1024.0f))) + " KB";
        } else if (1073741824 > j) {
            return String.valueOf(String.format("%." + i + "f", Float.valueOf(((float) j) / 1048576.0f))) + " MB";
        } else {
            return String.valueOf(String.format("%.2f", Float.valueOf(((float) j) / 1.07374195E9f))) + " GB";
        }
    }
}
