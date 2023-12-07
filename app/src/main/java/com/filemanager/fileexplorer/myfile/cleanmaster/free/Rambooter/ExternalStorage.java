package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;


public class ExternalStorage {
    public static final String EXTERNAL_SD_CARD = "externalSdCard";
    public static final String SD_CARD = "sdCard";

    public static boolean isAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return "mounted".equals(externalStorageState) || "mounted_ro".equals(externalStorageState);
    }

    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public static boolean isWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static Map<String, File> getAllStorageLocations() {
        HashMap hashMap = new HashMap(10);
        ArrayList arrayList = new ArrayList(10);
        ArrayList arrayList2 = new ArrayList(10);
        arrayList.add("/mnt/sdcard");
        arrayList2.add("/mnt/sdcard");
        try {
            File file = new File("/proc/mounts");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("/dev/block/vold/")) {
                        String str = nextLine.split(" ")[1];
                        if (!str.equals("/mnt/sdcard")) {
                            arrayList.add(str);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File file2 = new File("/system/etc/vold.fstab");
            if (file2.exists()) {
                Scanner scanner2 = new Scanner(file2);
                while (scanner2.hasNext()) {
                    String nextLine2 = scanner2.nextLine();
                    if (nextLine2.startsWith("dev_mount")) {
                        String str2 = nextLine2.split(" ")[2];
                        if (str2.contains(":")) {
                            str2 = str2.substring(0, str2.indexOf(":"));
                        }
                        if (!str2.equals("/mnt/sdcard")) {
                            arrayList2.add(str2);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        int i = 0;
        while (i < arrayList.size()) {
            if (!arrayList2.contains((String) arrayList.get(i))) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        arrayList2.clear();
        ArrayList arrayList3 = new ArrayList(10);
        Iterator it = arrayList.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            String str3 = SD_CARD;
            if (!hasNext) {
                break;
            }
            File file3 = new File((String) it.next());
            if (file3.exists() && file3.isDirectory() && file3.canWrite()) {
                File[] listFiles = file3.listFiles();
                String str4 = "[";
                if (listFiles != null) {
                    for (File file4 : listFiles) {
                        str4 = str4 + file4.getName().hashCode() + ":" + file4.length() + ", ";
                    }
                }
                String str5 = str4 + "]";
                if (!arrayList3.contains(str5)) {
                    String str6 = "sdCard_" + hashMap.size();
                    if (hashMap.size() != 0) {
                        str3 = hashMap.size() == 1 ? EXTERNAL_SD_CARD : str6;
                    }
                    arrayList3.add(str5);
                    hashMap.put(str3, file3);
                }
            }
        }
        arrayList.clear();
        if (hashMap.isEmpty()) {
            hashMap.put(SD_CARD, Environment.getExternalStorageDirectory());
        }
        return hashMap;
    }
}
