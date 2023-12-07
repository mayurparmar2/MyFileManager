package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;


public class DocumnetActivity extends AppCompatActivity {
    private CardView cardView_doc;
    private CardView cardView_ppt;
    private CardView cardView_txt;
    private CardView cardView_xlsx;
    private CardView cardViewpdf;
    private ImageView img_back;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_documnet);
        new Data_Manager(this);
        this.cardView_doc = (CardView) findViewById(R.id.card_doc);
        this.cardView_ppt = (CardView) findViewById(R.id.card_ppt);
        this.cardView_txt = (CardView) findViewById(R.id.card_txt);
        this.cardView_xlsx = (CardView) findViewById(R.id.card_xlsx);
        this.cardViewpdf = (CardView) findViewById(R.id.card_pdf);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.cardView_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                DocumnetActivity.this.m62x5adbd1b6(view);
            }
        });
        this.cardViewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumnetActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "pdf");
                DocumnetActivity.this.startActivity(intent);

                Animatoo.animateSwipeLeft(DocumnetActivity.this);
            }
        });
        this.cardView_xlsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumnetActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "xls");
                DocumnetActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(DocumnetActivity.this);
            }
        });
        this.cardView_ppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumnetActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "ppt");
                DocumnetActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(DocumnetActivity.this);
            }
        });
        this.cardView_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumnetActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "txt");
                DocumnetActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(DocumnetActivity.this);
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumnetActivity.this.finish();
                Animatoo.animateSwipeRight(DocumnetActivity.this);
            }
        });
    }


    public void m62x5adbd1b6(View view) {
        Intent intent = new Intent(this, ResulfActivity.class);
        intent.putExtra("nameitem", "doc");
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
    }
}
