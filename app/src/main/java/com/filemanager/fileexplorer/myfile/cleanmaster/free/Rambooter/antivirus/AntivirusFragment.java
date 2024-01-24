package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.MonitorShieldService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AntivirusFragment extends Fragment implements MonitorShieldService.IClientInterface {
    public static final String kIgnoredFragmentTag = "IgnoredFragmentTag";
    public static final String kInfoFragmnetTag = "InfoFragmentTag";
    public static final String kMainFragmentTag = "MainFragmentTag";
    public static final String kResultFragmentTag = "ResultFragmentTag";
    Menu _menu = null;
    String _logTag = "AntivirusActivity";
    MonitorShieldService _serviceInstance = null;
    boolean _bound = false;
    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AntivirusFragment.this._bound = true;
            AntivirusFragment.this._serviceInstance = ((MonitorShieldService.MonitorShieldLocalBinder) iBinder).getServiceInstance();
            if (AntivirusFragment.this._serviceInstance != null) {
                new Exception("Service instance is null. At it can't be!!!!");
            }
            AntivirusFragment.this._serviceInstance.registerClient(AntivirusFragment.this);
            if (AntivirusFragment.this.getActivity().getSupportFragmentManager().getBackStackEntryCount() <= 0) {
                AntivirusFragment.this.slideInFragment("MainFragmentTag");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AntivirusFragment.this._serviceInstance = null;
        }
    };
    MonitorShieldService.IClientInterface _appMonitorServiceListener = null;

    public Menu getMenu() {
        return this._menu;
    }

    public MainFragment getMainFragment() {
        MainFragment mainFragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainFragmentTag");
        return mainFragment == null ? new MainFragment() : mainFragment;
    }

    public ResultsFragment getResultFragment() {
        ResultsFragment resultsFragment = (ResultsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("ResultFragmentTag");
        return resultsFragment == null ? new ResultsFragment() : resultsFragment;
    }

    public InfoAppFragment getInfoFragment() {
        InfoAppFragment infoAppFragment = (InfoAppFragment) getActivity().getSupportFragmentManager().findFragmentByTag("InfoFragmentTag");
        return infoAppFragment == null ? new InfoAppFragment() : infoAppFragment;
    }

    public IgnoredListFragment getIgnoredFragment() {
        IgnoredListFragment ignoredListFragment = (IgnoredListFragment) getActivity().getSupportFragmentManager().findFragmentByTag("IgnoredFragmentTag");
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
        ProblemsDataSetTools.removeNotExistingProblems(getActivity(), getUserWhiteList());
        ProblemsDataSetTools.removeNotExistingProblems(getActivity(), getMenacesCacheSet());
        Scanner.scanSystemProblems(getActivity(), getUserWhiteList(), getMenacesCacheSet().getSet());
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
        return AppData.getInstance(getActivity());
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.anti_activity_main, viewGroup, false);
        makeActionOverflowMenuShown();
        return inflate;
    }

    void showIgnoredFragment(UserWhiteList userWhiteList) {
        if (IgnoredListFragment.getExistingProblems(getActivity(), new ArrayList(userWhiteList.getSet())).size() > 0) {
            IgnoredListFragment ignoredListFragment = (IgnoredListFragment) slideInFragment("IgnoredFragmentTag");
            if (ignoredListFragment != null) {
                ignoredListFragment.setData(getActivity(), userWhiteList);
                return;
            }
            return;
        }
        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.warning)).setMessage(getString(R.string.igonred_message_dialog)).setPositiveButton(getString(R.string.accept_eula), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    void _handleBackButton() {
        goBack();
    }

    public void showNoInetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(R.string.information);
        builder.setMessage(R.string.no_inet_no_app);
        builder.setCancelable(false);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton(R.string.quit_app, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AntivirusFragment.this.getActivity().finish();
            }
        });
        builder.create().show();
    }

    protected void _showTimedDialog(AlertDialog alertDialog, final boolean z, boolean z2, boolean z3) {
        alertDialog.show();
        Handler handler = new Handler();
        final Button button = alertDialog.getButton(-1);
        if (z2) {
            button.setEnabled(false);
        }
        final Button button2 = alertDialog.getButton(-2);
        if (z && z3) {
            button2.setEnabled(false);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Button button3 = button;
                if (button3 != null) {
                    button3.setEnabled(true);
                }
                if (z) {
                    button2.setEnabled(true);
                }
            }
        }, 20000L);
    }

    void _showVoteUs() {
        final AppData appData = AppData.getInstance(getActivity());
        if (appData.getVoted()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.vote));
        builder.setMessage(getResources().getString(R.string.gplay_vote)).setCancelable(false).setPositiveButton(getResources().getString(17039379), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String packageName = StaticTools.getPackageName(AntivirusFragment.this.getActivity());
                StaticTools.openMarketURL(AntivirusFragment.this.getActivity(), "market://details?id=" + packageName, "http://play.google.com/store/apps/details?id=" + packageName);
                appData.setVoted(true);
                appData.serialize(AntivirusFragment.this.getActivity());
            }
        }).setNegativeButton(17039369, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    public Fragment slideInFragment(String str) {
        Fragment infoFragment;
        Fragment findFragmentByTag = getActivity().getSupportFragmentManager().findFragmentByTag(str);
        if (findFragmentByTag == null || !findFragmentByTag.isVisible()) {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            supportFragmentManager.popBackStack();
        } else {
            new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.warning)).setMessage(getString(R.string.dialog_message_exit)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AntivirusFragment.this.getActivity().finish();
                }
            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!StaticTools.isServiceRunning(getActivity(), MonitorShieldService.class)) {
            Intent intent = new Intent(getActivity(), MonitorShieldService.class);
            getActivity().startService(intent);
            getActivity().bindService(intent, this._serviceConnection, 1);
            return;
        }
        getActivity().bindService(new Intent(getActivity(), MonitorShieldService.class), this._serviceConnection, 1);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!this._bound || this._serviceConnection == null) {
            return;
        }
        getActivity().unbindService(this._serviceConnection);
        this._bound = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(getActivity());
            @SuppressLint("SoonBlockedPrivateApi") Field declaredField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (declaredField != null) {
                declaredField.setAccessible(true);
                declaredField.setBoolean(viewConfiguration, false);
            }
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
        }
    }
}
