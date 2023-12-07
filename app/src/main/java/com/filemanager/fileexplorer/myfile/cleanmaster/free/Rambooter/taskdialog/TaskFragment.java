package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.taskdialog;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process.AppShort;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process.PackagesInfo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process.ProcessListAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process.TaskInfo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.taskdialog.TaskDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.taskdialog.TaskKillDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Sharepre_Ulti;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.TaskAdapter.TaskAdapater;
 

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TaskFragment extends Fragment implements View.OnClickListener, TaskDialog.DialogDismissListener, TaskKillDialog.DialogTaskKillListener {
    private static final String TAG = "AAAA";
    public static int mem;
    private static ArrayList<TaskInfo> taskInfos;
    private long aftermemory;
    private long beforeMemory;
    private Button btnKill;
    private boolean check_permission;
    private DeleteCallback deleteCallback;
    private ActivityResultLauncher<Intent> launcher;
    private ServiceConnection mConnection;
    private int processesKilled;
    private RelativeLayout r_resulft;
    private int ramFreed;
    private Sharepre_Ulti sharepre_ulti;
    private RecyclerView swipeListView;
    private TaskAdapater taskAdapater;
    private int v_pos;
    private ProcessListAdapter adapter = null;
    private ProgressDialog pd = null;
    private ActivityManager acm = null;
    private int pos = 0;
    boolean mStopHandler = false;

    @Override
    public void onDialogDismiss() {
    }

    @Override
    public void onDismiss(AbsListView absListView, int[] iArr) {
    }

    @Override
    public void onTaskKIll(int i) {
    }

    static int access$808(TaskFragment taskFragment) {
        int i = taskFragment.v_pos;
        taskFragment.v_pos = i + 1;
        return i;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.task_list_layout, viewGroup, false);
        this.sharepre_ulti = new Sharepre_Ulti(getContext());
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    new updateUI().execute(new String[0]);
                } else {
                    new updateUI().execute(new String[0]);
                }
                TaskFragment.this.sharepre_ulti.writeSharedPrefs("permissionGR", (Boolean) true);
            }
        });
        this.swipeListView = (RecyclerView) inflate.findViewById(R.id.list);
        this.btnKill = (Button) inflate.findViewById(R.id.btnKill);
        this.r_resulft = (RelativeLayout) inflate.findViewById(R.id.r_resulf);
        this.btnKill.setOnClickListener(this);
        this.swipeListView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        this.deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                TaskFragment.this.launcher.launch(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
            }
        };
        new updateUI().execute(new String[0]);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 15) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                TaskFragment.this.taskAdapater.notifyDataSetChanged();
            }
        }).attachToRecyclerView(this.swipeListView);
        return inflate;
    }

    private boolean Checpermission() {
        int checkOpNoThrow = ((AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE)).checkOpNoThrow("android:get_usage_stats", Process.myUid(), getContext().getPackageName());
        if (checkOpNoThrow == 3) {
            if (getContext().checkCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS") == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } else if (checkOpNoThrow == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void setMenuVisibility(boolean z) {
        super.setMenuVisibility(z);
    }

    public void DisplayList(Context context) {
        getActivity();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        mem = 0;
        ArrayList arrayList = new ArrayList();
        PackagesInfo packagesInfo = new PackagesInfo(context);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            String str = runningAppProcessInfo.processName;
            if (!str.contains(getActivity().getPackageName()) && (runningAppProcessInfo.importance == 130 || runningAppProcessInfo.importance == 300 || runningAppProcessInfo.importance == 100 || runningAppProcessInfo.importance == 400)) {
                TaskInfo taskInfo = new TaskInfo(context, runningAppProcessInfo);
                taskInfo.getAppInfo(packagesInfo);
                if (!isImportant(str)) {
                    taskInfo.setChceked(true);
                }
                if (taskInfo.isGoodProcess()) {
                    Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                    int length = processMemoryInfo.length;
                    for (Debug.MemoryInfo memoryInfo : processMemoryInfo) {
                        taskInfo.setMem(memoryInfo.getTotalPss() * 1024);
                        if (memoryInfo.getTotalPss() * 1024 > mem) {
                            mem = memoryInfo.getTotalPss() * 1024;
                        }
                    }
                    if (mem > 0) {
                        arrayList.add(taskInfo);
                    }
                }
            }
        }
        Collections.sort(taskInfo(getUsageStatistics()), new AppShort());
    }

    private boolean isImportant(String str) {
        return str.equals("android") || str.equals("android.process.acore") || str.equals("system") || str.equals("com.android.phone") || str.equals("com.android.systemui") || str.equals("com.android.launcher");
    }

    @Override
    public void onClick(View view) {
        if (this.check_permission) {
            deleteAllItems();
            return;
        }
        Toast.makeText(getContext(), getContext().getString(R.string.toast_permisson_usage_data), Toast.LENGTH_SHORT).show();
        showdialogChekpermission();
    }

    private void deleteAllItems() {
        @SuppressLint("ResourceType") final Dialog dialog = new Dialog(getContext(), 16973834);
        dialog.setContentView(R.layout.dialog_clear_ram);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();
        final Handler handler = new Handler();
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TaskFragment.taskInfos.size() < 2) {
                    TaskFragment.this.mStopHandler = true;
                    TaskFragment.this.r_resulft.setVisibility(View.VISIBLE);
                    TaskFragment.this.swipeListView.setVisibility(View.GONE);
                    dialog.cancel();
                }
                if (!TaskFragment.this.mStopHandler) {
                    TaskFragment.this.deleteItem(TaskFragment.this.swipeListView.findViewHolderForAdapterPosition(((LinearLayoutManager) TaskFragment.this.swipeListView.getLayoutManager()).findFirstVisibleItemPosition()).itemView, 0);
                    TaskFragment.access$808(TaskFragment.this);
                } else {
                    handler.removeCallbacksAndMessages(null);
                }
                handler.postDelayed(this, 250L);
            }
        });
    }

    public List<UsageStats> getUsageStatistics() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, -1);
        return ((UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE)).queryUsageStats(4, calendar.getTimeInMillis(), System.currentTimeMillis());
    }


    public ArrayList<TaskInfo> taskInfo(List<UsageStats> list) {
        ArrayList<TaskInfo> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TaskInfo taskInfo = new TaskInfo();
            String packageName = list.get(i).getPackageName();
            taskInfo.setUsageStats(list.get(i));
            try {
                taskInfo.setIcon(getActivity().getPackageManager().getApplicationIcon((String) packageName));
                PackageManager packageManager = getContext().getPackageManager();
                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException unused) {
                }
                if (applicationInfo != null) {
                    packageName = (String) packageManager.getApplicationLabel(applicationInfo);
                }
                taskInfo.setName((String) packageName);
                taskInfo.setTimeusedlast(list.get(i).getLastTimeUsed());
            } catch (PackageManager.NameNotFoundException unused2) {
                Log.w(TAG, String.format("App Icon is not found for %s", taskInfo.usageStats.getPackageName()));
                taskInfo.setIcon(getActivity().getDrawable(R.drawable.icon_app));
            }
            arrayList.add(taskInfo);
        }
        return arrayList;
    }


    public ArrayList<TaskInfo> getappRecent(ArrayList<TaskInfo> arrayList) {
        ArrayList<TaskInfo> arrayList2 = new ArrayList<>();
        new ArrayList();
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < arrayList.size(); i++) {
            if (currentTimeMillis - (arrayList.get(i).getTimeusedlast() * 1000) <= 86400000) {
                arrayList2.add(arrayList.get(i));
            }
        }
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            if (isAppInSystemPartition(arrayList2.get(i2).getUsageStats().getPackageName())) {
                arrayList2.remove(i2);
            }
        }
        for (int i3 = 0; i3 < arrayList2.size(); i3++) {
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                if (arrayList2.get(i3).getUsageStats().getPackageName().equals(arrayList2.get(i4).getUsageStats().getPackageName())) {
                    arrayList2.remove(i4);
                }
            }
        }
        return arrayList2;
    }

    public boolean isAppInSystemPartition(String str) {
        try {
            return (getContext().getPackageManager().getApplicationInfo(str, 0).flags & 129) != 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }


    public class updateUI extends AsyncTask<String, String, String> {
        Dialog_thread dialog_thread;

        private updateUI() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog_thread dialog_thread = new Dialog_thread(TaskFragment.this.getContext());
            this.dialog_thread = dialog_thread;
            dialog_thread.show();
            ArrayList unused = TaskFragment.taskInfos = new ArrayList();
            TaskFragment.this.taskAdapater = new TaskAdapater(TaskFragment.this.getContext());
        }


        @Override
        public String doInBackground(String... strArr) {
            TaskFragment taskFragment = TaskFragment.this;
            ArrayList unused = TaskFragment.taskInfos = taskFragment.getappRecent(taskFragment.taskInfo(taskFragment.getUsageStatistics()));
            Collections.sort(TaskFragment.taskInfos, new Comparator<TaskInfo>() {
                @Override
                public int compare(TaskInfo taskInfo, TaskInfo taskInfo2) {
                    return String.valueOf(taskInfo2.getTimeusedlast()).compareTo(String.valueOf(taskInfo.getTimeusedlast()));
                }
            });
            return null;
        }


        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            TaskFragment.this.taskAdapater.setList(TaskFragment.taskInfos);
            TaskFragment.this.swipeListView.setAdapter(TaskFragment.this.taskAdapater);
            this.dialog_thread.dissmiss();
            if (TaskFragment.taskInfos.size() == 0) {
                TaskFragment.this.showdialogChekpermission();
                TaskFragment.this.check_permission = false;
                return;
            }
            TaskFragment.this.check_permission = true;
        }
    }


    public void showdialogChekpermission() {
        CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(getContext(), this.deleteCallback);
        customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        customDeleteDialog.show();
        customDeleteDialog.set_titile_permission_Usage();
    }

    public boolean isAppRunning(String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.processName.equals(str)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    private class clean extends AsyncTask<ArrayList<TaskInfo>, String, ArrayList<TaskInfo>> {
        int delay = 0;
        Dialog dialog;

        private clean() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            @SuppressLint("ResourceType") Dialog dialog = new Dialog(TaskFragment.this.getContext(), 16973834);
            this.dialog = dialog;
            dialog.setContentView(R.layout.dialog_clear_ram);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            this.dialog.show();
        }


        @Override
        public ArrayList<TaskInfo> doInBackground(ArrayList<TaskInfo>... arrayListArr) {
            for (int i = 0; i < TaskFragment.taskInfos.size(); i++) {
                TaskFragment.taskInfos.remove(i);
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = TaskFragment.this.swipeListView.findViewHolderForAdapterPosition(i);
                if (findViewHolderForAdapterPosition != null) {
                    View view = findViewHolderForAdapterPosition.itemView;
                    this.delay += 300;
                }
            }
            return null;
        }


        @Override
        public void onPostExecute(ArrayList<TaskInfo> arrayList) {
            super.onPostExecute(arrayList);
            if (TaskFragment.taskInfos.size() == 0) {
                ArrayList unused = TaskFragment.taskInfos = new ArrayList();
                TaskFragment.this.r_resulft.setVisibility(View.VISIBLE);
                TaskFragment.this.swipeListView.setVisibility(View.GONE);
                this.dialog.cancel();
            }
        }
    }


    public void killAppBypackage(String str) {
        List<ApplicationInfo> installedApplications = getContext().getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getContext().getApplicationContext().getPackageName();
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & 1) != 1 && !applicationInfo.packageName.equals(packageName) && applicationInfo.packageName.equals(str)) {
                activityManager.killBackgroundProcesses(applicationInfo.packageName);
            }
        }
    }


    public void deleteItem(View view, final int i) {
        @SuppressLint("ResourceType") Animation loadAnimation = AnimationUtils.loadAnimation(requireContext(), 17432579);
        loadAnimation.setDuration(400L);
        view.startAnimation(loadAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityManager activityManager = (ActivityManager) TaskFragment.this.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                String packageName = ((TaskInfo) TaskFragment.taskInfos.get(i)).getUsageStats().getPackageName();
                Log.d(TaskFragment.TAG, "run: " + packageName + "isruning:" + TaskFragment.this.isAppRunning(packageName));
                if (!TaskFragment.this.isAppRunning(packageName)) {
                    activityManager.killBackgroundProcesses(packageName);
                    TaskFragment.this.killAppBypackage(packageName);
                    TaskFragment.this.amKillProcess(packageName);
                }
                TaskFragment.taskInfos.remove(i);
                TaskFragment.this.taskAdapater.setList(TaskFragment.taskInfos);
            }
        }, loadAnimation.getDuration());
    }

    public void amKillProcess(String str) {
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.equals(str)) {
                Process.sendSignal(runningAppProcessInfo.pid, 9);
            }
        }
    }
}
