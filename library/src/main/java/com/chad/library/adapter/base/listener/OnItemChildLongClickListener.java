package com.chad.library.adapter.base.listener;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;


public interface OnItemChildLongClickListener {
    
    boolean onItemChildLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position);
}
