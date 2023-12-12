package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.os.EnvironmentCompat;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Image_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.MusicUltil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.UStats;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class StatisticActivity extends AppCompatActivity {
    long audioSize;
    Data_Manager data_manager;
    long docsSize;
    Image_Ultil image_ultil;
    long imagesSize;
    ImageView img_back;
    MusicUltil musicUltil;
    PieChart pieChart;
    PieChart sdPieChart;
    long sdaudioSize;
    long sddocsSize;
    long sdimagesSize;
    long sdvideoSize;
    long videoSize;
    Video_Ultil video_ultil;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_statistic);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.musicUltil = new MusicUltil(this);
        this.video_ultil = new Video_Ultil(this);
        this.image_ultil = new Image_Ultil(this);
        this.data_manager = new Data_Manager(this);
        this.pieChart = (PieChart) findViewById(R.id.pie_chart);
        this.sdPieChart = (PieChart) findViewById(R.id.sd_pie_chart);
        this.musicUltil.allsong();
        this.video_ultil.getallvideo();
        this.image_ultil.allImage();
        this.imagesSize = Image_Ultil.totolsize;
        this.videoSize = Video_Ultil.totolsize;
        this.audioSize = MusicUltil.totolsize;
        this.data_manager.setDocs();
        this.docsSize = this.data_manager.sizedoccument;
        setPieData();
        if (externalMemoryAvailable()) {
            new BackgroundSizeCalculation().start();
            this.sdPieChart.setVisibility(View.VISIBLE);
            setPieChart();
        } else {
            this.sdPieChart.setVisibility(View.GONE);
        }
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                m63xa77a9205(view);
            }
        });
    }


    public void m63xa77a9205(View view) {
        finish();
    }

    public boolean externalMemoryAvailable() {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(this, null);
        return (externalFilesDirs.length <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) ? false : true;
    }

    void setPieData() {
        ArrayList arrayList = new ArrayList();
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        arrayList.add(new PieEntry((float) statFs.getFreeBytes(), "Free"));
        arrayList.add(new PieEntry((float) this.videoSize, "Videos"));
        arrayList.add(new PieEntry((float) this.imagesSize, "Images"));
        arrayList.add(new PieEntry((float) this.audioSize, "Audio"));
        arrayList.add(new PieEntry((float) this.docsSize, "Documents"));
        arrayList.add(new PieEntry((float) (((((statFs.getTotalBytes() - this.audioSize) - this.imagesSize) - this.docsSize) - this.videoSize) - statFs.getFreeBytes()), "Other"));
        PieDataSet pieDataSet = new PieDataSet(arrayList, ":File Types");
        pieDataSet.setColors(ColorTemplate.createColors(new int[]{getResources().getColor(R.color.piechart_red), getResources().getColor(R.color.piechart_blue), getResources().getColor(R.color.piechart_origin), getResources().getColor(R.color.piechart_violet), getResources().getColor(R.color.piechart_yl), getResources().getColor(R.color.piechart_green)}));
        pieDataSet.setValueTextSize(15.0f);
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        this.pieChart.setData(new PieData(pieDataSet));
        this.pieChart.animateY(1500);
        this.pieChart.setUsePercentValues(true);
        this.pieChart.setCenterTextSize(15.0f);
        this.pieChart.setDrawCenterText(true);
        this.pieChart.setDrawHoleEnabled(true);
        this.pieChart.invalidate();
        PieChart pieChart = this.pieChart;
        pieChart.setCenterText(Formatter.formatFileSize(this, statFs.getFreeBytes()) + " Free");
        this.pieChart.setDrawEntryLabels(false);
        Description description = new Description();
        description.setText("Internal Storage");
        description.setTextColor(0xffff0000);
        description.setTextSize(15.0f);
        this.pieChart.setDescription(description);
        this.pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    void setPieChart() {
        ArrayList arrayList = new ArrayList();
        StatFs statFs = new StatFs(getExternalStorageDirectories()[0]);
        arrayList.add(new PieEntry((float) this.sdvideoSize, "Video"));
        arrayList.add(new PieEntry((float) statFs.getFreeBytes(), "Free"));
        arrayList.add(new PieEntry((float) this.sdimagesSize, "Images"));
        arrayList.add(new PieEntry((float) this.sdaudioSize, "Audio"));
        arrayList.add(new PieEntry((float) this.sddocsSize, "Documents"));
        arrayList.add(new PieEntry((float) (((((statFs.getTotalBytes() - this.sdaudioSize) - this.sdimagesSize) - this.sddocsSize) - this.sdvideoSize) - statFs.getFreeBytes()), "Other"));
        PieDataSet pieDataSet = new PieDataSet(arrayList, "File Type");
        pieDataSet.setColors(ColorTemplate.createColors(new int[]{getResources().getColor(R.color.piechart_red), getResources().getColor(R.color.piechart_blue), getResources().getColor(R.color.piechart_origin), getResources().getColor(R.color.piechart_violet), getResources().getColor(R.color.piechart_yl), getResources().getColor(R.color.piechart_green)}));
        this.sdPieChart.setData(new PieData(pieDataSet));
        this.sdPieChart.animateY(1000);
        this.sdPieChart.setUsePercentValues(true);
        this.sdPieChart.invalidate();
        PieChart pieChart = this.sdPieChart;
        pieChart.setCenterText(Formatter.formatFileSize(this, statFs.getFreeBytes()) + " Free");
        this.sdPieChart.setDrawEntryLabels(false);
        Description description = new Description();
        description.setText("External Storage");
        description.setTextSize(16.0f);
        this.sdPieChart.setDescription(description);
        this.sdPieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
    }

    public String[] getExternalStorageDirectories() {
        String LOG_TAG = "SDCARD";
        List<String> results = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = getExternalFilesDirs(null);

            for (File file : externalDirs) {
                String path = file.getPath().split("/Android")[0];

                boolean addPath = false;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);
                }
                else{
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if(addPath){
                    results.add(path);
                }
            }
        }

        if(results.isEmpty()) { //Method 2 for all versions
            // better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if(!output.trim().isEmpty()) {
                String devicePoints[] = output.split("\n");
                for(String voldPoint: devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }
        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d(LOG_TAG, results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
                    Log.d(LOG_TAG, results.get(i)+" might not be extSDcard");
                    results.remove(i--);
                }
            }
        }

        String[] storageDirectories = new String[results.size()];
        for(int i=0; i<results.size(); ++i) storageDirectories[i] = results.get(i);

        return storageDirectories;
    }

