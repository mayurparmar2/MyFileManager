package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FileUltils {
    public static final String AUTHORITY = "YOUR_AUTHORITY.provider";
    private static final boolean DEBUG = false;
    public static final String DOCUMENTS_DIR = "documents";
    public static final String HIDDEN_PREFIX = ".";
    static final String TAG = "FileUtils";

    private static void logDir(File file) {
    }

    public static String getExtension(String str) {
        if (str == null) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf(HIDDEN_PREFIX);
        return lastIndexOf >= 0 ? str.substring(lastIndexOf) : "";
    }

    public static boolean isLocal(String str) {
        return (str == null || str.startsWith("http://") || str.startsWith("https://")) ? false : true;
    }

    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    public static String getMimeType(File file) {
        String extension = getExtension(file.getName());
        return extension.length() > 0 ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1)) : "application/octet-stream";
    }

    public static String getMimeType(Context context, Uri uri) {
        return getMimeType(new File(getPath(context, uri)));
    }

    public static String getMimeType(Context context, String str) {
        String type = context.getContentResolver().getType(Uri.parse(str));
        return type == null ? "application/octet-stream" : type;
    }

    public static boolean isLocalStorageDocument(Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Files.FileColumns.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
//    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
//        Cursor cursor;
//        Cursor cursor2 = null;
//        try {
//            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
//            if (cursor != null) {
//                try {
//                    try {
//                        if (cursor.moveToFirst()) {
//                            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
//                            if (cursor != null) {
//                                cursor.close();
//                            }
//                            return string;
//                        }
//                    } catch (Exception e) {
//                        e = e;
//                        e.printStackTrace();
//                    }
//                } catch (Throwable th) {
//                    th = th;
//                    cursor2 = cursor;
//                    if (cursor2 != null) {
//                        cursor2.close();
//                    }
//                    throw th;
//                }
//            }
//        } catch (Exception e2) {
//            e = e2;
//            cursor = null;
//        } catch (Throwable th2) {
//            th = th2;
//            if (cursor2 != null) {
//            }
//            throw th;
//        }
//    }

    public static String getPath(Context context, Uri uri) {
        String localPath = getLocalPath(context, uri);
        return localPath != null ? localPath : uri.toString();
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getLocalPath(final Context context, final Uri uri) {

        if (DEBUG)
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                if (id != null && !id.startsWith("msf:")) {
                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads"
                    };

                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

//    private static String getLocalPath(Context context, Uri uri) {
//        String dataColumn;
//        Uri uri2 = null;
//        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
//            if (isLocalStorageDocument(uri)) {
//                return DocumentsContract.getDocumentId(uri);
//            }
//            if (isExternalStorageDocument(uri)) {
//                String[] split = DocumentsContract.getDocumentId(uri).split(":");
//                String str = split[0];
//                if ("primary".equalsIgnoreCase(str)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                } else if ("home".equalsIgnoreCase(str)) {
//                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
//                }
//            } else if (isDownloadsDocument(uri)) {
//                String documentId = DocumentsContract.getDocumentId(uri);
//                if (documentId != null && documentId.startsWith("raw:")) {
//                    return documentId.substring(4);
//                }
//                String[] strArr = {"content://downloads/public_downloads", "content://downloads/my_downloads"};
//                for (int i = 0; i < 2; i++) {
//                    try {
//                        dataColumn = getDataColumn(context, ContentUris.withAppendedId(Uri.parse(strArr[i]), Long.valueOf(documentId).longValue()), null, null);
//                    } catch (Exception unused) {
//                    }
//                    if (dataColumn != null) {
//                        return dataColumn;
//                    }
//                }
//                File generateFileName = generateFileName(getFileName(context, uri), getDocumentCacheDir(context));
//                if (generateFileName != null) {
//                    String absolutePath = generateFileName.getAbsolutePath();
//                    saveFileFromUri(context, uri, absolutePath);
//                    return absolutePath;
//                }
//                return null;
//            } else if (isMediaDocument(uri)) {
//                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
//                String str2 = split2[0];
//                if ("image".equals(str2)) {
//                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(str2)) {
//                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(str2)) {
//                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//                return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
//            } else if (isGoogleDriveUri(uri)) {
//                return getGoogleDriveFilePath(uri, context);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            if (isGooglePhotosUri(uri)) {
//                return uri.getLastPathSegment();
//            }
//            if (isGoogleDriveUri(uri)) {
//                return getGoogleDriveFilePath(uri, context);
//            }
//            return getDataColumn(context, uri, null, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }

    public static File getFile(Context context, Uri uri) {
        String path;
        if (uri == null || (path = getPath(context, uri)) == null || !isLocal(path)) {
            return null;
        }
        return new File(path);
    }

    public static File getDownloadsDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public static File getDocumentCacheDir(Context context) {
        File file = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(file);
        return file;
    }
    public static File generateFileName(String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }

        logDir(directory);

        return file;
    }
//    public static File generateFileName(String str, File file) {
//        String str2;
//        if (str == null) {
//            return null;
//        }
//        File file2 = new File(file, str);
//        if (file2.exists()) {
//            int lastIndexOf = str.lastIndexOf(46);
//            int i = 0;
//            if (lastIndexOf > 0) {
//                String substring = str.substring(0, lastIndexOf);
//                str2 = str.substring(lastIndexOf);
//                str = substring;
//            } else {
//                str2 = "";
//            }
//            while (file2.exists()) {
//                i++;
//                file2 = new File(file, str + '(' + i + ')' + str2);
//            }
//        }
//        try {
//            if (file2.createNewFile()) {
//                logDir(file);
//                return file2;
//            }
//            return null;
//        } catch (IOException e) {
//            Log.w(TAG, e);
//            return null;
//        }
//    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public static void saveFileFromUri(Context context, Uri uri, String str) {
//        BufferedOutputStream bufferedOutputStream;
//        InputStream openInputStream;
//        InputStream inputStream = null;
//        try {
//            try {
//                openInputStream = context.getContentResolver().openInputStream(uri);
//                try {
//                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str, false));
//                } catch (IOException e) {
//                    e = e;
//                    bufferedOutputStream = null;
//                } catch (Throwable th) {
//                    th = th;
//                    bufferedOutputStream = null;
//                }
//            } catch (IOException e2) {
//                e2.printStackTrace();
//                return;
//            }
//        } catch (IOException e3) {
//            e = e3;
//            bufferedOutputStream = null;
//        } catch (Throwable th2) {
//            th = th2;
//            bufferedOutputStream = null;
//        }
//        try {
//            byte[] bArr = new byte[1024];
//            openInputStream.read(bArr);
//            do {
//                bufferedOutputStream.write(bArr);
//            } while (openInputStream.read(bArr) != -1);
//            if (openInputStream != null) {
//                openInputStream.close();
//            }
//            bufferedOutputStream.close();
//        } catch (IOException e4) {
//            e = e4;
//            inputStream = openInputStream;
//            try {
//                e.printStackTrace();
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (bufferedOutputStream != null) {
//                    bufferedOutputStream.close();
//                }
//            } catch (Throwable th3) {
//                th = th3;
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e5) {
//                        e5.printStackTrace();
//                        throw th;
//                    }
//                }
//                if (bufferedOutputStream != null) {
//                    bufferedOutputStream.close();
//                }
//                throw th;
//            }
//        } catch (Throwable th4) {
//            th = th4;
//            inputStream = openInputStream;
//            if (inputStream != null) {
//            }
//            if (bufferedOutputStream != null) {
//            }
//            throw th;
//        }
//    }


//    public static byte[] readBytesFromFile(String str) {
//        FileInputStream fileInputStream;
//        IOException e;
//        FileInputStream fileInputStream2 = null;
//        try {
//            try {
//                try {
//                    try {
//                        File file = new File(str);
//                        str = new byte[(int) file.length()];
//                        try {
//                            fileInputStream = new FileInputStream(file);
//                        } catch (IOException e2) {
//                            fileInputStream = null;
//                            e = e2;
//                        }
//                    } catch (IOException e3) {
//                        e3.printStackTrace();
//                    }
//                } catch (Throwable th) {
//                    th = th;
//                    if (fileInputStream2 != null) {
//                        try {
//                            fileInputStream2.close();
//                        } catch (IOException e4) {
//                            e4.printStackTrace();
//                        }
//                    }
//                    throw th;
//                }
//            } catch (IOException e5) {
//                fileInputStream = null;
//                e = e5;
//                str = null;
//            }
//            try {
//                fileInputStream.read(str);
//                fileInputStream.close();
//                str = str;
//            } catch (IOException e6) {
//                e = e6;
//                e.printStackTrace();
//                if (fileInputStream != null) {
//                    fileInputStream.close();
//                    str = str;
//                }
//                return str;
//            }
//            return str;
//        } catch (Throwable th2) {
//            th = th2;
//            fileInputStream2 = fileInputStream;
//        }
//    }

    public static File createTempImageFile(Context context, String str) throws IOException {
        return File.createTempFile(str, ".jpg", new File(context.getCacheDir(), DOCUMENTS_DIR));
    }
    public static String getFileName(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;
        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }


    public static String getName(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(str.lastIndexOf(47) + 1);
    }

    private static String getGoogleDriveFilePath(Uri uri, Context context) {
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        int columnIndex = query.getColumnIndex("_display_name");
        int columnIndex2 = query.getColumnIndex("_size");
        query.moveToFirst();
        String string = query.getString(columnIndex);
        Long.toString(query.getLong(columnIndex2));
        File file = new File(context.getCacheDir(), string);
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[Math.min(openInputStream.available(), 1048576)];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            openInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static String getNow() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
    }

    public static int getOrientationCode(String str) {
        try {
            return new ExifInterface(str).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int[] getArrayIntegerIds(String str) {
        String trim = str.trim();
        if (!trim.contains(" ")) {
            return trim.equals("") ? new int[0] : new int[]{Integer.parseInt(trim)};
        }
        String[] split = trim.split("\\s+");
        int[] iArr = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            iArr[i] = Integer.parseInt(split[i]);
        }
        return iArr;
    }

    public static Bitmap resize(Bitmap bitmap, int i, int i2) {
        if (i2 <= 0 || i <= 0) {
            return bitmap;
        }
        float width = bitmap.getWidth() / bitmap.getHeight();
        float f = i;
        float f2 = i2;
        if (f / f2 > width) {
            i = (int) (f2 * width);
        } else {
            i2 = (int) (f / width);
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    public static int dpToPx(float f, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, context.getResources().getDisplayMetrics());
    }

//
//    public static String getPathFromInputStreamUri(Context context, Uri uri) {
//        String str = null;
//        str = null;
//        str = null;
//        str = null;
//        str = null;
//        InputStream inputStream = null;
//        str = null;
//        try {
//            try {
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (uri.getAuthority() != null) {
//                try {
//                    uri = context.getContentResolver().openInputStream(uri);
//                    try {
//                        str = createTemporalFileFrom(uri, context).getPath();
//                    } catch (FileNotFoundException e2) {
//                        e = e2;
//                        e.printStackTrace();
//                        if (uri != 0) {
//                            uri.close();
//                            uri = uri;
//                        }
//                        return str;
//                    } catch (IOException e3) {
//                        e = e3;
//                        e.printStackTrace();
//                        if (uri != 0) {
//                            uri.close();
//                            uri = uri;
//                        }
//                        return str;
//                    }
//                } catch (FileNotFoundException e4) {
//                    e = e4;
//                    uri = 0;
//                } catch (IOException e5) {
//                    e = e5;
//                    uri = 0;
//                } catch (Throwable th) {
//                    th = th;
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e6) {
//                            e6.printStackTrace();
//                        }
//                    }
//                    throw th;
//                }
//                if (uri != 0) {
//                    uri.close();
//                    uri = uri;
//                }
//            }
//            return str;
//        } catch (Throwable th2) {
//            th = th2;
//            inputStream = uri;
//        }
//    }

    private static File createTemporalFileFrom(InputStream inputStream, Context context) throws IOException {
        if (inputStream == null) {
            return null;
        }
        byte[] bArr = new byte[8192];
        File createTemporalFile = createTemporalFile(context);
        FileOutputStream fileOutputStream = new FileOutputStream(createTemporalFile);
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                fileOutputStream.flush();
                try {
                    fileOutputStream.close();
                    return createTemporalFile;
                } catch (IOException e) {
                    e.printStackTrace();
                    return createTemporalFile;
                }
            }
        }
    }

    private static File createTemporalFile(Context context) {
        return new File(context.getCacheDir(), "tempPicture.jpg");
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

//    public static String getFilePath(Context context, Uri uri) {
//        Cursor cursor = null;
//        try {
//            Cursor query = context.getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
//            if (query != null) {
//                try {
//                    if (query.moveToFirst()) {
//                        String string = query.getString(query.getColumnIndexOrThrow("_display_name"));
//                        if (query != null) {
//                            query.close();
//                        }
//                        return string;
//                    }
//                } catch (Throwable th) {
//                    th = th;
//                    cursor = query;
//                    if (cursor != null) {
//                        cursor.close();
//                    }
//                    throw th;
//                }
//            }
//            if (query != null) {
//                query.close();
//            }
//            return null;
//        } catch (Throwable th2) {
//            th = th2;
//        }
//    }

    public static Bitmap modifyOrientation(Context context, Bitmap bitmap, String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt != 2) {
                if (attributeInt != 3) {
                    if (attributeInt != 4) {
                        if (attributeInt != 6) {
                            return attributeInt != 8 ? bitmap : rotate(bitmap, 270.0f);
                        }
                        return rotate(bitmap, 90.0f);
                    }
                    return flip(bitmap, false, true);
                }
                return rotate(bitmap, 180.0f);
            }
            return flip(bitmap, true, false);
        } catch (IOException unused) {
            return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean z, boolean z2) {
        Matrix matrix = new Matrix();
        matrix.preScale(z ? -1.0f : 1.0f, z2 ? -1.0f : 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public static ArrayList<File_DTO> getallfilewithMediaconetnt(Context context) {
        int i = 0;
        ArrayList arrayList = new ArrayList();
        Cursor query = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data", "_size", "date_modified"}, null, null, null);
        if (query != null && query.moveToFirst()) {
            do {
                File_DTO file_DTO = new File_DTO();
                String string = query.getString(query.getColumnIndexOrThrow("_data"));
                query.getLong(query.getColumnIndexOrThrow("_size"));
                long j = query.getLong(query.getColumnIndexOrThrow("date_modified"));
                file_DTO.setPath(string);
                file_DTO.setName(new File(string).getName());
                file_DTO.setDate(new Ultil(context).getDate(j));
                file_DTO.setSize(String.valueOf(new File(string).length()));
                arrayList.add(file_DTO);
            } while (query.moveToNext());
            ArrayList<File_DTO> arrayList2 = new ArrayList<>();
            while (i < arrayList.size()) {
            }
            return arrayList2;
        }
        ArrayList<File_DTO> arrayList22 = new ArrayList<>();
        for (i = 0; i < arrayList.size(); i++) {
            File file = new File(((File_DTO) arrayList.get(i)).getPath());
            int length = (int) new File(((File_DTO) arrayList.get(i)).getPath()).length();
            if (file.isFile() && length > 10485760) {
                arrayList22.add((File_DTO) arrayList.get(i));
            }
        }
        return arrayList22;
    }
}
