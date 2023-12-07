package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.tasks;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Constants;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.MemoryFreedPredication;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.ProcessInfo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.RAMBooster;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.ScanListener;

import java.util.ArrayList;
import java.util.List;


public class MemoryScanner implements Runnable, Constants {
    private Context context;
    private ScanListener listener;
    RAMBooster ramBooster;

    public MemoryScanner(Context context, ScanListener scanListener) {
        this.context = context;
        this.listener = scanListener;
        this.ramBooster = new RAMBooster(context);
    }

    @Override
    public void run() {
        if (RAMBooster.isDEBUG()) {
            Log.d(Constants.TAG, "Scanner started...");
        }
        RAMBooster.setAppProcessInfos(createProcessInfosFromRunningApps(filterProcesses(((ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses())));
        long calculateAvailableRAM = Ultil.calculateAvailableRAM(this.context) / 1048576;
        long calculateTotalRAM = Ultil.calculateTotalRAM() / 1048576;
        if (RAMBooster.isDEBUG()) {
            Log.d(Constants.TAG, "Scanner finished");
        }
    }

    private List<ActivityManager.RunningAppProcessInfo> filterProcesses(List<ActivityManager.RunningAppProcessInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            try {
                ApplicationInfo applicationInfo = getApplicationInfo(runningAppProcessInfo.processName);
                if (RAMBooster.isDEBUG()) {
                    Log.d(Constants.TAG, "Scanner founded process: " + applicationInfo.packageName);
                }
                if (RAMBooster.mShouldCleanSystemApps() || (applicationInfo.flags & 1) != 1) {
                    if (!applicationInfo.packageName.equals(this.context.getPackageName())) {
                        arrayList.add(runningAppProcessInfo);
                    }
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return arrayList;
    }

    private ApplicationInfo getApplicationInfo(String str) throws PackageManager.NameNotFoundException {
        return this.context.getPackageManager().getApplicationInfo(str, 0);
    }

    private List<ProcessInfo> createProcessInfosFromRunningApps(List<ActivityManager.RunningAppProcessInfo> list) {
        MemoryFreedPredication memoryFreedPredication = MemoryFreedPredication.getInstance(this.context);
        ArrayList arrayList = new ArrayList();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            ProcessInfo processInfo = new ProcessInfo(runningAppProcessInfo);
            processInfo.setMemoryUsage(memoryFreedPredication.calculateMemoryUsage(runningAppProcessInfo.pid));
            arrayList.add(processInfo);
        }
        return arrayList;
    }
}
