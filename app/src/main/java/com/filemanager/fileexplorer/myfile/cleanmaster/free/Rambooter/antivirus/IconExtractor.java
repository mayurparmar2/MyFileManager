package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.TextView;


public class IconExtractor {
    public void getIcon(String str, ImageView imageView, Context context) {
        try {
            imageView.setImageDrawable(context.getPackageManager().getApplicationIcon(str));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getAppName(Context context, String str, TextView textView) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        String str2 = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
        textView.setText(str2);
        return str2;
    }
}
