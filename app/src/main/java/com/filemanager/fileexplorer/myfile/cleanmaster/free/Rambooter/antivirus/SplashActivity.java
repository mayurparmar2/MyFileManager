package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.example.R;


public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.splash);
        final AppData appData = AppData.getInstance(this);
        new Thread() {
            @Override
            public void run() {
                Intent intent;
                Intent intent2;
                try {
                    try {
                        sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (appData.getEulaAccepted()) {
                            intent2 = new Intent(SplashActivity.this, AntivirusActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, EulaActivity.class);
                        }
                    }
                    if (appData.getEulaAccepted()) {
                        intent2 = new Intent(SplashActivity.this, AntivirusActivity.class);
                        SplashActivity.this.startActivity(intent2);
                        return;
                    }
                    intent = new Intent(SplashActivity.this, EulaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    SplashActivity.this.startActivity(intent);
                } catch (Throwable th) {
                    if (appData.getEulaAccepted()) {
                        SplashActivity.this.startActivity(new Intent(SplashActivity.this, AntivirusActivity.class));
                    } else {
                        Intent intent3 = new Intent(SplashActivity.this, EulaActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        SplashActivity.this.startActivity(intent3);
                    }
                    throw th;
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
