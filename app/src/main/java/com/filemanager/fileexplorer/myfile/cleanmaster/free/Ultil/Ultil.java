package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
 
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.core.content.FileProvider;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMusic;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdatealbum;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.FavoritSongs;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.RecentAdd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Ultil {
    private static final String PRIMARY_VOLUME_NAME = "primary";
    private static Context context;
    private ArrayList<Object> hide_list;
    public ArrayList<File_DTO> hidefile;

    public boolean rename(String str, String str2) {
        return false;
    }

    public Ultil(Context context2) {
        context = context2;
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static Float floatForm2(double d) {
        return Float.valueOf(new DecimalFormat("#.##").format(d));
    }

    public String bytesToHuman(long j) {
        int i = (j > 1024 ? 1 : (j == 1024 ? 0 : -1));
        if (i < 0) {
            return floatForm(j) + " byte";
        } else if (i >= 0 && j < 1048576) {
            return floatForm(j / ((double) 1024)) + " Kb";
        } else if (j >= 1048576 && j < 1073741824) {
            return floatForm(j / 1048576) + " Mb";
        } else if (j >= 1073741824 && j < 1099511627776L) {
            return floatForm(j / 1073741824) + " Gb";
        } else if (j >= 1099511627776L && j < 1125899906842624L) {
            return floatForm(j / 1099511627776L) + " Tb";
        } else if (j >= 1125899906842624L && j < 1152921504606846976L) {
            return floatForm(j / 1125899906842624L) + " Pb";
        } else if (j >= 1152921504606846976L) {
            return floatForm(j / 1152921504606846976L) + " Eb";
        } else {
            return "???";
        }
    }

    public static Float getsize(long j) {
        int i = (j > 1024 ? 1 : (j == 1024 ? 0 : -1));
        if (i < 0) {
            return floatForm2(j);
        }
        if (i < 0 || j >= 1048576) {
            if (j < 1048576 || j >= 1073741824) {
                if (j < 1073741824 || j >= 1099511627776L) {
                    if (j < 1099511627776L || j >= 1125899906842624L) {
                        if (j < 1125899906842624L || j >= 1152921504606846976L) {
                            if (j >= 1152921504606846976L) {
                                return floatForm2(j / 1152921504606846976L);
                            }
                            return Float.valueOf(Float.parseFloat("0.99"));
                        }
                        return floatForm2(j / 1125899906842624L);
                    }
                    return floatForm2(j / 1099511627776L);
                }
                return floatForm2(j / 1073741824);
            }
            return floatForm2(j / 1048576);
        }
        return floatForm2(j / ((double) 1024));
    }

    public String getDate(long j) {
        return new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(Long.valueOf(j));
    }

    public static long calculateTotalRAM() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            String[] split = bufferedReader.readLine().split("\\s+");
            for (String str : split) {
            }
            long longValue = Long.valueOf(split[1]).longValue() * 1024;
            bufferedReader.close();
            return longValue;
        } catch (IOException unused) {
            return -1L;
        }
    }

    public static long calculateAvailableRAM(Context context2) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context2.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public void addFav(File_DTO file_DTO) {
        FavoritSongs favoritSongs = new FavoritSongs(context);
        favoritSongs.open();
        if (check(file_DTO.getPath())) {
            Toast.makeText(context, "File already exists !", Toast.LENGTH_SHORT).show();
        } else {
            favoritSongs.addRow(file_DTO);
            Toast.makeText(context, "File was added !", Toast.LENGTH_SHORT).show();
        }
        favoritSongs.close();
    }

    private boolean check(String str) {
        FavoritSongs favoritSongs = new FavoritSongs(context);
        favoritSongs.open();
        new ArrayList();
        ArrayList<File_DTO> allRows = favoritSongs.getAllRows();
        boolean z = false;
        if (allRows != null) {
            boolean z2 = false;
            for (int i = 0; i < allRows.size(); i++) {
                if (allRows.get(i).getPath().equals(str)) {
                    z2 = true;
                }
            }
            z = z2;
        }
        favoritSongs.close();
        return z;
    }

    public void showdiloginfo(File_DTO file_DTO, String str) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_info);
        TextView textView = (TextView) dialog.findViewById(R.id.file_name);
        TextView textView2 = (TextView) dialog.findViewById(R.id.size);
        TextView textView3 = (TextView) dialog.findViewById(R.id.location);
        TextView textView4 = (TextView) dialog.findViewById(R.id.modifiled);
        TextView textView5 = (TextView) dialog.findViewById(R.id.dissmis);
        String path = file_DTO.getPath();
        String name = new File(path).getName();
        String replace = path.replace("/" + name, "");
        if (str.equals("almumimage")) {
            textView.setText("File Name: " + file_DTO.getAbumname());
            textView2.setText("Total: " + file_DTO.getTotalitem());
            textView3.setText("Location: " + replace);
            textView4.setText("Last Modified: " + file_DTO.getDate());
        } else if (str.equals("Videoalbum")) {
            textView.setText("File Name: " + file_DTO.getAbumname());
            textView2.setText("Toltal: " + file_DTO.getTotalitem());
            textView3.setText("Location: " + replace);
            textView4.setText("Last Modified: " + file_DTO.getDate());
        } else if (str.equals("Video")) {
            textView.setText("File Name: " + file_DTO.getName());
            textView2.setText(context.getString(R.string.size1) + file_DTO.getSize());
            textView3.setText("Location: " + file_DTO.getPath());
            textView4.setText("Last Modified: " + file_DTO.getDate());
        } else if (str.equals("app")) {
            textView.setText("App Name: " + file_DTO.getName());
            textView2.setText("Size: " + file_DTO.getSize());
            textView3.setText("Package Name: " + file_DTO.getPackagename());
            textView4.setText("Version: " + file_DTO.getAbumname());
        } else {
            textView.setText("File Name: " + file_DTO.getName());
            textView2.setText("Size: " + file_DTO.getSize());
            textView3.setText("Location: " + file_DTO.getPath());
            textView4.setText("Last Modified: " + file_DTO.getDate());
        }
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void dialogRenamAlbumImage(String str) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialogrename);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_OK);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.img_delete);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt_Cancel);
        final EditText editText = (EditText) dialog.findViewById(R.id.edt_filename);
        editText.requestFocus();
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(2, 0);
        final String replace = str.replace("/" + new File(str).getName(), "");
        editText.setText("" + new File(replace).getName());
        editText.setSelection(editText.getText().length());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().equals("") || editText.getText().length() == 0) {
                    Toast.makeText(Ultil.context, Ultil.context.getResources().getString(R.string.cannot_be_left_be_blank), Toast.LENGTH_SHORT).show();
                } else {
                    new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            new AndroidXI(Ultil.context).renamealbum(replace, editText.getText().toString());
                            try {
                                Thread.sleep(2000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Callbackupdatealbum.getInstance().change();
                            inputMethodManager.toggleSoftInput(1, 0);
                            dialog.cancel();
                        }
                    }.obtainMessage(111, "parameter").sendToTarget();
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.toggleSoftInput(1, 0);
                dialog.cancel();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (editText.getText().toString().length() > 5) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }
        });
        dialog.show();
    }

    public void DeleteAlbum(final ArrayList<File_DTO> arrayList) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_setwallpaper);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.titile)).setText(R.string.titile_delete_folder);
        final Dialog_thread dialog_thread = new Dialog_thread(context);
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        ((TextView) dialog.findViewById(R.id.txt_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_thread.show();
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            Ultil.this.copyFile(((File_DTO) arrayList.get(i)).getPath());
                            Ultil.this.delewithcontentResolver((File_DTO) arrayList.get(i));
                        }
                        Toast.makeText(Ultil.context, "Moved to trash", Toast.LENGTH_SHORT).show();
                        Callbackupdatealbum.getInstance().change();
                        dialog.cancel();
                        dialog_thread.dissmiss();
                    }
                }.obtainMessage(111, "parameter").sendToTarget();
            }
        });
        dialog.show();
    }

    public void dialogRename(File_DTO file_DTO, String str) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialogrename);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_OK);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt_Cancel);
        final EditText editText = (EditText) dialog.findViewById(R.id.edt_filename);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.img_delete);
        editText.requestFocus();
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(2, 0);
        final String path = file_DTO.getPath();
        new File(path);
        String name = new File(file_DTO.getPath()).getName();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().equals("") || editText.getText().length() == 0) {
                    Toast.makeText(Ultil.context, Ultil.context.getResources().getString(R.string.cannot_be_left_be_blank), Toast.LENGTH_SHORT).show();
                    return;
                }
                new AndroidXI(Ultil.context).renamewithpathAPI29(path, editText.getText().toString());
                Ultil.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"));
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CallbackUpdateMusic.getInstance().change();
                inputMethodManager.toggleSoftInput(1, 0);
                dialog.cancel();
            }
        });
        try {
            name = name.substring(0, name.lastIndexOf(FileUltils.HIDDEN_PREFIX));
        } catch (Exception unused) {
        }
        editText.setText("" + name);
        editText.setSelection(editText.getText().length());
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.toggleSoftInput(1, 0);
                dialog.cancel();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (editText.getText().toString().length() > 5) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }
        });
        dialog.show();
    }

    public void copyFile(String str) {
        String replaceAll = str.replaceAll("/", "%");
        File file = new File(MainActivity.getStore(context) + "/Bin", replaceAll);
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.fromFile(new File(str)));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MainActivity.copyStream(openInputStream, fileOutputStream);
            fileOutputStream.close();
            openInputStream.close();
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void shareimge(File_DTO file_DTO) {
        Uri uriForFile = FileProvider.getUriForFile(context, this.context.getPackageName()+".provider", new File(file_DTO.getPath()));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.setType("video/*");
        intent.setDataAndType(uriForFile, "image/*");
        context.startActivity(Intent.createChooser(intent, "Remi_FileManger"));
    }

    public void openFile(File file) {
        Uri.fromFile(file);
        Uri uriForFile = FileProvider.getUriForFile(context, this.context.getPackageName()+".provider", file);
        Intent intent = new Intent("android.intent.action.VIEW");
        if (file.toString().toLowerCase().contains(".apk") || file.toString().toLowerCase().contains(".apks")) {
            installAPK(file.getPath());
            return;
        }
        if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
            intent.setDataAndType(uriForFile, "application/msword");
        } else if (file.toString().contains(".pdf")) {
            intent.setDataAndType(uriForFile, "application/pdf");
        } else if (file.toString().contains(".ppt") || file.toString().contains(".pptx")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
        } else if (file.toString().contains(".xls") || file.toString().contains(".xlsx")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
        } else if (file.toString().contains(".zip") || file.toString().contains(".rar")) {
            intent.setDataAndType(uriForFile, "application/x-wav");
        } else if (file.toString().contains(".rtf")) {
            intent.setDataAndType(uriForFile, "application/rtf");
        } else if (file.toString().contains(".gif") || file.toString().contains(".jpg") || file.toString().contains(".png") || file.toString().contains(".tiff")) {
            intent.setDataAndType(uriForFile, "image/*");
        } else if (file.toString().contains(".txt")) {
            intent.setDataAndType(uriForFile, "text/plain");
        } else if (file.toString().endsWith(".mp3") || file.toString().endsWith(".wav") || file.toString().endsWith(".flac") || file.toString().endsWith(".wma") || file.toString().endsWith(".mp4a")) {
            intent.setDataAndType(uriForFile, "audio/*");
        } else if (file.toString().contains(".mp4") || file.toString().contains(".avi") || file.toString().contains(".mkv") || file.toString().contains(".vob") || file.toString().contains(".mov")) {
            openVideo(uriForFile);
            return;
        } else {
            intent.setDataAndType(uriForFile, "*/*");
        }
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(context, "No activity for action", Toast.LENGTH_SHORT).show();
        }
    }

    private void openVideo(Uri uri) {
        uri.getPath();
        Intent intent = new Intent();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(uri, "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public void installAPK(String str) {
        if (Build.VERSION.SDK_INT >= 26 && !context.getPackageManager().canRequestPackageInstalls()) {
            context.startActivity(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES").setData(Uri.parse(String.format("package:%s", context.getPackageName()))));
        }
        File file = new File(str);
        Uri uriForFile = FileProvider.getUriForFile(context, this.context.getPackageName()+".provider", file);
        if (file.exists()) {
            Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.getApplicationContext().startActivity(intent);
                return;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
                return;
            }
        }
        Toast.makeText(context, "installing", Toast.LENGTH_LONG).show();
    }

    private static Uri uriFromFile(File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "com.filemanager.fileexplorer.myfile.cleanmaster.free.provider", file);
        }
        return Uri.fromFile(file);
    }

    public void sharefile(File_DTO file_DTO) {
        String path = file_DTO.getPath();
        Uri uriForFile = FileProvider.getUriForFile(context, this.context.getPackageName()+".provider", new File(file_DTO.getPath()));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".tiff")) {
            intent.setDataAndType(uriForFile, "image/*");
        } else if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac") || path.endsWith(".wma") || path.endsWith(".mp4a")) {
            intent.setDataAndType(uriForFile, "audio/*");
        } else if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".mkv") || path.endsWith(".vob") || path.endsWith(".mov")) {
            intent.setDataAndType(uriForFile, "video/*");
        } else {
            intent.setDataAndType(uriForFile, "document/*");
        }
        context.startActivity(Intent.createChooser(intent, "Remi_FileManger"));
    }

    public void shareMultil_file(ArrayList<File_DTO> arrayList) {
        ArrayList<Uri> arrayList2 = new ArrayList<>();
        String str = "";
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(FileProvider.getUriForFile(context, this.context.getPackageName()+".provider", new File(arrayList.get(i).getPath())));
            str = arrayList.get(i).getPath();
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList2);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (str.endsWith(".jpg") || str.endsWith(".png") || str.endsWith(".gif") || str.endsWith(".tiff")) {
            intent.setType("image/*");
        } else if (str.endsWith(".mp3") || str.endsWith(".wav") || str.endsWith(".flac") || str.endsWith(".wma") || str.endsWith(".mp4a")) {
            intent.setType("audio/*");
        } else if (str.endsWith(".mp4") || str.endsWith(".avi") || str.endsWith(".mkv") || str.endsWith(".vob") || str.endsWith(".mov")) {
            intent.setType("video/*");
        } else {
            intent.setType("document/*");
        }
        context.startActivity(Intent.createChooser(intent, "Share File with My File Manager"));
    }

    public void delete(File_DTO file_DTO, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        AndroidXI androidXI = new AndroidXI(context);
        String path = file_DTO.getPath();
        if (path.contains(".png") || path.contains(".jpg") || path.contains(".gif") || path.equals(".tiff")) {
            androidXI.delete(activityResultLauncher, Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, file_DTO.getId()));
        } else if (path.contains(".mp3") || path.contains(".flac") || path.contains(".m4a") || path.contains("wav") || path.contains("vma")) {
            androidXI.delete(activityResultLauncher, Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, file_DTO.getId()));
        } else if (path.contains(".mp4") || path.contains(".avi") || path.contains(".mov") || path.contains(".mkv") || path.contains("vob")) {
            androidXI.delete(activityResultLauncher, Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, file_DTO.getId()));
        } else if (file_DTO.getId().equals("") || file_DTO.getId() == null) {
            new File(file_DTO.getPath()).delete();
        } else {
            androidXI.delete(activityResultLauncher, Uri.withAppendedPath(MediaStore.Files.getContentUri(file_DTO.getVolumname()), file_DTO.getId()));
        }
    }

    public void dialogdelete(final File_DTO file_DTO, final ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_setwallpaper);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        ((TextView) dialog.findViewById(R.id.txt_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ultil.this.copyFile(file_DTO.getPath());
                Ultil.this.delete(file_DTO, activityResultLauncher);
                CallbackUpdateMusic.getInstance().change();
                dialog.cancel();
                Toast.makeText(Ultil.context, "Moved to trash", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void dialogdeletewihtpath(final File_DTO file_DTO) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_setwallpaper);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_yes);
        TextView textView2 = (TextView) dialog.findViewById(R.id.titile);
        String lowerCase = file_DTO.getPath().toLowerCase();
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        if (lowerCase.contains(".mp3") || lowerCase.contains(".flac") || lowerCase.contains(".m4a") || lowerCase.contains("wav") || lowerCase.contains("vma")) {
            textView2.setText(R.string.titile_delete_thisaudio);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ultil.this.copyFile(file_DTO.getPath());
                Ultil.this.delewithcontentResolver(file_DTO);
                CallbackUpdateMusic.getInstance().change();
                dialog.cancel();
                Toast.makeText(Ultil.context, "Moved to trash", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void DeleteMultil(ArrayList<File_DTO> arrayList, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        for (int i = 0; i < arrayList.size(); i++) {
            copyFile(arrayList.get(i).getPath());
            delete(arrayList.get(i), activityResultLauncher);
        }
    }

    public Boolean addRecent(File_DTO file_DTO) {
        file_DTO.setLastmodified(String.valueOf(System.currentTimeMillis()));
        RecentAdd recentAdd = new RecentAdd(context);
        recentAdd.open();
        new ArrayList();
        ArrayList<File_DTO> allRows = recentAdd.getAllRows();
        boolean z = false;
        int i = 0;
        boolean z2 = false;
        while (true) {
            if (i >= allRows.size()) {
                z = z2;
                break;
            } else if (allRows.get(i).getPath().equals(file_DTO.getPath())) {
                break;
            } else {
                i++;
                z2 = true;
            }
        }
        if (z || allRows.size() == 0) {
            recentAdd.addRow(file_DTO);
        }
        recentAdd.close();
        return Boolean.valueOf(z);
    }

    public ArrayList<File_DTO> getallfileRecent() {
        RecentAdd recentAdd = new RecentAdd(context);
        recentAdd.open();
        new ArrayList();
        ArrayList<File_DTO> allRows = recentAdd.getAllRows();
        recentAdd.close();
        return allRows;
    }

    public void checkrecent_fileExist() {
        RecentAdd recentAdd = new RecentAdd(context);
        recentAdd.open();
        new ArrayList();
        ArrayList<File_DTO> allRows = recentAdd.getAllRows();
        for (int i = 0; i < allRows.size(); i++) {
            if (!new File(allRows.get(i).getPath()).exists()) {
                recentAdd.deleteRowByPath(allRows.get(i).getPath());
            }
        }
        recentAdd.close();
    }

    public void checkfav_fileExist() {
        FavoritSongs favoritSongs = new FavoritSongs(context);
        favoritSongs.open();
        new ArrayList();
        ArrayList<File_DTO> allRows = favoritSongs.getAllRows();
        for (int i = 0; i < allRows.size(); i++) {
            if (!new File(allRows.get(i).getPath()).exists()) {
                favoritSongs.deleteRowByPath(allRows.get(i).getPath());
            }
        }
        favoritSongs.close();
    }

    public void deletefav(String str) {
        FavoritSongs favoritSongs = new FavoritSongs(context);
        favoritSongs.open();
        favoritSongs.deleteRowByPath(str);
        favoritSongs.close();
    }

    public void intent_tree() {
        if (Build.VERSION.SDK_INT >= 29) {
            Intent createOpenDocumentTreeIntent = ((StorageManager) context.getSystemService(Context.STORAGE_SERVICE)).getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String uri = ((Uri) createOpenDocumentTreeIntent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString();
            Log.d(UStats.TAG, "INITIAL_URI scheme: " + uri);
            String replace = uri.replace("/root/", "/document/");
            String replace2 = "Android/data".replace("/", "%2F");
            createOpenDocumentTreeIntent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + replace2));
            createOpenDocumentTreeIntent.putExtra("android.content.extra.SHOW_ADVANCED", true);
            ((Activity) context).startActivityForResult(createOpenDocumentTreeIntent, 111);
        }
    }

    public static String getFullPathFromTreeUri(Uri uri, Context context2) {
        if (uri == null) {
            return null;
        }
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(uri), context2);
        if (volumePath == null) {
            return File.separator;
        }
        if (volumePath.endsWith(File.separator)) {
            volumePath = volumePath.substring(0, volumePath.length() - 1);
        }
        String documentPathFromTreeUri = getDocumentPathFromTreeUri(uri);
        if (documentPathFromTreeUri.endsWith(File.separator)) {
            documentPathFromTreeUri = documentPathFromTreeUri.substring(0, documentPathFromTreeUri.length() - 1);
        }
        if (documentPathFromTreeUri.length() > 0) {
            if (documentPathFromTreeUri.startsWith(File.separator)) {
                return volumePath + documentPathFromTreeUri;
            }
            return volumePath + File.separator + documentPathFromTreeUri;
        }
        return volumePath;
    }

    private static String getVolumePath(String str, Context context2) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            return getVolumePathForAndroid11AndAbove(str, context2);
        }
        return getVolumePathBeforeAndroid11(str, context2);
    }

    private static String getVolumePathBeforeAndroid11(String str, Context context2) {
        try {
            StorageManager storageManager = (StorageManager) context2.getSystemService(Context.STORAGE_SERVICE);
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getUuid", new Class[0]);
            Method method3 = cls.getMethod("getPath", new Class[0]);
            Method method4 = cls.getMethod("isPrimary", new Class[0]);
            Object invoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(invoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(invoke, i);
                String str2 = (String) method2.invoke(obj, new Object[0]);
                if (((Boolean) method4.invoke(obj, new Object[0])).booleanValue() && PRIMARY_VOLUME_NAME.equals(str)) {
                    return (String) method3.invoke(obj, new Object[0]);
                }
                if (str2 != null && str2.equals(str)) {
                    return (String) method3.invoke(obj, new Object[0]);
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    private static String getVolumePathForAndroid11AndAbove(String str, Context context2) {
        try {
            for (StorageVolume storageVolume : ((StorageManager) context2.getSystemService(Context.STORAGE_SERVICE)).getStorageVolumes()) {
                if (storageVolume.isPrimary() && PRIMARY_VOLUME_NAME.equals(str)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        return storageVolume.getDirectory().getPath();
                    }
                }
                String uuid = storageVolume.getUuid();
                if (uuid != null && uuid.equals(str)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        return storageVolume.getDirectory().getPath();
                    }
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    private static String getVolumeIdFromTreeUri(Uri uri) {
        String[] split = DocumentsContract.getTreeDocumentId(uri).split(":");
        if (split.length > 0) {
            return split[0];
        }
        return null;
    }

    private static String getDocumentPathFromTreeUri(Uri uri) {
        String[] split = DocumentsContract.getTreeDocumentId(uri).split(":");
        return (split.length < 2 || split[1] == null) ? File.separator : split[1];
    }

    public String getPath(Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if (PRIMARY_VOLUME_NAME.equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()));
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String str = split2[0];
                if ("image".equals(str)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str)) {
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String str2 = split2[1];
                return getDataColumn(uri2);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(uri);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Uri uri) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                        Log.d(UStats.TAG, "getDataColumn: " + columnIndexOrThrow);
                        String string = query.getString(columnIndexOrThrow);
                        if (query != null) {
                            query.close();
                        }
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
//            th = th2;
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public ArrayList<File_DTO> gethidefile() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        File file = new File(MainActivity.getStore(context) + "/remiImagehide");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                String absolutePath = listFiles[i].getAbsolutePath();
                long lastModified = listFiles[i].lastModified();
                String name = listFiles[i].getName();
                long length = listFiles[i].length();
                File_DTO file_DTO = new File_DTO();
                file_DTO.setId("1");
                file_DTO.setPath(absolutePath);
                file_DTO.setDate(new Ultil(context).getDate(lastModified));
                file_DTO.setName(name);
                file_DTO.setSize(new Ultil(context).bytesToHuman(length));
                arrayList.add(file_DTO);
                Log.d("TAG*", "gethidefile: " + arrayList.get(i).getPath());
            }
        }
        return arrayList;
    }

    public void createhidefile(File_DTO file_DTO) {
        String replaceAll = String.format("%s.txt", file_DTO.getPath()).replaceAll("/", "&");
        File file = new File(MainActivity.getStore(context) + "/remiImagehide", replaceAll);
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.fromFile(new File(file_DTO.getPath())));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MainActivity.copyStream(openInputStream, fileOutputStream);
            fileOutputStream.close();
            openInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public ArrayList<File_DTO> setRecylerView() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        File file = new File(MainActivity.getStore(context) + "/Bin");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                String absolutePath = listFiles[i].getAbsolutePath();
                long lastModified = listFiles[i].lastModified();
                String substring = listFiles[i].getName().substring(listFiles[i].getName().lastIndexOf("%") + 1);
                long length = listFiles[i].length();
                File_DTO file_DTO = new File_DTO();
                file_DTO.setPath(absolutePath);
                file_DTO.setDate(new Ultil(context).getDate(lastModified));
                file_DTO.setName(substring);
                file_DTO.setSize(new Ultil(context).bytesToHuman(length));
                arrayList.add(file_DTO);
            }
        }
        return arrayList;
    }

    public void restoreMultil(ArrayList<File_DTO> arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            File file = new File(arrayList.get(i).getPath());
            File file2 = new File(file.getName().replaceAll("&", "/").substring(0, file.getName().lastIndexOf("&")), file.getName().substring(file.getName().lastIndexOf("&") + 1, file.getName().lastIndexOf(FileUltils.HIDDEN_PREFIX)));
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                MainActivity.copyStream(fileInputStream, fileOutputStream);
                fileOutputStream.close();
                fileInputStream.close();
                file.delete();
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void restore(File_DTO file_DTO) {
        File file = new File(file_DTO.getPath());
        Log.d(UStats.TAG, "restore: " + file.getName());
        String replaceAll = file.getName().replaceAll("%", "/");
        replaceAll.substring(replaceAll.lastIndexOf("/") + 1);
        File file2 = new File(replaceAll);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            MainActivity.copyStream(fileInputStream, fileOutputStream);
            fileOutputStream.close();
            fileInputStream.close();
            file.delete();
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void restorefilehide(File_DTO file_DTO) {
        File file = new File(file_DTO.getPath());
        File file2 = new File(file.getName().replaceAll("&", "/").substring(0, file.getName().lastIndexOf("&")), file.getName().substring(file.getName().lastIndexOf("&") + 1, file.getName().lastIndexOf(FileUltils.HIDDEN_PREFIX)));
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            MainActivity.copyStream(fileInputStream, fileOutputStream);
            fileOutputStream.close();
            fileInputStream.close();
            file.delete();
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public ArrayList<File_DTO> videohide() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        if (gethidefile().size() > 0) {
            for (int i = 0; i < gethidefile().size(); i++) {
                String path = gethidefile().get(i).getPath();
                if (path.contains(".mp4") || path.contains(".avi") || path.contains(".mkv") || path.contains(".vob") || path.contains("mov")) {
                    arrayList.add(gethidefile().get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> imagehide() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < gethidefile().size(); i++) {
            String path = gethidefile().get(i).getPath();
            if (path.contains(".jpg") || path.contains(".png") || path.contains(".gif") || path.contains(".tiff")) {
                arrayList.add(gethidefile().get(i));
            }
        }
        return arrayList;
    }

    public void delewithcontentResolver(File_DTO file_DTO) {
        deletewithPath(new File(file_DTO.getPath()));
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"));
    }

    public void deleteSwithcontentResolver(ArrayList<File_DTO> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            copyFile(arrayList.get(i).getPath());
            if (!deletewithPath(new File(arrayList.get(i).getPath()))) {
                delewithcontentResolver(arrayList.get(i));
            }
        }
    }

    public boolean deletewithPath(File file) {
        String[] strArr = {file.getAbsolutePath()};
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(contentUri, "_data=?", strArr);
        if (file.exists()) {
            contentResolver.delete(contentUri, "_data=?", strArr);
        }
        return !file.exists();
    }

    public void deleteItemInBin() {
        File[] listFiles;
        File file = new File(MainActivity.getStore(context) + "/Bin");
        if (!file.isDirectory() || (listFiles = file.listFiles()) == null) {
            return;
        }
        for (int i = 0; i < listFiles.length; i++) {
            if ((listFiles[i].lastModified() - 1702967296) - System.currentTimeMillis() <= 0) {
                listFiles[i].delete();
            }
        }
    }

    public void check_dialog_recyclebin() {
        final Sharepre_Ulti sharepre_Ulti = new Sharepre_Ulti(context);
        if (sharepre_Ulti.readSharedPrefsInt("check_recycle", 1) == 1) {
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            dialog.setContentView(R.layout.dialog_titile);
            dialog.setCancelable(false);
            ((TextView) dialog.findViewById(R.id.txt_dismiss)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharepre_Ulti.writeSharedPrefs("check_recycle", 2);
                    dialog.cancel();
                }
            });
            dialog.show();
            return;
        }
        Log.d("TAG", "check_dialog_recyclebin:");
    }

    public ArrayList<File_DTO> getListfav() {
        new ArrayList();
        FavoritSongs favoritSongs = new FavoritSongs(context);
        favoritSongs.open();
        ArrayList<File_DTO> allRows = favoritSongs.getAllRows();
        favoritSongs.close();
        return allRows;
    }

    public float dpToPx(float f) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, Resources.getSystem().getDisplayMetrics());
    }
}
