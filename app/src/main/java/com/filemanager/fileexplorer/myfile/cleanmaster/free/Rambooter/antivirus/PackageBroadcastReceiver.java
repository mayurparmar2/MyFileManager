package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class PackageBroadcastReceiver extends BroadcastReceiver {
    static IPackageChangesListener _listener;

    public static void setPackageBroadcastListener(IPackageChangesListener iPackageChangesListener) {
        _listener = iPackageChangesListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (_listener != null && "android.intent.action.PACKAGE_ADDED".equals(intent.getAction())) {
            _listener.OnPackageAdded(intent);
        }
        if (_listener == null || !"android.intent.action.PACKAGE_REMOVED".equals(intent.getAction())) {
            return;
        }
        _listener.OnPackageRemoved(intent);
    }
}
