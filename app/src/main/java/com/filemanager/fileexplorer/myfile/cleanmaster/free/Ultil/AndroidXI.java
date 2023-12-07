package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class AndroidXI {
    private Context context;

    public AndroidXI(Context context) {
        this.context = context;
    }

    public Uri create(String str, String str2, String str3, String str4) {
        ContentResolver contentResolver = this.context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str2);
        contentValues.put("mime_type", str3);
        if (Build.VERSION.SDK_INT >= 29) {
            contentValues.put("relative_path", str);
        }
        return contentResolver.insert(MediaStore.Files.getContentUri(str4), contentValues);
    }

    public void delete(ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, Uri uri) {
        ContentResolver contentResolver = this.context.getContentResolver();
        PendingIntent pendingIntent = null;
        try {
            contentResolver.delete(uri, null, null);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= 30) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(uri);
                try {
                    pendingIntent = MediaStore.createTrashRequest(contentResolver, arrayList, true);
                } catch (Exception unused) {
                    new File(uri.getPath()).delete();
                }
            } else if (Build.VERSION.SDK_INT == 29 && (e instanceof RecoverableSecurityException)) {
                pendingIntent = ((RecoverableSecurityException) e).getUserAction().getActionIntent();
            }
            if (pendingIntent != null) {
                activityResultLauncher.launch(new IntentSenderRequest.Builder(pendingIntent.getIntentSender()).build());
            }
        }
    }

    public void deleteList(ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, ArrayList<Uri> arrayList) {
        ContentResolver contentResolver = this.context.getContentResolver();
        int i = 0;
        while (true) {
            PendingIntent pendingIntent = null;
            try {
                if (i >= arrayList.size()) {
                    return;
                }
                contentResolver.delete(arrayList.get(i), null, null);
                i++;
            } catch (SecurityException e) {
                if (Build.VERSION.SDK_INT >= 30) {
                    pendingIntent = MediaStore.createDeleteRequest(contentResolver, arrayList);
                } else if (Build.VERSION.SDK_INT >= 29 && (e instanceof RecoverableSecurityException)) {
                    pendingIntent = ((RecoverableSecurityException) e).getUserAction().getActionIntent();
                }
                if (pendingIntent != null) {
                    activityResultLauncher.launch(new IntentSenderRequest.Builder(pendingIntent.getIntentSender()).build());
                    return;
                }
                return;
            }
        }
    }

    public void rename(String str, String str2, String str3) {
        Uri uriFromPath = getUriFromPath(this.context, new File(str3));
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str);
        if (Build.VERSION.SDK_INT >= 30) {
            this.context.getContentResolver().update(uriFromPath, contentValues, null);
            return;
        }
        String[] strArr = {str3};
        if (str2.equals("img")) {
            this.context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues, "_data=?", strArr);
        }
        if (str2.equals("video")) {
            this.context.getContentResolver().update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues, "_data=?", strArr);
        }
        if (str2.equals("music")) {
            this.context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues, "_data=?", strArr);
        }
        if (str2.equals("def")) {
            this.context.getContentResolver().update(MediaStore.Files.getContentUri("external"), contentValues, "_data=?", strArr);
        }
    }

    public void renamewithpathAPI29(String str, String str2) {
        File file = new File(str);
        String parent = file.getParent();
        String absolutePath = file.getAbsolutePath();
        String substring = absolutePath.substring(absolutePath.lastIndexOf(FileUltils.HIDDEN_PREFIX));
        File file2 = new File(parent + "/" + str2 + substring);
        if (!file2.getPath().equals(file.getPath())) {
            if (file.renameTo(file2)) {
                this.context.getApplicationContext().getContentResolver().delete(MediaStore.Files.getContentUri("external"), "_data=?", new String[]{file.getAbsolutePath()});
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2));
                intent.setData(Uri.fromFile(file2));
                this.context.getApplicationContext().sendBroadcast(intent);
                this.context.sendBroadcast(intent);
                Toast.makeText(this.context, "Done", Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str2);
            this.context.getContentResolver().update(MediaStore.Files.getContentUri("external"), contentValues, "_data=?", new String[]{str});
            this.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
            Toast.makeText(this.context, "Done", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this.context, "No change", Toast.LENGTH_SHORT).show();
    }

    private static boolean rename(File file, String str, boolean z) {
        String str2 = file.getParent() + "/" + str;
        if (file.getParentFile().canWrite()) {
            return file.renameTo(new File(str2));
        }
        return z;
    }

    public Uri duplicate(Uri uri) {
        ContentResolver contentResolver = this.context.getContentResolver();
        Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        try {
            FileInputStream fileInputStream = new FileInputStream(getPathFromUri(uri));
            OutputStream openOutputStream = contentResolver.openOutputStream(insert);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                openOutputStream.write(bArr, 0, read);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insert;
    }

    public String getPathFromUri(Uri uri) {
        Cursor query = this.context.getContentResolver().query(uri, null, null, null, null);
        String string = query.moveToNext() ? query.getString(query.getColumnIndex("_data")) : null;
        query.close();
        return string;
    }

    public void trash(ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, Uri uri, boolean z) {
        ContentResolver contentResolver = this.context.getContentResolver();
        if (Build.VERSION.SDK_INT >= 30) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(uri);
            PendingIntent createTrashRequest = MediaStore.createTrashRequest(contentResolver, arrayList, z);
            if (createTrashRequest != null) {
                activityResultLauncher.launch(new IntentSenderRequest.Builder(createTrashRequest.getIntentSender()).build());
            }
        }
    }

    public void trashList(ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, ArrayList<Uri> arrayList, boolean z) {
        PendingIntent createTrashRequest;
        ContentResolver contentResolver = this.context.getContentResolver();
        if (Build.VERSION.SDK_INT < 30 || (createTrashRequest = MediaStore.createTrashRequest(contentResolver, arrayList, z)) == null) {
            return;
        }
        activityResultLauncher.launch(new IntentSenderRequest.Builder(createTrashRequest.getIntentSender()).build());
    }

    public void renamealbum(String str, String str2) {
        String replace = str.replace("/" + new File(str).getName(), "");
        File file = new File(str);
        File file2 = new File(replace + "/" + str2);
        if (!file2.getPath().equals(file.getPath())) {
            if (file.renameTo(file2)) {
                this.context.getApplicationContext().getContentResolver().delete(MediaStore.Files.getContentUri("external"), "_data=?", new String[]{file.getPath()});
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(file2));
                this.context.getApplicationContext().sendBroadcast(intent);
                this.context.sendBroadcast(intent);
                Toast.makeText(this.context, "Done!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this.context, "Oops! rename failed", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this.context, "Oops! No change", Toast.LENGTH_SHORT).show();
    }

    public void dialogRename(File_DTO file_DTO) {
        final Dialog dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.dialogrename);
        dialog.getWindow().setLayout(-1, -2);
        final EditText editText = (EditText) dialog.findViewById(R.id.edt_filename);
        final File file = new File(file_DTO.getPath());
        String name = file.getName();
        editText.setText(name.substring(0, name.lastIndexOf(FileUltils.HIDDEN_PREFIX)));
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(5);
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.txt_OK)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String absolutePath = file.getParentFile().getAbsolutePath();
                String absolutePath2 = file.getAbsolutePath();
                String substring = absolutePath2.substring(absolutePath2.lastIndexOf(FileUltils.HIDDEN_PREFIX));
                File file2 = new File(absolutePath + "/" + editText.getText().toString() + substring);
                if (!file.renameTo(file2)) {
                    Toast.makeText(AndroidXI.this.context, "Oops! rename failed", Toast.LENGTH_SHORT).show();
                } else {
                    AndroidXI.this.context.getApplicationContext().getContentResolver().delete(MediaStore.Files.getContentUri("external"), "_data=?", new String[]{file.getAbsolutePath()});
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(file2));
                    AndroidXI.this.context.getApplicationContext().sendBroadcast(intent);
                    Toast.makeText(AndroidXI.this.context, "SuccessFull!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static Uri getUriFromPath(Context context, File file) {
        String absolutePath = file.getAbsolutePath();
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{absolutePath}, null);
        if (query != null && query.moveToFirst()) {
            int i = query.getInt(query.getColumnIndex("_id"));
            query.close();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            return Uri.withAppendedPath(uri, "" + i);
        } else if (file.exists()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", absolutePath);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            return null;
        }
    }

    public static String getPathFromUri(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else {
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str = split2[0];
                    if ("image".equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    String string = query.getString(query.getColumnIndexOrThrow("_data"));
                    if (query != null) {
                        query.close();
                    }
                    return string;
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
            return null;
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getFileName(String str) {
        return str.substring(str.lastIndexOf(47) + 1);
    }
}
