package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces;


public interface Booster {
    void setCleanListener(CleanListener cleanListener);

    void setDebug(boolean z);

    void setScanListener(ScanListener scanListener);

    void startClean();

    void startScan(boolean z);
}
