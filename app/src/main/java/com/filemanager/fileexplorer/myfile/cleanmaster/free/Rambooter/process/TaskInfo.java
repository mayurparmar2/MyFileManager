package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;


public class TaskInfo {
    private ApplicationInfo appinfo;
    private boolean chceked;
    private CheckBox chkTask;
    private Drawable icon;
    public long mem;
    private String name;
    private PackagesInfo pkgInfo;
    private PackageManager pm;
    private ActivityManager.RunningAppProcessInfo runinfo;
    public long timeusedlast;
    private String title;
    public UsageStats usageStats;

    public long getTimeusedlast() {
        return this.timeusedlast;
    }

    public void setTimeusedlast(long j) {
        this.timeusedlast = j;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public String getName() {
        return this.name;
    }

    public Drawable getdraw() {
        return this.icon;
    }

    public void setName(String str) {
        this.name = str;
    }

    public UsageStats getUsageStats() {
        return this.usageStats;
    }

    public void setUsageStats(UsageStats usageStats) {
        this.usageStats = usageStats;
    }

    public TaskInfo(Context context, ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        this.appinfo = null;
        this.pkgInfo = null;
        this.title = null;
        this.runinfo = runningAppProcessInfo;
        this.pm = context.getApplicationContext().getPackageManager();
    }

    public TaskInfo() {
    }

    public TaskInfo(Context context, ApplicationInfo applicationInfo) {
        this.appinfo = null;
        this.pkgInfo = null;
        this.runinfo = null;
        this.title = null;
        this.appinfo = applicationInfo;
        this.pm = context.getApplicationContext().getPackageManager();
    }

    public void getAppInfo(PackagesInfo packagesInfo) {
        if (this.appinfo == null) {
            try {
                this.appinfo = this.pm.getApplicationInfo(this.runinfo.processName, 128);
            } catch (Exception unused) {
            }
        }
    }

    public int getIcon() {
        return this.appinfo.icon;
    }

    public String getPackageName() {
        return this.appinfo.packageName;
    }

    public String getTitle() {
        if (this.title == null) {
            try {
                this.title = this.appinfo.loadLabel(this.pm).toString();
            } catch (Exception unused) {
            }
        }
        return this.title;
    }

    public void setMem(int i) {
        this.mem = i;
    }

    public boolean isGoodProcess() {
        return this.appinfo != null;
    }

    public ApplicationInfo getAppinfo() {
        return this.appinfo;
    }

    public void setAppinfo(ApplicationInfo applicationInfo) {
        this.appinfo = applicationInfo;
    }

    public PackagesInfo getPkgInfo() {
        return this.pkgInfo;
    }

    public void setPkgInfo(PackagesInfo packagesInfo) {
        this.pkgInfo = packagesInfo;
    }

    public ActivityManager.RunningAppProcessInfo getRuninfo() {
        return this.runinfo;
    }

    public void setRuninfo(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        this.runinfo = runningAppProcessInfo;
    }

    public boolean isChceked() {
        return this.chceked;
    }

    public void setChceked(boolean z) {
        this.chceked = z;
    }

    public void setChkTask(CheckBox checkBox) {
        this.chkTask = checkBox;
    }
}
