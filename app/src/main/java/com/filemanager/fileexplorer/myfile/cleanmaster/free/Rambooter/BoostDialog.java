package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.demo.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BoostDialog extends DialogFragment implements View.OnClickListener {
    private Button btnOK;
    private String cacheCleaned;
    private Dialog dialog = null;
    private String memoryCleaned;
    private String processKilled;
    private TextView txtCacheCleaned;
    private TextView txtMemoryCleaned;
    private TextView txtProcessKilled;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity());
        this.dialog = dialog;
        dialog.setContentView(R.layout.boost_dialog);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.txtProcessKilled = (TextView) this.dialog.findViewById(R.id.txtDialogProcessKilled);
        this.txtMemoryCleaned = (TextView) this.dialog.findViewById(R.id.txtMemoryCleaned);
        this.txtCacheCleaned = (TextView) this.dialog.findViewById(R.id.txtCacheCleaned);
        Button button = (Button) this.dialog.findViewById(R.id.btnDialogOK);
        this.btnOK = button;
        button.setOnClickListener(this);
        ShowInfo();
        return this.dialog;
    }

    public void ShowInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        SharedPreferences.Editor edit = getActivity().getSharedPreferences("last_boost", 0).edit();
        edit.putString("boost_time", simpleDateFormat.format(time));
        edit.putString("memory_cleaned", getMemoryCleaned());
        edit.putString("cache_cleaned", getCacheCleaned());
        edit.commit();
        TextView textView = this.txtProcessKilled;
        textView.setText("Process Killed: " + getProcessKilled());
        TextView textView2 = this.txtMemoryCleaned;
        textView2.setText("Memory Cleaned: " + getMemoryCleaned());
        TextView textView3 = this.txtCacheCleaned;
        textView3.setText("Cache Cleaned: " + getCacheCleaned());
    }

    private String todayDate() {
        StringBuilder sb;
        String str;
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(5);
        int i2 = calendar.get(2) + 1;
        if (i < 10) {
            sb = new StringBuilder();
            sb.append("0");
        } else {
            sb = new StringBuilder();
            sb.append("");
        }
        sb.append(i);
        String sb2 = sb.toString();
        if (i2 < 10) {
            str = "0" + i2;
        } else {
            str = "" + i2;
        }
        return sb2 + "/" + str + "/" + ("" + calendar.get(1)) + " " + String.valueOf(10) + ":" + String.valueOf(12);
    }

    public String getProcessKilled() {
        return this.processKilled;
    }

    public void setProcessKilled(String str) {
        this.processKilled = str;
    }

    public String getMemoryCleaned() {
        return this.memoryCleaned;
    }

    public void setMemoryCleaned(String str) {
        this.memoryCleaned = str;
    }

    public String getCacheCleaned() {
        return this.cacheCleaned;
    }

    public void setCacheCleaned(String str) {
        this.cacheCleaned = str;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
