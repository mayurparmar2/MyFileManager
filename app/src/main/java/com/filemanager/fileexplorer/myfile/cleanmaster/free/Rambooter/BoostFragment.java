package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CircularProgressIndicator;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.progressWheel.ProgressWheel;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class BoostFragment extends Fragment implements View.OnClickListener {
    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double d) {
            return String.valueOf((long) ((d / BoostFragment.getTotalMemory()) * 100.0d)) + "%";
        }
    };
    private long aftermemory;
    private long beforeMemory;
    private Button btnBoost;
    private int cacheFreed;
    private CircularProgressIndicator circularProgressIndicator;
    private ProgressDialog dialog;
    private Dialog dialogrocket;
    private boolean isruning;
    private int processesKilled;
    private int ramFreed;
    private long totalMemory;
    private TextView txtFreeMemory;
    private TextView txtLastBoostTime;
    private TextView txtLastCacheCleaned;
    private TextView txtLastMemoryCleaned;
    private TextView txtTotalMemory;
    private TextView txtUsedMemory;
    private TextView txt_ramav;
    private TextView txt_ramtotal;
    private TextView txt_ramussed;
    private ProgressWheel pw = null;
    private SharedPreferences boostPrefs = null;
    private Handler timerHandler = null;
    private ArrayList<String> pList = null;
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            BoostFragment.this.updateMemoryStatus();
            BoostFragment.this.timerHandler.postDelayed(BoostFragment.this.timerRunnable, 5000L);
        }
    };

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.boost_layout, viewGroup, false);
        this.txtFreeMemory = (TextView) inflate.findViewById(R.id.txtFreeMemory);
        this.txtUsedMemory = (TextView) inflate.findViewById(R.id.txtUsedMemory);
        this.txtTotalMemory = (TextView) inflate.findViewById(R.id.txtTotalMemory);
        this.txt_ramav = (TextView) inflate.findViewById(R.id.ram_av);
        this.txt_ramtotal = (TextView) inflate.findViewById(R.id.ram_total);
        this.txt_ramussed = (TextView) inflate.findViewById(R.id.ram_used);
        this.txtLastBoostTime = (TextView) inflate.findViewById(R.id.txtLastBoost);
        this.txtLastMemoryCleaned = (TextView) inflate.findViewById(R.id.txtLastMemoryCleaned);
        this.txtLastCacheCleaned = (TextView) inflate.findViewById(R.id.txtLastCacheCleaned);
        Button button = (Button) inflate.findViewById(R.id.btnBoost);
        this.btnBoost = button;
        button.setOnClickListener(this);
        CircularProgressIndicator circularProgressIndicator = (CircularProgressIndicator) inflate.findViewById(R.id.circular_progress);
        this.circularProgressIndicator = circularProgressIndicator;
        circularProgressIndicator.setTextSize1((int) new Ultil(getContext()).dpToPx(40.0f));
        this.circularProgressIndicator.setAnimationEnabled(true);
        this.circularProgressIndicator.setFlag(true);
        this.pList = new ArrayList<>();
        ProgressWheel progressWheel = (ProgressWheel) inflate.findViewById(R.id.pw_spinner);
        this.pw = progressWheel;
        progressWheel.spin();
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.totalMemory = getTotalMemory();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BOOST", 0);
        this.boostPrefs = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong("totalMemory", this.totalMemory);
        edit.commit();
        updateMemoryStatus();
        this.timerHandler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.timerHandler.postDelayed(this.timerRunnable, 0L);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.timerHandler.removeCallbacks(this.timerRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.timerHandler.removeCallbacks(this.timerRunnable);
    }


    public void updateMemoryStatus() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        long j = memoryInfo.availMem;
        int i = (int) ((((float) j) / ((float) this.totalMemory)) * 100.0f);
        if (i != 0) {
            String formatMemSize = formatMemSize(j, 0);
            this.txtFreeMemory.setText(formatMemSize);
            this.txt_ramav.setText(formatMemSize);
            String formatMemSize2 = formatMemSize(this.totalMemory, 0);
            this.txt_ramtotal.setText(formatMemSize2);
            this.txtFreeMemory.setText(formatMemSize2);
            String formatMemSize3 = formatMemSize(this.totalMemory - j, 0);
            this.txt_ramussed.setText(formatMemSize3);
            this.txtUsedMemory.setText(formatMemSize3);
            setPercentage(i);
        }
        this.circularProgressIndicator.setProgress(j, getTotalMemory());
        this.circularProgressIndicator.setAnimationEnabled(true);
        this.circularProgressIndicator.setProgressTextAdapter(TIME_TEXT_ADAPTER);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("last_boost", 0);
        String string = sharedPreferences.getString("boost_time", "");
        String string2 = sharedPreferences.getString("memory_cleaned", "");
        String string3 = sharedPreferences.getString("cache_cleaned", "");
        TextView textView = this.txtLastBoostTime;
        textView.setText("Last Boost: " + string);
        TextView textView2 = this.txtLastMemoryCleaned;
        textView2.setText("Memory Cleaned: " + string2);
        TextView textView3 = this.txtLastCacheCleaned;
        textView3.setText("Cache Cleaned: " + string3);
    }

    public void setPercentage(int i) {
        this.pw.setText(i + "%");
        this.pw.setProgress((int) (((double) i) * 3.6d));
    }

    public static long getTotalMemory() {
        long j = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String str = "tag";
            for (int i = 0; i < 2; i++) {
                str = str + " " + bufferedReader.readLine();
            }
            String[] split = str.split("\\s+");
            for (String str2 : split) {
                Log.i(str, str2 + "\t");
            }
            j = Integer.valueOf(split[2]).intValue();
            Log.d("MEM", "FREE " + (Integer.valueOf(split[5]).intValue() / 1024) + " MB");
            Log.d("MEM", "INIT " + (j * 1024) + " MB");
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return j * 1024;
    }

    public static String formatMemSize(long j, int i) {
        if (1024 > j) {
            return String.valueOf(j) + " B";
        } else if (1048576 > j) {
            return String.valueOf(String.format("%." + i + "f", Float.valueOf(((float) j) / 1024.0f))) + " KB";
        } else if (1073741824 > j) {
            return String.valueOf(String.format("%." + i + "f", Float.valueOf(((float) j) / 1048576.0f))) + " MB";
        } else {
            return String.valueOf(String.format("%.2f", Float.valueOf(((float) j) / 1.07374195E9f))) + " GB";
        }
    }

    private boolean MemBoost() {
        PackageManager packageManager = getActivity().getPackageManager();
        int length = packageManager.getClass().getDeclaredMethods().length;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CACHE", 0);
        long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong("date_last", 0L);
        if (currentTimeMillis > 600000 || currentTimeMillis > 7200000) {
            long currentTimeMillis2 = System.currentTimeMillis();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putLong("date_last", currentTimeMillis2);
            edit.commit();
            if (currentTimeMillis > 86400000) {
                currentTimeMillis = 86400000;
            }
            int i = (int) (currentTimeMillis / 1000);
            int i2 = i / 360;
            Random random = new Random();
            if (i2 > 0) {
                i *= i2;
            }
            int nextInt = random.nextInt(((i * 15) - i) + 1) + i;
            Method[] declaredMethods = packageManager.getClass().getDeclaredMethods();
            if (length > 0) {
                Method method = declaredMethods[0];
                if (!method.getName().equals("freeStorage")) {
                    try {
                        method.invoke(packageManager, 0L, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            this.beforeMemory = memoryInfo.availMem;
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            int size = runningAppProcesses.size();
            for (int i3 = 0; i3 < runningAppProcesses.size(); i3++) {
                Log.v("process: " + i3, runningAppProcesses.get(i3).processName + " pid: " + runningAppProcesses.get(i3).pid + " importance: " + runningAppProcesses.get(i3).importance + " reason: " + runningAppProcesses.get(i3).importanceReasonCode);
            }
            for (int i4 = 0; i4 < runningAppProcesses.size(); i4++) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(i4);
                int i5 = runningAppProcessInfo.importance;
                int i6 = runningAppProcessInfo.pid;
                String str = runningAppProcessInfo.processName;
                if (!str.equals("com.raihanbd.easyrambooster") && !str.equals("android") && !str.equals("com.android.bluetooth") && !str.equals("android.process.acore") && !str.equals("system") && !str.equals("com.android.phone") && !str.equals("com.android.systemui") && !str.equals("com.android.launcher")) {
                    Log.v("manager", "task " + str + " pid: " + i6 + " has importance: " + i5 + " WILL KILL");
                    this.pList.add(str);
                    for (int i7 = 0; i7 < 3; i7++) {
                        activityManager.killBackgroundProcesses(runningAppProcesses.get(i4).processName);
                    }
                }
            }
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses2 = activityManager.getRunningAppProcesses();
            int size2 = runningAppProcesses2.size();
            for (int i8 = 0; i8 < runningAppProcesses2.size(); i8++) {
                Log.v("proces after killings: " + i8, runningAppProcesses2.get(i8).processName + " pid:" + runningAppProcesses2.get(i8).pid + " importance: " + runningAppProcesses2.get(i8).importance + " reason: " + runningAppProcesses2.get(i8).importanceReasonCode);
            }
            this.processesKilled = size - size2;
            this.cacheFreed = nextInt;
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        MemBoost();
        DialogTask dialogTask = new DialogTask();
        if (this.isruning) {
            dialogTask.cancel(true);
            dialogTask = new DialogTask();
        }
        dialogTask.execute(new Void[0]);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 999) {
            return;
        }
        Toast.makeText(getContext(), "AAAA", Toast.LENGTH_SHORT).show();
    }


    private class DialogTask extends AsyncTask<Void, Void, Void> {
        private DialogTask() {
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPreExecute() {
            BoostFragment.this.dialogrocket = new Dialog(BoostFragment.this.requireContext(), 16973834);
            BoostFragment.this.dialogrocket.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            BoostFragment.this.dialogrocket.setCancelable(false);
            BoostFragment.this.dialogrocket.setContentView(R.layout.dialog_rocket);
            Glide.with(BoostFragment.this.getContext()).load(Integer.valueOf((int) R.raw.rocket_boost_memory_unscreen)).error((int) R.raw.rocket).into((ImageView) BoostFragment.this.dialogrocket.findViewById(R.id.img_gif));
            BoostFragment.this.dialogrocket.show();
        }


        @Override
        public Void doInBackground(Void... voidArr) {
            BoostFragment.this.isruning = true;
            try {
                Thread.sleep(5000L);
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public void onPostExecute(Void r6) {
            BoostFragment.this.isruning = false;
            if (BoostFragment.this.dialogrocket != null) {
                BoostFragment.this.dialogrocket.dismiss();
            }
            BoostFragment.this.updateMemoryStatus();
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) BoostFragment.this.getActivity().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
            BoostFragment.this.aftermemory = memoryInfo.availMem;
            if (BoostFragment.this.aftermemory <= BoostFragment.this.beforeMemory) {
                BoostFragment.this.ramFreed = 0;
            } else {
                BoostFragment boostFragment = BoostFragment.this;
                boostFragment.ramFreed = (int) (boostFragment.aftermemory - BoostFragment.this.beforeMemory);
            }
            BoostDialog boostDialog = new BoostDialog();
            boostDialog.setProcessKilled(String.valueOf(BoostFragment.this.processesKilled));
            boostDialog.setMemoryCleaned(BoostFragment.formatMemSize(BoostFragment.this.ramFreed, 0));
            boostDialog.setCacheCleaned(BoostFragment.formatMemSize(BoostFragment.this.cacheFreed, 0));
            boostDialog.setCancelable(false);
            boostDialog.show(BoostFragment.this.getFragmentManager(), "dialog");
        }
    }
}
