package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.util.ArrayList;


public class HideChoseOptionActivity extends AppCompatActivity {
    ArrayList<File_DTO> hide_list;
    ImageView imageView_image;
    ImageView imageView_video;
    ImageView img_back;
    LinearLayout l_img;
    LinearLayout l_video;
    ArrayList<File_DTO> listall;
    TextView txt_contimg;
    TextView txt_countvideo;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_hide_chose_option);
        this.imageView_image = (ImageView) findViewById(R.id.img_IM);
        this.imageView_video = (ImageView) findViewById(R.id.img_VD);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.l_img = (LinearLayout) findViewById(R.id.l_imghide);
        this.l_video = (LinearLayout) findViewById(R.id.l_videohide);
        this.txt_contimg = (TextView) findViewById(R.id.txt_count_image);
        this.txt_countvideo = (TextView) findViewById(R.id.txt_count_video);
        this.listall = new Ultil(this).gethidefile();
        this.imageView_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HideChoseOptionActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "videohide");
                HideChoseOptionActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(HideChoseOptionActivity.this);
            }
        });
        this.imageView_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HideChoseOptionActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "imagehide");
                HideChoseOptionActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(HideChoseOptionActivity.this);
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideChoseOptionActivity.this.finish();
                Animatoo.animateSwipeRight(HideChoseOptionActivity.this);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                TextView textView = HideChoseOptionActivity.this.txt_countvideo;
                textView.setText("" + HideChoseOptionActivity.this.videohide().size());
                TextView textView2 = HideChoseOptionActivity.this.txt_contimg;
                textView2.setText("" + HideChoseOptionActivity.this.imagehide().size());
            }
        }).start();
    }


    public ArrayList<File_DTO> videohide() {
        this.hide_list = new ArrayList<>();
        for (int i = 0; i < this.listall.size(); i++) {
            String path = this.listall.get(i).getPath();
            if (path.toLowerCase().contains(".mp4") || path.toLowerCase().contains(".avi") || path.toLowerCase().contains(".mkv") || path.contains(".vob") || path.toLowerCase().contains("mov")) {
                this.hide_list.add(this.listall.get(i));
            }
        }
        return this.hide_list;
    }


    public ArrayList<File_DTO> imagehide() {
        this.hide_list = new ArrayList<>();
        for (int i = 0; i < this.listall.size(); i++) {
            String path = this.listall.get(i).getPath();
            if (path.toLowerCase().contains(".jpg") || path.toLowerCase().contains(".png") || path.toLowerCase().contains(".gif") || path.toLowerCase().contains(".tiff") || path.toLowerCase().contains("jpeg")) {
                this.hide_list.add(this.listall.get(i));
            }
        }
        return this.hide_list;
    }


    @Override
    public void onResume() {
        this.listall = new Ultil(this).gethidefile();
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                TextView textView = HideChoseOptionActivity.this.txt_countvideo;
                textView.setText("" + HideChoseOptionActivity.this.videohide().size());
                TextView textView2 = HideChoseOptionActivity.this.txt_contimg;
                textView2.setText("" + HideChoseOptionActivity.this.imagehide().size());
            }
        }.obtainMessage(111, "parameter").sendToTarget();
        super.onResume();
    }
}
