package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.AlertDialog;
import android.widget.TextView;

import com.demo.example.R;

import java.io.File;
import java.text.SimpleDateFormat;


public class File_Properties {
    AlertDialog alertDialog;
    TextView fsize;
    TextView ftype;
    TextView last_Modified;
    TextView path;
    TextView writable;

    public File_Properties(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    private static long getFileFolderSize(File file) {
        File[] listFiles;
        long fileFolderSize;
        long j = 0;
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (file2.isFile()) {
                    fileFolderSize = file2.length();
                } else {
                    fileFolderSize = getFileFolderSize(file2);
                }
                j += fileFolderSize;
            }
            return j;
        } else if (file.isFile()) {
            return 0 + file.length();
        } else {
            return 0L;
        }
    }

//    public void setProperties(File file) {
//        String str;
//        setVars();
//        if (file.isDirectory()) {
//            this.ftype.setText("Folder");
//        } else {
//            this.ftype.setText("File");
//        }
//        this.path.setText(file.getPath());
//        this.last_Modified.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Long.valueOf(file.lastModified())));
//        if (file.canWrite()) {
//            this.writable.setText("Yes");
//        } else {
//            this.writable.setText("No");
//        }
//        double fileFolderSize = getFileFolderSize(file) / 1024.0d;
//        double d = fileFolderSize / 1024.0d;
//        if (d < 1.0d) {
//            str = " KB";
//        } else {
//            str = " MB";
//            fileFolderSize = d;
//        }
//        TextView textView = this.fsize;
//        textView.setText(fileFolderSize + str);
//    }
//
//    private void setVars() {
//        this.ftype = (TextView) this.alertDialog.findViewById(R.id.type);
//        this.path = (TextView) this.alertDialog.findViewById(R.id.path);
//        this.fsize = (TextView) this.alertDialog.findViewById(R.id.size);
//        this.last_Modified = (TextView) this.alertDialog.findViewById(R.id.last_modified);
//        this.writable = (TextView) this.alertDialog.findViewById(R.id.writable);
//    }
}
