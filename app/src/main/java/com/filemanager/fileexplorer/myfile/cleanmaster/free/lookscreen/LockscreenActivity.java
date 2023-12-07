package com.filemanager.fileexplorer.myfile.cleanmaster.free.lookscreen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.HideChoseOptionActivity;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Sharepre_Ulti;
 


public class LockscreenActivity extends LockscreenHandler implements ActivityChanger {
    public static boolean check_over;
    private static Class classToGoAfter;
    public static Handler handler;
    public static int r;
    public static Runnable runnable;
    private TextView buttonTick;
    private int checkx;
    private int checky;
    private boolean forgottrue;
    private boolean idchek;
    private ImageView imageButtonDelete;
    private ImageView img_back;
    private ImageView img_changepass;
    private ImageView img_hidepass;
    private int int_dem;
    private RelativeLayout relativeLayoutBackground;
    private Sharepre_Ulti sharepre_ulti;
    private TextView textViewDot;
    private TextView textViewForgotPassword;
    private TextView textViewHAHA;
    private TextView txt_check;
    private int x;
    private int y;
    String tempPass = "";
    private int[] passButtonIds = {R.id.lbtn1, R.id.lbtn2, R.id.lbtn3, R.id.lbtn4, R.id.lbtn5, R.id.lbtn6, R.id.lbtn7, R.id.lbtn8, R.id.lbtn9, R.id.lbtn0};
    private String passString = "";
    private String realPass = "";
    private String status = "";
    private String checkStatus = "check";
    private String setStatus = "set";
    private String setStatus1 = "set1";
    private String disableStatus = "disable";
    private String changeStatus = "change";
    private String changeStatus1 = "change1";
    private String changeStatus2 = "change2";

    static int access$1908(LockscreenActivity lockscreenActivity) {
        int i = lockscreenActivity.int_dem;
        lockscreenActivity.int_dem = i + 1;
        return i;
    }

