package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.demo.example.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MonitorShieldService extends Service {
    static int _currentNotificationId;
    Set<PackageData> _blackListActivities;
    Set<PackageData> _blackListPackages;
    PackageBroadcastReceiver _packageBroadcastReceiver;
    Set<PermissionData> _suspiciousPermissions;
    Set<PackageData> _whiteListPackages;
    final String _logTag = "MonitorShieldService";
    private final IBinder _binder = new MonitorShieldLocalBinder();
    UserWhiteList _userWhiteList = null;
    MenacesCacheSet _menacesCacheSet = null;
    private int _appIcon = R.drawable.button_app;
    IClientInterface _clientInterface = null;


    public interface IClientInterface {
        void onMonitorFoundMenace(IProblem iProblem);

        void onScanResult(List<PackageInfo> list, Set<IProblem> set);
    }

    public Set<PackageData> getWhiteListPackages() {
        return this._whiteListPackages;
    }

    public Set<PackageData> getBlackListPackages() {
        return this._blackListPackages;
    }

    public Set<PackageData> getBlackListActivities() {
        return this._blackListActivities;
    }

    public Set<PermissionData> getSuspiciousPermissions() {
        return this._suspiciousPermissions;
    }

    public UserWhiteList getUserWhiteList() {
        return this._userWhiteList;
    }

    public MenacesCacheSet getMenacesCacheSet() {
        return this._menacesCacheSet;
    }

    public void registerClient(IClientInterface iClientInterface) {
        this._clientInterface = iClientInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this._packageBroadcastReceiver = new PackageBroadcastReceiver();
        PackageBroadcastReceiver.setPackageBroadcastListener(new IPackageChangesListener() {
            @Override
            public void OnPackageRemoved(Intent intent) {
            }

            @Override
            public void OnPackageAdded(Intent intent) {
                MonitorShieldService.this.scanApp(intent.getData().getSchemeSpecificPart());
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        registerReceiver(this._packageBroadcastReceiver, intentFilter);
        _loadDataFiles();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this._packageBroadcastReceiver);
        this._packageBroadcastReceiver = null;
        stopSelf();
        Log.d("CRESAN", "################## Service onDestroy command called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this._binder;
    }


    public class MonitorShieldLocalBinder extends Binder {
        public MonitorShieldLocalBinder() {
        }

        public MonitorShieldService getServiceInstance() {
            return MonitorShieldService.this;
        }
    }

    private void _loadDataFiles() {
        this._whiteListPackages = new HashSet();
        this._blackListPackages = new HashSet();
        this._blackListActivities = new HashSet();
        this._suspiciousPermissions = new HashSet();
        this._userWhiteList = new UserWhiteList(this);
        this._menacesCacheSet = new MenacesCacheSet(this);
        try {
            JSONArray jSONArray = new JSONObject(StaticTools.loadJSONFromAsset(this, "whiteList.json")).getJSONArray("data");
            for (int i = 0; i < jSONArray.length(); i++) {
                this._whiteListPackages.add(new PackageData(jSONArray.getJSONObject(i).getString("packageName")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jSONArray2 = new JSONObject(StaticTools.loadJSONFromAsset(this, "blackListPackages.json")).getJSONArray("data");
            for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                this._blackListPackages.add(new PackageData(jSONArray2.getJSONObject(i2).getString("packageName")));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        try {
            JSONArray jSONArray3 = new JSONObject(StaticTools.loadJSONFromAsset(this, "blackListActivities.json")).getJSONArray("data");
            for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                this._blackListActivities.add(new PackageData(jSONArray3.getJSONObject(i3).getString("packageName")));
            }
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        try {
            JSONArray jSONArray4 = new JSONObject(StaticTools.loadJSONFromAsset(this, "permissions.json")).getJSONArray("data");
            for (int i4 = 0; i4 < jSONArray4.length(); i4++) {
                JSONObject jSONObject = jSONArray4.getJSONObject(i4);
                this._suspiciousPermissions.add(new PermissionData(jSONObject.getString("permissionName"), jSONObject.getInt("dangerous")));
            }
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
    }

    protected boolean _checkIfPackageInWhiteList(String str, Set<PackageData> set) {
        for (PackageData packageData : set) {
            if (StaticTools.stringMatchesMask(str, packageData.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public void scanFileSystem() {
        List<PackageInfo> apps = StaticTools.getApps(this, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        List<PackageInfo> nonSystemApps = StaticTools.getNonSystemApps(this, apps);
        HashSet hashSet = new HashSet();
        List<PackageInfo> _removeWhiteListPackagesFromPackageList = _removeWhiteListPackagesFromPackageList(_removeWhiteListPackagesFromPackageList(nonSystemApps, this._whiteListPackages), ProblemsDataSetTools.getAppProblemsAsPackageDataList(this._userWhiteList));
        Scanner.scanForBlackListedActivityApps(_removeWhiteListPackagesFromPackageList, this._blackListActivities, hashSet);
        Scanner.scanForSuspiciousPermissionsApps(_removeWhiteListPackagesFromPackageList, this._suspiciousPermissions, hashSet);
        Scanner.scanInstalledAppsFromGooglePlay(this, _removeWhiteListPackagesFromPackageList, hashSet);
        Scanner.scanSystemProblems(this, this._userWhiteList, hashSet);
        this._menacesCacheSet.addItems(hashSet);
        this._menacesCacheSet.writeToJSON();
        IClientInterface iClientInterface = this._clientInterface;
        if (iClientInterface != null) {
            iClientInterface.onScanResult(apps, hashSet);
        }
    }

    public void scanApp(String str) {
        PackageInfo packageInfo;
        AppData appData = AppData.getInstance(this);
        Intent intent = new Intent(this, AntivirusActivity.class);
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(str);
        String appNameFromPackage = StaticTools.getAppNameFromPackage(this, str);
        if (Scanner.isAppWhiteListed(str, this._whiteListPackages)) {
            int i = _currentNotificationId;
            _currentNotificationId = i + 1;
            StaticTools.notificatePush(this, i, this._appIcon, appNameFromPackage + " " + getString(R.string.trusted_message), appNameFromPackage, "App " + appNameFromPackage + " " + getString(R.string.trusted_by_app), launchIntentForPackage);
            return;
        }
        try {
            packageInfo = StaticTools.getPackageInfo(this, str, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        if (packageInfo != null) {
            AppProblem appProblem = new AppProblem(packageInfo.packageName);
            Scanner.scanForBlackListedActivityApp(packageInfo, appProblem, this._blackListActivities, new ArrayList());
            Scanner.scanForSuspiciousPermissionsApp(packageInfo, appProblem, this._suspiciousPermissions);
            Scanner.scanInstalledAppFromGooglePlay(this, appProblem);
            if (appProblem.isMenace()) {
                if (appData.getFirstScanDone()) {
                    this._menacesCacheSet.addItem( appProblem);
                    this._menacesCacheSet.writeToJSON();
                }
                IClientInterface iClientInterface = this._clientInterface;
                if (iClientInterface != null) {
                    iClientInterface.onMonitorFoundMenace(appProblem);
                }
                int i2 = _currentNotificationId;
                _currentNotificationId = i2 + 1;
                StaticTools.notificatePush(this, i2, this._appIcon, appNameFromPackage + " " + getString(R.string.has_been_scanned), appNameFromPackage, getString(R.string.enter_to_solve_problems), intent);
                return;
            }
            int i3 = _currentNotificationId;
            _currentNotificationId = i3 + 1;
            StaticTools.notificatePush(this, i3, this._appIcon, appNameFromPackage + " " + getString(R.string.is_secure), appNameFromPackage, getString(R.string.has_no_threats), intent);
        }
    }

    public void cancelService() {
        stopSelf();
    }

    protected List<PackageInfo> _removeWhiteListPackagesFromPackageList(List<PackageInfo> list, Set<? extends PackageData> set) {
        ArrayList arrayList = new ArrayList(list);
        for (PackageData packageData : set) {
            int i = 0;
            String packageName = packageData.getPackageName();
            while (i < arrayList.size()) {
                if (StaticTools.stringMatchesMask(((PackageInfo) arrayList.get(i)).packageName, packageName)) {
                    arrayList.remove(i);
                } else {
                    i++;
                }
            }
        }
        return arrayList;
    }
}
