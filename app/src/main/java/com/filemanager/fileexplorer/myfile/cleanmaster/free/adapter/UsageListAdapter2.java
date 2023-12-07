package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.UStats;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.test.CustomUsageStats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class UsageListAdapter2 extends RecyclerView.Adapter<UsageListAdapter2.ViewHolder> {
    private Context context;
    private List<CustomUsageStats> mCustomUsageStatsList = new ArrayList();
    private DateFormat mDateFormat = new SimpleDateFormat();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mAppIcon;

        public ViewHolder(View view) {
            super(view);
            this.mAppIcon = (ImageView) view.findViewById(R.id.img_e);
        }

        public ImageView getAppIcon() {
            return this.mAppIcon;
        }
    }

    public UsageListAdapter2(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_dowloadadapter_laucher, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            this.context.getPackageManager().getPackageInfo(this.mCustomUsageStatsList.get(i).usageStats.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        viewHolder.getAppIcon().setImageDrawable(this.mCustomUsageStatsList.get(i).appIcon);
    }

    @Override
    public int getItemCount() {
        return this.mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<CustomUsageStats> list) {
        Log.d(UStats.TAG, "setCustomUsageStatsList: " + list.size());
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            if (currentTimeMillis - list.get(i).usageStats.getLastTimeUsed() > -1702967296) {
                this.mCustomUsageStatsList.add(list.get(i));
            }
        }
    }
}
