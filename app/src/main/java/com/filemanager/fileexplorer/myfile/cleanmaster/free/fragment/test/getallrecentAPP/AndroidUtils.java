package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test.getallrecentAPP;

import android.os.Build;


public class AndroidUtils {
    private static final String RECENT_ACTIVITY;

    static {
        if (Build.VERSION.SDK_INT > 21) {
            RECENT_ACTIVITY = "com.android.systemui.recents.RecentsActivity";
        } else if (Build.VERSION.SDK_INT > 17) {
            RECENT_ACTIVITY = "com.android.systemui.recent.RecentsActivity";
        } else {
            RECENT_ACTIVITY = "com.android.internal.policy.impl.RecentApplicationDialog";
        }
    }

    public static boolean isRecentActivity(String str) {
        return RECENT_ACTIVITY.equalsIgnoreCase(str);
    }
}