    static String access$284(LockscreenActivity lockscreenActivity, Object obj) {
        String str = lockscreenActivity.passString + obj;
        lockscreenActivity.passString = str;
        return str;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screenlooked_activity);
        FayazSP.init(this);
        this.realPass = getPassword();
        initViews();
        handler = new Handler();
        this.sharepre_ulti = new Sharepre_Ulti(this);
        String str = this.realPass;
        if (str == null || str.equals("")) {
            this.textViewForgotPassword.setVisibility(View.GONE);
            this.img_changepass.setVisibility(View.GONE);
        } else {
            this.textViewForgotPassword.setVisibility(View.VISIBLE);
            this.img_changepass.setVisibility(View.VISIBLE);
        }
        String string = getIntent().getExtras().getString("passStatus", "check");
        this.status = string;
        if (string.equals(this.setStatus)) {
            this.textViewHAHA.setText("Enter a New Password");
        }
        if (this.status.equals(this.disableStatus)) {
            FayazSP.put("password", (String) null);
            Toast.makeText(this, "Password Disabled", Toast.LENGTH_SHORT).show();
            gotoActivity();
        }
        this.img_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockscreenActivity.this.startActivity(new Intent(LockscreenActivity.this, ChangePassActivity.class));
                LockscreenActivity.this.finish();
                Animatoo.animateSwipeLeft(LockscreenActivity.this);
            }
        });
    }

    private void initViews() {
        this.img_changepass = (ImageView) findViewById(R.id.img_changepass);
        this.textViewHAHA = (TextView) findViewById(R.id.haha_text);
        this.textViewDot = (TextView) findViewById(R.id.dotText);
        this.textViewForgotPassword = (TextView) findViewById(R.id.forgot_pass_textview);
        this.buttonTick = (TextView) findViewById(R.id.lbtnTick);
        this.imageButtonDelete = (ImageView) findViewById(R.id.lbtnDelete);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.img_hidepass = (ImageView) findViewById(R.id.imghide_pass);
        this.txt_check = (TextView) findViewById(R.id.txt_check);
        this.relativeLayoutBackground = (RelativeLayout) findViewById(R.id.background_layout);
        final String string = FayazSP.getString("password", "");
        this.textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockscreenActivity.this.forgottrue = true;
                if (string.equals("") || string != null) {
                    LockscreenActivity.this.dialogForgotpass();
                } else {
                    Toast.makeText(LockscreenActivity.this, "Password not set", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LockscreenActivity.this.passString.length() > 0) {
                    LockscreenActivity lockscreenActivity = LockscreenActivity.this;
                    lockscreenActivity.passString = lockscreenActivity.passString.substring(0, LockscreenActivity.this.passString.length() - 1);
                }
                LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
            }
        });
        this.buttonTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LockscreenActivity.this.textViewDot.getText().length() >= 1) {
                    int readSharedPrefsInt = LockscreenActivity.this.sharepre_ulti.readSharedPrefsInt("key_forgot", 10);
                    int readSharedPrefsInt2 = LockscreenActivity.this.sharepre_ulti.readSharedPrefsInt("key_forgot2", 10);
                    if (LockscreenActivity.this.status.equals(LockscreenActivity.this.checkStatus)) {
                        if (!LockscreenActivity.this.passString.equals(LockscreenActivity.this.realPass)) {
                            LockscreenActivity.this.passString = "";
                            LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                            Toast.makeText(LockscreenActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(LockscreenActivity.this, HideChoseOptionActivity.class);
                        if (readSharedPrefsInt == 10 || readSharedPrefsInt2 == 10) {
                            LockscreenActivity.this.forgottrue = false;
                            LockscreenActivity.this.dialogForgotpass();
                            return;
                        }
                        LockscreenActivity.this.startActivity(intent);
                        LockscreenActivity.this.finish();
                        return;
                    } else if (!LockscreenActivity.this.status.equals(LockscreenActivity.this.setStatus)) {
                        if (LockscreenActivity.this.status.equals(LockscreenActivity.this.setStatus1)) {
                            if (LockscreenActivity.this.passString.equals(LockscreenActivity.this.tempPass)) {
                                FayazSP.put("password", LockscreenActivity.this.passString);
                                Toast.makeText(LockscreenActivity.this, "Password is set", Toast.LENGTH_SHORT).show();
                                LockscreenActivity.this.dialogForgotpass();
                                return;
                            }
                            LockscreenActivity lockscreenActivity = LockscreenActivity.this;
                            lockscreenActivity.tempPass = lockscreenActivity.passString;
                            LockscreenActivity.this.passString = "";
                            LockscreenActivity.this.tempPass = "";
                            LockscreenActivity lockscreenActivity2 = LockscreenActivity.this;
                            lockscreenActivity2.status = lockscreenActivity2.setStatus;
                            LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                            LockscreenActivity.this.textViewHAHA.setText("Enter a New Password");
                            Toast.makeText(LockscreenActivity.this, "Please Enter a New Password Again", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (LockscreenActivity.this.status.equals(LockscreenActivity.this.changeStatus)) {
                            if (!LockscreenActivity.this.passString.equals(LockscreenActivity.this.realPass)) {
                                LockscreenActivity.this.passString = "";
                                LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                                Toast.makeText(LockscreenActivity.this, "Please Enter Current Password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            LockscreenActivity lockscreenActivity3 = LockscreenActivity.this;
                            lockscreenActivity3.tempPass = lockscreenActivity3.passString;
                            LockscreenActivity.this.passString = "";
                            LockscreenActivity.this.tempPass = "";
                            LockscreenActivity lockscreenActivity4 = LockscreenActivity.this;
                            lockscreenActivity4.status = lockscreenActivity4.changeStatus1;
                            LockscreenActivity.this.textViewHAHA.setText("Enter a New Password");
                            LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                            return;
                        } else if (!LockscreenActivity.this.status.equals(LockscreenActivity.this.changeStatus1) || LockscreenActivity.this.passString.length() <= 0) {
                            if (LockscreenActivity.this.status.equals(LockscreenActivity.this.changeStatus2)) {
                                if (LockscreenActivity.this.passString.equals(LockscreenActivity.this.tempPass)) {
                                    FayazSP.put("password", LockscreenActivity.this.passString);
                                    Toast.makeText(LockscreenActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                    LockscreenActivity.this.gotoActivity();
                                    return;
                                }
                                LockscreenActivity lockscreenActivity5 = LockscreenActivity.this;
                                lockscreenActivity5.tempPass = lockscreenActivity5.passString;
                                LockscreenActivity.this.passString = "";
                                LockscreenActivity.this.tempPass = "";
                                LockscreenActivity lockscreenActivity6 = LockscreenActivity.this;
                                lockscreenActivity6.status = lockscreenActivity6.changeStatus1;
                                LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                                LockscreenActivity.this.textViewHAHA.setText("Enter a New Password");
                                Toast.makeText(LockscreenActivity.this, "Please Enter a New Password Again", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            return;
                        } else {
                            LockscreenActivity lockscreenActivity7 = LockscreenActivity.this;
                            lockscreenActivity7.tempPass = lockscreenActivity7.passString;
                            LockscreenActivity.this.passString = "";
                            LockscreenActivity lockscreenActivity8 = LockscreenActivity.this;
                            lockscreenActivity8.status = lockscreenActivity8.changeStatus2;
                            LockscreenActivity.this.textViewHAHA.setText("Confirm Password");
                            LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                            return;
                        }
                    } else {
                        LockscreenActivity lockscreenActivity9 = LockscreenActivity.this;
                        lockscreenActivity9.tempPass = lockscreenActivity9.passString;
                        LockscreenActivity.this.passString = "";
                        LockscreenActivity lockscreenActivity10 = LockscreenActivity.this;
                        lockscreenActivity10.status = lockscreenActivity10.setStatus1;
                        LockscreenActivity.this.textViewHAHA.setText("Confirm Password");
                        LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                        return;
                    }
                }
                Toast.makeText(LockscreenActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            }
        });
        this.img_hidepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockscreenActivity lockscreenActivity = LockscreenActivity.this;
                lockscreenActivity.idchek = !lockscreenActivity.idchek;
                LockscreenActivity lockscreenActivity2 = LockscreenActivity.this;
                lockscreenActivity2.view_pass(lockscreenActivity2.idchek);
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockscreenActivity.this.finish();
                Animatoo.animateSwipeRight(LockscreenActivity.this);
            }
        });
        int i = 0;
        while (true) {
            int[] iArr = this.passButtonIds;
            if (i >= iArr.length) {
                return;
            }
            final Button button = (Button) findViewById(iArr[i]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (LockscreenActivity.this.passString.length() >= 8) {
                        Toast.makeText(LockscreenActivity.this, "Up to 8 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        LockscreenActivity.access$284(LockscreenActivity.this, button.getText().toString());
                    }
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                }
            });
            i++;
        }
    }


    public void view_pass(boolean z) {
        if (z) {
            this.textViewDot.setInputType(1);
            this.img_hidepass.setImageResource(R.drawable.no_hide);
            return;
        }
        this.textViewDot.setInputType(129);
        this.img_hidepass.setImageResource(R.drawable.hide);
    }

    private String getPassword() {
        return FayazSP.getString("password", null);
    }


    public void gotoActivity() {
        startActivity(new Intent(this, classToGoAfter));
        finish();
    }

    @Override
    public void activityClass(Class cls) {
        classToGoAfter = cls;
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getRepeatCount() == 1) {
            handler.removeCallbacks(runnable);
            if (this.status.equals("check")) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }


    public void dialogForgotpass() {
        int r1;
        this.y = 0;
        this.x = 0;
        int[] iArr = {R.id.card_black, R.id.cardview_red, R.id.cardview_origin, R.id.cardview_pink, R.id.cardview_blue, R.id.cardview_green, R.id.cardview_green2, R.id.cardview_yellow};
        final int[] iArr2 = {R.id.checkblack, R.id.checkred, R.id.checkorigin, R.id.checkpink, R.id.checkblue, R.id.checkgreen, R.id.checkgreen2, R.id.checkyellow};
        final int[] iArr3 = {R.id.checkimg1, R.id.checkimg2, R.id.checkimg3, R.id.checkimg4, R.id.checkimg5, R.id.checkimg6};
        final ImageView[] imageViewArr = new ImageView[8];
        final ImageView[] imageViewArr2 = new ImageView[6];
        int[] iArr4 = {R.id.cardview_fl, R.id.cardview_house, R.id.cardview_moon, R.id.cardview_key, R.id.cardview_rank, R.id.cardview_coffe};
        @SuppressLint("ResourceType") Dialog dialog = new Dialog(this, 16973834);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_survey);
        for (int i = 0; i < 8; i++) {
            imageViewArr[i] = (ImageView) dialog.findViewById(iArr2[i]);
        }
        for (int i2 = 0; i2 < 6; i2++) {
            imageViewArr2[i2] = (ImageView) dialog.findViewById(iArr3[i2]);
        }
        Sharepre_Ulti sharepre_Ulti = new Sharepre_Ulti(this);
        int i3 = 0;
        for (int i4 = 8; i3 < i4; i4 = 8) {
            final int i5 = i3;
            ((CardView) dialog.findViewById(iArr[i3])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i6 = 0; i6 < iArr2.length; i6++) {
                        imageViewArr[i6].setVisibility(View.GONE);
                    }
                    imageViewArr[i5].setVisibility(View.VISIBLE);
                    LockscreenActivity.this.x = i5;
                }
            });
            i3 = i5 + 1;
            sharepre_Ulti = sharepre_Ulti;
            dialog = dialog;
        }
        final Sharepre_Ulti sharepre_Ulti2 = sharepre_Ulti;
        final Dialog dialog2 = dialog;
        for (int i6 = 0; i6 < 6; i6++) {
            final int i7 = i6;
            final int i8 = i6;
            ((CardView) dialog2.findViewById(iArr4[i6])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i9 = 0; i9 < iArr3.length; i9++) {
                        imageViewArr2[i9].setVisibility(View.GONE);
                    }
                    imageViewArr2[i7].setVisibility(View.VISIBLE);
                    LockscreenActivity.this.y = i8;
                }
            });
        }
        LinearLayout linearLayout = (LinearLayout) dialog2.findViewById(R.id.l_check);
        ImageView imageView = (ImageView) dialog2.findViewById(R.id.img_check);
        final TextView textView = (TextView) dialog2.findViewById(R.id.txt_resulf);
        final TextView textView2 = (TextView) dialog2.findViewById(R.id.txt_Cancel);
        ImageView imageView2 = (ImageView) dialog2.findViewById(R.id.img_back);
        final TextView textView3 = (TextView) dialog2.findViewById(R.id.txt_timecheck);
        final TextView textView4 = (TextView) dialog2.findViewById(R.id.txt_titile_check);
        if (this.forgottrue) {
            r1 = 0;
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            r1 = 0;
            linearLayout.setVisibility(View.GONE);
        }
        this.int_dem = r1;
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                LockscreenActivity.r--;
                textView4.setVisibility(View.VISIBLE);
                textView3.setText("Try laster :" + LockscreenActivity.r + "(s)");
                LockscreenActivity.handler.postDelayed(this, 1000L);
                if (LockscreenActivity.r <= 0) {
                    LockscreenActivity.handler.removeCallbacks(this);
                    LockscreenActivity.r = 0;
                    LockscreenActivity.this.int_dem = 0;
                    textView3.setText("");
                    textView4.setVisibility(View.GONE);
                    textView2.setEnabled(true);
                }
            }
        };
        runnable = runnable2;
        if (r > 0) {
            handler.postDelayed(runnable2, 1000L);
            textView2.setEnabled(r1==1?true:false);
        } else {
            textView3.setText("");
            textView2.setEnabled(true);
        }
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LockscreenActivity.this.forgottrue) {
                    if (LockscreenActivity.this.int_dem >= 5) {
                        LockscreenActivity.check_over = true;
                        LockscreenActivity.r = 300;
                        Toast.makeText(LockscreenActivity.this, "you have tried too many times", Toast.LENGTH_SHORT).show();
                        textView2.setEnabled(false);
                        textView4.setVisibility(View.VISIBLE);
                        LockscreenActivity.handler.postDelayed(LockscreenActivity.runnable, 1000L);
                    }
                    if (LockscreenActivity.this.x != sharepre_Ulti2.readSharedPrefsInt("key_forgot", 10) || LockscreenActivity.this.y != sharepre_Ulti2.readSharedPrefsInt("key_forgot2", 10)) {
                        if (LockscreenActivity.this.x > 8 || LockscreenActivity.this.y > 6) {
                            LockscreenActivity lockscreenActivity = LockscreenActivity.this;
                            Toast.makeText(lockscreenActivity, lockscreenActivity.getString(R.string.choose_missing), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LockscreenActivity.access$1908(LockscreenActivity.this);
                        Toast.makeText(LockscreenActivity.this, "false", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LockscreenActivity.r = 0;
                    TextView textView5 = textView;
                    textView5.setText("" + FayazSP.getString("password", "\\u2022\\u2022\\u2022\\u2022\\u2022\\u2022\\u2022"));
                    textView3.setText("");
                    textView4.setVisibility(View.GONE);
                    textView2.setEnabled(false);
                } else if (LockscreenActivity.this.x != 10 && LockscreenActivity.this.y != 10) {
                    sharepre_Ulti2.writeSharedPrefs("key_forgot", LockscreenActivity.this.x);
                    sharepre_Ulti2.writeSharedPrefs("key_forgot2", LockscreenActivity.this.y);
                    dialog2.dismiss();
                    LockscreenActivity.this.startActivity(new Intent(LockscreenActivity.this, HideChoseOptionActivity.class));
                    LockscreenActivity.this.finish();
                } else {
                    LockscreenActivity lockscreenActivity2 = LockscreenActivity.this;
                    Toast.makeText(lockscreenActivity2, lockscreenActivity2.getResources().getString(R.string.complete_survey), Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockscreenActivity.handler.removeCallbacks(LockscreenActivity.runnable);
                dialog2.cancel();
            }
        });
        dialog2.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (r > 0) {
            handler.postDelayed(runnable, 1000L);
        }
    }
}
