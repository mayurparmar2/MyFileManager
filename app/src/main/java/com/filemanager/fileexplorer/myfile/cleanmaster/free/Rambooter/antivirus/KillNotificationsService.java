package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class KillNotificationsService extends Service {
    public static int NOTIFICATION_ID = 666;
    private Intent intent;
    private final IBinder mBinder = new KillBinder(this);
    private NotificationManager mNM;

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_STICKY;
    }


    public class KillBinder extends Binder {
        public final Service service;

        public KillBinder(Service service) {
            this.service = service;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return this.mBinder;
    }

    @Override
    public void onCreate() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        this.mNM = notificationManager;
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
