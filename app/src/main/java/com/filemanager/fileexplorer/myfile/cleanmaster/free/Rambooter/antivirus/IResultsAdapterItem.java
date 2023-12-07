package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;


public interface IResultsAdapterItem {


    public enum ResultsAdapterItemType {
        Header,
        AppMenace,
        SystemMenace
    }

    ResultsAdapterItemType getType();
}
