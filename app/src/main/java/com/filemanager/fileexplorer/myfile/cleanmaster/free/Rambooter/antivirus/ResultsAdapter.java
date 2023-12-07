package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.demo.example.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ResultsAdapter extends ArrayAdapter<IResultsAdapterItem> {
    int _appHeaderIndex;
    Context _context;
    private IResultItemSelectedListener _onItemChangedStateListener;
    int _systemMenacesHeaderIndex;
    final int kAPP_TYPE;
    final int kHEADER_TYPE;
    final int kSYSTEM_TYPE;

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public void setResultItemSelectedStateChangedListener(IResultItemSelectedListener iResultItemSelectedListener) {
        this._onItemChangedStateListener = iResultItemSelectedListener;
    }

    public ResultsAdapter(Context context, List<IProblem> list) {
        super(context, (int) R.layout.results_list_item, new ArrayList());
        this.kHEADER_TYPE = 0;
        this.kAPP_TYPE = 1;
        this.kSYSTEM_TYPE = 2;
        this._appHeaderIndex = -1;
        this._systemMenacesHeaderIndex = -1;
        this._onItemChangedStateListener = null;
        this._context = context;
        refreshByProblems(list);
    }

    public void refreshByProblems(List<IProblem> list) {
        clear();
        ArrayList arrayList = new ArrayList();
        ProblemsDataSetTools.getAppProblems(list, arrayList);
        if (arrayList.size() > 0) {
            this._appHeaderIndex = 0;
            add(new ResultsAdapterHeaderItem(this._context.getString(R.string.applications_header_text)));
            _addProblems(arrayList);
        } else {
            this._appHeaderIndex = -1;
        }
        ArrayList arrayList2 = new ArrayList();
        ProblemsDataSetTools.getSystemProblems(list, arrayList2);
        if (arrayList2.size() > 0) {
            this._systemMenacesHeaderIndex = getCount();
            add(new ResultsAdapterHeaderItem(this._context.getString(R.string.system_header_text)));
            _addProblems(arrayList2);
            return;
        }
        this._systemMenacesHeaderIndex = -1;
    }

    public void refreshByResults(List<IResultsAdapterItem> list) {
        clear();
        addAll(list);
    }

    public void _addProblems(Collection<IProblem> collection) {
        for (IProblem iProblem : collection) {
            add(new ResultsAdapterProblemItem(iProblem));
        }
    }

    public View _createView(int i, ViewGroup viewGroup) {
        return ((LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate((i == this._appHeaderIndex || i == this._systemMenacesHeaderIndex) ? R.layout.results_list_header : R.layout.results_list_item, viewGroup, false);
    }

    public void _fillRowData(int i, View view) {
        int i2;
        if (i == this._appHeaderIndex || i == (i2 = this._systemMenacesHeaderIndex)) {
            ((TextView) view.findViewById(R.id.Titlelabel)).setText(((ResultsAdapterHeaderItem) getItem(i)).getDescription());
        } else if (i2 == -1 || i < i2) {
            final AppProblem appProblem = ((ResultsAdapterProblemItem) getItem(i)).getAppProblem();
            TextView textView = (TextView) view.findViewById(R.id.Titlelabel);
            TextView textView2 = (TextView) view.findViewById(R.id.qualityApp);
            ImageView imageView = (ImageView) view.findViewById(R.id.logo);
            if (appProblem.isDangerous()) {
                textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                textView2.setText(R.string.high_risk);
            } else {
                textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                textView2.setText(R.string.medium_risk);
            }
            ((RelativeLayout) view.findViewById(R.id.itemParent)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (ResultsAdapter.this._onItemChangedStateListener != null) {
                        ResultsAdapter.this._onItemChangedStateListener.onItemSelected(appProblem);
                    }
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (ResultsAdapter.this._onItemChangedStateListener != null) {
                        ResultsAdapter.this._onItemChangedStateListener.onItemSelected(appProblem);
                    }
                }
            });
            textView.setText(StaticTools.getAppNameFromPackage(getContext(), appProblem.getPackageName()));
            imageView.setImageDrawable(StaticTools.getIconFromPackage(appProblem.getPackageName(), getContext()));
        } else {
            final SystemProblem systemProblem = ((ResultsAdapterProblemItem) getItem(i)).getSystemProblem();
            TextView textView3 = (TextView) view.findViewById(R.id.Titlelabel);
            TextView textView4 = (TextView) view.findViewById(R.id.qualityApp);
            ImageView imageView2 = (ImageView) view.findViewById(R.id.logo);
            if (systemProblem.isDangerous()) {
                textView4.setTextColor(ContextCompat.getColor(getContext(), R.color.HighRiskColor));
                textView4.setText(R.string.high_risk);
            } else {
                textView4.setTextColor(ContextCompat.getColor(getContext(), R.color.MediumRiskColor));
                textView4.setText(R.string.medium_risk);
            }
            ((RelativeLayout) view.findViewById(R.id.itemParent)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (ResultsAdapter.this._onItemChangedStateListener != null) {
                        ResultsAdapter.this._onItemChangedStateListener.onItemSelected(systemProblem);
                    }
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (ResultsAdapter.this._onItemChangedStateListener != null) {
                        ResultsAdapter.this._onItemChangedStateListener.onItemSelected(systemProblem);
                    }
                }
            });
            textView3.setText(systemProblem.getTitle(getContext()));
            imageView2.setImageDrawable(systemProblem.getIcon(getContext()));
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = _createView(i, viewGroup);
        }
        _fillRowData(i, view);
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        int i2;
        if (i == this._appHeaderIndex || i == (i2 = this._systemMenacesHeaderIndex)) {
            return 0;
        }
        return i < i2 ? 1 : 2;
    }

    public int getRealCount() {
        int count = super.getCount();
        if (this._appHeaderIndex != -1) {
            count--;
        }
        return this._systemMenacesHeaderIndex != -1 ? count - 1 : count;
    }
}
