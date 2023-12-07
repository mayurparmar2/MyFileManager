package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IResultsAdapterItem;


class ResultsAdapterHeaderItem implements IResultsAdapterItem {
    String _description;

    public ResultsAdapterHeaderItem(String str) {
        this._description = null;
        this._description = str;
    }

    public String getDescription() {
        return this._description;
    }

    @Override
    public IResultsAdapterItem.ResultsAdapterItemType getType() {
        return IResultsAdapterItem.ResultsAdapterItemType.Header;
    }
}
