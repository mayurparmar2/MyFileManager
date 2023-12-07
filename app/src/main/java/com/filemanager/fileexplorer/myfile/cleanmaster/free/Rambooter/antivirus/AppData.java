package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;

import java.io.IOException;
import java.io.Serializable;

import org.joda.time.DateTime;


public class AppData implements Serializable {
    static transient AppData _instance = null;
    static final transient String filePath = "state.data";
    public static transient DateTime kNullDate = new DateTime(1973, 1, 1, 0, 0);
    private boolean _voted;
    private boolean _firstScanDone = false;
    private boolean _eulaAccepted = false;
    private DateTime _lastScanDate = new DateTime(1973, 1, 1, 0, 0);
    private DateTime _lastAdDate = new DateTime(1973, 1, 1, 0, 0);

    public boolean getVoted() {
        return this._voted;
    }

    public void setVoted(boolean z) {
        this._voted = z;
    }

    public boolean getFirstScanDone() {
        return this._firstScanDone;
    }

    public void setFirstScanDone(boolean z) {
        this._firstScanDone = z;
    }

    public boolean getEulaAccepted() {
        return this._eulaAccepted;
    }

    public void setEulaAccepted(boolean z) {
        this._eulaAccepted = z;
    }

    public DateTime getLastScanDate() {
        return this._lastScanDate;
    }

    public void setLastScanDate(DateTime dateTime) {
        this._lastScanDate = dateTime;
    }

    public DateTime getLastAdDate() {
        return this._lastAdDate;
    }

    public void setLastAdDate(DateTime dateTime) {
        this._lastAdDate = dateTime;
    }

    public static boolean isAppDataInited() {
        return _instance != null;
    }

    public static synchronized AppData getInstance(Context context) {
        synchronized (AppData.class) {
            AppData appData = _instance;
            if (appData != null) {
                return appData;
            }
            AppData appData2 = (AppData) StaticTools.deserializeFromDataFolder(context, filePath);
            _instance = appData2;
            if (appData2 == null) {
                _instance = new AppData();
            }
            return _instance;
        }
    }

    public synchronized void serialize(Context context) {
        try {
            StaticTools.serializeToDataFolder(context, this, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
