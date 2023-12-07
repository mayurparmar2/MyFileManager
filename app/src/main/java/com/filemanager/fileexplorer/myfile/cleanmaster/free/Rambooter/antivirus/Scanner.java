package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Scanner {
    Scanner() {
    }

    public static Set<GoodPackageResultData> scanForWhiteListedApps(List<PackageInfo> list, Set<PackageData> set, Set<GoodPackageResultData> set2) {
        HashSet hashSet = new HashSet();
        for (PackageData packageData : set) {
            getPackagesByNameFilter(list, packageData.getPackageName(), hashSet);
            set2.addAll(hashSet);
        }
        return set2;
    }

    public static boolean isAppWhiteListed(String str, Set<PackageData> set) {
        for (PackageData packageData : set) {
            if (StaticTools.stringMatchesMask(str, packageData.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static Set<IProblem> scanForBlackListedActivityApps(List<PackageInfo> list, Set<PackageData> set, Set<IProblem> set2) {
        ArrayList<ActivityInfo> arrayList = new ArrayList();
        for (PackageInfo packageInfo : list) {
            for (PackageData packageData : set) {
                getActivitiesByNameFilter(packageInfo, packageData.getPackageName(), arrayList);
                if (arrayList.size() > 0) {
                    AppProblem badPackageResultByPackageName = getBadPackageResultByPackageName(set2, packageInfo.packageName);
                    if (badPackageResultByPackageName == null) {
                        badPackageResultByPackageName = new AppProblem(packageInfo.packageName);
                        set2.add(badPackageResultByPackageName);
                    }
                    for (ActivityInfo activityInfo : arrayList) {
                        badPackageResultByPackageName.addActivityData(new ActivityData(activityInfo.name));
                    }
                }
            }
        }
        return set2;
    }

    public static AppProblem scanForBlackListedActivityApp(PackageInfo packageInfo, AppProblem appProblem, Set<PackageData> set, List<ActivityInfo> list) {
        for (PackageData packageData : set) {
            getActivitiesByNameFilter(packageInfo, packageData.getPackageName(), list);
            if (list.size() > 0) {
                for (ActivityInfo activityInfo : list) {
                    appProblem.addActivityData(new ActivityData(activityInfo.packageName));
                }
            }
        }
        return appProblem;
    }

    public static AppProblem scanForBlackListedActivityApp(Context context, String str, Set<PackageData> set) {
        PackageInfo packageInfo;
        try {
            packageInfo = StaticTools.getPackageInfo(context, str, 1);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return null;
        }
        AppProblem appProblem = new AppProblem(packageInfo.packageName);
        scanForBlackListedActivityApp(packageInfo, appProblem, set, new ArrayList());
        return appProblem;
    }

    public static Set<IProblem> scanForSuspiciousPermissionsApps(List<PackageInfo> list, Set<PermissionData> set, Set<IProblem> set2) {
        for (PackageInfo packageInfo : list) {
            AppProblem badPackageResultByPackageName = getBadPackageResultByPackageName(set2, packageInfo.packageName);
            if (badPackageResultByPackageName == null) {
                badPackageResultByPackageName = new AppProblem(packageInfo.packageName);
            }
            scanForSuspiciousPermissionsApp(packageInfo, badPackageResultByPackageName, set);
            if (badPackageResultByPackageName.getPermissionData().size() > 0) {
                set2.add(badPackageResultByPackageName);
            }
        }
        return set2;
    }

    public static AppProblem scanForSuspiciousPermissionsApp(PackageInfo packageInfo, AppProblem appProblem, Set<PermissionData> set) {
        for (PermissionData permissionData : set) {
            if (StaticTools.packageInfoHasPermission(packageInfo, permissionData.getPermissionName())) {
                appProblem.addPermissionData(permissionData);
            }
        }
        return appProblem;
    }

    public static AppProblem scanForSuspiciousPermissionsApp(Context context, String str, Set<PermissionData> set) {
        PackageInfo packageInfo;
        try {
            packageInfo = StaticTools.getPackageInfo(context, str, 1);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return null;
        }
        return scanForSuspiciousPermissionsApp(packageInfo, new AppProblem(packageInfo.packageName), set);
    }

    public static AppProblem getBadPackageResultByPackageName(Set<IProblem> set, String str) {
        for (IProblem iProblem : set) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                AppProblem appProblem = (AppProblem) iProblem;
                if (appProblem.getPackageName().equals(str)) {
                    return appProblem;
                }
            }
        }
        return null;
    }

    public static Set<GoodPackageResultData> getPackagesByNameFilter(List<PackageInfo> list, String str, Set<GoodPackageResultData> set) {
        set.clear();
        boolean z = str.charAt(str.length() - 1) == '*';
        for (int i = 0; i < list.size(); i++) {
            PackageInfo packageInfo = list.get(i);
            if (StaticTools.stringMatchesMask(packageInfo.packageName, str)) {
                set.add(new GoodPackageResultData(packageInfo));
                if (!z) {
                    break;
                }
            }
        }
        return set;
    }

    public static List<ActivityInfo> getActivitiesByNameFilter(PackageInfo packageInfo, String str, List<ActivityInfo> list) {
        list.clear();
        if (packageInfo.activities == null) {
            return list;
        }
        if (str.charAt(str.length() - 1) == '*') {
            str = str.substring(0, str.length() - 2);
        }
        for (int i = 0; i < packageInfo.activities.length; i++) {
            ActivityInfo activityInfo = packageInfo.activities[i];
            if (activityInfo.name.startsWith(str)) {
                list.add(activityInfo);
            }
        }
        return list;
    }

    public static Set<IProblem> scanInstalledAppsFromGooglePlay(Context context, List<PackageInfo> list, Set<IProblem> set) {
        for (PackageInfo packageInfo : list) {
            if (!StaticTools.checkIfAppWasInstalledThroughGooglePlay(context, packageInfo.packageName)) {
                AppProblem badPackageResultByPackageName = getBadPackageResultByPackageName(set, packageInfo.packageName);
                if (badPackageResultByPackageName == null) {
                    badPackageResultByPackageName = new AppProblem(packageInfo.packageName);
                    set.add(badPackageResultByPackageName);
                }
                badPackageResultByPackageName.setInstalledThroughGooglePlay(false);
            } else {
                AppProblem badPackageResultByPackageName2 = getBadPackageResultByPackageName(set, packageInfo.packageName);
                if (badPackageResultByPackageName2 != null) {
                    badPackageResultByPackageName2.setInstalledThroughGooglePlay(true);
                }
            }
        }
        return set;
    }


    public static AppProblem scanInstalledAppFromGooglePlay(Context context, AppProblem appProblem) {
        if (!StaticTools.checkIfAppWasInstalledThroughGooglePlay(context, appProblem.getPackageName())) {
            appProblem.setInstalledThroughGooglePlay(false);
        } else {
            appProblem.setInstalledThroughGooglePlay(true);
        }
        return appProblem;
    }

    public static Set<IProblem> scanSystemProblems(Context context, UserWhiteList userWhiteList, Set<IProblem> set) {
        if (StaticTools.checkIfUSBDebugIsEnabled(context) && !userWhiteList.checkIfSystemPackageInList(DebugUSBEnabledProblem.class)) {
            set.add(new DebugUSBEnabledProblem());
        }
        if (StaticTools.checkIfUnknownAppIsEnabled(context) && !userWhiteList.checkIfSystemPackageInList(UnknownAppEnabledProblem.class)) {
            set.add(new UnknownAppEnabledProblem());
        }
        return set;
    }
}
