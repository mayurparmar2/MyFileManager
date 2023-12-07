package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.tasks;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Constants;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.ProcessInfo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.RAMBooster;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.CleanListener;

import java.util.List;


public class CleanTask implements Runnable, Constants {
    private List<ProcessInfo> appProcessInfos;
    private Context context;
    private CleanListener listener;

    public CleanTask(Context context, List<ProcessInfo> list, CleanListener cleanListener) {
        this.context = context;
        this.appProcessInfos = list;
        this.listener = cleanListener;
    }

    @Override
    public void run() {
        if (RAMBooster.isDEBUG()) {
            Log.d(Constants.TAG, "Cleaner started...");
        }
        try {
            Thread.sleep(500L);
        } catch (InterruptedException unused) {
        }
        List<ProcessInfo> list = this.appProcessInfos;
        if (list != null) {
            killAppProcesses(list);
        }
        long calculateAvailableRAM = Ultil.calculateAvailableRAM(this.context) / 1048576;
        long calculateTotalRAM = Ultil.calculateTotalRAM() / 1048576;
        if (RAMBooster.isDEBUG()) {
            Log.d(Constants.TAG, "Cleaner finished");
        }
    }

    private void killAppProcesses(List<ProcessInfo> list) {
        for (ProcessInfo processInfo : list) {
            killBackgroundProcess(processInfo.getProcessName());
        }
    }

    private void killBackgroundProcess(String str) {
        ((ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(str);
    }
}
