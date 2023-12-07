package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.ProcessInfo;

import java.util.List;


public interface ScanListener {
    void onFinished(long j, long j2, List<ProcessInfo> list);

    void onStarted();
}
