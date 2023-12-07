package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.pm.PackageInfo;


public class GoodPackageResultData {
    private PackageInfo _packageInfo;

    public String getPackageName() {
        return this._packageInfo.packageName;
    }

    public PackageInfo getPackageInfo() {
        return this._packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this._packageInfo = packageInfo;
    }

    public GoodPackageResultData(PackageInfo packageInfo) {
        this._packageInfo = packageInfo;
    }

    public int hashCode() {
        return getPackageName().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this._packageInfo.packageName.equals(((GoodPackageResultData) obj)._packageInfo);
    }
}
