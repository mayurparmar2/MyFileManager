package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import org.json.JSONException;
import org.json.JSONObject;


public class UnknownAppEnabledProblem extends SystemProblem implements IProblem {
    public static final String kSerializationType = "unknownApp";
    final int kUnknownAppIconResId = R.drawable.imageicon;
    final int kUnknownAppDescriptionId = R.string.unknownApp_message;
    final int kUnknownAppTitleId = R.string.system_app_unknown_app_menace_title;
    final int kWhiteListAddText = R.string.unknownApp_add_whitelist_message;
    final int kWhiteListRemoveText = R.string.unknownApp_remove_whitelist_message;

    @Override
    public JSONObject buildJSONObject() throws JSONException {
        return null;
    }

    @Override
    public String getSerializationTypeString() {
        return kSerializationType;
    }

    @Override
    public boolean isDangerous() {
        return false;
    }

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
    public String getWhiteListOnAddDescription(Context context) {
        return context.getString(R.string.unknownApp_add_whitelist_message);
    }

    @Override
    public String getWhiteListOnRemoveDescription(Context context) {
        return context.getString(R.string.unknownApp_remove_whitelist_message);
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.system_app_unknown_app_menace_title);
    }

    @Override
    public String getSubTitle(Context context) {
        return context.getString(R.string.usb_title);
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.unknownApp_message);
    }

    @Override
    public Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.imageicon);
    }

    @Override
    public Drawable getSubIcon(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.antivirus);
    }

    @Override
    public void doAction(Context context) {
        StaticTools.openSecuritySettings(context);
    }

    @Override
    public boolean problemExists(Context context) {
        return StaticTools.checkIfUnknownAppIsEnabled(context);
    }
}
