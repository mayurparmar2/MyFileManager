package com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil;

import android.content.Context;
import android.webkit.MimeTypeMap;

import androidx.documentfile.provider.DocumentFile;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Callback.ActionModeCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileOperations {
    File file;

    public static void delete(File file) throws Exception {
        if (file.isFile()) {
            boolean able = file.delete();
            if (!able)
                throw new Exception();
        } else {
            File[] files = file.listFiles();
            if (files.length == 0) {
                boolean able = file.delete();
                if (!able)
                    throw new Exception();
            }
            for (File file1 : files) {
                if (file1.isDirectory())
                    delete(file1);
                else {
                    file1.delete();
                }
            }
        }
    }

    public static String mime(String URI) {
        String type = null;
        String extention = MimeTypeMap.getFileExtensionFromUrl(URI);
        if (extention != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention);
        }
        return type;
    }

    public static void copyFolder(File src, File dest)
            throws IOException {

        if (src.isDirectory()) {
            dest = new File(dest, src.getName());
            if (!dest.exists()) {
                dest.mkdirs();
            }

            String files[] = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                copyFolder(srcFile, dest);
            }

        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(new File(dest, src.getName()));
            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    public void pasteDoc(File source, File destination_Path, Context context) {
        if (source.isFile()) {
            copy(source, destination_Path, context);
        } else {
            ActionModeCallBack.getDocumentFile(destination_Path).createDirectory(source.getName());
            File createdDir = new File(destination_Path, source.getName());
            File[] content = source.listFiles();
            for (int i = 0; i < content.length; i++) {
                try {
                    if (content[i].isFile())
                        copy(content[i], createdDir, context);
                    else
                        pasteDoc(content[i], createdDir, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    File file;
//
//    public static void delete(File file) throws Exception {
//        if (file.isFile()) {
//            if (!file.delete()) {
//                throw new Exception();
//            }
//            return;
//        }
//        File[] listFiles = file.listFiles();
//        if (listFiles.length == 0 && !file.delete()) {
//            throw new Exception();
//        }
//        for (File file2 : listFiles) {
//            if (file2.isDirectory()) {
//                delete(file2);
//            } else {
//                file2.delete();
//            }
//        }
//    }
//
//    public static String mime(String str) {
//        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
//        if (fileExtensionFromUrl != null) {
//            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
//        }
//        return null;
//    }
//
//    public static void copyFolder(File file, File file2) throws IOException {
//        if (file.isDirectory()) {
//            File file3 = new File(file2, file.getName());
//            if (!file3.exists()) {
//                file3.mkdirs();
//            }
//            for (String str : file.list()) {
//                copyFolder(new File(file, str), file3);
//            }
//            return;
//        }
//        FileInputStream fileInputStream = new FileInputStream(file);
//        FileOutputStream fileOutputStream = new FileOutputStream(new File(file2, file.getName()));
//        byte[] bArr = new byte[1024];
//        while (true) {
//            int read = fileInputStream.read(bArr);
//            if (read > 0) {
//                fileOutputStream.write(bArr, 0, read);
//            } else {
//                fileInputStream.close();
//                fileOutputStream.close();
//                return;
//            }
//        }
//    }
//
//    public void pasteDoc(File file, File file2, Context context) {
//        if (file.isFile()) {
//            copy(file, file2, context);
//            return;
//        }
//        ActionModeCallBack.getDocumentFile(file2).createDirectory(file.getName());
//        File file3 = new File(file2, file.getName());
//        File[] listFiles = file.listFiles();
//        for (int i = 0; i < listFiles.length; i++) {
//            try {
//                if (listFiles[i].isFile()) {
//                    copy(listFiles[i], file3, context);
//                } else {
//                    pasteDoc(listFiles[i], file3, context);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public boolean copy(File copy, File directory, Context con) {
        FileInputStream inStream = null;
        OutputStream outStream = null;
        DocumentFile dir = ActionModeCallBack.getDocumentFile(directory);
        String mime = mime(copy.toURI().toString());
        DocumentFile copy1 = dir.createFile(mime, copy.getName());
        try {
            inStream = new FileInputStream(copy);
            outStream = con.getContentResolver().openOutputStream(copy1.getUri());
            byte[] buffer = new byte[inStream.available()];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                outStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
//    public boolean copy(File file, File file2, Context context) {
//        OutputStream outputStream;
//        DocumentFile createFile = ActionModeCallBack.getDocumentFile(file2).createFile(mime(file.toURI().toString()), file.getName());
//        FileInputStream fileInputStream = null;
//        Object r1 = null;
//        OutputStream outputStream2 = null;
//        fileInputStream = null;
//        try {
//            try {
//                FileInputStream fileInputStream2 = new FileInputStream(file);
//                try {
//                    outputStream2 = context.getContentResolver().openOutputStream(createFile.getUri());
//                    byte[] bArr = new byte[fileInputStream2.available()];
//                    while (true) {
//                        int read = fileInputStream2.read(bArr);
//                        if (read != -1) {
//                            outputStream2.write(bArr, 0, read);
//                        } else {
//                            fileInputStream2.close();
//                            outputStream2.close();
//                            return true;
//                        }
//                    }
//                } catch (IOException e) {
//                    e = e;
//                    outputStream = outputStream2;
//                    fileInputStream = fileInputStream2;
//                    try {
//                        e.printStackTrace();
//                        fileInputStream.close();
//                        outputStream.close();
//                        return true;
//                    } catch (Throwable th) {
//                        th = th;
//                        try {
//                            fileInputStream.close();
//                            outputStream.close();
//                            return true;
//                        } catch (IOException e2) {
//                            e2.printStackTrace();
//                            throw th;
//                        }
//                    }
//                } catch (Throwable th2) {
//                    outputStream = outputStream2;
//                    fileInputStream = fileInputStream2;
//                    fileInputStream.close();
//                    outputStream.close();
//                    return true;
//                }
//            } catch (IOException e3) {
//                e3.printStackTrace();
//                return false;
//            }
//        } catch (IOException e4) {
//            outputStream = null;
//        } catch (Throwable th3) {
//            outputStream = null;
//        }
//        return false;
//    }
}
