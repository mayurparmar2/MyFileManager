package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import at.grabner.circleprogress.CircleProgressView;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class ScanningFileSystemAsyncTask extends AsyncTask<Void, ScanningFileSystemAsyncTask.DataToPublish, Void> {
    Context _activity;
    IOnActionFinished _asyncTaskCallBack;
    TextView _bottomMenacesCounterText;
    TextView _bottomScannedAppsText;
    CircleProgressView _circleProgressBar;
    ArrayList<Float> _foundProblemsTimeLine;
    List<PackageInfo> _packagesToScan;
    TextView _problemsTextView;
    ImageView _progressPanelIconImageView;
    TextView _progressPanelTextView;
    TextView textView;
    boolean _isPaused = false;
    final String _logTag = "ScanningFileSystemAsyncTask";
    boolean running = true;
    int numFiles = 0;
    Random _random = new Random();
    DataToPublish dtp = new DataToPublish();
    Collection<AppProblem> _menaces = new ArrayList();

    @Override
    protected void onPreExecute() {
    }

    public void setAsyncTaskCallback(IOnActionFinished iOnActionFinished) {
        this._asyncTaskCallBack = iOnActionFinished;
    }

    public void pause() {
        this._isPaused = true;
    }

    public void resume() {
        this._isPaused = false;
    }


    public class DataToPublish {
        public String appName;
        public int foundMenaces;
        public Drawable icon;
        public int scannedFiles;
        public int totalFiles;

        DataToPublish() {
        }
    }

    public ScanningFileSystemAsyncTask(AntivirusActivity antivirusActivity, List<PackageInfo> list, Collection<IProblem> collection) {
        this._activity = antivirusActivity;
        this._packagesToScan = list;
        for (IProblem iProblem : collection) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                this._menaces.add((AppProblem) iProblem);
            }
        }
        this.textView = (TextView) antivirusActivity.findViewById(R.id.txt_p);
        this._progressPanelIconImageView = (ImageView) antivirusActivity.findViewById(R.id.animationProgressPanelIconImageView);
        this._progressPanelTextView = (TextView) antivirusActivity.findViewById(R.id.animationProgressPanelTextView);
        this._circleProgressBar = (CircleProgressView) antivirusActivity.findViewById(R.id.circleView);
        TextView textView = (TextView) antivirusActivity.findViewById(R.id.bottomFoundMenacesCount);
        this._bottomMenacesCounterText = textView;
        textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this._bottomScannedAppsText = (TextView) antivirusActivity.findViewById(R.id.bottomScannedApp);
        this.textView.setText("0%");
        this._circleProgressBar.setMaxValue(this._packagesToScan.size());
    }

    AntivirusActivity getMainActivity() {
        return (AntivirusActivity) this._activity;
    }

    @Override
    protected void onCancelled() {
        this.running = false;
    }


    @Override
    public Void doInBackground(Void... voidArr) {
        int i = 0;
        while (this.running && i < this._packagesToScan.size()) {
            try {
                Thread.sleep(100L);
                Log.d("!!!!", "doInBackground: ");
                if (!this._isPaused) {
                    Log.d("!!!!", "doInBackground:00000000000000000000000 ");
                    this.dtp.scannedFiles = i;
                    this.dtp.appName = this._packagesToScan.get(i).packageName;
                    DataToPublish dataToPublish = this.dtp;
                    dataToPublish.icon = StaticTools.getIconFromPackage(dataToPublish.appName, this._activity);
                    this.dtp.totalFiles = this._packagesToScan.size();
                    if (isPackageInMenacesSet(this.dtp.appName)) {
                        this.dtp.foundMenaces++;
                    }
                    publishProgress(this.dtp);
                    i++;
                }
            } catch (InterruptedException unused) {
                Log.w("APP", "Scanning task was interrupted");
                return null;
            }
        }
        return null;
    }

    boolean isPackageInMenacesSet(String str) {
        for (AppProblem appProblem : this._menaces) {
            if (appProblem.getPackageName().equals(str)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onProgressUpdate(DataToPublish... dataToPublishArr) {
        DataToPublish dataToPublish = dataToPublishArr[0];
        this._progressPanelIconImageView.setImageDrawable(dataToPublish.icon);
        this._progressPanelTextView.setText(dataToPublish.appName);
        this._circleProgressBar.setValue(dataToPublish.scannedFiles);
        TextView textView = this.textView;
        textView.setText(((int) (((dataToPublish.scannedFiles / this._packagesToScan.size()) * 100.0f) + 0.0f)) + " %");
        TextView textView2 = this._bottomMenacesCounterText;
        textView2.setText("" + dataToPublish.foundMenaces);
        TextView textView3 = this._bottomScannedAppsText;
        textView3.setText("" + dataToPublish.scannedFiles);
    }


    @Override
    public void onPostExecute(Void r2) {
        CircleProgressView circleProgressView = this._circleProgressBar;
        circleProgressView.setValue(circleProgressView.getMaxValue());
        IOnActionFinished iOnActionFinished = this._asyncTaskCallBack;
        if (iOnActionFinished != null) {
            iOnActionFinished.onFinished();
        }
    }
}
