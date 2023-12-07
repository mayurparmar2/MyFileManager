package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.ActivityManager;


public class ProcessInfo {
    private int memoryUsage;
    private int pid;
    private String processName;

    public ProcessInfo(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        this.pid = runningAppProcessInfo.pid;
        this.processName = runningAppProcessInfo.processName;
    }

    public long getSize() {
        return this.memoryUsage;
    }

    public void setMemoryUsage(int i) {
        this.memoryUsage = i;
    }

    public String getProcessName() {
        return this.processName;
    }

    public int getPid() {
        return this.pid;
    }
}
