package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class Callbackupdatealbum {
    private static Callbackupdatealbum callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private Callbackupdatealbum() {
    }

    public static Callbackupdatealbum getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new Callbackupdatealbum();
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
