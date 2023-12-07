package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import org.json.JSONException;
import org.json.JSONObject;


public class DebugUSBEnabledProblem extends SystemProblem implements IProblem {
    public static final String kSerializationType = "usb";
    final int kUsbIconResId = R.drawable.icon_apk;
    final int kUsbDescriptionId = R.string.usb_title;
    final int kUsbTitleId = R.string.system_app_usb_menace_title;
    final int kWhiteListAddText = R.string.usb_add_whitelist_message;
    final int kWhiteListRemoveText = R.string.usb_remove_whitelist_message;
    final boolean kUSBIsDangerousMenace = false;

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
        return context.getString(R.string.usb_add_whitelist_message);
    }

    @Override
    public String getWhiteListOnRemoveDescription(Context context) {
        return context.getString(R.string.usb_remove_whitelist_message);
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.system_app_usb_menace_title);
    }

    @Override
    public String getSubTitle(Context context) {
        return context.getString(R.string.usb_title);
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.usb_title);
    }

    @Override
    public Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_apk);
    }

    @Override
    public Drawable getSubIcon(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.icon_apk);
    }

    @Override
    public void doAction(Context context) {
        StaticTools.openDeveloperSettings(context);
    }

    @Override
    public boolean problemExists(Context context) {
        return StaticTools.checkIfUSBDebugIsEnabled(context);
    }
}
