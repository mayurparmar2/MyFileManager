package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class IgnoredListFragment extends Fragment {
    private TextView _ignoredCounter;
    private ListView _listView;
    IgnoredAdapter _ignoredAdapter = null;
    UserWhiteList _userWhiteList = null;

    AntivirusActivity getMainActivity() {
        return (AntivirusActivity) getActivity();
    }

    public void setData(Context context, UserWhiteList userWhiteList) {
        this._userWhiteList = userWhiteList;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.ignored_list_fragment, viewGroup, false);
        this._listView = (ListView) inflate.findViewById(R.id.ignoredList);
        this._ignoredAdapter = new IgnoredAdapter(getMainActivity(), new ArrayList(this._userWhiteList.getSet()));
        this._ignoredCounter = (TextView) inflate.findViewById(R.id.ignoredCounterText);
        this._ignoredAdapter.setOnAdapterItemRemovedListener(new IOnAdapterItemRemoved<IProblem>() {
            @Override
            public void onItemRemoved(IProblem iProblem) {
                MenacesCacheSet menacesCacheSet = IgnoredListFragment.this.getMainActivity().getMenacesCacheSet();
                IgnoredListFragment.this._userWhiteList.removeItem(iProblem);
                IgnoredListFragment.this._userWhiteList.writeToJSON();
                menacesCacheSet.addItem((MenacesCacheSet) iProblem);
                menacesCacheSet.writeToJSON();
                IgnoredListFragment ignoredListFragment = IgnoredListFragment.this;
                ignoredListFragment._updateFoundThreatsText(ignoredListFragment._ignoredCounter, IgnoredListFragment.this._userWhiteList.getItemCount());
                if (IgnoredListFragment.this._ignoredAdapter.getCount() <= 0) {
                    IgnoredListFragment.this.getMainActivity().goBack();
                }
            }
        });
        this._listView.setAdapter((ListAdapter) this._ignoredAdapter);
        _updateFoundThreatsText(this._ignoredCounter, this._userWhiteList.getItemCount());
        return inflate;
    }

    void _updateFoundThreatsText(TextView textView, int i) {
        textView.setText(StaticTools.fillParams(getString(R.string.ignored_counter), "#", Integer.toString(i)));
    }

    public static List<IProblem> getExistingProblems(Context context, Collection<IProblem> collection) {
        ArrayList arrayList = new ArrayList();
        for (IProblem iProblem : collection) {
            if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                AppProblem appProblem = (AppProblem) iProblem;
                if (StaticTools.isPackageInstalled(context, appProblem.getPackageName())) {
                    arrayList.add(appProblem);
                }
            } else if (iProblem.problemExists(context)) {
                arrayList.add(iProblem);
            }
        }
        return arrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().updateMenacesAndWhiteUserList();
        this._ignoredAdapter.refresh(new ArrayList<>(this._userWhiteList.getSet()));
        _updateFoundThreatsText(this._ignoredCounter, this._userWhiteList.getItemCount());
        ProblemsDataSetTools.printProblems(this._userWhiteList);
        if (this._userWhiteList.getItemCount() <= 0) {
            getMainActivity().goBack();
        }
    }
}
