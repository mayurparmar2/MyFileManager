package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class Callbackupdate {
    private static Callbackupdate callbackupdate;
    private OncustomStateListen stateListen;
    private Updatecount updatecount;


    public interface OncustomStateListen {
        void statechange();
    }


    public interface Updatecount {
        void updatecount();
    }

    private Callbackupdate() {
    }

    public static Callbackupdate getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new Callbackupdate();
        }
        return callbackupdate;
    }

    public void setStateListen(OncustomStateListen oncustomStateListen) {
        this.stateListen = oncustomStateListen;
    }

    public void setstateListenuodatecout(Updatecount updatecount) {
        this.updatecount = updatecount;
    }

    public void change() {
        if (this.stateListen != null) {
            notidatachange();
        } else if (this.updatecount != null) {
            notidatachangeupdatecount();
        }
    }

    protected void notidatachange() {
        this.stateListen.statechange();
    }

    protected void notidatachangeupdatecount() {
        this.updatecount.updatecount();
    }
}
