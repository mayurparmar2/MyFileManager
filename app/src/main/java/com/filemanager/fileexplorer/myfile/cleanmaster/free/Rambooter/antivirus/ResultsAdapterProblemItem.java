package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IResultsAdapterItem;


class ResultsAdapterProblemItem implements IResultsAdapterItem {
    IProblem _problem;

    public ResultsAdapterProblemItem(IProblem iProblem) {
        this._problem = null;
        this._problem = iProblem;
    }

    public IProblem getProblem() {
        return this._problem;
    }

    public AppProblem getAppProblem() throws ClassCastException {
        if (AppProblem.class.isAssignableFrom(this._problem.getClass())) {
            return (AppProblem) this._problem;
        }
        throw new ClassCastException();
    }

    public SystemProblem getSystemProblem() throws ClassCastException {
        if (SystemProblem.class.isAssignableFrom(this._problem.getClass())) {
            return (SystemProblem) this._problem;
        }
        throw new ClassCastException();
    }

    @Override
    public IResultsAdapterItem.ResultsAdapterItemType getType() {
        return this._problem.getType() == IProblem.ProblemType.AppProblem ? IResultsAdapterItem.ResultsAdapterItemType.AppMenace : IResultsAdapterItem.ResultsAdapterItemType.SystemMenace;
    }
}
