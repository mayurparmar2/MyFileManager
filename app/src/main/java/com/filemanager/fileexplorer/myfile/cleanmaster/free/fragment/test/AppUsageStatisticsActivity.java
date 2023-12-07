package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;


public class AppUsageStatisticsActivity extends AppCompatActivity {
    public void getallfilewithpath(File file) {
    }


    @Override
    public void onCreate(Bundle bundle) {
        Class<?> cls;
        Field field;
        Object obj;
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_usage_statistics);
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, AppUsageStatisticsFragment.newInstance()).commit();
        }
        Method method = null;
        try {
            cls = Class.forName("libcore.io.Libcore");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        try {
            field = cls.getDeclaredField("os");
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            field = null;
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            obj = field.get(null);
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            obj = null;
        }
        try {
            method = obj.getClass().getMethod("lstat", String.class);
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        }
        File[] listFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).listFiles();
        if (listFiles == null) {
            listFiles = new File[0];
        }
        for (File file : listFiles) {
            new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
            try {
                Object invoke = method.invoke(obj, file.getAbsolutePath());
                try {
                    field = invoke.getClass().getDeclaredField("st_atime");
                } catch (NoSuchFieldException e5) {
                    e5.printStackTrace();
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                long j = field.getLong(invoke);
                Log.d("TAG!!", "onCreate: " + new Ultil(this).getDate(j * 1000));
            } catch (IllegalAccessException e6) {
                e6.printStackTrace();
            } catch (InvocationTargetException e7) {
                e7.printStackTrace();
            }
        }
    }
}
