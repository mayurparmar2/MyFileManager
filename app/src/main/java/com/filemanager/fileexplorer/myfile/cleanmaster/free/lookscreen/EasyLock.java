package com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;


public class EasyLock {
    private static ActivityChanger activityChanger;
    public static int backgroundColor = Color.parseColor("#019689");
    public static View.OnClickListener onClickListener;

    public static void init(Context context) {
        FayazSP.init(context);
        if (activityChanger == null) {
            activityChanger = new LockscreenActivity();
        }
    }

    public static void setPassword(Context context, Class cls) {
        init(context);
        activityChanger.activityClass(cls);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "set");
        Log.d("TAGvvvv", "setPassword: ");
        context.startActivity(intent);
    }

    public static void changePassword(Context context, Class cls) {
        init(context);
        activityChanger.activityClass(cls);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "change");
        context.startActivity(intent);
    }

    public static void disablePassword(Context context, Class cls) {
        init(context);
        activityChanger.activityClass(cls);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "disable");
        context.startActivity(intent);
    }

    public static void checkPassword(Context context) {
        init(context);
        if (FayazSP.getString("password", null) != null) {
            Intent intent = new Intent(context, LockscreenActivity.class);
            intent.putExtra("passStatus", "check");
            context.startActivity(intent);
        }
    }

    public static void setBackgroundColor(int i) {
        backgroundColor = i;
    }

    public static void forgotPassword(View.OnClickListener onClickListener2) {
        onClickListener = onClickListener2;
    }
}
