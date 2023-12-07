package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;


public class ActivityData {
    private String _package;

    public String getPackage() {
        return this._package;
    }

    public ActivityData(String str) {
        this._package = str;
    }

    public int hashCode() {
        return this._package.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this._package.equals(((ActivityData) obj)._package);
    }
}
