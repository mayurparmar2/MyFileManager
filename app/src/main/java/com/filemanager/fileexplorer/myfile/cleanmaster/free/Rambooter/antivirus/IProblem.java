package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;


public interface IProblem extends IJSONSerializer {


    public enum ProblemType {
        AppProblem,
        SystemProblem
    }

    ProblemType getType();

    boolean isDangerous();

    boolean problemExists(Context context);
}
