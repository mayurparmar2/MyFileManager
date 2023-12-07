package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.load.Key;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileUltils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class StaticTools {
    private static final int NOTIFICATION_DEFAULT_COLOR = -256;
    private static final int NOTIFICATION_DEFAULT_OFF = 4000;
    private static final int NOTIFICATION_DEFAULT_ON = 1000;
    public static final String TAG = "NotificationUtils";

    public static void notificatePush(Context context, int i, int i2, String str, String str2, String str3, Intent intent) {
        NotificationCompat.Builder ticker = new NotificationCompat.Builder(context).setSmallIcon(i2).setContentTitle(str2).setContentText(str3).setTicker(str);
        ticker.setContentIntent(PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ticker.setAutoCancel(true);
        ticker.setOnlyAlertOnce(true);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(i, ticker.build());
    }

    public static void notificatePermanentPush(Context context, int i, int i2, String str, String str2, String str3, Intent intent) {
        NotificationCompat.Builder ongoing = new NotificationCompat.Builder(context).setSmallIcon(i2).setContentTitle(str2).setContentText(str3).setCategory(NotificationCompat.CATEGORY_SERVICE).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setTicker(str).setOngoing(true);
        ongoing.setContentIntent(PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        ongoing.setAutoCancel(true);
        ongoing.setOnlyAlertOnce(true);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(i, ongoing.build());
    }

    public static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getInternalDataPath(Context context) {
        return new ContextWrapper(context).getFilesDir().getPath();
    }

    public static boolean existsFile(String str) {
        File file = new File(str);
        return file.exists() && !file.isDirectory();
    }

    public static boolean existsFolder(String str) {
        File file = new File(str);
        return file.exists() && file.isDirectory();
    }

    public static boolean existsInternalStorageFile(Context context, String str) {
        return existsFile(getInternalDataPath(context) + str);
    }

    public static boolean existsSDFile(Context context, String str) {
        return existsFile(getSDPath() + str);
    }

    public static boolean deleteFile(String str) {
        File file = new File(str);
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        return file.delete();
    }

    public static StatFs getSDData() {
        return new StatFs(getSDPath());
    }

    public static File createTempFile(Context context, String str, String str2) {
        try {
            return File.createTempFile(str, str2, context.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copyAssetFileToCacheFile(Context context, String str, File file) {
        try {
            _copyAssetFile(new BufferedReader(new InputStreamReader(context.getAssets().open(str))), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyAssetFileToFileSystem(Context context, String str, String str2) {
        return copyAssetFileToFileSystem(context, str, new File(str2));
    }

    public static boolean copyAssetFileToFileSystem(Context context, String str, File file) {
        try {
            _copyAssetFile(new BufferedReader(new InputStreamReader(context.getAssets().open(str))), file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void _copyAssetFile(BufferedReader bufferedReader, File file) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(file));
            while (true) {
                try {
                    int read = bufferedReader.read();
                    if (read != -1) {
                        bufferedWriter2.write(read);
                    } else {
                        bufferedWriter2.close();
                        bufferedReader.close();
                        return;
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedWriter = bufferedWriter2;
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    bufferedReader.close();
                    throw th;
                }
            }
        } catch (Throwable th2) {
//            th = th2;
        }
    }

    public static String loadJSONFromAsset(Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String loadJSONFromFile(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append('\n');
                } else {
                    bufferedReader.close();
                    return sb.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileExtension(String str) {
        try {
            return str.substring(str.lastIndexOf(FileUltils.HIDDEN_PREFIX) + 1);
        } catch (Exception unused) {
            return "";
        }
    }

    public static String loadTextFile(String str) throws IOException {
        File file = new File(str);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                        sb.append('\n');
                    } else {
                        bufferedReader2.close();
                        return sb.toString();
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    bufferedReader.close();
                    throw th;
                }
            }
        } catch (Throwable th2) {
//            th = th2;
        }
        return str;
    }

    public static void writeTextFile(String str, String str2) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(str));
            try {
                bufferedWriter2.write(str2);
                bufferedWriter2.close();
            } catch (Throwable th) {
                th = th;
                bufferedWriter = bufferedWriter2;
                bufferedWriter.close();
                throw th;
            }
        } catch (Throwable th2) {
//            th = th2;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static List<PackageInfo> getSystemApps(Context context, List<PackageInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            PackageInfo packageInfo = list.get(i);
            if ((packageInfo.applicationInfo.flags & 129) != 0) {
                arrayList.add(packageInfo);
            }
        }
        return arrayList;
    }

    public static List<PackageInfo> getNonSystemApps(Context context, List<PackageInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            PackageInfo packageInfo = list.get(i);
            if ((packageInfo.applicationInfo.flags & 129) == 0) {
                arrayList.add(packageInfo);
            }
        }
        return arrayList;
    }

    public static void logPackageNames(List<PackageInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            Log.d("Package", list.get(i).packageName);
        }
    }

    public static List<PackageInfo> getApps(Context context, int i) {
        return context.getPackageManager().getInstalledPackages(i);
    }

    public static String getAppNameFromPackage(Context context, String str) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128));
        } catch (PackageManager.NameNotFoundException unused) {
            return "Unkown app";
        }
    }

    public static Drawable getIconFromPackage(String str, Context context) {
        try {
            return context.getPackageManager().getApplicationIcon(str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkIfAppWasInstalledThroughGooglePlay(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return "com.android.vending".equals(packageManager.getInstallerPackageName(packageManager.getApplicationInfo(str, 0).packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkIfUSBDebugIsEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "adb_enabled", 0) == 1;
    }

    public static ActivityInfo[] getActivitiesInPackage(Context context, String str, int i) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context, str, i).activities;
    }

    public static PackageInfo getPackageInfo(Context context, String str, int i) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(str, i);
    }

    public static boolean packageInfoHasPermission(PackageInfo packageInfo, String str) {
        if (packageInfo.requestedPermissions == null) {
            return false;
        }
        for (String str2 : packageInfo.requestedPermissions) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfUnknownAppIsEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "install_non_market_apps", 0) == 1;
    }

    public static void openSecuritySettings(Context context) {
        context.startActivity(new Intent("android.settings.SECURITY_SETTINGS"));
    }

    public static void openDeveloperSettings(Context context) {
        context.startActivity(new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS"));
    }

    public static String fillParams(String str, String str2, String... strArr) {
        int i = 0;
        while (i < strArr.length) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            int i2 = i + 1;
            sb.append(i2);
            str = str.replace(sb.toString(), strArr[i]);
            i = i2;
        }
        return str;
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String hexStrToStr(String str) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < str.length()) {
            int i2 = i + 2;
            sb.append((char) Integer.parseInt(str.substring(i, i2), 16));
            i = i2;
        }
        return sb.toString();
    }

    public static String mixUp(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer(str);
        for (int i2 = 0; i2 < str.length() - i; i2++) {
            char charAt = stringBuffer.charAt(i2);
            int i3 = i2 + i;
            stringBuffer.setCharAt(i2, stringBuffer.charAt(i3));
            stringBuffer.setCharAt(i3, charAt);
        }
        return stringBuffer.toString();
    }

    public static String unMixUp(String str, int i) {
        StringBuffer stringBuffer = new StringBuffer(str);
        for (int length = str.length() - 1; length >= i; length--) {
            char charAt = stringBuffer.charAt(length);
            int i2 = length - i;
            stringBuffer.setCharAt(length, stringBuffer.charAt(i2));
            stringBuffer.setCharAt(i2, charAt);
        }
        return stringBuffer.toString();
    }

    public static String padRight(String str, int i, char c) {
        return String.format("%1$-" + i + "s", str).replace(' ', c);
    }

    public static String padLeft(String str, int i, char c) {
        return String.format("%1$" + i + "s", str).replace(' ', c);
    }

    public static void convertFileSizeToString(long j, String[] strArr) {
        if (strArr.length != 2) {
            throw new IllegalArgumentException("output parts must be an array of length 2");
        }
        String[] strArr2 = {"b", "Kb", "Mb", "Gb", "Tb", "Pb"};
        if (j <= 0) {
            strArr[0] = "0";
            strArr[1] = "Kb";
            return;
        }
        double d = j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        if (log10 > 5) {
            strArr[0] = "Too big";
            strArr[1] = "";
            return;
        }
        strArr[0] = new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, log10));
        strArr[1] = strArr2[log10];
    }

    public static String convertFileSizeToString(long j) {
        double d = 0;
        int log10 = 0;
        String[] strArr = {"b", "Kb", "Mb", "Gb", "Tb", "Pb"};
        if (j <= 0) {
            return "0Kb";
        }
        if (((int) (Math.log10(j) / Math.log10(1024.0d))) > 5) {
            return "Too big";
        }
        return new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, log10)) + strArr[log10];
    }

    public static boolean stringMatchesMask(String str, String str2) {
        boolean z;
        if (str2.charAt(str2.length() - 1) == '*') {
            str2 = str2.substring(0, str2.length() - 2);
            z = true;
        } else {
            z = false;
        }
        return z ? str.startsWith(str2) : str.equals(str2);
    }

    public static void setViewBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.service.getClassName().equals(cls.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void openMarketURL(Context context, String str, String str2) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str2)));
        }
    }

    public static <T> T deserializeFromFile(String str) throws FileNotFoundException, IOException {
        T t = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            t = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return t;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return t;
        }
    }

    public static <T> T deserializeFromDataFolder(Context context, String str) {
        try {
            String str2 = getInternalDataPath(context) + File.separatorChar + str;
            if (existsFile(str2)) {
                return (T) deserializeFromFile(str2);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void serializeToFile(String str, Serializable serializable) throws IOException {
        String parent = new File(str).getParent();
        File file = new File(parent);
        if (parent != null && !existsFolder(parent)) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(serializable);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static void serializeToDataFolder(Context context, Serializable serializable, String str) throws IOException {
        String internalDataPath = getInternalDataPath(context);
        serializeToFile(internalDataPath + File.separatorChar + str, serializable);
    }
}
