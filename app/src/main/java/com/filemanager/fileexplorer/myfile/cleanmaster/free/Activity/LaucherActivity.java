package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.RamBooterActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileUltils;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.ProcessInfo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.RAMBooster;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.CleanListener;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.interfaces.ScanListener;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.DowloadApdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.UsageListAdapter2;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.apdapterlaucher.DowloadApdapterLaucher;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.apdapterlaucher.LagreApdapterLaucher;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test.AppUsageStatisticsActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test.CustomUsageStats;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class LaucherActivity extends AppCompatActivity {
    private int a;
    RAMBooster booster;
    private DowloadApdapter dowloadApdapter;
    private DowloadApdapterLaucher dowloadApdapterLaucher;
    private ArrayList<File_DTO> file_dtos;
    ImageView imageView_back;
    ImageView img_rocket;
    private LagreApdapterLaucher lagreApdapterLaucher;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private LinearLayoutManager layoutManager;
    private ArrayList<File_DTO> list_download;
    private LottieAnimationView lotte_dialog;
    private LottieAnimationView lottieAnimationView;
    private UsageStatsManager mUsageStatsManager;
    private RecyclerView recyclerView_dowload;
    private RecyclerView recyclerView_lagrefile;
    private RecyclerView recyclerview_uncendapp;
    private TextView textView;
    private TextView txt_clear_dowload;
    private TextView txt_clearlagrefile;
    private TextView txt_countfile_dowload;
    private TextView txt_countlargefile;
    private TextView txt_freeuplangrefile;
    private TextView txt_uncenapp;
    private TextView txt_upsize_dowload;
    private UsageListAdapter2 usageListAdapter;
    public final long weight = 1048576;

    private void updateUI() {
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_laucher);
        this.recyclerView_dowload = (RecyclerView) findViewById(R.id.recyclerview_freeup);
        this.recyclerview_uncendapp = (RecyclerView) findViewById(R.id.recyclerview_unapp);
        this.recyclerView_lagrefile = (RecyclerView) findViewById(R.id.recyclerview_langefile);
        this.lottieAnimationView = (LottieAnimationView) findViewById(R.id.logo);
        this.imageView_back = (ImageView) findViewById(R.id.img_back);
        this.img_rocket = (ImageView) findViewById(R.id.img_rocket);
        this.txt_countfile_dowload = (TextView) findViewById(R.id.count_file_freeeup);
        this.txt_upsize_dowload = (TextView) findViewById(R.id.up_size_dowload);
        this.txt_uncenapp = (TextView) findViewById(R.id.txt_clear_unapp);
        this.txt_freeuplangrefile = (TextView) findViewById(R.id.freeup_largefile);
        this.txt_countlargefile = (TextView) findViewById(R.id.count_largefile);
        this.txt_clearlagrefile = (TextView) findViewById(R.id.txt_clear_largegile);
        this.txt_clear_dowload = (TextView) findViewById(R.id.txt_clear_dowload);
        this.mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        this.lottieAnimationView.setImageAssetsFolder("/images");

        YoYo.with(Techniques.Bounce).duration(7000L).playOn(this.lottieAnimationView);
        this.textView = (TextView) findViewById(R.id.txt_clear);
        this.booster = new RAMBooster(this);
        Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.raw.rocket)).into(this.img_rocket);
        this.file_dtos = new ArrayList<>();
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(LaucherActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        long calculateAvailableRAM = Ultil.calculateAvailableRAM(this) / 1048576;
        long calculateTotalRAM = Ultil.calculateTotalRAM() / 1048576;
        new ArrayList();
        this.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaucherActivity.this.startActivity(new Intent(LaucherActivity.this, RamBooterActivity.class));
            }
        });
        this.imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaucherActivity.this.finish();
                Animatoo.animateSwipeRight(LaucherActivity.this);
            }
        });
        this.txt_uncenapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaucherActivity.this.startActivity(new Intent(LaucherActivity.this, AppUsageStatisticsActivity.class));
                Animatoo.animateSwipeLeft(LaucherActivity.this);
            }
        });
        this.txt_clear_dowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaucherActivity.this, ClearMultilActivity.class);
                intent.putExtra("namekey", "dowload");
                LaucherActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(LaucherActivity.this);
            }
        });
        this.txt_clearlagrefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaucherActivity.this, ClearMultilActivity.class);
                intent.putExtra("namekey", "filelagre");
                LaucherActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(LaucherActivity.this);
            }
        });
        this.layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        new updateUIinBG().execute(new String[0]);
    }


    public void clearanscan() {
        if (this.booster == null) {
            this.booster = null;
        }
        RAMBooster rAMBooster = new RAMBooster(this);
        this.booster = rAMBooster;
        rAMBooster.setDebug(true);
        this.booster.setScanListener(new ScanListener() {
            @Override
            public void onStarted() {
                Log.d("TAG", "Scan started");
            }

            @Override
            public void onFinished(long j, long j2, List<ProcessInfo> list) {
                ArrayList arrayList = new ArrayList();
                for (ProcessInfo processInfo : list) {
                    arrayList.add(processInfo.getProcessName());
                }
                for (int i = 0; i < list.size(); i++) {
                    Log.d("TAG", String.format(Locale.US, "Going to clean founded processes: %s", list.get(i).getProcessName()));
                }
            }
        });
        this.booster.setCleanListener(new CleanListener() {
            @Override
            public void onStarted() {
                LaucherActivity.this.booster.startClean();
                Log.d("TAG", "Clean started");
            }

            @Override
            public void onFinished(long j, long j2) {
                Log.d("TAG", String.format(Locale.US, "Clean finished, available RAM: %dMB, total RAM: %dMB", Long.valueOf(j), Long.valueOf(j2)));
                LaucherActivity.this.booster = null;
            }
        });
    }


    public void view_uncenapp() {
        this.recyclerview_uncendapp.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerview_uncendapp.setAdapter(this.usageListAdapter);
        this.usageListAdapter.notifyDataSetChanged();
    }

    public List<UsageStats> getUsageStatistics(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, -1);
        return this.mUsageStatsManager.queryUsageStats(i, calendar.getTimeInMillis(), System.currentTimeMillis());
    }

    void updateAppsList(List<UsageStats> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            CustomUsageStats customUsageStats = new CustomUsageStats();
            customUsageStats.usageStats = list.get(i);
            try {
                customUsageStats.appIcon = getPackageManager().getApplicationIcon(customUsageStats.usageStats.getPackageName()).getCurrent();
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("TAG", String.format("App Icon is not found for %s", customUsageStats.usageStats.getPackageName()));
                customUsageStats.appIcon = getDrawable(R.drawable.icon_app);
            }
            arrayList.add(customUsageStats);
        }
        this.usageListAdapter.setCustomUsageStatsList(arrayList);
    }


    public void dowlaod() {
        this.recyclerView_dowload.setLayoutManager(this.layoutManager);
        DowloadApdapterLaucher dowloadApdapterLaucher = new DowloadApdapterLaucher(this);
        this.dowloadApdapterLaucher = dowloadApdapterLaucher;
        dowloadApdapterLaucher.setList(this.list_download);
        this.dowloadApdapterLaucher.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView_dowload.setAdapter(this.dowloadApdapterLaucher);
        this.dowloadApdapterLaucher.notifyDataSetChanged();
    }


    public void lagrefileUI() {
        this.recyclerView_lagrefile.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        LagreApdapterLaucher lagreApdapterLaucher = new LagreApdapterLaucher(this);
        this.lagreApdapterLaucher = lagreApdapterLaucher;
        lagreApdapterLaucher.setList(this.file_dtos);
        this.lagreApdapterLaucher.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView_lagrefile.setAdapter(this.lagreApdapterLaucher);
    }

    private void showdialog() {
        this.a = Integer.valueOf((int) getAvailableMemory().totalMem).intValue();
        @SuppressLint("ResourceType") final Dialog dialog = new Dialog(this, 16973834);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.background_dialog_rocket)));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_rocket);
        this.lotte_dialog = (LottieAnimationView) dialog.findViewById(R.id.lt_background);
        final TextView textView = (TextView) dialog.findViewById(R.id.txt_ram);
        textView.setText("" + this.a);
        YoYo.with(Techniques.Bounce).duration(1000L).playOn(this.lotte_dialog);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000L);
                TextView textView2 = textView;
                textView2.setText("" + LaucherActivity.this.a);
                LaucherActivity.this.clearanscan();
                if (LaucherActivity.this.a <= 0) {
                    dialog.cancel();
                    handler.removeCallbacks(this);
                    Toast.makeText(LaucherActivity.this, "Device  has been cleaned", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000L);
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    public static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
        private LastTimeLaunchedComparatorDesc() {
        }

        @Override
        public int compare(UsageStats usageStats, UsageStats usageStats2) {
            return Long.compare(usageStats2.getLastTimeUsed(), usageStats.getLastTimeUsed());
        }
    }


    public enum StatsUsageInterval {
        DAILY("Daily", 0),
        WEEKLY("Weekly", 1),
        MONTHLY("Monthly", 2),
        YEARLY("Yearly", 3);

        private int mInterval;
        private String mStringRepresentation;

        StatsUsageInterval(String str, int i) {
            this.mStringRepresentation = str;
            this.mInterval = i;
        }

        static StatsUsageInterval getValue(String str) {
            StatsUsageInterval[] values;
            for (StatsUsageInterval statsUsageInterval : values()) {
                if (statsUsageInterval.mStringRepresentation.equals(str)) {
                    return statsUsageInterval;
                }
            }
            return null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        new updateUIinBG().execute(new String[0]);
    }


    class updateUIinBG extends AsyncTask<String, Boolean, String> {
        File file;
        File file1;
        StatsUsageInterval statsUsageInterval;
        String[] string;
        List<UsageStats> usageStatsList;
        long totalsize_dowload = 0;
        long totalallflile = 0;
        int a = 0;

        updateUIinBG() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.string = LaucherActivity.this.getResources().getStringArray(R.array.action_list);
            LaucherActivity.this.usageListAdapter = new UsageListAdapter2(LaucherActivity.this);
            this.statsUsageInterval = StatsUsageInterval.getValue(this.string[1]);
        }


        @Override
        public String doInBackground(String... strArr) {
            LaucherActivity laucherActivity = LaucherActivity.this;
            laucherActivity.file_dtos = FileUltils.getallfilewithMediaconetnt(laucherActivity);
            LaucherActivity.this.list_download = new Data_Manager(LaucherActivity.this).getallfilewithpath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            StatsUsageInterval statsUsageInterval = this.statsUsageInterval;
            if (statsUsageInterval != null) {
                List<UsageStats> usageStatistics = LaucherActivity.this.getUsageStatistics(statsUsageInterval.mInterval);
                this.usageStatsList = usageStatistics;
                Collections.sort(usageStatistics, new LastTimeLaunchedComparatorDesc());
            }
            for (int i = 0; i < LaucherActivity.this.list_download.size(); i++) {
                File file = new File(((File_DTO) LaucherActivity.this.list_download.get(i)).getPath());
                this.file = file;
                this.totalsize_dowload += file.length();
            }
            this.a = LaucherActivity.this.file_dtos.size();
            for (int i2 = 0; i2 < LaucherActivity.this.file_dtos.size(); i2++) {
                this.totalallflile += Long.parseLong(((File_DTO) LaucherActivity.this.file_dtos.get(i2)).getSize());
            }
            LaucherActivity.this.updateAppsList(this.usageStatsList);
            return null;
        }


        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            LaucherActivity.this.lagrefileUI();
            LaucherActivity.this.dowlaod();
            LaucherActivity.this.view_uncenapp();
            String bytesToHuman = new Ultil(LaucherActivity.this).bytesToHuman(this.totalsize_dowload);
            String bytesToHuman2 = new Ultil(LaucherActivity.this).bytesToHuman(this.totalallflile);
            TextView textView = LaucherActivity.this.txt_freeuplangrefile;
            textView.setText("Free up to " + bytesToHuman2);
            TextView textView2 = LaucherActivity.this.txt_upsize_dowload;
            textView2.setText("Free up to " + bytesToHuman);
            TextView textView3 = LaucherActivity.this.txt_countlargefile;
            textView3.setText("have at least  " + this.a + "  larger files");
            TextView textView4 = LaucherActivity.this.txt_countfile_dowload;
            textView4.setText(LaucherActivity.this.list_download.size() + " files in folder Download");
        }
    }
}
