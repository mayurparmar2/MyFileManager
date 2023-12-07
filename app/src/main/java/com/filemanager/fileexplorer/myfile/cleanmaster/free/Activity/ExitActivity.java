package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


public class ExitActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    public static void exitApplication(Context context) {
        Intent intent = new Intent(context, ExitActivity.class);
        intent.addFlags(276922368);
        context.startActivity(intent);
    }
}
