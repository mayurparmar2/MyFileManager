package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;


public class PackageData {
    private String _packageName;

    public String getPackageName() {
        return this._packageName;
    }

    public void setPackageName(String str) {
        this._packageName = str;
    }

    public PackageData() {
    }

    public PackageData(String str) {
        setPackageName(str);
    }

    public int hashCode() {
        return this._packageName.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return this._packageName.equals(((PackageData) obj)._packageName);
    }

    public JSONObject buildJSONObject() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("packageName", this._packageName);
        return jSONObject;
    }

    public static List<PackageData> getPackagesByName(Set<PackageData> set, String str, List<PackageData> list) {
        list.clear();
        boolean z = true;
        if (str.charAt(str.length() - 1) == '*') {
            str = str.substring(0, str.length() - 2);
        } else {
            z = false;
        }
        for (PackageData packageData : set) {
            if (packageData._packageName.startsWith(str)) {
                list.add(packageData);
                if (!z) {
                    break;
                }
            }
        }
        return list;
    }

    public static boolean isPackageInListByName(Set<PackageData> set, String str) {
        if (str.charAt(str.length() - 1) == '*') {
            str = str.substring(0, str.length() - 2);
        }
        for (PackageData packageData : set) {
            if (packageData._packageName.startsWith(str)) {
                return true;
            }
        }
        return false;
    }
}
