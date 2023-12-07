package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;


public class InfoAppFragment extends Fragment {
    public static ListView _listview;
    IProblem _problem = null;
    boolean _uninstallingPackage = false;
    private LinearLayout _containerButtonsApp = null;
    private LinearLayout _containerButtonsSystem = null;
    private Button _uninstallButton = null;

    AntivirusActivity getMainActivity() {
        return (AntivirusActivity) getActivity();
    }

    public void setData(IProblem iProblem) {
        this._problem = iProblem;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.app_info_fragment, viewGroup, false);
        _setupFragment(inflate);
        return inflate;
    }

    protected void _setupFragment(View view) {
        this._containerButtonsApp = (LinearLayout) view.findViewById(R.id.buttonsAppContainer);
        this._containerButtonsSystem = (LinearLayout) view.findViewById(R.id.buttonsConfigContainer);
        TextView textView = (TextView) view.findViewById(R.id.titleApp);
        TextView textView2 = (TextView) view.findViewById(R.id.warningLevel);
        ImageView imageView = (ImageView) view.findViewById(R.id.iconGeneral);
        _listview = (ListView) view.findViewById(R.id.listView);
        if (this._problem.isDangerous()) {
            textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            textView2.setText(R.string.high_risk);
        } else {
            textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            textView2.setText(R.string.medium_risk);
        }
        if (this._problem.getType() == IProblem.ProblemType.AppProblem) {
            this._containerButtonsApp.setVisibility(View.VISIBLE);
            this._containerButtonsSystem.setVisibility(View.GONE);
            final AppProblem appProblem = (AppProblem) this._problem;
            Drawable iconFromPackage = StaticTools.getIconFromPackage(appProblem.getPackageName(), getContext());
            this._uninstallButton = (Button) view.findViewById(R.id.buttonUninstall);
            final Button button = (Button) view.findViewById(R.id.buttonTrust);
            this._uninstallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    InfoAppFragment.this._uninstallingPackage = true;
                    InfoAppFragment.this.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appProblem.getPackageName(), null)));
                    InfoAppFragment.this._uninstallButton.setEnabled(false);
                }
            });
            final DeleteCallback deleteCallback = new DeleteCallback() {
                @Override
                public void update() {
                    UserWhiteList userWhiteList = InfoAppFragment.this.getMainActivity().getUserWhiteList();
                    userWhiteList.addItem((IProblem) appProblem);
                    userWhiteList.writeToJSON();
                    MenacesCacheSet menacesCacheSet = InfoAppFragment.this.getMainActivity().getMenacesCacheSet();
                    menacesCacheSet.removeItem(appProblem);
                    menacesCacheSet.writeToJSON();
                    InfoAppFragment.this.getMainActivity().goBack();
                    button.setEnabled(true);
                }
            };
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    button.setEnabled(false);
                    CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(InfoAppFragment.this.requireContext(), deleteCallback);
                    customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    customDeleteDialog.show();
                    customDeleteDialog.set_title_trustapp();
                }
            });
            textView.setText(StaticTools.getAppNameFromPackage(getContext(), appProblem.getPackageName()));
            imageView.setImageDrawable(iconFromPackage);
        } else {
            this._containerButtonsApp.setVisibility(View.GONE);
            this._containerButtonsSystem.setVisibility(View.VISIBLE);
            final SystemProblem systemProblem = (SystemProblem) this._problem;
            imageView.setImageDrawable(systemProblem.getIcon(getContext()));
            textView.setText(systemProblem.getTitle(getContext()));
            ((Button) view.findViewById(R.id.buuttonOpenSettings)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    systemProblem.doAction(InfoAppFragment.this.getContext());
                }
            });
            ((Button) view.findViewById(R.id.buttonIgnoreConfig)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    UserWhiteList userWhiteList = InfoAppFragment.this.getMainActivity().getUserWhiteList();
                    userWhiteList.addItem(InfoAppFragment.this._problem);
                    userWhiteList.writeToJSON();
                    MenacesCacheSet menacesCacheSet = InfoAppFragment.this.getMainActivity().getMenacesCacheSet();
                    menacesCacheSet.removeItem( InfoAppFragment.this._problem);
                    menacesCacheSet.writeToJSON();
                    InfoAppFragment.this.getMainActivity().goBack();
                }
            });
        }
        _listview.setAdapter((ListAdapter) new WarningsAdapter(getMainActivity(), this._problem));
    }

    @Override
    public void onResume() {
        super.onResume();
        AntivirusActivity mainActivity = getMainActivity();
        if (this._uninstallingPackage) {
            IProblem iProblem = this._problem;
            if (iProblem != null) {
                AppProblem appProblem = (AppProblem) iProblem;
                if (!StaticTools.isPackageInstalled(getMainActivity(), appProblem.getPackageName())) {
                    MenacesCacheSet menacesCacheSet = mainActivity.getMenacesCacheSet();
                    menacesCacheSet.removeItem( appProblem);
                    menacesCacheSet.writeToJSON();
                }
            }
            this._uninstallingPackage = false;
            getMainActivity().goBack();
        } else if (this._problem.getType() == IProblem.ProblemType.AppProblem) {
            MenacesCacheSet menacesCacheSet2 = mainActivity.getMenacesCacheSet();
            AppProblem appProblem2 = (AppProblem) this._problem;
            if (!ProblemsDataSetTools.checkIfPackageInCollection(appProblem2.getPackageName(), menacesCacheSet2.getSet()) && !StaticTools.isPackageInstalled(mainActivity, appProblem2.getPackageName())) {
                MenacesCacheSet menacesCacheSet3 = mainActivity.getMenacesCacheSet();
                menacesCacheSet3.removeItem( appProblem2);
                menacesCacheSet3.writeToJSON();
                mainActivity.goBack();
            }
        } else if (this._problem.getType() == IProblem.ProblemType.SystemProblem) {
            mainActivity.getMenacesCacheSet();
            SystemProblem systemProblem = (SystemProblem) this._problem;
            if (!systemProblem.problemExists(getContext())) {
                MenacesCacheSet menacesCacheSet4 = mainActivity.getMenacesCacheSet();
                menacesCacheSet4.removeItem( systemProblem);
                menacesCacheSet4.writeToJSON();
                mainActivity.goBack();
            }
        }
        Button button = this._uninstallButton;
        if (button != null) {
            button.setEnabled(true);
        }
    }
}
