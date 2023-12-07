package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.taskdialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.demo.example.R;


public class TaskKillDialog extends DialogFragment implements View.OnClickListener {
    private String appName;
    private Button btnTaskCancel;
    private Button btnTaskKill;
    private int icon;
    private ImageView imgTaskIcon;
    private DialogTaskKillListener listener;
    private TextView txtTaskTitle;
    private Dialog dialog = null;
    private int pos = 0;


    public interface DialogTaskKillListener {
        void onTaskKIll(int i);
    }

    public void setDialgTaskKillListener(TaskFragment taskFragment) {
        this.listener = taskFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity());
        this.dialog = dialog;
        dialog.getWindow().setFlags(4, 4);
        this.dialog.getWindow().requestFeature(1);
        this.dialog.getWindow().setFlags(1024, 1024);
        this.dialog.setContentView(R.layout.taskclean_dialog);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.txtTaskTitle = (TextView) this.dialog.findViewById(R.id.txtTaskTitle);
        this.imgTaskIcon = (ImageView) this.dialog.findViewById(R.id.imgTaskIcon);
        this.btnTaskKill = (Button) this.dialog.findViewById(R.id.btnTaskClean);
        this.btnTaskCancel = (Button) this.dialog.findViewById(R.id.btnTaskCancel);
        this.btnTaskKill.setOnClickListener(this);
        this.btnTaskCancel.setOnClickListener(this);
        int i = this.icon;
        if (i > 0) {
            this.imgTaskIcon.setImageResource(i);
        }
        if (!TextUtils.isEmpty(this.appName)) {
            this.txtTaskTitle.setText(this.appName);
        } else {
            this.txtTaskTitle.setText(getActivity().getResources().getString(R.string.dialog_title));
        }
        return this.dialog;
    }

    public void setPos(int i) {
        this.pos = i;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public void setIcon(int i) {
        this.icon = i;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTaskCancel:
                dismiss();
                return;
            case R.id.btnTaskClean:
                this.listener.onTaskKIll(this.pos);
                dismiss();
                return;
            default:
                return;
        }
    }
}
