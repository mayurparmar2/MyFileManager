package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Sharepre_Ulti {
    private SharedPreferences sharedPref;

    public Sharepre_Ulti(Context context) {
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void writeSharedPrefs(String str, String str2) {
        SharedPreferences.Editor edit = this.sharedPref.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public void writeSharedPrefs(String str, int i) {
        SharedPreferences.Editor edit = this.sharedPref.edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public void writeSharedPrefs(String str, Boolean bool) {
        SharedPreferences.Editor edit = this.sharedPref.edit();
        edit.putBoolean(str, bool.booleanValue());
        edit.apply();
    }

    public String readSharedPrefsString(String str, String str2) {
        return this.sharedPref.getString(str, str2);
    }

    public Boolean readSharedPrefsBoolean(String str, Boolean bool) {
        return Boolean.valueOf(this.sharedPref.getBoolean(str, bool.booleanValue()));
    }

    public int readSharedPrefsInt(String str, int i) {
        return this.sharedPref.getInt(str, i);
    }
}
