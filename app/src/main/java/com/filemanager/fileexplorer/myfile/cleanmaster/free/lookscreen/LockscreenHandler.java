package com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class LockscreenHandler extends AppCompatActivity implements ComponentCallbacks2 {
    private static boolean wentToBg = false;
    private String packageName = "";
    private String TAG = "Fayaz";

    @Override
    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        Log.d("topActivity", "CURRENT Activity ::" + runningTasks.get(0).topActivity.getClassName());
        if (runningTasks != null && runningTasks.size() > 0) {
            this.packageName = runningTasks.get(0).topActivity.getPackageName();
        }
        if (this.packageName.equals(getPackageName()) || i != 20) {
            return;
        }

        wentToBg = true;
        String str = this.TAG;
        Log.d(str, "wentToBg: " + wentToBg);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!wentToBg || FayazSP.getString("password", null) == null) {
            return;
        }
        wentToBg = false;
        String str = this.TAG;
        Log.d(str, "wentToBg: " + wentToBg);
        EasyLock.checkPassword(this);
    }
}
