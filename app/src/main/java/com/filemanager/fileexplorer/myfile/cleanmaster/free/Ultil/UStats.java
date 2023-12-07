package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class UStats {
    public static final String TAG = "AAA";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    public static void getStats(Context context) {
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        calendar.add(1, -1);
        long timeInMillis2 = calendar.getTimeInMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("Range start:");
        SimpleDateFormat simpleDateFormat = dateFormat;
        sb.append(simpleDateFormat.format(Long.valueOf(timeInMillis2)));
        Log.d(TAG, sb.toString());
        Log.d(TAG, "Range end:" + simpleDateFormat.format(Long.valueOf(timeInMillis)));
        UsageEvents queryEvents = ((UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE)).queryEvents(timeInMillis2, timeInMillis);
        while (queryEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            queryEvents.getNextEvent(event);
            Log.d(TAG, "Event: " + event.getPackageName() + "\t" + event.getTimeStamp());
        }
    }

    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usageStatsManager = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        calendar.add(1, -1);
        long timeInMillis2 = calendar.getTimeInMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("Range start:");
        SimpleDateFormat simpleDateFormat = dateFormat;
        sb.append(simpleDateFormat.format(Long.valueOf(timeInMillis2)));
        Log.d(TAG, sb.toString());
        Log.d(TAG, "Range end:" + simpleDateFormat.format(Long.valueOf(timeInMillis)));
        return usageStatsManager.queryUsageStats(0, timeInMillis2, timeInMillis);
    }

    public void printUsageStats(List<UsageStats> list) {
        for (UsageStats usageStats : list) {
            Log.d(TAG, "Pkg: " + usageStats.getPackageName() + "\tForegroundTime: " + usageStats.getTotalTimeInForeground());
            usageStats.getPackageName();
            usageStats.getTotalTimeInForeground();
        }
    }

    public void printCurrentUsageStatus(Context context) {
        printUsageStats(getUsageStatsList(context));
    }

    public static String printUsageStatus(Context context) {
        return printUsageStatss(getUsageStatsList(context));
    }

    private static UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    public static String printUsageStatss(List<UsageStats> list) {
        String str = null;
        for (UsageStats usageStats : list) {
            Log.d(TAG, "Pkg: " + usageStats.getPackageName() + "\tForegroundTime: " + usageStats.getTotalTimeInForeground());
            str = "Pkg: " + usageStats.getPackageName() + "\tForegroundTime: " + usageStats.getTotalTimeInForeground();
        }
        return str;
    }

//    public static void getUsageStatistics(long j, long j2, Context context) {
//        int i;
//        int i2;
//        HashMap hashMap = new HashMap();
//        HashMap hashMap2 = new HashMap();
//        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        if (usageStatsManager != null) {
//            UsageEvents queryEvents = usageStatsManager.queryEvents(j, j2);
//            while (true) {
//                i = 1;
//                if (!queryEvents.hasNextEvent()) {
//                    break;
//                }
//                UsageEvents.Event event = new UsageEvents.Event();
//                queryEvents.getNextEvent(event);
//                if (event.getEventType() == 1 || event.getEventType() == 2) {
//                    String packageName = event.getPackageName();
//                    if (hashMap.get(packageName) == null) {
//                        hashMap.put(packageName, new AppUsageInfo(packageName));
//                        hashMap2.put(packageName, new ArrayList());
//                    }
//                    ((List) hashMap2.get(packageName)).add(event);
//                }
//            }
//            for (Map.Entry entry : hashMap2.entrySet()) {
//                int size = ((List) entry.getValue()).size();
//                if (size > i) {
//                    int i3 = 0;
//                    while (i3 < size - 1) {
//                        UsageEvents.Event event2 = (UsageEvents.Event) ((List) entry.getValue()).get(i3);
//                        int i4 = i3 + 1;
//                        UsageEvents.Event event3 = (UsageEvents.Event) ((List) entry.getValue()).get(i4);
//                        if (event3.getEventType() == i || event2.getEventType() == i) {
//                            ((AppUsageInfo) hashMap.get(event3.getPackageName())).launchCount += i;
//                        }
//                        if (event2.getEventType() == i && event3.getEventType() == 2) {
//                            i2 = i4;
//                            ((AppUsageInfo) hashMap.get(event2.getPackageName())).timeInForeground += event3.getTimeStamp() - event2.getTimeStamp();
//                        } else {
//                            i2 = i4;
//                        }
//                        i3 = i2;
//                        i = 1;
//                    }
//                }
//                if (((UsageEvents.Event) ((List) entry.getValue()).get(0)).getEventType() == 2) {
//                    ((AppUsageInfo) hashMap.get(((UsageEvents.Event) ((List) entry.getValue()).get(0)).getPackageName())).timeInForeground += ((UsageEvents.Event) ((List) entry.getValue()).get(0)).getTimeStamp() - j;
//                }
//                int i5 = size - 1;
//                if (((UsageEvents.Event) ((List) entry.getValue()).get(i5)).getEventType() == 1) {
//                    ((AppUsageInfo) hashMap.get(((UsageEvents.Event) ((List) entry.getValue()).get(i5)).getPackageName())).timeInForeground += j2 - ((UsageEvents.Event) ((List) entry.getValue()).get(i5)).getTimeStamp();
//                }
//                i = 1;
//            }
//            ArrayList arrayList = new ArrayList(hashMap.values());
//            Log.d(TAG, "getUsageStatistics: " + hashMap.size());
//            Iterator it = arrayList.iterator();
//            while (it.hasNext()) {
//                Log.d(TAG, "getUsageStatistics: " + ((AppUsageInfo) it.next()).appName);
//            }
//            return;
//        }
//        Toast.makeText(context, "Sorry...", Toast.LENGTH_SHORT).show();
//    }


    static class AppUsageInfo {
        Drawable appIcon;
        String appName;
        int launchCount;
        String packageName;
        long timeInForeground;

        AppUsageInfo(String str) {
            this.packageName = str;
        }
    }
}
