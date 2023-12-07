package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.process;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.example.R;
import com.github.mikephil.charting.utils.Utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class ProcessListAdapter extends ArrayAdapter<TaskInfo> implements View.OnClickListener {
    private ActivityManager am;
    private boolean check;
    private Context context;
    private PackageManager pm;
    private ArrayList<TaskInfo> taskList;

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ProcessListAdapter(Context context, ArrayList<TaskInfo> arrayList) {
        super(context, (int) R.layout.process_list_layout, arrayList);
        this.am = null;
        this.check = false;
        this.taskList = null;
        this.pm = null;
        this.context = context;
        this.taskList = arrayList;
        this.pm = context.getPackageManager();
        this.am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public int getCount() {
        return this.taskList.size();
    }

    @Override
    public TaskInfo getItem(int i) {
        return this.taskList.get(i);
    }


    class ViewHolder {
        private TextView taskCPU;
        private CheckBox taskCheck;
        private ImageView taskIcon;
        private TextView taskMem;
        private TextView tastTitle;

        ViewHolder() {
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.process_list_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.taskIcon = (ImageView) view.findViewById(R.id.imgTaskIcon);
            viewHolder.tastTitle = (TextView) view.findViewById(R.id.txtTaskTitle);
            viewHolder.taskMem = (TextView) view.findViewById(R.id.txtTaskMemory);
            viewHolder.taskCPU = (TextView) view.findViewById(R.id.txtTaskCPU);
            viewHolder.taskCheck = (CheckBox) view.findViewById(R.id.chkTaskChecked);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TaskInfo taskInfo = this.taskList.get(i);
        viewHolder.taskIcon.setImageDrawable(taskInfo.getdraw());
        if (taskInfo.mem == 0) {
            viewHolder.taskMem.setText("");
        } else {
            viewHolder.tastTitle.setText(taskInfo.getName());
            String formatFileSize = Formatter.formatFileSize(this.context, taskInfo.mem);
            TextView textView = viewHolder.taskMem;
            textView.setText("MEM: " + formatFileSize);
            StringBuilder sb = new StringBuilder(String.valueOf(new DecimalFormat("#.##").format(getCPUTime(taskInfo.getRuninfo().pid))));
            sb.append("%");
            String sb2 = sb.toString();
            TextView textView2 = viewHolder.taskCPU;
            textView2.setText("CPU: " + sb2);
            viewHolder.taskCheck.setChecked(taskInfo.isChceked());
            viewHolder.taskCheck.setTag(new Integer(i));
            viewHolder.taskCheck.setOnClickListener(this);
        }
        return view;
    }

    public double getCPUTime(int pid) {

        double d = 0;
        String path = (new StringBuilder("/proc/")).append(pid).append("/stat")
                .toString();
        File file = new File(path);
        DataInputStream dis = null;
        try {
            FileInputStream is = new FileInputStream(file);
            dis = new DataInputStream(is);
            String[] as = dis.readLine().split("\\s+");
            int j = 13;
            while (j < 17) {
                int k = Integer.parseInt(as[j]);
                double dl = k;
                d += dl;
                j++;
            }

            d /= 1000D;
            while (d > 10D) {
                d /= 10D;
            }

            if (d < 0.01D) {
                if (d == 0D) {
                    d = (double) ((new Random()).nextInt(22) + 3) / 100D;
                    return (double) (int) (d * 0F) / 100D;
                }
                d *= 10D;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return d;
    }
//    public double getCPUTime(int i) {
//        Throwable th;
//        DataInputStream dataInputStream;
//        IOException e;
//        FileNotFoundException e2;
//        File file = new File("/proc/" + i + "/stat");
//        double d = Utils.DOUBLE_EPSILON;
//        try {
//            try {
//                dataInputStream = new DataInputStream(new FileInputStream((File) file));
//                try {
//                    String[] split = dataInputStream.readLine().split("\\s+");
//                    double d2 = 0.0d;
//                    for (int i2 = 13; i2 < 17; i2++) {
//                        try {
//                            d2 += Integer.parseInt(split[i2]);
//                        } catch (IOException e5) {
//                            e = e5;
//                            d = d2;
//                            e.printStackTrace();
//                            if (dataInputStream != null) {
//                                try {
//                                    dataInputStream.close();
//                                } catch (IOException e6) {
//                                    e = e6;
//                                    e.printStackTrace();
//                                    return d;
//                                }
//                            }
//                            return d;
//                        }
//                    }
//                    double d3 = 1000.0d;
//                    while (true) {
//                        d2 /= d3;
//                        d3 = 10.0d;
//                        if (d2 <= 10.0d) {
//                            break;
//                        }
//                    }
//                    if (d2 < 0.01d) {
//                        if (d2 == Utils.DOUBLE_EPSILON) {
//                            double nextInt = ((int) (((new Random().nextInt(22) + 3) / 100.0d) * Utils.DOUBLE_EPSILON)) / 100.0d;
//                            try {
//                                dataInputStream.close();
//                            } catch (IOException e7) {
//                                e7.printStackTrace();
//                            }
//                            return nextInt;
//                        }
//                        d2 *= 10.0d;
//                    }
//                    try {
//                        dataInputStream.close();
//                        return d2;
//                    } catch (IOException e8) {
//                        e8.printStackTrace();
//                        return d2;
//                    }
//                } catch (FileNotFoundException e9) {
//                    e2 = e9;
//                } catch (IOException e10) {
//                    e = e10;
//                }
//            } catch (Throwable th2) {
//                th = th2;
//                if (file != 0) {
//                    try {
//                        file.close();
//                    } catch (IOException e11) {
//                        e11.printStackTrace();
//                    }
//                }
//                throw th;
//            }
//        } catch (FileNotFoundException e12) {
//            dataInputStream = null;
//            e2 = e12;
//        } catch (IOException e13) {
//            dataInputStream = null;
//            e = e13;
//        } catch (Throwable th3) {
//            file = 0;
//            th = th3;
//            if (file != 0) {
//            }
//            throw th;
//        }
//    }

    @Override
    public void onClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        this.taskList.get(((Integer) checkBox.getTag()).intValue()).setChceked(checkBox.isChecked());
    }
}
