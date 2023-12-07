package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.TextUtils;

import java.util.List;


public class MemoryFreedPredication {
    private static MemoryFreedPredication INSTANCE;
    private ActivityManager activityManager;

    public static MemoryFreedPredication getInstance(Context context) {
        MemoryFreedPredication memoryFreedPredication = INSTANCE;
        if (memoryFreedPredication != null) {
            return memoryFreedPredication;
        }
        MemoryFreedPredication memoryFreedPredication2 = new MemoryFreedPredication(context);
        INSTANCE = memoryFreedPredication2;
        return memoryFreedPredication2;
    }

    private MemoryFreedPredication(Context context) {
        this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    private int[] getPids(String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = this.activityManager.getRunningAppProcesses();
        int[] iArr = new int[runningAppProcesses.size()];
        int i = 0;
        if (TextUtils.isEmpty(str)) {
            while (i < runningAppProcesses.size()) {
                iArr[i] = runningAppProcesses.get(i).pid;
                i++;
            }
        } else {
            while (i < runningAppProcesses.size()) {
                if (runningAppProcesses.get(i).processName.equalsIgnoreCase(str)) {
                    iArr[i] = runningAppProcesses.get(i).pid;
                }
                i++;
            }
        }
        return iArr;
    }

    public int calculateMemoryUsage(int i) {
        Debug.MemoryInfo[] processMemoryInfo;
        int i2 = 0;
        for (Debug.MemoryInfo memoryInfo : this.activityManager.getProcessMemoryInfo(new int[]{i})) {
            i2 += memoryInfo.dalvikPss + memoryInfo.nativePss + memoryInfo.otherPss;
        }
        return i2 * 1024;
    }

    public static String formatFileSize(String str) {
        int i;
        String[] strArr = {"B", "KB", "MB", "GB"};
        double parseDouble = Double.parseDouble(str);
        if (parseDouble < 1024.0d) {
            return String.format("%s %s", 1, strArr[1]);
        }
        int i2 = 0;
        while (parseDouble >= 1024.0d && (i = i2 + 1) < 4) {
            parseDouble /= 1024.0d;
            i2 = i;
        }
        return String.format("%s %s", Double.valueOf(Math.round(parseDouble * 100.0d) / 100.0d), strArr[i2]);
    }
}
