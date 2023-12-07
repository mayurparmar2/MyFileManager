package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;


public class SplashActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 9920;
    private static final long NUM_BYTES_NEEDED_FOR_MY_APP = 10485760;
    private static final int PERMISSION_REQUEST_CODE = 111;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            if (activityResult.getResultCode() == -1) {
                if (Build.VERSION.SDK_INT >= 30) {
                    if (Environment.isExternalStorageManager()) {
                        Toast.makeText(SplashActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }
            } else if (activityResult.getResultCode() == 0) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, SlideActivity.class));
                SplashActivity.this.finish();
            }
        }
    });


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        if (checkPermission()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        requestPermission();
    }

    public void checkPermissionReadStorage(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.READ_EXTERNAL_STORAGE") && ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                startActivity(new Intent(this, MainActivity.class));
                return;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
                return;
            }
        }
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 111 && iArr.length > 0) {
            boolean z = iArr[0] == 0;
            boolean z2 = iArr[1] == 0;
            if (z && z2) {
                startActivity(new Intent(this, MainActivity.class));
                return;
            }
            Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 2296 || Build.VERSION.SDK_INT < 30) {
            return;
        }
        if (Environment.isExternalStorageManager()) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
        finishAffinity();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                this.activityResultLauncher.launch(intent);
                return;
            } catch (Exception unused) {
                Intent intent2 = new Intent();
                intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                this.activityResultLauncher.launch(intent2);
                return;
            }
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 111);
    }
}
