package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.Iterator;
import java.util.List;


public class PackagesInfo {
    private List appList;

    public PackagesInfo(Context context) {
        this.appList = context.getApplicationContext().getPackageManager().getInstalledApplications(0);
    }

    public PackagesInfo(Context context, String str) {
        this.appList = context.getApplicationContext().getPackageManager().getInstalledApplications(128);
    }

    public ApplicationInfo getInfo(String str) {
        ApplicationInfo applicationInfo = null;
        if (str != null) {
            Iterator it = this.appList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                applicationInfo = (ApplicationInfo) it.next();
                String str2 = applicationInfo.processName;
                if (str.equals(str2)) {
                    Log.d("SHORT: ", str + " " + str2);
                    break;
                }
            }
        }
        return applicationInfo;
    }
}
