package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackUpdateHideFile {
    private static CallbackUpdateHideFile callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private CallbackUpdateHideFile() {
    }

    public static CallbackUpdateHideFile getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new CallbackUpdateHideFile();
        }
        return callbackupdate;
    }

    public void setStateListen(OncustomStateListen oncustomStateListen) {
        this.stateListen = oncustomStateListen;
    }

    public void change() {
        notidatachange();
    }

    protected void notidatachange() {
        this.stateListen.statechange();
    }
}
