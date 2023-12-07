package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class ApkUltil {
    private Context mContext;

    public ApkUltil(Context context) {
        this.mContext = context;
    }

    public HashMap<String, String> getAllInstalledApkFiles() {
        HashMap<String, String> hashMap = new HashMap<>();
        PackageManager packageManager = this.mContext.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        if (isValid(installedPackages)) {
            for (PackageInfo packageInfo : installedPackages) {
                ApplicationInfo applicationInfoFrom = getApplicationInfoFrom(packageManager, packageInfo);
                String str = applicationInfoFrom.packageName;
                String str2 = packageInfo.versionName;
                int i = packageInfo.versionCode;
                File file = new File(applicationInfoFrom.publicSourceDir);
                if (file.exists()) {
                    hashMap.put(str, file.getAbsolutePath());
                    Log.d("TAGW", str + " = " + file.getName());
                }
            }
        }
        return hashMap;
    }

    private boolean isValid(List<PackageInfo> list) {
        return (list == null || list.isEmpty()) ? false : true;
    }

    private ApplicationInfo getApplicationInfoFrom(PackageManager packageManager, PackageInfo packageInfo) {
        return packageInfo.applicationInfo;
    }

    public File getApkFile(String str) {
        File file = new File(getAllInstalledApkFiles().get(str));
        if (file.exists()) {
            return file;
        }
        return null;
    }
}
