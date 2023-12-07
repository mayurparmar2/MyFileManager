package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.ArrayList;
import java.util.List;


public class IgnoredAdapter extends ArrayAdapter<IProblem> {
    IOnAdapterItemRemoved _adapterListener;
    private final Context _context;
    private List<IProblem> _values;


    public void setOnAdapterItemRemovedListener(IOnAdapterItemRemoved<IProblem> iOnAdapterItemRemoved) {
        this._adapterListener = iOnAdapterItemRemoved;
    }

    public IgnoredAdapter(Context context, ArrayList<IProblem> arrayList) {
        super(context, (int) R.layout.ignored_list_item, arrayList);
        this._values = null;
        this._adapterListener = null;
        this._context = context;
        this._values = arrayList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ignored_list_item, viewGroup, false);
        }
        final IProblem iProblem = this._values.get(i);
        TextView textView = (TextView) view.findViewById(R.id.nameAppIgnored);
        ImageView imageView = (ImageView) view.findViewById(R.id.iconAppIgnored);
        ((LinearLayout) view.findViewById(R.id.linearLayoutAdapter)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                String whiteListOnRemoveDescription;
                AlertDialog.Builder builder = new AlertDialog.Builder(IgnoredAdapter.this.getContext());
                builder.setTitle(IgnoredAdapter.this._context.getString(R.string.warning)).setPositiveButton(IgnoredAdapter.this._context.getString(R.string.accept_eula), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        IgnoredAdapter.this.remove(iProblem);
                        IgnoredAdapter.this._adapterListener.onItemRemoved(iProblem);
                    }
                }).setNegativeButton(IgnoredAdapter.this._context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                    }
                });
                if (iProblem.getType() != IProblem.ProblemType.AppProblem) {
                    whiteListOnRemoveDescription = ((SystemProblem) iProblem).getWhiteListOnRemoveDescription(IgnoredAdapter.this._context);
                } else {
                    whiteListOnRemoveDescription = IgnoredAdapter.this._context.getString(R.string.remove_ignored_app_message) + " " + StaticTools.getAppNameFromPackage(IgnoredAdapter.this.getContext(), ((AppProblem) iProblem).getPackageName());
                }
                builder.setMessage(whiteListOnRemoveDescription);
                builder.show();
            }
        });
        if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
            AppProblem appProblem = (AppProblem) iProblem;
            textView.setText(StaticTools.getAppNameFromPackage(getContext(), appProblem.getPackageName()));
            imageView.setImageDrawable(StaticTools.getIconFromPackage(appProblem.getPackageName(), getContext()));
        } else {
            SystemProblem systemProblem = (SystemProblem) iProblem;
            textView.setText(systemProblem.getTitle(getContext()));
            imageView.setImageDrawable(systemProblem.getIcon(getContext()));
        }
        return view;
    }

    public void refresh(ArrayList<IProblem> arrayList) {
        clear();
        addAll(arrayList);
    }
}
