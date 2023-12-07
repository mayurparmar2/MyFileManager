package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.example.R;


public class Dialog_thread {
    private Dialog aDialog;
    private AlertDialog.Builder aDialogBuilder;
    private Context context;
    private ImageView imageView;

    public Dialog_thread(Context context) {
        this.context = context;
        Dialog dialog = new Dialog(context);
        this.aDialog = dialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.aDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        this.aDialog.setContentView(R.layout.dialog_process);
        this.imageView = (ImageView) this.aDialog.findViewById(R.id.gif);
        Glide.with(context).load(Integer.valueOf((int) R.raw.rocket)).into(this.imageView);
    }

    public void show() {
        this.aDialog.show();
    }

    public void dissmiss() {
        this.aDialog.cancel();
    }
}
