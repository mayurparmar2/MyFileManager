package com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.example.R;


public class ChangePassActivity extends AppCompatActivity {
    private EditText edt_newpass;
    private EditText edt_newpass2;
    private EditText edt_oldpass;
    private ImageView img_back;
    private LinearLayout l_changpass;
    private LinearLayout l_passchanged;
    private String newpass;
    private String newpass2;
    private String oldpass;
    private TextView txt_checknewpass;
    private TextView txt_checkoldpass;
    private TextView txt_setpass;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_change_pass);
        this.edt_oldpass = (EditText) findViewById(R.id.edt_oldpass);
        this.edt_newpass = (EditText) findViewById(R.id.edt_newpass);
        this.edt_newpass2 = (EditText) findViewById(R.id.edt_newpass2);
        this.l_changpass = (LinearLayout) findViewById(R.id.l_changepass);
        this.l_passchanged = (LinearLayout) findViewById(R.id.l_passchanged);
        this.txt_checknewpass = (TextView) findViewById(R.id.txt_idchecknewpass);
        this.txt_checkoldpass = (TextView) findViewById(R.id.txt_idcheckoldpass);
        this.txt_setpass = (TextView) findViewById(R.id.txt_setpass);
        ImageView imageView = (ImageView) findViewById(R.id.img_back);
        this.img_back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassActivity.this.finish();
                Animatoo.animateSlideRight(ChangePassActivity.this);
            }
        });
        this.oldpass = FayazSP.getString("password", "");
        this.edt_oldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!ChangePassActivity.this.oldpass.equals(charSequence.toString())) {
                    ChangePassActivity.this.txt_checkoldpass.setTextColor(0xffff0000);
                    ChangePassActivity.this.txt_checkoldpass.setText(R.string.incorect_password);
                    ChangePassActivity.this.edt_newpass.setEnabled(false);
                    ChangePassActivity.this.edt_newpass2.setEnabled(false);
                    return;
                }
                ChangePassActivity.this.txt_checkoldpass.setTextColor(-16776961);
                ChangePassActivity.this.txt_checkoldpass.setText(R.string.exactly);
                ChangePassActivity.this.edt_newpass.setEnabled(true);
                ChangePassActivity.this.edt_newpass2.setEnabled(true);
            }
        });
        this.edt_newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ChangePassActivity.this.newpass = charSequence.toString();
                if (ChangePassActivity.this.newpass.length() <= 2) {
                    Toast.makeText(ChangePassActivity.this, "Weak password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.edt_newpass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ChangePassActivity.this.newpass2 = charSequence.toString();
                if (ChangePassActivity.this.newpass2.length() != ChangePassActivity.this.newpass.length() || !ChangePassActivity.this.newpass.equals(ChangePassActivity.this.newpass2)) {
                    ChangePassActivity.this.txt_checknewpass.setText(R.string.invalid_password);
                } else {
                    ChangePassActivity.this.txt_checknewpass.setText("");
                }
            }
        });
        this.txt_setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChangePassActivity.this.newpass2.equals(ChangePassActivity.this.newpass)) {
                    FayazSP.put("password", ChangePassActivity.this.newpass2);
                    Toast.makeText(ChangePassActivity.this, "password ", Toast.LENGTH_SHORT).show();
                    ChangePassActivity.this.l_changpass.setVisibility(View.GONE);
                    ChangePassActivity.this.l_passchanged.setVisibility(View.VISIBLE);
                    return;
                }
                Toast.makeText(ChangePassActivity.this, "password incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
