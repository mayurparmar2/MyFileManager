package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus.IProblem;

import java.util.ArrayList;
import java.util.List;


public class WarningsAdapter extends ArrayAdapter<WarningData> {
    private final Context _context;
    private List<WarningData> _convertedData;
    private IProblem _resultData;

    public WarningsAdapter(Context context, IProblem iProblem) {
        super(context, R.layout.warning_item);
        this._resultData = null;
        this._convertedData = null;
        this._context = context;
        this._resultData = iProblem;
        this._convertedData = _fillDataArray(iProblem);
        clear();
        addAll(this._convertedData);
        notifyDataSetChanged();
    }

    public List<WarningData> _fillDataArray(IProblem iProblem) {
        ArrayList arrayList = new ArrayList();
        if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
            AppProblem appProblem = (AppProblem) iProblem;
            appProblem.getActivityData();
            for (PermissionData permissionData : appProblem.getPermissionData()) {
                WarningData warningData = new WarningData();
                warningData.icon = ContextCompat.getDrawable(this._context, setPermissionIcon(permissionData.getPermissionName()));
                warningData.title = setPermissionTitle(permissionData.getPermissionName());
                warningData.text = setPermissionMessage(permissionData.getPermissionName());
                arrayList.add(warningData);
            }
            if (!appProblem.getInstalledThroughGooglePlay()) {
                WarningData warningData2 = new WarningData();
                warningData2.icon = ContextCompat.getDrawable(this._context, R.drawable.information);
                warningData2.title = getContext().getResources().getString(R.string.title_installedGPlay);
                warningData2.text = getContext().getResources().getString(R.string.installedGPlay_message);
                arrayList.add(warningData2);
            }
        } else {
            SystemProblem systemProblem = (SystemProblem) iProblem;
            Context context = getContext();
            WarningData warningData3 = new WarningData();
            warningData3.icon = systemProblem.getSubIcon(context);
            warningData3.title = systemProblem.getSubTitle(context);
            warningData3.text = systemProblem.getDescription(context);
            arrayList.add(warningData3);
        }
        return arrayList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.warning_item, viewGroup, false);
        }
        WarningData warningData = this._convertedData.get(i);
        ((ImageView) view.findViewById(R.id.iconWarning)).setImageDrawable(warningData.icon);
        ((TextView) view.findViewById(R.id.titleWarning)).setText(warningData.title);
        ((TextView) view.findViewById(R.id.messageWarning)).setText(warningData.text);
        return view;
    }

    public String setPermissionMessage(String str) {
        Resources resources = getContext().getResources();
        if (str.contains("READ_PHONE_STATE")) {
            return resources.getString(R.string.read_phone_message);
        }
        if (str.contains("ACCESS_FINE_LOCATION")) {
            return resources.getString(R.string.access_fine_message);
        }
        if (str.contains("READ_SMS")) {
            return resources.getString(R.string.read_sms_message);
        }
        if (str.contains("WRITE_SMS")) {
            return resources.getString(R.string.write_sms_message);
        }
        if (str.contains("SEND_SMS")) {
            return resources.getString(R.string.send_sms_message);
        }
        if (str.contains("READ_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.read_history_message);
        }
        if (str.contains("WRITE_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.write_history_message);
        }
        if (str.contains("CALL_PHONE")) {
            return resources.getString(R.string.call_phone_message);
        }
        if (str.contains("PROCESS_OUTGOING_CALLS")) {
            return resources.getString(R.string.outgoing_phone_message);
        }
        if (str.contains("RECORD_AUDIO")) {
            return resources.getString(R.string.record_audio_message);
        }
        return str.contains("CAMERA") ? resources.getString(R.string.camera_message) : "";
    }

    public String setPermissionTitle(String str) {
        Resources resources = getContext().getResources();
        if (str.contains("READ_PHONE_STATE")) {
            return resources.getString(R.string.phone_data_shared);
        }
        if (str.contains("ACCESS_FINE_LOCATION")) {
            return resources.getString(R.string.location_shared);
        }
        if (str.contains("READ_SMS")) {
            return resources.getString(R.string.read_your_sms);
        }
        if (str.contains("WRITE_SMS")) {
            return resources.getString(R.string.write_sms_title);
        }
        if (str.contains("SEND_SMS")) {
            return resources.getString(R.string.send_sms_title);
        }
        if (str.contains("READ_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.read_history_bookmark_title);
        }
        if (str.contains("WRITE_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.write_history_bookmark_title);
        }
        if (str.contains("CALL_PHONE")) {
            return resources.getString(R.string.can_make_call_title);
        }
        if (str.contains("PROCESS_OUTGOING_CALLS")) {
            return resources.getString(R.string.outgoing_calls_title);
        }
        if (str.contains("RECORD_AUDIO")) {
            return resources.getString(R.string.record_audio_title);
        }
        return str.contains("CAMERA") ? resources.getString(R.string.access_camera_title) : "";
    }

    public int setPermissionIcon(String str) {
        if (!str.contains("READ_PHONE_STATE")) {
            if (str.contains("ACCESS_FINE_LOCATION")) {
                return R.drawable.fine_location_icon;
            }
            if (str.contains("READ_SMS")) {
                return R.drawable.read_sms;
            }
            if (str.contains("WRITE_SMS") || str.contains("SEND_SMS")) {
                return R.drawable.send_sms;
            }
            if (str.contains("READ_HISTORY_BOOKMARKS") || str.contains("WRITE_HISTORY_BOOKMARKS")) {
                return R.drawable.history_icon;
            }
            if (!str.contains("CALL_PHONE") && !str.contains("PROCESS_OUTGOING_CALLS") && !str.contains("RECORD_AUDIO")) {
                if (str.contains("CAMERA")) {
                    return R.drawable.icon_app;
                }
                return 0;
            }
        }
        return R.drawable.phone_icon;
    }
}
