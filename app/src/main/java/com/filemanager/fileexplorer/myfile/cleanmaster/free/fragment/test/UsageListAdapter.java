package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class UsageListAdapter extends RecyclerView.Adapter<UsageListAdapter.ViewHolder> {
    private Context context;
    private int type;
    Ultil ultil;
    private List<CustomUsageStats> mCustomUsageStatsList = new ArrayList();
    private List<CustomUsageStats> listcontain30 = new ArrayList();
    private DateFormat mDateFormat = new SimpleDateFormat();
    private ArrayList<String> strings = new ArrayList<>();
    private ArrayList<Item_unappp> list = new ArrayList<>();

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mAppIcon;
        private final TextView mLastTimeUsed;
        private final TextView mPackageName;
        private RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            this.mPackageName = (TextView) view.findViewById(R.id.textview_package_name);
            this.mLastTimeUsed = (TextView) view.findViewById(R.id.textview_last_time_used);
            this.mAppIcon = (ImageView) view.findViewById(R.id.app_icon);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.rel_app);
        }

        public TextView getLastTimeUsed() {
            return this.mLastTimeUsed;
        }

        public TextView getPackageName() {
            return this.mPackageName;
        }

        public ImageView getAppIcon() {
            return this.mAppIcon;
        }

        public RelativeLayout getrela() {
            return this.relativeLayout;
        }
    }

    public UsageListAdapter(Context context) {
        this.context = context;
        this.ultil = new Ultil(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usage_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ApplicationInfo applicationInfo;
        final String pkg = this.list.get(i).getPkg();
        PackageManager packageManager = this.context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(pkg, 0);
            try {
                PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(pkg, 0);
                TextView lastTimeUsed = viewHolder.getLastTimeUsed();
                lastTimeUsed.setText(" " + packageInfo.versionName + " - size: " + this.ultil.bytesToHuman(new File(applicationInfo.publicSourceDir).length()));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            applicationInfo = null;
        }
        viewHolder.getPackageName().setText((String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : pkg));
        viewHolder.getAppIcon().setImageDrawable(this.list.get(i).icon);
        viewHolder.getrela().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + pkg));
                UsageListAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void setCustomUsageStatsList(List<CustomUsageStats> list) {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (currentTimeMillis - (list.get(i).usageStats.getLastTimeUsed() * 1000) < -1944854528) {
                arrayList.add(list.get(i));
            } else {
                this.strings.add(list.get(i).usageStats.getPackageName());
            }
        }
        this.mCustomUsageStatsList = new ArrayList();
        this.list = new ArrayList<>();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (!isAppInSystemPartition(((CustomUsageStats) arrayList.get(i2)).usageStats.getPackageName())) {
                this.mCustomUsageStatsList.add((CustomUsageStats) arrayList.get(i2));
            }
        }
        for (int i3 = 0; i3 < this.mCustomUsageStatsList.size(); i3++) {
            for (int i4 = 0; i4 < this.mCustomUsageStatsList.size(); i4++) {
                if (this.mCustomUsageStatsList.get(i3).usageStats.getPackageName().equals(this.mCustomUsageStatsList.get(i4).usageStats.getPackageName())) {
                    this.mCustomUsageStatsList.remove(i4);
                }
            }
        }
        Collections.sort(this.mCustomUsageStatsList, new Comparator<CustomUsageStats>() {
            @Override
            public int compare(CustomUsageStats customUsageStats, CustomUsageStats customUsageStats2) {
                return String.valueOf(customUsageStats2.usageStats.getLastTimeUsed()).compareTo(String.valueOf(customUsageStats.usageStats.getLastTimeUsed()));
            }
        });
        ArrayList arrayList2 = new ArrayList();
        for (ApplicationInfo applicationInfo : this.context.getPackageManager().getInstalledApplications(9344)) {
            if ((applicationInfo.flags & 1) != 1) {
                arrayList2.add(applicationInfo.packageName);
            }
        }
        for (int i5 = 0; i5 < arrayList2.size(); i5++) {
            Log.d("TAG!", "setCustomUsageStatsList: " + ((String) arrayList2.get(i5)) + "sss:" + arrayList2.size());
        }
        for (int i6 = 0; i6 < this.mCustomUsageStatsList.size(); i6++) {
            Log.d("TAGA", "setCustomUsageStatsList: " + this.mCustomUsageStatsList.get(i6).usageStats.getPackageName() + "sss:" + this.mCustomUsageStatsList.size() + "time:" + new Ultil(this.context).getDate(this.mCustomUsageStatsList.get(i6).usageStats.getLastTimeUsed()));
        }
        for (int i7 = 0; i7 < this.mCustomUsageStatsList.size(); i7++) {
            String packageName = this.mCustomUsageStatsList.get(i7).usageStats.getPackageName();
            if (arrayList2.contains(packageName)) {
                if (arrayList2.contains(this.context.getPackageName())) {
                    arrayList2.remove(this.context.getPackageName());
                }
                arrayList2.remove(packageName);
            }
        }
        for (int i8 = 0; i8 < arrayList2.size(); i8++) {
            Item_unappp item_unappp = new Item_unappp();
            try {
                item_unappp.setPkg((String) arrayList2.get(i8));
                item_unappp.setIcon(this.context.getPackageManager().getApplicationIcon((String) arrayList2.get(i8)));
                this.list.add(item_unappp);
            } catch (Exception unused) {
            }
        }
        notifyDataSetChanged();
    }

    public boolean isAppInSystemPartition(String str) {
        try {
            return (this.context.getPackageManager().getApplicationInfo(str, 0).flags & 129) != 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
