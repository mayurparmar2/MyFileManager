package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AppProblem extends PackageData implements IProblem {
    static final String kSerializationType = "app";
    private Set<ActivityData> _activities;
    private boolean _installedThroughGooglePlay;
    private Set<PermissionData> _permissions;

    @Override
    public void writeToJSON(String str) {
    }

    public AppProblem() {
        this._activities = new HashSet();
        this._permissions = new HashSet();
        this._installedThroughGooglePlay = false;
    }

    public AppProblem(String str) {
        super(str);
        this._activities = new HashSet();
        this._permissions = new HashSet();
        this._installedThroughGooglePlay = false;
    }

    @Override
    public IProblem.ProblemType getType() {
        return IProblem.ProblemType.AppProblem;
    }

    public void addActivityData(ActivityData activityData) {
        this._activities.add(activityData);
    }

    public Set<ActivityData> getActivityData() {
        return this._activities;
    }

    public void addPermissionData(PermissionData permissionData) {
        this._permissions.add(permissionData);
    }

    public Set<PermissionData> getPermissionData() {
        return this._permissions;
    }

    public boolean getInstalledThroughGooglePlay() {
        return this._installedThroughGooglePlay;
    }

    public void setInstalledThroughGooglePlay(boolean z) {
        this._installedThroughGooglePlay = z;
    }

    public boolean isMenace() {
        return !getInstalledThroughGooglePlay() || getActivityData().size() > 0 || getPermissionData().size() > 0;
    }

    @Override
    public boolean isDangerous() {
        for (PermissionData permissionData : this._permissions) {
            if (permissionData.getDangerous() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean problemExists(Context context) {
        return StaticTools.isPackageInstalled(context, getPackageName());
    }

    @Override
    public JSONObject buildJSONObject() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", kSerializationType);
        jSONObject.put("packageName", getPackageName());
        jSONObject.put("gplayinstalled", getInstalledThroughGooglePlay());
        JSONArray jSONArray = new JSONArray();
        for (ActivityData activityData : this._activities) {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("packageName", activityData.getPackage());
            jSONArray.put(jSONObject2);
        }
        jSONObject.put("activities", jSONArray);
        JSONArray jSONArray2 = new JSONArray();
        for (PermissionData permissionData : this._permissions) {
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("permissionName", permissionData.getPermissionName());
            jSONObject3.put("dangerous", permissionData.getDangerous());
            jSONArray2.put(jSONObject3);
        }
        jSONObject.put("permissions", jSONArray2);
        return jSONObject;
    }

    @Override
    public void loadFromJSON(JSONObject jSONObject) {
        try {
            setPackageName(jSONObject.getString("packageName"));
            setInstalledThroughGooglePlay(jSONObject.getBoolean("gplayinstalled"));
            _loadActivitesFromJSON(jSONObject);
            _loadPermissionsFromJSON(jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _loadActivitesFromJSON(JSONObject jSONObject) {
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("activities");
            for (int i = 0; i < jSONArray.length(); i++) {
                addActivityData(new ActivityData(jSONArray.getJSONObject(i).getString("packageName")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void _loadPermissionsFromJSON(JSONObject jSONObject) {
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("permissions");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                addPermissionData(new PermissionData(jSONObject2.getString("permissionName"), jSONObject2.getInt("dangerous")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
