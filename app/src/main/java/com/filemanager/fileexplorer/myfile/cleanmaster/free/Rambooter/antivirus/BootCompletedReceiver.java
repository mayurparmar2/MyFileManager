package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootCompletedReceiver extends BroadcastReceiver {
    final String _logTag = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (StaticTools.isServiceRunning(context, MonitorShieldService.class)) {
            return;
        }
        context.startService(new Intent(context, MonitorShieldService.class));
    }
}
