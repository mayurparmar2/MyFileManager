package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.MonitorShieldService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


public class AntivirusActivity extends AppCompatActivity implements MonitorShieldService.IClientInterface {
    public static final String kIgnoredFragmentTag = "IgnoredFragmentTag";
    public static final String kInfoFragmnetTag = "InfoFragmentTag";
    public static final String kMainFragmentTag = "MainFragmentTag";
    public static final String kResultFragmentTag = "ResultFragmentTag";
    MonitorShieldService.MonitorShieldLocalBinder binder;
    public DeleteCallback deleteCallback;
    Toolbar toolbar;
    Menu _menu = null;
    String _logTag = "AntivirusActivity";
    MonitorShieldService _serviceInstance = null;
    boolean _bound = false;
    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AntivirusActivity.this.binder = (MonitorShieldService.MonitorShieldLocalBinder) iBinder;
            AntivirusActivity antivirusActivity = AntivirusActivity.this;
            antivirusActivity._serviceInstance = antivirusActivity.binder.getServiceInstance();
            if (AntivirusActivity.this._serviceInstance != null) {
                new Exception("Service instance is null. At it can't be!!!!");
            }
            AntivirusActivity.this._serviceInstance.registerClient(AntivirusActivity.this);
            if (AntivirusActivity.this.getSupportFragmentManager().getBackStackEntryCount() <= 0) {
                AntivirusActivity.this.slideInFragment("MainFragmentTag");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AntivirusActivity.this._serviceInstance = null;
        }
    };
    MonitorShieldService.IClientInterface _appMonitorServiceListener = null;

    private void _requestNewInterstitial() {
    }

    public Menu getMenu() {
        return this._menu;
    }

    public MainFragment getMainFragment() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("MainFragmentTag");
        return mainFragment == null ? new MainFragment() : mainFragment;
    }

    public ResultsFragment getResultFragment() {
        ResultsFragment resultsFragment = (ResultsFragment) getSupportFragmentManager().findFragmentByTag("ResultFragmentTag");
        return resultsFragment == null ? new ResultsFragment() : resultsFragment;
    }

    public InfoAppFragment getInfoFragment() {
        InfoAppFragment infoAppFragment = (InfoAppFragment) getSupportFragmentManager().findFragmentByTag("InfoFragmentTag");
        return infoAppFragment == null ? new InfoAppFragment() : infoAppFragment;
    }

    public IgnoredListFragment getIgnoredFragment() {
        IgnoredListFragment ignoredListFragment = (IgnoredListFragment) getSupportFragmentManager().findFragmentByTag("IgnoredFragmentTag");
        return ignoredListFragment == null ? new IgnoredListFragment() : ignoredListFragment;
    }

    public UserWhiteList getUserWhiteList() {
        return this._serviceInstance.getUserWhiteList();
    }

    public Set<IProblem> getProblemsFromMenaceSet() {
        return this._serviceInstance.getMenacesCacheSet().getSet();
    }

    public MenacesCacheSet getMenacesCacheSet() {
        return this._serviceInstance.getMenacesCacheSet();
    }

    public void updateMenacesAndWhiteUserList() {
        ProblemsDataSetTools.removeNotExistingProblems(this, getUserWhiteList());
        ProblemsDataSetTools.removeNotExistingProblems(this, getMenacesCacheSet());
        Scanner.scanSystemProblems(this, getUserWhiteList(), getMenacesCacheSet().getSet());
    }

    public void setMonitorServiceListener(MonitorShieldService.IClientInterface iClientInterface) {
        this._appMonitorServiceListener = iClientInterface;
    }

    @Override
    public void onMonitorFoundMenace(IProblem iProblem) {
        MonitorShieldService.IClientInterface iClientInterface = this._appMonitorServiceListener;
        if (iClientInterface != null) {
            iClientInterface.onMonitorFoundMenace(iProblem);
        }
    }

    @Override
    public void onScanResult(List<PackageInfo> list, Set<IProblem> set) {
        MonitorShieldService.IClientInterface iClientInterface = this._appMonitorServiceListener;
        if (iClientInterface != null) {
            iClientInterface.onScanResult(list, set);
        }
    }

    public void startMonitorScan(MonitorShieldService.IClientInterface iClientInterface) {
        this._appMonitorServiceListener = iClientInterface;
        MonitorShieldService monitorShieldService = this._serviceInstance;
        if (monitorShieldService != null) {
            monitorShieldService.scanFileSystem();
        }
    }

    public AppData getAppData() {
        return AppData.getInstance(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        setContentView(R.layout.anti_activity_main);
        this.deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                if (AntivirusActivity.this._bound && AntivirusActivity.this._serviceConnection != null) {
                    AntivirusActivity antivirusActivity = AntivirusActivity.this;
                    antivirusActivity.unbindService(antivirusActivity._serviceConnection);
                    AntivirusActivity.this._bound = false;
                }
                AntivirusActivity.this.finish();
                Animatoo.animateSlideRight(AntivirusActivity.this);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        menuItem.getItemId();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        _handleBackButton();
    }

    void _handleBackButton() {
        goBack();
    }

    void _showVoteUs() {
        final AppData appData = AppData.getInstance(this);
        if (appData.getVoted()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.vote));
        builder.setMessage(getResources().getString(R.string.gplay_vote)).setCancelable(false).setPositiveButton(getResources().getString(17039379), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String packageName = StaticTools.getPackageName(AntivirusActivity.this);
                StaticTools.openMarketURL(AntivirusActivity.this, "market://details?id=" + packageName, "http://play.google.com/store/apps/details?id=" + packageName);
                appData.setVoted(true);
                appData.serialize(AntivirusActivity.this);
            }
        }).setNegativeButton(17039369, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    public Fragment slideInFragment(String str) {
        Fragment infoFragment;
        Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(str);
        if (findFragmentByTag == null || !findFragmentByTag.isVisible()) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1132305028:
                    if (str.equals("InfoFragmentTag")) {
                        c = 0;
                        break;
                    }
                    break;
                case -972729576:
                    if (str.equals("IgnoredFragmentTag")) {
                        c = 1;
                        break;
                    }
                    break;
                case 974282413:
                    if (str.equals("ResultFragmentTag")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1870938001:
                    if (str.equals("MainFragmentTag")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    infoFragment = getInfoFragment();
                    break;
                case 1:
                    infoFragment = getIgnoredFragment();
                    break;
                case 2:
                    infoFragment = getResultFragment();
                    break;
                case 3:
                    infoFragment = getMainFragment();
                    break;
                default:
                    infoFragment = null;
                    break;
            }
            beginTransaction.replace(R.id.container, infoFragment, str);
            beginTransaction.addToBackStack(null);
            beginTransaction.commitAllowingStateLoss();
            return infoFragment;
        }
        return null;
    }

    public void goBack() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            supportFragmentManager.popBackStack();
            return;
        }
        CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(this, this.deleteCallback);
        customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        customDeleteDialog.show();
        customDeleteDialog.set_title_exit();
        customDeleteDialog.set_titile_dialog_exit_antivirus();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!StaticTools.isServiceRunning(this, MonitorShieldService.class)) {
            Intent intent = new Intent(this, MonitorShieldService.class);
            startService(intent);
            this._bound = bindService(intent, this._serviceConnection, 1);
            return;
        }
        this._bound = bindService(new Intent(this, MonitorShieldService.class), this._serviceConnection, 1);
    }

    @Override
    public void onStop() {
        ServiceConnection serviceConnection;
        super.onStop();
        if (!this._bound || (serviceConnection = this._serviceConnection) == null) {
            return;
        }
        unbindService(serviceConnection);
        this._bound = false;
    }

    @Override
    public void onDestroy() {
        ServiceConnection serviceConnection;
        super.onDestroy();
        if (!this._bound || (serviceConnection = this._serviceConnection) == null) {
            return;
        }
        unbindService(serviceConnection);
        this._bound = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this._menu = menu;
        return true;
    }

    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(this);
            Field declaredField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (declaredField != null) {
                declaredField.setAccessible(true);
                declaredField.setBoolean(viewConfiguration, false);
            }
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
        }
    }
}
