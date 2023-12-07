package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Constants;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.RAMBooster;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.CleanListener;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.ScanListener;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.tasks.CleanTask;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.tasks.MemoryScanner;


public class LightService extends IntentService implements Constants {
    public static boolean alreadyRunning = false;

    public LightService() {
        super(Constants.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(Constants.ACTION_SCAN)) {
            if (RAMBooster.isDEBUG()) {
                Log.d(Constants.TAG, "Start scanning task");
            }
            ScanListener scanListener = RAMBooster.getScanListener();
            if (scanListener != null) {
                new Thread(new MemoryScanner(getApplicationContext(), scanListener)).start();
            } else if (RAMBooster.isDEBUG()) {
                Log.d(Constants.TAG, "Cannot start scanning task, listener is empty. Skip");
            }
        } else if (intent.getAction().equals(Constants.ACTION_CLEAN)) {
            if (RAMBooster.isDEBUG()) {
                Log.d(Constants.TAG, "Start cleaning task");
            }
            CleanListener cleanListener = RAMBooster.getCleanListener();
            if (cleanListener != null) {
                new Thread(new CleanTask(getApplicationContext(), RAMBooster.getAppProcessInfos(), cleanListener)).start();
            } else if (RAMBooster.isDEBUG()) {
                Log.d(Constants.TAG, "Cannot start cleaning task, listener is empty. Skip");
            }
        }
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alreadyRunning = true;
    }

    @Override
    public void onDestroy() {
        alreadyRunning = false;
        if (RAMBooster.isDEBUG()) {
            Log.d(Constants.TAG, "Service disabled");
        }
        super.onDestroy();
    }
}
