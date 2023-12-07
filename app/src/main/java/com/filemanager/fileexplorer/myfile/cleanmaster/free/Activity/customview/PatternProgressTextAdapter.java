package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CircularProgressIndicator;


public final class PatternProgressTextAdapter implements CircularProgressIndicator.ProgressTextAdapter {
    private String pattern;

    public PatternProgressTextAdapter(String str) {
        this.pattern = str;
    }

    @Override
    public String formatText(double d) {
        return String.format(this.pattern, Double.valueOf(d));
    }
}
