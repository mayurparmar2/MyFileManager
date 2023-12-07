package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;


public class EulaActivity extends Activity {
    long m_dwSplashTime = 3000;
    boolean m_bPaused = false;
    boolean m_bSplashActive = true;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.eula);
        Button button = (Button) findViewById(R.id.accept_eula_button);
        Button button2 = (Button) findViewById(R.id.decline_eula_button);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#ff790b"));
        }
        new Thread() {
            @Override
            public void run() {
                long j = 0;
                while (EulaActivity.this.m_bSplashActive && j < EulaActivity.this.m_dwSplashTime) {
                    try {
                        try {
                            sleep(50L);
                            if (!EulaActivity.this.m_bPaused) {
                                j += 100;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } finally {
                        EulaActivity.this.finish();
                    }
                }
                EulaActivity.this.startActivity(new Intent(EulaActivity.this, MainActivity.class));
            }
        }.start();
    }
}
