package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import at.grabner.circleprogress.CircleProgressView;

import com.bumptech.glide.Glide;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.MonitorShieldService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;


public class MainFragment extends Fragment {
    TextView _bottomMenacesCounterText;
    TextView _bottomScannedAppsText;
    RelativeLayout _buttonContainer;
    CircleProgressView _circleProgressBar;
    LinearLayout _deviceRiskPanel;
    TextView _lastScanText;
    RelativeLayout _noMenacesInformationContainer;
    RelativeLayout _progressContainer;
    ImageView _progressPanelIconImageView;
    TextView _progressPanelTextView;
    ImageView _riskIcon;
    LinearLayout _scanningProgressPanel;
    RelativeLayout _superContainer;
    TextView _topMenacesCounterText;
    ImageView img_back;
    ImageView img_gif;
    final String _logTag = getClass().getSimpleName();
    final Random _random = new Random();
    Button _runAntivirusNow = null;
    Button _resolvePersistProblems = null;
    private boolean firstScan = false;
    final int kProgressBarRefressTime = 50;
    final int kEnterAppTime = 50;
    final int kScanningAppTime = 100;
    final int kIconChangeToGoodOrBadTime = 100;
    ScanningFileSystemAsyncTask _currentScanTask = null;

    AntivirusActivity getMainActivity() {
        return (AntivirusActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.main_fragment, viewGroup, false);
        _setupFragment(inflate);
        return inflate;
    }

