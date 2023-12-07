package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test.getallrecentAPP;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;


public class AppLockService {
    public String getRecentApps(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            UsageEvents queryEvents = ((UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE)).queryEvents(System.currentTimeMillis() - 30000, System.currentTimeMillis() + 10000);
            UsageEvents.Event event = new UsageEvents.Event();
            while (queryEvents.hasNextEvent()) {
                queryEvents.getNextEvent(event);
            }
            if (TextUtils.isEmpty(event.getPackageName()) || event.getEventType() != 1) {
                return "";
            }
            if (AndroidUtils.isRecentActivity(event.getClassName())) {
                return event.getClassName();
            }
            return event.getPackageName();
        }
        ComponentName componentName = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity;
        if (AndroidUtils.isRecentActivity(componentName.getClassName())) {
            return componentName.getClassName();
        }
        return componentName.getPackageName();
    }
}
