package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.Booster;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.CleanListener;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.ScanListener;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.services.LightService;

import java.util.List;


public class RAMBooster implements Booster, Constants {
    private static boolean DEBUG = false;
    private static List<ProcessInfo> appProcessInfos = null;
    private static CleanListener mCleanListener = null;
    private static ScanListener mScanListener = null;
    private static boolean shouldCleanSystemApps = false;
    private Context context;

    public static boolean mShouldCleanSystemApps() {
        return shouldCleanSystemApps;
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    @Override
    public void setDebug(boolean z) {
        DEBUG = z;
    }

    public RAMBooster(Context context) {
        appProcessInfos = null;
        mScanListener = null;
        mCleanListener = null;
        shouldCleanSystemApps = false;
        DEBUG = false;
        this.context = context;
    }

    public static List<ProcessInfo> getAppProcessInfos() {
        return appProcessInfos;
    }

    public static void setAppProcessInfos(List<ProcessInfo> list) {
        appProcessInfos = list;
    }

    @Override
    public void setScanListener(ScanListener scanListener) {
        mScanListener = scanListener;
    }

    public static ScanListener getScanListener() {
        return mScanListener;
    }

    @Override
    public void setCleanListener(CleanListener cleanListener) {
        mCleanListener = cleanListener;
    }

    public static CleanListener getCleanListener() {
        return mCleanListener;
    }

    @Override
    public void startScan(boolean z) {
        if (!LightService.alreadyRunning) {
            shouldCleanSystemApps = z;
            Intent intent = new Intent(this.context, LightService.class);
            intent.setAction(Constants.ACTION_SCAN);
            this.context.startService(intent);
        } else if (DEBUG) {
            Log.d(Constants.TAG, "Already Scanning.Skip");
        }
    }

    @Override
    public void startClean() {
        Intent intent = new Intent(this.context, LightService.class);
        intent.setAction(Constants.ACTION_CLEAN);
        this.context.startService(intent);
    }
}
