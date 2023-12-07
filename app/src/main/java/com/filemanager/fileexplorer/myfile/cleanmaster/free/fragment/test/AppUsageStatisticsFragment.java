package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.BackgroundTask;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.HandleLooper;
import com.demo.example.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppUsageStatisticsFragment extends Fragment {
    private static final String TAG = "AppUsageStatisticsFragment";
    private boolean check;
    private HandleLooper handleLooper;
    ImageView imageView_showSiner;
    ImageView img_dropuplayout;
    LinearLayout linearLayout;
    RecyclerView.LayoutManager mLayoutManager;
    Button mOpenUsageSettingButton;
    RecyclerView mRecyclerView;
    Spinner mSpinner;
    UsageListAdapter mUsageListAdapter;
    UsageStatsManager mUsageStatsManager;
    private int pos = 0;
    TextView txt_dialy;
    TextView txt_month;
    TextView txt_resulft;
    TextView txt_week;
    TextView txt_year;

    public static Fragment newInstance() {
        return new AppUsageStatisticsFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_app_usage_statistics, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mUsageListAdapter = new UsageListAdapter(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_app_usage);
        this.mRecyclerView = recyclerView;
        this.mLayoutManager = recyclerView.getLayoutManager();
        this.mRecyclerView.scrollToPosition(0);
        this.mRecyclerView.setAdapter(this.mUsageListAdapter);
        this.linearLayout = (LinearLayout) view.findViewById(R.id.l_1);
        this.mOpenUsageSettingButton = (Button) view.findViewById(R.id.button_open_usage_setting);
        this.img_dropuplayout = (ImageView) view.findViewById(R.id.img_dropup_layout);
        this.txt_dialy = (TextView) view.findViewById(R.id.txt_dialy);
        this.txt_month = (TextView) view.findViewById(R.id.txt_month);
        this.txt_resulft = (TextView) view.findViewById(R.id.txt_spinerselected);
        this.txt_week = (TextView) view.findViewById(R.id.txt_week);
        this.txt_year = (TextView) view.findViewById(R.id.txt_year);
        this.mSpinner = (Spinner) view.findViewById(R.id.spinner_time_span);
        ArrayAdapter<CharSequence> createFromResource = ArrayAdapter.createFromResource(getActivity(), R.array.action_list, R.layout.spinner_item);
        this.imageView_showSiner = (ImageView) view.findViewById(R.id.show_spiner);
        this.mSpinner.setAdapter((SpinnerAdapter) createFromResource);
        this.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view2, int i, long j) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.handleLooper = new HandleLooper() {
            @Override
            public void update() {
                String[] stringArray = AppUsageStatisticsFragment.this.getResources().getStringArray(R.array.action_list);
                StatsUsageInterval value = StatsUsageInterval.getValue(stringArray[AppUsageStatisticsFragment.this.pos]);
                if (value != null) {
                    List<UsageStats> usageStatistics = AppUsageStatisticsFragment.this.getUsageStatistics(value.mInterval);
                    Collections.sort(usageStatistics, new LastTimeLaunchedComparatorDesc());
                    AppUsageStatisticsFragment appUsageStatisticsFragment = AppUsageStatisticsFragment.this;
                    appUsageStatisticsFragment.updateAppsList(usageStatistics, appUsageStatisticsFragment.pos);
                }
                AppUsageStatisticsFragment.this.view_down(view);
                AppUsageStatisticsFragment.this.txt_resulft.setText(stringArray[AppUsageStatisticsFragment.this.pos]);
            }
        };
        this.img_dropuplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                AppUsageStatisticsFragment appUsageStatisticsFragment = AppUsageStatisticsFragment.this;
                appUsageStatisticsFragment.check = !appUsageStatisticsFragment.check;
                if (AppUsageStatisticsFragment.this.check) {
                    AppUsageStatisticsFragment.this.view_down(view);
                } else {
                    AppUsageStatisticsFragment.this.view_up(view);
                }
            }
        });
        onloaddata();
        this.txt_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                AppUsageStatisticsFragment.this.pos = 1;
                AppUsageStatisticsFragment.this.onloaddata();
            }
        });
        this.txt_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                AppUsageStatisticsFragment.this.pos = 3;
                AppUsageStatisticsFragment.this.onloaddata();
            }
        });
        this.txt_dialy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                AppUsageStatisticsFragment.this.pos = 0;
                AppUsageStatisticsFragment.this.onloaddata();
            }
        });
        this.txt_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                AppUsageStatisticsFragment.this.pos = 2;
                AppUsageStatisticsFragment.this.onloaddata();
            }
        });
    }


    public void view_up(View view) {
        view.findViewById(R.id.l_up).setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInDown).duration(400L).playOn(view.findViewById(R.id.l_up));
    }


    public void view_down(View view) {
        YoYo.with(Techniques.SlideOutUp).duration(400L).playOn(view.findViewById(R.id.l_up));
    }


    public void onloaddata() {
        new BackgroundTask(getContext()).Handleloop(this.handleLooper);
    }

    public List<UsageStats> getUsageStatistics(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, -1);
        List<UsageStats> queryUsageStats = this.mUsageStatsManager.queryUsageStats(1, calendar.getTimeInMillis(), System.currentTimeMillis());
        DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                AppUsageStatisticsFragment.this.startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), 111);
            }
        };
        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the access to apps usage. ");
            this.mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(getContext(), deleteCallback);
            customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            customDeleteDialog.show();
            customDeleteDialog.set_titile_permission_Usage();
            this.linearLayout.setVisibility(View.GONE);
            this.mRecyclerView.setVisibility(View.GONE);
            this.mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUsageStatisticsFragment.this.startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), 111);
                }
            });
        }
        return queryUsageStats;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 111) {
            this.mOpenUsageSettingButton.setVisibility(View.GONE);
            this.mRecyclerView.setVisibility(View.VISIBLE);
            this.linearLayout.setVisibility(View.VISIBLE);
            List<UsageStats> usageStatistics = getUsageStatistics(0);
            Collections.sort(usageStatistics, new LastTimeLaunchedComparatorDesc());
            updateAppsList(usageStatistics, 1);
        }
    }

    void updateAppsList(List<UsageStats> list, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (!list.get(i2).getPackageName().equals(getContext().getPackageName())) {
                CustomUsageStats customUsageStats = new CustomUsageStats();
                customUsageStats.usageStats = list.get(i2);
                try {
                    customUsageStats.appIcon = getActivity().getPackageManager().getApplicationIcon(customUsageStats.usageStats.getPackageName());
                } catch (PackageManager.NameNotFoundException unused) {
                    Log.w(TAG, String.format("App Icon is not found for %s", customUsageStats.usageStats.getPackageName()));
                    customUsageStats.appIcon = getActivity().getDrawable(R.drawable.icon_app);
                }
                arrayList.add(customUsageStats);
            } else {
                Log.d(TAG, "updateAppsList: ");
            }
        }
        this.mUsageListAdapter.setType(i);
        this.mUsageListAdapter.setCustomUsageStatsList(arrayList);
        this.mUsageListAdapter.notifyDataSetChanged();
        this.mRecyclerView.scrollToPosition(0);
    }


    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
        private LastTimeLaunchedComparatorDesc() {
        }

        @Override
        public int compare(UsageStats usageStats, UsageStats usageStats2) {
            return Long.compare(usageStats2.getLastTimeUsed(), usageStats.getLastTimeUsed());
        }
    }


    enum StatsUsageInterval {
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
}
