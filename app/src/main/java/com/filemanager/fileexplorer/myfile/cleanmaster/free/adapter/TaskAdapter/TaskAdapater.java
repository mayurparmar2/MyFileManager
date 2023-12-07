package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.TaskAdapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process.TaskInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class TaskAdapater extends BaseQuickAdapter<TaskInfo, BaseViewHolder> {
    private Context context;
    private DateFormat mDateFormat;

    public TaskAdapater(Context context) {
        super(R.layout.process_list_layout);
        this.context = context;
        this.mDateFormat = new SimpleDateFormat();
    }


    @Override
    public void convert(BaseViewHolder baseViewHolder, TaskInfo taskInfo) {
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.txtTaskCPU);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.txtTaskMemory);
        ((ImageView) baseViewHolder.itemView.findViewById(R.id.imgTaskIcon)).setImageDrawable(taskInfo.getdraw());
        ((TextView) baseViewHolder.itemView.findViewById(R.id.txtTaskTitle)).setText(taskInfo.getName());
        new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(taskInfo.getUsageStats().getPackageName(), 0);
            textView2.setText("" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
