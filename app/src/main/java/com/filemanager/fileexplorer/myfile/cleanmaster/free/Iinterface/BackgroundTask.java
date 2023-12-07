package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;


public class BackgroundTask {
    private Context activity;

    public BackgroundTask(Context context) {
        this.activity = context;
    }

    public void Handleloop(final HandleLooper handleLooper) {
        final Dialog_thread dialog_thread = new Dialog_thread(this.activity);
        dialog_thread.show();
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                handleLooper.update();
                dialog_thread.dissmiss();
            }
        }.obtainMessage(111, "parameter").sendToTarget();
    }
}
