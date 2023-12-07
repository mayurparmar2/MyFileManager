package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.progressWheel;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.R;


public class Main extends AppCompatActivity {
    int progress = 0;
    ProgressWheel pw_four;
    ProgressWheel pw_three;
    ProgressWheel pw_two;
    boolean running;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main2);
        this.pw_two = (ProgressWheel) findViewById(R.id.progressBarTwo);
        this.pw_three = (ProgressWheel) findViewById(R.id.progressBarThree);
        this.pw_four = (ProgressWheel) findViewById(R.id.progressBarFour);
        new ShapeDrawable(new RectShape());
        this.pw_three.setRimShader(new BitmapShader(Bitmap.createBitmap(new int[]{-13725407, -13725407, -13725407, -13725407, -13725407, -13725407, -1, -1}, 8, 1, Bitmap.Config.ARGB_8888), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        this.pw_three.spin();
        this.pw_four.spin();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Main.this.running = true;
                while (Main.this.progress < 361) {
                    Main.this.pw_two.incrementProgress();
                    Main.this.progress++;
                    try {
                        Thread.sleep(15L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Main.this.running = false;
            }
        };
        ((Button) findViewById(R.id.btn_spin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Main.this.running) {
                    return;
                }
                if (Main.this.pw_two.isSpinning) {
                    Main.this.pw_two.stopSpinning();
                }
                Main.this.pw_two.resetCount();
                Main.this.pw_two.setText("Loading...");
                Main.this.pw_two.spin();
            }
        });
        ((Button) findViewById(R.id.btn_increment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Main.this.running) {
                    return;
                }
                Main.this.progress = 0;
                Main.this.pw_two.resetCount();
                new Thread(runnable).start();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        this.progress = 361;
        this.pw_two.stopSpinning();
        this.pw_two.resetCount();
        this.pw_two.setText("Click\none of the\nbuttons");
    }
}
