package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.demo.example.R;

import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment {
    private ListView _listview;
    final String _logTag = getClass().getSimpleName();
    List<IProblem> _problems = null;
    ResultsAdapter _resultAdapter = null;
    TextView _threatsFoundSummary = null;

    AntivirusActivity getMainActivity() {
        return (AntivirusActivity) getActivity();
    }

    public void setData(AntivirusActivity antivirusActivity, List<IProblem> list) {
        this._problems = list;
        ResultsAdapter resultsAdapter = new ResultsAdapter(antivirusActivity, list);
        this._resultAdapter = resultsAdapter;
        resultsAdapter.setResultItemSelectedStateChangedListener(new IResultItemSelectedListener() {
            @Override
            public void onItemSelected(IProblem iProblem) {
                ResultsFragment.this.showInfoAppFragment(iProblem);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.results_fragment, viewGroup, false);
        this._threatsFoundSummary = (TextView) inflate.findViewById(R.id.counterApps);
        _setupFragment(inflate);
        return inflate;
    }

    protected void _setupFragment(View view) {
        ListView listView = (ListView) view.findViewById(R.id.list);
        this._listview = listView;
        listView.setAdapter((ListAdapter) this._resultAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MenacesCacheSet menacesCacheSet = getMainActivity().getMenacesCacheSet();
        getMainActivity().updateMenacesAndWhiteUserList();
        this._resultAdapter.refreshByProblems(new ArrayList(menacesCacheSet.getSet()));
        _updateFoundThreatsText(this._threatsFoundSummary, this._resultAdapter.getRealCount());
        if (menacesCacheSet.getItemCount() <= 0) {
            getMainActivity().goBack();
        }
    }

    void _updateFoundThreatsText(TextView textView, int i) {
        textView.setText(StaticTools.fillParams(getString(R.string.threats_found), "#", Integer.toString(i)));
    }

    void showInfoAppFragment(IProblem iProblem) {
        ((InfoAppFragment) getMainActivity().slideInFragment("InfoFragmentTag")).setData(iProblem);
    }
}
