package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test.getallrecentAPP;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.Calendar;
import java.util.List;


public class UStats {
    public static final String TAG = "UStats";

    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        calendar.add(5, -1);
        return usageStatsManager.queryUsageStats(0, calendar.getTimeInMillis(), timeInMillis);
    }
}