//    public String[] getExternalStorageDirectories() {
//        byte[] bArr = new byte[0];
//        boolean equals;
//        ArrayList arrayList = new ArrayList();
//        if (Build.VERSION.SDK_INT >= 19) {
//            File[] externalFilesDirs = getExternalFilesDirs(null);
//            String lowerCase = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();
//            for (File file : externalFilesDirs) {
//                if (file != null) {
//                    String str = file.getPath().split("/Android")[0];
//                    if (!str.toLowerCase().startsWith(lowerCase)) {
//                        if (Build.VERSION.SDK_INT >= 21) {
//                            equals = Environment.isExternalStorageRemovable(file);
//                        } else {
//                            equals = "mounted".equals(EnvironmentCompat.getStorageState(file));
//                        }
//                        if (equals) {
//                            arrayList.add(str);
//                        }
//                    }
//                }
//            }
//        }
//        if (arrayList.isEmpty()) {
//            String str2 = "";
//            try {
//                Process start = new ProcessBuilder(new String[0]).command("mount | grep /dev/block/vold").redirectErrorStream(true).start();
//                start.waitFor();
//                InputStream inputStream = start.getInputStream();
//                while (inputStream.read(new byte[1024]) != -1) {
//                    str2 = str2 + new String(bArr);
//                }
//                inputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (!str2.trim().isEmpty()) {
//                for (String str3 : str2.split("\n")) {
//                    arrayList.add(str3.split(" ")[2]);
//                }
//            }
//        }
//        if (Build.VERSION.SDK_INT >= 23) {
//            int i = 0;
//            while (i < arrayList.size()) {
//                if (!((String) arrayList.get(i)).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
//                    Log.d("LOG_TAG", ((String) arrayList.get(i)) + " might not be extSDcard");
//                    arrayList.remove(i);
//                    i += -1;
//                }
//                i++;
//            }
//        } else {
//            int i2 = 0;
//            while (i2 < arrayList.size()) {
//                if (!((String) arrayList.get(i2)).toLowerCase().contains("ext") && !((String) arrayList.get(i2)).toLowerCase().contains("sdcard")) {
//                    Log.d("LOG_TAG", ((String) arrayList.get(i2)) + " might not be extSDcard");
//                    arrayList.remove(i2);
//                    i2 += -1;
//                }
//                i2++;
//            }
//        }
//        String[] strArr = new String[arrayList.size()];
//        for (int i3 = 0; i3 < arrayList.size(); i3++) {
//            strArr[i3] = (String) arrayList.get(i3);
//        }
//        return strArr;
//    }


    class BackgroundSizeCalculation extends Thread {
        @Override
        public void run() {
            Cursor cursor;
            String[] proj = new String[]{MediaStore.Images.Media.SIZE
                    , MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
            cursor.moveToFirst();
            do {
                if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)).contains(Environment.getExternalStorageDirectory().getPath()))
                    imagesSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                else
                    sdimagesSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
            } while (cursor.moveToNext());
            String[] proj_audio = new String[]{MediaStore.Audio.Media.SIZE
                    , MediaStore.Audio.Media.DATA};
            cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj_audio, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)).contains(Environment.getExternalStorageDirectory().getPath()))
                        audioSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                    else
                        sdaudioSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                } while (cursor.moveToNext());
            }
            String[] proj_video = new String[]{MediaStore.Video.Media.SIZE
                    , MediaStore.Video.Media.DATA};
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj_video, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)).contains(Environment.getExternalStorageDirectory().getPath()))
                        videoSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                    else
                        sdvideoSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                } while (cursor.moveToNext());
            }


            String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
            String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
            String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
            String xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
            String xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
            String ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
            String pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
            String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
            String rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx");
            String rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf");
            String html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");

            Uri table = MediaStore.Files.getContentUri("external");
            String[] column = {MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.SIZE};
            String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
                    + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?";
            String[] args = new String[]{pdf, doc, docx, xls, xlsx, ppt, pptx, txt, rtx, rtf, html};

            cursor = getContentResolver().query(table, column, where, args, null);
            cursor.moveToFirst();
            do {
                if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)).contains(Environment.getExternalStorageDirectory().getPath()))
                    docsSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
                else
                    sddocsSize += Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
            } while (cursor.moveToNext());
        }
    }

