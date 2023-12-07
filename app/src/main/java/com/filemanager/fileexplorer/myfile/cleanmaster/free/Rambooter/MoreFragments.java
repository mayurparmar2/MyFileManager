package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CircularProgressIndicator;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.AntivirusActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.AppData;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.progressWheel.ProgressWheel;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.io.File;


public class MoreFragments extends Fragment {
    private ImageView anti;
    CircularProgressIndicator circularProgressIndicator;
    private LinearLayout layourExternal;
    private TextView txtAvail;
    private TextView txtExAvail;
    private TextView txtExFree;
    private TextView txtExTotal;
    private TextView txtFree;
    private TextView txtTotal;
    private TextView txt_scan;
    private ProgressWheel pw = null;
    private ProgressWheel exPW = null;
    long total = 0;
    private final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double d) {
            return String.valueOf((long) ((d / MoreFragments.this.total) * 100.0d)) + "%";
        }
    };

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.memory_info_layout, viewGroup, false);
        this.pw = (ProgressWheel) inflate.findViewById(R.id.pw_spinner_internal);
        this.exPW = (ProgressWheel) inflate.findViewById(R.id.pw_spinner_external);
        this.anti = (ImageView) inflate.findViewById(R.id.antivirus);
        this.layourExternal = (LinearLayout) inflate.findViewById(R.id.layourExternal);
        this.txtTotal = (TextView) inflate.findViewById(R.id.txtTotalInternalMemory);
        this.txtAvail = (TextView) inflate.findViewById(R.id.txtAvailInternalMemory);
        this.txtFree = (TextView) inflate.findViewById(R.id.txtFreeInternalMemory);
        this.txt_scan = (TextView) inflate.findViewById(R.id.txt_scan);
        this.txtExTotal = (TextView) inflate.findViewById(R.id.txtTotalExternalMemory);
        this.txtExAvail = (TextView) inflate.findViewById(R.id.txtAvailExternalMemory);
        this.txtExFree = (TextView) inflate.findViewById(R.id.txtFreeExternalMemory);
        CircularProgressIndicator circularProgressIndicator = (CircularProgressIndicator) inflate.findViewById(R.id.circular_progress);
        this.circularProgressIndicator = circularProgressIndicator;
        circularProgressIndicator.setTextSize1((int) new Ultil(getContext()).dpToPx(40.0f));
        this.circularProgressIndicator.setAnimationEnabled(true);
        this.circularProgressIndicator.setFlag(true);
        this.total = DeviceMemoryInfo.getTotalInternalMemorySize();
        this.circularProgressIndicator.setProgress(DeviceMemoryInfo.getAvailableInternalMemorySize(), this.total);
        this.circularProgressIndicator.setAnimationEnabled(true);
        this.circularProgressIndicator.setProgressTextAdapter(this.TIME_TEXT_ADAPTER);
        this.txt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoreFragments.this.startActivity(new Intent(MoreFragments.this.getActivity(), AntivirusActivity.class));
                Animatoo.animateSlideLeft(MoreFragments.this.getContext());
            }
        });
        this.anti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.getInstance(MoreFragments.this.getActivity());
                new Thread() {
                    @Override
                    public void run() {
                        Intent intent;
                        try {
                            try {
                                sleep(500L);
                                intent = new Intent(MoreFragments.this.getActivity(), AntivirusActivity.class);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                intent = new Intent(MoreFragments.this.getActivity(), AntivirusActivity.class);
                            }
                            MoreFragments.this.startActivity(intent);
                        } catch (Throwable th) {
                            MoreFragments.this.startActivity(new Intent(MoreFragments.this.getActivity(), AntivirusActivity.class));
                            throw th;
                        }
                    }
                }.start();
            }
        });
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        new DeviceMemoryInfo(getActivity());
        String formatFileSize = Formatter.formatFileSize(getActivity(), DeviceMemoryInfo.getTotalInternalMemorySize());
        String formatFileSize2 = Formatter.formatFileSize(getActivity(), DeviceMemoryInfo.getAvailableInternalMemorySize());
        this.txtTotal.setText(formatFileSize);
        this.txtAvail.setText(formatFileSize2);
        this.txtFree.setText(DeviceMemoryInfo.getFreeInternalMemorySize());
        setInternalPercentage((int) ((((float) DeviceMemoryInfo.getAvailableInternalMemorySize()) / ((float) DeviceMemoryInfo.getTotalInternalMemorySize())) * 100.0f));
        String[] storageDirectories = DeviceMemoryInfo.getStorageDirectories();
        for (int i = 0; i < storageDirectories.length; i++) {
            if (!TextUtils.isEmpty(storageDirectories[i])) {
                File file = new File(storageDirectories[i]);
                if (file.exists() && file.length() > 0) {
                    ShowExternalInfo(file);
                }
            }
        }
    }

    private void ShowExternalInfo(File file) {
        this.layourExternal.setVisibility(View.VISIBLE);
        String formatFileSize = Formatter.formatFileSize(getActivity(), DeviceMemoryInfo.getTotalExternalMemorySize(file));
        String formatFileSize2 = Formatter.formatFileSize(getActivity(), DeviceMemoryInfo.getAvailableExternalMemorySize(file));
        TextView textView = this.txtExTotal;
        textView.setText("Total: " + formatFileSize);
        TextView textView2 = this.txtExAvail;
        textView2.setText("Available: " + formatFileSize2);
        TextView textView3 = this.txtExFree;
        textView3.setText("Used: " + DeviceMemoryInfo.getFreeExternalMemorySize(file));
        setExternalPercentage((int) ((((float) DeviceMemoryInfo.getAvailableExternalMemorySize(file)) / ((float) DeviceMemoryInfo.getTotalExternalMemorySize(file))) * 100.0f));
    }

    public void setInternalPercentage(int i) {
        this.pw.setText(i + "%");
        this.pw.setProgress((int) (((double) i) * 3.6d));
    }

    public void setExternalPercentage(int i) {
        this.exPW.setText(i + "%");
        this.exPW.setProgress((int) (((double) i) * 3.6d));
    }
}