    protected void _setupFragment(View view) {
        this._progressPanelIconImageView = (ImageView) view.findViewById(R.id.animationProgressPanelIconImageView);
        this._progressPanelTextView = (TextView) view.findViewById(R.id.animationProgressPanelTextView);
        this._circleProgressBar = (CircleProgressView) view.findViewById(R.id.circleView);
        this.img_gif = (ImageView) view.findViewById(R.id.img_gif);
        this.img_back = (ImageView) view.findViewById(R.id.img_back);
        Glide.with(this).load(Integer.valueOf((int) R.raw.animaiton_antivirus)).into(this.img_gif);
        this._bottomMenacesCounterText = (TextView) view.findViewById(R.id.bottomFoundMenacesCount);
        this._bottomScannedAppsText = (TextView) view.findViewById(R.id.bottomScannedApp);
        this._buttonContainer = (RelativeLayout) view.findViewById(R.id.buttonLayout);
        this._progressContainer = (RelativeLayout) view.findViewById(R.id.animationProgressPanel);
        this._superContainer = (RelativeLayout) view.findViewById(R.id.superContainer);
        this._noMenacesInformationContainer = (RelativeLayout) view.findViewById(R.id.noMenacesFoundPanel);
        this._riskIcon = (ImageView) view.findViewById(R.id.iconRisk);
        this._deviceRiskPanel = (LinearLayout) view.findViewById(R.id.deviceRiskPanel);
        this._scanningProgressPanel = (LinearLayout) view.findViewById(R.id.scanningProgressPanel);
        Button button = (Button) view.findViewById(R.id.button_resolve_problems);
        this._resolvePersistProblems = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                MainFragment.this._doActionResolveProblemsButton();
            }
        });
        final DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                MainFragment.this.getMainActivity().finish();
            }
        };
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(MainFragment.this.getContext(), deleteCallback);
                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                customDeleteDialog.show();
                customDeleteDialog.set_title_exit();
            }
        });
        this._topMenacesCounterText = (TextView) view.findViewById(R.id.topMenacesCounter);
        Button button2 = (Button) view.findViewById(R.id.runAntivirusNow);
        this._runAntivirusNow = button2;
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                MainFragment.this._scanFileSystem();
            }
        });
        this._lastScanText = (TextView) view.findViewById(R.id.lastScanText);
        DateTime lastScanDate = getMainActivity().getAppData().getLastScanDate();
        if (!lastScanDate.equals(AppData.kNullDate)) {
            this._lastScanText.setText(StaticTools.fillParams(getString(R.string.last_scanned), "#", DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").print(lastScanDate)));
        } else {
            this._lastScanText.setText(StaticTools.fillParams(getString(R.string.last_scanned), "#", getString(R.string.never)));
        }
        setUIRiskState();
        _resetFormLayout();
    }

    private void _resetFormLayout() {
        this._progressContainer.setVisibility(View.INVISIBLE);
        this._buttonContainer.setVisibility(View.VISIBLE);
        this._buttonContainer.setTranslationX(0.0f);
        this._runAntivirusNow.setEnabled(true);
    }

    protected void _startRealScan() {
        getMainActivity().startMonitorScan(new MonitorShieldService.IClientInterface() {
            @Override
            public void onMonitorFoundMenace(IProblem iProblem) {
            }

            @Override
            public void onScanResult(List<PackageInfo> list, Set<IProblem> set) {
                AppData appData = MainFragment.this.getMainActivity().getAppData();
                appData.setFirstScanDone(true);
                appData.serialize(MainFragment.this.getMainActivity());
                MainFragment.this._startScanningAnimation(list, set);
            }
        });
    }


    public void _scanFileSystem() {
        this._scanningProgressPanel.setAlpha(0.0f);
        this._scanningProgressPanel.setVisibility(View.VISIBLE);
        this._circleProgressBar.setValue(0.0f);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this._scanningProgressPanel, "alpha", 0.0f, 1.0f);
        ofFloat.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                MainFragment.this._startRealScan();
            }
        });
        ofFloat.setDuration(500L);
        ofFloat.start();
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this._deviceRiskPanel, "alpha", 1.0f, 0.0f);
        ofFloat2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationStart(Animator animator) {
                MainFragment.this._deviceRiskPanel.setVisibility(View.INVISIBLE);
            }
        });
        ofFloat2.setDuration(500L);
        ofFloat2.start();
    }


    public void _topProgressBarGoesToScanningState(int i) {
        this._deviceRiskPanel.setAlpha(0.0f);
        this._deviceRiskPanel.setVisibility(View.VISIBLE);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this._deviceRiskPanel, "alpha", 0.0f, 1.0f);
        long j = i;
        ofFloat.setDuration(j);
        ofFloat.start();
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this._scanningProgressPanel, "alpha", 1.0f, 0.0f);
        ofFloat2.setDuration(j);
        ofFloat2.start();
    }


    public void _startScanningAnimation(final List<PackageInfo> list, final Collection<? extends IProblem> collection) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this._buttonContainer, "translationX", 0.0f, ((-this._superContainer.getWidth()) / 2.0f) - this._buttonContainer.getWidth());
        ofFloat.setDuration(100L);
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d("AAAA", "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                MainFragment.this._configureScanningUI();
                ArrayList arrayList = new ArrayList();
                ProblemsDataSetTools.getAppProblems(collection, arrayList);
                MainFragment.this._currentScanTask = new ScanningFileSystemAsyncTask(MainFragment.this.getMainActivity(), list, arrayList);
                if (MainFragment.this._currentScanTask._isPaused) {
                    MainFragment.this._currentScanTask._isPaused = false;
                    MainFragment.this._currentScanTask.cancel(true);
                    MainFragment.this._currentScanTask = new ScanningFileSystemAsyncTask(MainFragment.this.getMainActivity(), list, arrayList);
                }
                MainFragment.this._currentScanTask.setAsyncTaskCallback(new IOnActionFinished() {
                    @Override
                    public void onFinished() {
                        MainFragment.this._currentScanTask = null;
                        AppData appData = MainFragment.this.getMainActivity().getAppData();
                        appData.setLastScanDate(new DateTime());
                        appData.serialize(MainFragment.this.getContext());
                        MainFragment.this._doAfterScanWork(collection);
                    }
                });
                MainFragment.this._currentScanTask.execute(new Void[0]);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Log.d("AAAA", "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                MainFragment.this._currentScanTask.execute(new Void[0]);
            }
        });
        ofFloat.start();
    }

    void _doAfterScanWork(Collection<? extends IProblem> collection) {
        this._currentScanTask = null;
        if (getMainActivity().getMenacesCacheSet().getItemCount() > 0) {
            showResultFragment(new ArrayList(collection));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainFragment.this._configureNonScanningUI();
                }
            }, 400L);
            return;
        }
        _playNoMenacesAnimationFound();
    }

    void _doActionResolveProblemsButton() {
        Set<IProblem> problemsFromMenaceSet = getMainActivity().getProblemsFromMenaceSet();
        if (problemsFromMenaceSet == null || problemsFromMenaceSet.size() == 0) {
            return;
        }
        showResultFragment(new ArrayList(problemsFromMenaceSet));
    }

    void _playNoMenacesAnimationFound() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this._progressContainer, "rotationY", 0.0f, 90.0f);
        ofFloat.addListener(new AnonymousClass10());
        ofFloat.setDuration(100L);
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.start();
    }


    public class AnonymousClass10 extends AnimatorListenerAdapter {
        AnonymousClass10() {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            MainFragment.this._noMenacesInformationContainer.setVisibility(View.VISIBLE);
            MainFragment.this._progressContainer.setVisibility(View.INVISIBLE);
            MainFragment.this._progressContainer.setRotationY(0.0f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(MainFragment.this._noMenacesInformationContainer, "rotationY", -90.0f, 0.0f);
            ofFloat.addListener(new AnonymousClass1());
            ofFloat.setDuration(100L);
            ofFloat.setInterpolator(new LinearInterpolator());
            ofFloat.start();
        }


        class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                new Handler().postDelayed(new RunnableC00081(), 2000L);
            }


            class RunnableC00081 implements Runnable {
                RunnableC00081() {
                }

                @Override
                public void run() {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(MainFragment.this._noMenacesInformationContainer, "rotationY", 0.0f, 90.0f);
                    ofFloat.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            MainFragment.this._noMenacesInformationContainer.setRotationY(0.0f);
                            MainFragment.this._noMenacesInformationContainer.setVisibility(View.INVISIBLE);
                            MainFragment.this._buttonContainer.setVisibility(View.VISIBLE);
                            MainFragment.this._buttonContainer.setTranslationX(0.0f);
                            MainFragment.this.setUIRiskState();
                            MainFragment.this._topProgressBarGoesToScanningState(200);
                            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(MainFragment.this._buttonContainer, "rotationY", -90.0f, 0.0f);
                            ofFloat2.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animator2) {
                                    MainFragment.this.getMainActivity().getMenu().setGroupVisible(0, true);
                                    MainFragment.this._runAntivirusNow.setEnabled(true);
                                }
                            });
                            ofFloat2.setDuration(100L);
                            ofFloat2.start();
                        }
                    });
                    ofFloat.setDuration(100L);
                    ofFloat.setInterpolator(new LinearInterpolator());
                    ofFloat.start();
                }
            }
        }
    }

    void showResultFragment(List<IProblem> list) {
        ((ResultsFragment) getMainActivity().slideInFragment("ResultFragmentTag")).setData(getMainActivity(), list);
    }

    @Override
    public void onPause() {
        super.onPause();
        ScanningFileSystemAsyncTask scanningFileSystemAsyncTask = this._currentScanTask;
        if (scanningFileSystemAsyncTask != null) {
            scanningFileSystemAsyncTask.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getMainActivity().getAppData().getFirstScanDone()) {
            getMainActivity().updateMenacesAndWhiteUserList();
        }
        ScanningFileSystemAsyncTask scanningFileSystemAsyncTask = this._currentScanTask;
        if (scanningFileSystemAsyncTask != null) {
            scanningFileSystemAsyncTask.resume();
        } else {
            setUIRiskState();
        }
    }

    void setUIRiskState() {
        boolean firstScanDone = getMainActivity().getAppData().getFirstScanDone();
        Set<IProblem> problemsFromMenaceSet = getMainActivity().getProblemsFromMenaceSet();
        boolean _isDangerousAppInSet = _isDangerousAppInSet(problemsFromMenaceSet);
        if (!problemsFromMenaceSet.isEmpty() && problemsFromMenaceSet != null) {
            if (_isDangerousAppInSet) {
                activateHighRiskState(problemsFromMenaceSet.size());
            } else {
                activateMediumRiskState(problemsFromMenaceSet.size());
            }
        } else if (!firstScanDone) {
            this._riskIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shield_medium_risk_icon));
            this._topMenacesCounterText.setText(R.string.run_first_analysis);
            this._deviceRiskPanel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.MediumRiskColor));
            this._resolvePersistProblems.setVisibility(View.GONE);
        } else {
            activateProtectedState();
        }
    }

    private boolean _isDangerousAppInSet(Set<IProblem> set) {
        for (IProblem iProblem : set) {
            if (iProblem.isDangerous()) {
                return true;
            }
        }
        return false;
    }

    void activateProtectedState() {
        _configureNonScanningUI();
        this._riskIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shield_protected_icon));
        this._deviceRiskPanel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ProtectedRiskColor));
        this._topMenacesCounterText.setText("R.string.are_protected");
        this._resolvePersistProblems.setVisibility(View.GONE);
    }

    void activateMediumRiskState(int i) {
        _configureNonScanningUI();
        this._riskIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shield_medium_risk_icon));
        this._deviceRiskPanel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.MediumRiskColor));
        _updateFoundThreatsText(this._topMenacesCounterText, i);
        this._resolvePersistProblems.setVisibility(View.VISIBLE);
        StaticTools.setViewBackgroundDrawable(this._resolvePersistProblems, ContextCompat.getDrawable(getContext(), R.drawable.killbtn_hv));
    }

    void activateHighRiskState(int i) {
        _configureNonScanningUI();
        this._riskIcon.setImageResource(R.drawable.shield_protected_icon);
        this._deviceRiskPanel.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.MediumRiskColor));
        _updateFoundThreatsText(this._topMenacesCounterText, i);
        this._resolvePersistProblems.setVisibility(View.VISIBLE);
        StaticTools.setViewBackgroundDrawable(this._resolvePersistProblems, ContextCompat.getDrawable(getContext(), R.drawable.killbtn_hv));
    }

    void _updateFoundThreatsText(TextView textView, int i) {
        textView.setText(StaticTools.fillParams(getString(R.string.menaces_unresolved), "#", Integer.toString(i)));
    }

    void _configureScanningUI() {
        Menu menu = getMainActivity().getMenu();
        if (menu != null) {
            menu.setGroupVisible(0, false);
        }
        this._scanningProgressPanel.setAlpha(1.0f);
        this._progressContainer.setVisibility(View.VISIBLE);
        this._progressContainer.setTranslationX(0.0f);
        this._buttonContainer.setVerticalGravity(4);
    }

    void _configureNonScanningUI() {
        if (getMainActivity() != null && getMainActivity().getMenu() != null) {
            getMainActivity().getMenu().setGroupVisible(0, true);
        }
        this._progressContainer.setVisibility(View.INVISIBLE);
        this._progressContainer.setTranslationX(0.0f);
        this._buttonContainer.setVerticalGravity(0);
        this._buttonContainer.setTranslationX(0.0f);
    }
}