//    class BackgroundSizeCalculation extends Thread {
//        BackgroundSizeCalculation() {
//        }
//        @Override
//        public void run() {
//            Cursor query;
//            String str;
//            Cursor query2;
//            String str2 = null;
//            Cursor query3 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_size", "_data"}, null, null, null);
//            query3.moveToFirst();
//            do {
//                if (query3.getString(query3.getColumnIndexOrThrow("_data")).contains(Environment.getExternalStorageDirectory().getPath())) {
//                    Log.d("TAG", "run: ");
//                } else {
//                    sdimagesSize += Long.parseLong(query3.getString(query3.getColumnIndexOrThrow("_size")));
//                }
//            } while (query3.moveToNext());
//            Cursor query4 = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_size", "_data"}, null, null, null);
//            query4.moveToFirst();
//            if (query4.getCount() != 0) {
//                do {
//                    if (query4.getString(query4.getColumnIndexOrThrow("_data")).contains(Environment.getExternalStorageDirectory().getPath())) {
//                        Log.d("TAG", "run: ");
//                    } else {
//                        sdaudioSize += Long.parseLong(query4.getString(query4.getColumnIndexOrThrow("_size")));
//                    }
//                } while (query4.moveToNext());
//                query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_size", "_data"}, null, null, null);
//                query.moveToFirst();
//                if (query.getCount() == 0) {
//                    do {
//                        if (query.getString(query.getColumnIndexOrThrow("_data")).contains(Environment.getExternalStorageDirectory().getPath())) {
//                            Log.d(UStats.TAG, "run: ");
//                        } else {
//                            sdvideoSize += Long.parseLong(query.getString(query.getColumnIndexOrThrow("_size")));
//                        }
//                    } while (query.moveToNext());
//                    str = "TAG";
//                    query2 = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "_size"}, "mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=?", new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("html")}, null);
//                    query2.moveToFirst();
//                    while (true) {
//                        if (!query2.getString(query2.getColumnIndexOrThrow("_data")).contains(Environment.getExternalStorageDirectory().getPath())) {
//                            str2 = str;
//                            Log.d(str2, "run: ");
//                        } else {
//                            str2 = str;
//                            sddocsSize += Long.parseLong(query2.getString(query2.getColumnIndexOrThrow("_size")));
//                        }
//                        if (query2.moveToNext()) {
//                            return;
//                        }
//                        str = str2;
//                    }
//                } else {
//                    str = "TAG";
//                    query2 = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "_size"}, "mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=? OR mime_type=?", new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf"), MimeTypeMap.getSingleton().getMimeTypeFromExtension("html")}, null);
//                    query2.moveToFirst();
//                    while (true) {
//                        if (!query2.getString(query2.getColumnIndexOrThrow("_data")).contains(Environment.getExternalStorageDirectory().getPath())) {
//                        }
//                        if (query2.moveToNext()) {
//                        }
//                        str = str2;
//                    }
//                }
//            } else {
//                query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_size", "_data"}, null, null, null);
//                query.moveToFirst();
//                if (query.getCount() == 0) {
//                }
//            }
//        }
//    }
}
