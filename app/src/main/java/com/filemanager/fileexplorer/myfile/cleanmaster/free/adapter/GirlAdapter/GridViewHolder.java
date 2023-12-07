package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.GirlAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;


public class GridViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    ImageView img_check;
    TextView textView;

    public GridViewHolder(View view) {
        super(view);
        this.textView = (TextView) view.findViewById(R.id.text_grid);
        this.imageView = (ImageView) view.findViewById(R.id.icon_grid);
        this.img_check = (ImageView) view.findViewById(R.id.img_check);
    }
}
