package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;


public class MenacesCacheSet extends JSONDataSet<IProblem> {
    public MenacesCacheSet(Context context) {
        super(context, "menacescache.json", new ProblemFactory());
    }
}
