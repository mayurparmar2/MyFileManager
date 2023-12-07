package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.R;


public class TestAnimationActivity extends AppCompatActivity {
    private int a;
    private int b;
    private ImageView imageViewl;
    private int k;
    private int l;
    private int o;
    private float prooss;
    private int x;
    private int y;
    private int z;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_test_animation);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        final int i2 = displayMetrics.widthPixels;
        this.imageViewl = (ImageView) findViewById(R.id.img_roket);
        double d = i / 2;
        double d2 = i2 / 2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, (float) Math.sqrt(Math.pow(d, 2.0d) + Math.pow(d2, 2.0d)));
        ofFloat.setRepeatCount(-1);
        ofFloat.setRepeatMode(2);
        ofFloat.setDuration(100L);
        this.x = (int) Math.sqrt(Math.pow(d, 2.0d) + Math.pow(d2, 2.0d));
        this.y = 740;
        this.a = -740;
        this.k = 0;
        this.b = -1480;
        this.o = -740;
        this.z = 1;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TestAnimationActivity.this.m64x85d8fec7(i2, valueAnimator);
            }
        });
        ofFloat.start();
        Log.d("TAGD", "onCreate: " + this.prooss);
    }


    public void m64x85d8fec7(int i, ValueAnimator valueAnimator) {
        this.prooss = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int i2 = this.z;
        if (i2 >= this.x / 2) {
            int i3 = this.y - 5;
            this.y = i3;
            this.a -= 5;
            this.imageViewl.setTranslationX(i3);
            this.imageViewl.setTranslationY(-this.a);
            if (this.y <= 0) {
                this.z = 0;
            }
            Log.d("TAGN", "onCreate: " + this.a);
        } else if (this.y <= 0) {
            int i4 = this.k - 5;
            this.k = i4;
            this.b += 5;
            this.imageViewl.setTranslationX(i4);
            this.imageViewl.setTranslationY(-this.b);
            Log.d("TAGP", "onCreate: " + this.k);
            if (this.k <= -740) {
                this.l = -740;
                this.y = 1;
            }
        } else if (this.l == -740) {
            int i5 = this.o + 5;
            this.o = i5;
            this.imageViewl.setTranslationX(i5);
            this.imageViewl.setTranslationY(-this.o);
            Log.d("TAGL", "onCreate: " + this.l);
        } else {
            this.imageViewl.setTranslationX(i2);
            this.imageViewl.setTranslationY(this.z);
            this.z += 5;
            Log.d("TAG", "onCreate: " + this.z + ":" + i);
        }
    }
}
