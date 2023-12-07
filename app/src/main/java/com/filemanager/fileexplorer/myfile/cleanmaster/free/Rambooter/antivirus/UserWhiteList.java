package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;


public class UserWhiteList extends JSONDataSet<IProblem> {
    @Override
    public boolean addItem(Object iProblem) {
        return false;
    }

    public UserWhiteList(Context context) {
        super(context, "userwhitelist.json", new ProblemFactory());
    }


    public boolean checkIfSystemPackageInList(Class<?> cls) {
        for (IProblem iProblem : getSet()) {
            if (iProblem.getType() == IProblem.ProblemType.SystemProblem && ((SystemProblem) iProblem).getClass() == cls) {
                return true;
            }
        }
        return false;
    }
}
