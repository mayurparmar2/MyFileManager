package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen.EasyLock;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen.FayazSP;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen.LockscreenHandler;


public class LookActivity extends LockscreenHandler {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_look);
        FayazSP.init(this);
        String string = FayazSP.getString("password", "");
        if (string.equals("") || string == null) {
            setPass();
            finish();
        } else {
            EasyLock.checkPassword(this);
            finish();
        }
        EasyLock.forgotPassword(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LookActivity.this, "Clicked on forgot password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPass() {
        EasyLock.setPassword(this, HideChoseOptionActivity.class);
    }

    public void changePass(View view) {
        EasyLock.changePassword(this, HideChoseOptionActivity.class);
    }

    public void disable(View view) {
        EasyLock.disablePassword(this, HideChoseOptionActivity.class);
    }

    public void checkPass(View view) {
        EasyLock.checkPassword(this);
    }
}
