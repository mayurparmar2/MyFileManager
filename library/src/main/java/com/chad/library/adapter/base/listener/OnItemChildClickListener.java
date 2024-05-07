package com.chad.library.adapter.base.listener;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;


public interface OnItemChildClickListener {
    
    void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position);
}
