package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class SystemProblem implements IProblem {
    public abstract void doAction(Context context);

    public abstract String getDescription(Context context);

    public abstract Drawable getIcon(Context context);

    public abstract String getSerializationTypeString();

    public abstract Drawable getSubIcon(Context context);

    public abstract String getSubTitle(Context context);

    public abstract String getTitle(Context context);

    public abstract String getWhiteListOnAddDescription(Context context);

    public abstract String getWhiteListOnRemoveDescription(Context context);

    @Override
    public void loadFromJSON(JSONObject jSONObject) {
    }

    @Override
    public void writeToJSON(String str) {
    }

    @Override
    public IProblem.ProblemType getType() {
        return IProblem.ProblemType.SystemProblem;
    }

    @Override
    public JSONObject buildJSONObject() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", getSerializationTypeString());
        return jSONObject;
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass();
    }
}
