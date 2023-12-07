package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;


public class PermissionData {
    private int _dangerous;
    private String _permissionName;

    public int getDangerous() {
        return this._dangerous;
    }

    public String getPermissionName() {
        return this._permissionName;
    }

    public PermissionData(String str, int i) {
        this._permissionName = str;
        this._dangerous = i;
    }

    public int hashCode() {
        return this._permissionName.hashCode() + this._dangerous;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this._permissionName.equals(((PermissionData) obj)._permissionName);
    }
}
