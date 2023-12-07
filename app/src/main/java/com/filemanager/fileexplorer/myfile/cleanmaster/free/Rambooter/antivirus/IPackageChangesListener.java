package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Intent;


public interface IPackageChangesListener {
    void OnPackageAdded(Intent intent);

    void OnPackageRemoved(Intent intent);
}
