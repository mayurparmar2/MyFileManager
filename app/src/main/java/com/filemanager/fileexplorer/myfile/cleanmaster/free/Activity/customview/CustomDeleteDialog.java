package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.demo.example.R;


public class CustomDeleteDialog extends Dialog {
    DeleteCallback deleteCallback;
    TextView textView_titile;
    TextView txt_yes;

    public TextView getTextView_titile() {
        return this.textView_titile;
    }

    public void setTextView_titile(TextView textView) {
        this.textView_titile = textView;
    }

    public CustomDeleteDialog(Context context, DeleteCallback deleteCallback) {
        super(context);
        this.deleteCallback = deleteCallback;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_recycle);
        this.textView_titile = (TextView) findViewById(R.id.titile);
        this.txt_yes = (TextView) findViewById(R.id.txt_yes);
        findViewById(R.id.txt_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                CustomDeleteDialog.this.m65x13accb41(view);
            }
        });
        this.txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                CustomDeleteDialog.this.m66xf95827c2(view);
            }
        });
    }


    public void m65x13accb41(View view) {
        dismiss();
    }


    public void m66xf95827c2(View view) {
        this.deleteCallback.update();
        dismiss();
    }

    public void setview() {
        this.textView_titile.setText(R.string.hide_titile_dialog_delete);
    }

    public void setTextdelete() {
        this.textView_titile.setText(R.string.delete_dialog_title);
    }

    public void setTextdeleteMultil() {
        this.textView_titile.setText(R.string.delete_therefiles);
    }

    public void settextdeleteone_file() {
        this.textView_titile.setText(R.string.delete_one_img);
    }

    public void setTextmul_file() {
        this.textView_titile.setText(R.string.delete_files);
    }

    public void set_titile_restore_oneVideo() {
        this.txt_yes.setText(R.string.restore_im);
        this.textView_titile.setText(R.string.delete_one_video);
    }

    public void set_tiltile_restore_mulvideo() {
        this.txt_yes.setText(R.string.restore_im);
        this.textView_titile.setText(R.string.delete_multil_video);
    }

    public void set_title_hide() {
        this.textView_titile.setText(R.string.titile_hide);
        this.txt_yes.setText(R.string.hide);
    }

    public void delete_video() {
        this.textView_titile.setText(R.string.delete_video);
        this.txt_yes.setText(R.string.delete);
    }

    public void delete_videos() {
        this.textView_titile.setText(R.string.delete_videos);
        this.txt_yes.setText(R.string.delete);
    }

    public void set_title_exit() {
        this.textView_titile.setText(R.string.exits_titile);
        this.txt_yes.setText(R.string.exit);
    }

    public void set_titile_permission_Usage() {
        this.txt_yes.setText(R.string.agree);
        this.textView_titile.setText(R.string.permision_titile_usage);
    }

    public void set_titile_dialog_exit_antivirus() {
        this.txt_yes.setText(R.string.exit);
    }

    public void set_titile_button_hide() {
        this.txt_yes.setText(R.string.hide);
    }

    public void settitle_Music() {
        this.textView_titile.setText(R.string.delete_audio_file);
    }

    public void set_titile_multil_audio() {
        this.textView_titile.setText(R.string.delete_multil_files);
    }

    public void delete_permanerly() {
        this.textView_titile.setText(R.string.delete_permanely);
    }

    public void delete_onefile_permanenly() {
        this.textView_titile.setText(R.string.delete_one_file_permanerly);
    }

    public void restore_img() {
        this.txt_yes.setText(R.string.restore_im);
        this.textView_titile.setText(R.string.tititle_restore);
    }

    public void restore_imgs() {
        this.txt_yes.setText(R.string.restore_im);
        this.textView_titile.setText(R.string.titile_restore_imgs);
    }

    public void set_title_trustapp() {
        this.textView_titile.setText(R.string.trust_this_app);
        this.txt_yes.setText(R.string.trust);
    }

    public void titile_retrict_access() {
        this.textView_titile.setText(R.string.titile_retrict);
        this.txt_yes.setText(R.string.ok);
    }
}
