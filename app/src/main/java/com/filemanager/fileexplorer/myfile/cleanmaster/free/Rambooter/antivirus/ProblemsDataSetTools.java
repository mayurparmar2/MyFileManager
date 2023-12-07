package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProblemsDataSetTools {
    public static Set<IProblem> getAppProblemsSet(IDataSet<? extends IProblem> iDataSet) {
        HashSet hashSet = new HashSet();
        getAppProblems(iDataSet.getSet(), hashSet);
        return hashSet;
    }

    public static List<IProblem> getAppProblemsList(IDataSet<? extends IProblem> iDataSet) {
        ArrayList arrayList = new ArrayList();
        getAppProblems(iDataSet.getSet(), arrayList);
        return arrayList;
    }

    public static Set<IProblem> getSystemProblemsSet(IDataSet<? extends IProblem> iDataSet) {
        HashSet hashSet = new HashSet();
        getSystemProblems(iDataSet.getSet(), hashSet);
        return hashSet;
    }

    public static List<IProblem> getSystemProblemsList(IDataSet<? extends IProblem> iDataSet) {
        ArrayList arrayList = new ArrayList();
        getSystemProblems(iDataSet.getSet(), arrayList);
        return arrayList;
    }

    public static Collection<? extends IProblem> getAppProblems(Collection<? extends IProblem> collection, Collection<IProblem> collection2) {
        for (IProblem iProblem : collection) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                collection2.add(iProblem);
            }
        }
        return collection2;
    }

    public static Collection<? extends IProblem> getSystemProblems(Collection<? extends IProblem> collection, Collection<IProblem> collection2) {
        for (IProblem iProblem : collection) {
            if (iProblem.getType() == IProblem.ProblemType.SystemProblem) {
                collection2.add(iProblem);
            }
        }
        return collection2;
    }

    public static Set<PackageData> getAppProblemsAsPackageDataList(IDataSet<? extends IProblem> iDataSet) {
        HashSet hashSet = new HashSet();
        for (IProblem iProblem : iDataSet.getSet()) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                hashSet.add((AppProblem) iProblem);
            }
        }
        return hashSet;
    }


    public static boolean checkIfPackageInCollection(String str, Collection<IProblem> collection) {
        for (IProblem iProblem : collection) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem && ((AppProblem) iProblem).getPackageName().equals(str)) {
                return true;
            }
        }
        return false;
    }


    public static boolean removeNotExistingProblems(Context context, IDataSet<IProblem> iDataSet) {
        ArrayList arrayList = new ArrayList();
        Set<IProblem> set = iDataSet.getSet();
        boolean z = false;
        for (IProblem iProblem : set) {
            if (!iProblem.problemExists(context)) {
                arrayList.add(iProblem);
                z = true;
            }
        }
        set.removeAll(arrayList);
        return z;
    }

    public static boolean removeAppProblemByPackage(IDataSet<IProblem> iDataSet, String str) {
        for (IProblem iProblem : iDataSet.getSet()) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem && str.equals(((AppProblem) iProblem).getPackageName())) {
                return iDataSet.removeItem(iProblem);
            }
        }
        return false;
    }

    public static void printProblems(IDataSet<IProblem> iDataSet) {
        for (IProblem iProblem : iDataSet.getSet()) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                AppProblem appProblem = (AppProblem) iProblem;
            } else {
                SystemProblem systemProblem = (SystemProblem) iProblem;
            }
        }
    }
}
