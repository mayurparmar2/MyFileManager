package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;


public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    ImageView imageView;
    ImageView img_check;
    TextView time;
    TextView title;


    public MyViewHolder(View view) {
        super(view);
        this.date = (TextView) view.findViewById(R.id.date);
        this.time = (TextView) view.findViewById(R.id.time);
        this.title = (TextView) view.findViewById(R.id.title);
        this.imageView = (ImageView) view.findViewById(R.id.icon_file);
        this.img_check = (ImageView) view.findViewById(R.id.img_check);
    }
}
