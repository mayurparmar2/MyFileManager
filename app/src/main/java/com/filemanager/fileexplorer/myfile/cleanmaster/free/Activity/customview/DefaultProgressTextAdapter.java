package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CircularProgressIndicator;


public final class DefaultProgressTextAdapter implements CircularProgressIndicator.ProgressTextAdapter {
    @Override
    public String formatText(double d) {
        return String.valueOf((int) d);
    }
}
