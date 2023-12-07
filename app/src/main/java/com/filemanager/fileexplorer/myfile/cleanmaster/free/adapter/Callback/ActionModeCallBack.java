package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Callback;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.ResulfActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileOperations;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.GirlAdapter.GridAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.MyRecyclerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ActionModeCallBack implements ActionMode.Callback {
    private static final int PRIORITY_DEFAULT = 111;
    private MyRecyclerAdapter adapter;
    private AlertDialog alertDialog1;
    private TextView b1;
    private TextView b2;
    private Context context;
    boolean cut;
    private Data_Manager data_manager;
    private EditText editText;
    FileOperations fileOperations = new FileOperations();
    GridAdapter gadapter;
    Handler handler;
    private int sortFlags;
    private List<DocumentFile> source_doc;
    private List<File> sources;

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    public ActionModeCallBack(MyRecyclerAdapter myRecyclerAdapter, Context context, Data_Manager data_Manager, int i) {
        this.adapter = myRecyclerAdapter;
        this.context = context;
        this.data_manager = data_Manager;
        this.sortFlags = i;
        this.handler = new Handler(context.getMainLooper());
    }

    public ActionModeCallBack(GridAdapter gridAdapter, Context context, Data_Manager data_Manager, int i) {
        this.gadapter = gridAdapter;
        this.context = context;
        this.data_manager = data_Manager;
        this.sortFlags = i;
        this.handler = new Handler(context.getMainLooper());
    }

    public static DocumentFile getDocumentFile(File file) {
        String[] split = file.getPath().substring(ResulfActivity.externalSD_root.getPath().length() + 1).split("/");
        DocumentFile documentFile = ResulfActivity.permadDocumentFile;
        for (String str : split) {
            DocumentFile findFile = documentFile.findFile(str);
            if (findFile != null) {
                documentFile = findFile;
            }
        }
        return documentFile;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
        if (ResulfActivity.favourites) {
            menu.findItem(R.id.remove).setVisible(true);
        } else {
            menu.findItem(R.id.remove).setVisible(false);
        }
        this.source_doc = new ArrayList();
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
        File file;
        boolean z;
        switch (menuItem.getItemId()) {
            case R.id.copy:
                ResulfActivity.isPasteMode = true;
                actionMode.getMenu().clear();
                actionMode.setTitle(Environment.getExternalStorageDirectory().getPath());
                if (ResulfActivity.gridView) {
                    this.sources = this.gadapter.getSelectedItemsFile();
                    this.gadapter.clearSelection();
                } else {
                    this.sources = this.adapter.getSelectedItemsFile();
                    this.adapter.clearSelection();
                }
                ResulfActivity.isSelection = false;
                if (!ResulfActivity.sdCardmode) {
                    ResulfActivity.path = Environment.getExternalStorageDirectory();
                    this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), this.sortFlags);
                    if (ResulfActivity.gridView) {
                        this.gadapter.notifyDataSetChanged();
                    } else {
                        this.adapter.notifyDataSetChanged();
                    }
                } else {
                    ResulfActivity.path = ResulfActivity.externalSD_root;
                    this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), this.sortFlags);
                    if (ResulfActivity.gridView) {
                        this.gadapter.notifyDataSetChanged();
                    } else {
                        this.adapter.notifyDataSetChanged();
                    }
                    ResulfActivity.documentFile = ResulfActivity.permadDocumentFile;
                }
                ResulfActivity.collections = false;
                actionMode.getMenuInflater().inflate(R.menu.paste_menu, actionMode.getMenu());
                break;
            case R.id.cut:
                this.cut = true;
                ResulfActivity.isPasteMode = true;
                actionMode.getMenu().clear();
                actionMode.setTitle(Environment.getExternalStorageDirectory().getPath());
                if (ResulfActivity.gridView) {
                    this.sources = this.gadapter.getSelectedItemsFile();
                    this.gadapter.clearSelection();
                } else {
                    this.sources = this.adapter.getSelectedItemsFile();
                    this.adapter.clearSelection();
                }
                ResulfActivity.isSelection = false;
                if (!ResulfActivity.sdCardmode) {
                    ResulfActivity.path = Environment.getExternalStorageDirectory();
                    this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), this.sortFlags);
                    if (ResulfActivity.gridView) {
                        this.gadapter.notifyDataSetChanged();
                    } else {
                        this.adapter.notifyDataSetChanged();
                    }
                } else {
                    ResulfActivity.path = ResulfActivity.externalSD_root;
                }
                this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), this.sortFlags);
                if (ResulfActivity.gridView) {
                    this.gadapter.notifyDataSetChanged();
                } else {
                    this.adapter.notifyDataSetChanged();
                }
                ResulfActivity.documentFile = ResulfActivity.permadDocumentFile;
                ResulfActivity.collections = false;
                actionMode.getMenuInflater().inflate(R.menu.paste_menu, actionMode.getMenu());
                break;
            case R.id.delete:
                new AlertDialog.Builder(this.context);
                if (ResulfActivity.gridView) {
                    this.gadapter.getSelectedItemCount();
                } else {
                    this.adapter.getSelectedItemCount();
                }
                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(this.context, new DeleteCallback() {
                    @Override
                    public void update() {
                        List<File> selectedItemsFile;
                        if (!ResulfActivity.gridView) {
                            selectedItemsFile = ActionModeCallBack.this.adapter.getSelectedItemsFile();
                        } else {
                            selectedItemsFile = ActionModeCallBack.this.gadapter.getSelectedItemsFile();
                        }
                        for (int i = 0; i < selectedItemsFile.size(); i++) {
                            new Ultil(ActionModeCallBack.this.context).copyFile(selectedItemsFile.get(i).getPath());
                            if (!ResulfActivity.sdCardmode && !ResulfActivity.collections) {
                                while (selectedItemsFile.get(i).exists()) {
                                    try {
                                        FileOperations.delete(selectedItemsFile.get(i));
                                    } catch (Exception unused) {
                                        Toast.makeText(ActionModeCallBack.this.context, "Sorry, unable to delete the file, don`t have permission", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else if (ResulfActivity.collections) {
                                if (ResulfActivity.whichCollection == 1) {
                                    ActionModeCallBack.this.context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{selectedItemsFile.get(i).getPath()});
                                }
                                if (ResulfActivity.whichCollection == 2) {
                                    ActionModeCallBack.this.context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "_data=?", new String[]{selectedItemsFile.get(i).getPath()});
                                }
                                if (ResulfActivity.whichCollection == 3) {
                                    ActionModeCallBack.this.context.getContentResolver().delete(MediaStore.Files.getContentUri("external"), "_data=?", new String[]{selectedItemsFile.get(i).getPath()});
                                }
                            } else {
                                ActionModeCallBack.getDocumentFile(selectedItemsFile.get(i)).delete();
                            }
                        }
                        if (ResulfActivity.collections) {
                            if (ResulfActivity.whichCollection == 1) {
                                ActionModeCallBack.this.data_manager.setImagesData();
                            }
                            if (ResulfActivity.whichCollection == 2) {
                                ActionModeCallBack.this.data_manager.setAudio(ActionModeCallBack.this.context);
                            }
                            if (ResulfActivity.whichCollection == 3) {
                                ActionModeCallBack.this.data_manager.setDocs();
                            }
                        }
                        if (!ResulfActivity.collections && !ResulfActivity.favourites) {
                            ActionModeCallBack.this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), ActionModeCallBack.this.sortFlags);
                        }
                        if (ResulfActivity.favourites) {
                            ActionModeCallBack.this.remove(actionMode);
                        }
                        if (!ResulfActivity.gridView) {
                            ActionModeCallBack.this.adapter.notifyDataSetChanged();
                        } else {
                            ActionModeCallBack.this.gadapter.notifyDataSetChanged();
                        }
                        actionMode.finish();
                    }
                });
                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                customDeleteDialog.show();
                break;
            case R.id.paste:
                ResulfActivity.collections = false;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, ResulfActivity.CHANNEL_ID);
                builder.setContentTitle("Copying Files").setSmallIcon(R.drawable.image_folder).setAutoCancel(true).setPriority(111);
                builder.setContentText("Copying files from " + this.sources.get(0).getName() + " to " + ResulfActivity.getCurrentPath().getName());
                builder.setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.image_folder));
                NotificationManagerCompat from = NotificationManagerCompat.from(this.context);
                builder.setContentTitle("Files copied sucessfully");
                builder.setContentText("100% completed");
                from.notify(1, builder.build());
                backGroundCopy(actionMode);
                if (this.cut) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < ActionModeCallBack.this.sources.size(); i++) {
                                if (!ResulfActivity.sdCardmode) {
                                    while (((File) ActionModeCallBack.this.sources.get(i)).exists()) {
                                        try {
                                            FileOperations.delete((File) ActionModeCallBack.this.sources.get(i));
                                            ActionModeCallBack.this.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile((File) ActionModeCallBack.this.sources.get(i))));
                                            MediaScannerConnection.scanFile(ActionModeCallBack.this.context, new String[]{((File) ActionModeCallBack.this.sources.get(i)).getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                                @Override
                                                public void onScanCompleted(String str, Uri uri) {
                                                    try {
                                                        ActionModeCallBack.this.context.getContentResolver().delete(uri, null, null);
                                                    } catch (Exception unused) {
                                                        Toast.makeText(ActionModeCallBack.this.context, ActionModeCallBack.this.context.getString(R.string.paste_tiitle), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } catch (Exception unused) {
                                            if (!ActionModeCallBack.getDocumentFile((File) ActionModeCallBack.this.sources.get(i)).delete()) {
                                                Toast.makeText(ActionModeCallBack.this.context, "Sorry, Could not delete the selected file", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } else {
                                    ActionModeCallBack.getDocumentFile((File) ActionModeCallBack.this.sources.get(i)).delete();
                                }
                            }
                            ActionModeCallBack.this.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    actionMode.finish();
                                    ActionModeCallBack.this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), ActionModeCallBack.this.sortFlags);
                                    if (!ResulfActivity.gridView) {
                                        ActionModeCallBack.this.adapter.notifyDataSetChanged();
                                    } else {
                                        ActionModeCallBack.this.gadapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }).start();
                }
                actionMode.finish();
                if (ResulfActivity.sdCardmode) {
                    this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), this.sortFlags);
                    if (ResulfActivity.gridView) {
                        this.gadapter.notifyDataSetChanged();
                    } else {
                        this.adapter.notifyDataSetChanged();
                    }
                }
                this.cut = false;
                break;
            case R.id.properties:
                Ultil ultil = new Ultil(this.context);
                File_DTO file_DTO = new File_DTO();
                if (ResulfActivity.gridView) {
                    file = this.gadapter.getSelectedItemsFile().get(0);
                } else {
                    file = this.adapter.getSelectedItemsFile().get(0);
                }
                file_DTO.setName(file.getName());
                file_DTO.setPath(file.getPath());
                file_DTO.setSize(ultil.bytesToHuman(file.length()));
                file_DTO.setDate(ultil.getDate(file.lastModified()));
                new Ultil(this.context).showdiloginfo(file_DTO, "");
                break;
            case R.id.remove:
                remove(actionMode);
                break;
            case R.id.rename:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this.context);
                builder2.setView(R.layout.dialogrename).setCancelable(false);
                AlertDialog create = builder2.create();
                this.alertDialog1 = create;
                create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                this.alertDialog1.show();
                this.editText = (EditText) this.alertDialog1.findViewById(R.id.edt_filename);
                if (ResulfActivity.gridView) {
                    this.editText.setText(this.gadapter.getSelectedItemsFile().get(0).getName());
                } else {
                    this.editText.setText(this.adapter.getSelectedItemsFile().get(0).getName());
                }
                this.editText.selectAll();
                this.b1 = (TextView) this.alertDialog1.findViewById(R.id.txt_Cancel);
                TextView textView = (TextView) this.alertDialog1.findViewById(R.id.txt_OK);
                this.b2 = textView;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean renameTo;
                        String obj = ActionModeCallBack.this.editText.getText().toString();
                        if (!ResulfActivity.sdCardmode && !ResulfActivity.collections) {
                            if (!ResulfActivity.gridView) {
                                if (!ActionModeCallBack.this.adapter.getSelectedItemsFile().get(0).renameTo(new File(ResulfActivity.getCurrentPath().getPath() + "/" + obj))) {
                                    Toast.makeText(ActionModeCallBack.this.context, "Invalid FileName", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (!ActionModeCallBack.this.gadapter.getSelectedItemsFile().get(0).renameTo(new File(ResulfActivity.getCurrentPath().getPath() + "/" + obj))) {
                                    Toast.makeText(ActionModeCallBack.this.context, "Invalid FileName", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (ResulfActivity.collections) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("_display_name", obj);
                            String[] strArr = ResulfActivity.gridView ? new String[]{ActionModeCallBack.this.gadapter.getSelectedItemsFile().get(0).getPath()} : new String[]{ActionModeCallBack.this.adapter.getSelectedItemsFile().get(0).getPath()};
                            if (ResulfActivity.whichCollection == 1) {
                                ActionModeCallBack.this.context.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues, "_data=?", strArr);
                            }
                            if (ResulfActivity.whichCollection == 2) {
                                ActionModeCallBack.this.context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues, "_data=?", strArr);
                            }
                            if (ResulfActivity.whichCollection == 3) {
                                ActionModeCallBack.this.context.getContentResolver().update(MediaStore.Files.getContentUri("external"), contentValues, "_data=?", strArr);
                            }
                        } else {
                            if (!ResulfActivity.gridView) {
                                renameTo = ActionModeCallBack.getDocumentFile(ActionModeCallBack.this.adapter.getSelectedItemsFile().get(0)).renameTo(obj);
                            } else {
                                renameTo = ActionModeCallBack.getDocumentFile(ActionModeCallBack.this.gadapter.getSelectedItemsFile().get(0)).renameTo(obj);
                            }
                            if (!renameTo) {
                                Toast.makeText(ActionModeCallBack.this.context, "Invalid FileName", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (ResulfActivity.favourites) {
                            ActionModeCallBack.this.remove(actionMode);
                        }
                        ActionModeCallBack.this.alertDialog1.cancel();
                        if (!ResulfActivity.gridView) {
                            ActionModeCallBack.this.adapter.clearSelection();
                        } else {
                            ActionModeCallBack.this.gadapter.clearSelection();
                        }
                        ResulfActivity.isPasteMode = false;
                        ResulfActivity.isSelection = false;
                        if (ResulfActivity.collections) {
                            if (ResulfActivity.whichCollection == 1) {
                                ActionModeCallBack.this.data_manager.setImagesData();
                            }
                            if (ResulfActivity.whichCollection == 2) {
                                ActionModeCallBack.this.data_manager.setAudio(ActionModeCallBack.this.context);
                            }
                            if (ResulfActivity.whichCollection == 3) {
                                ActionModeCallBack.this.data_manager.setDocs();
                            }
                        }
                        if (!ResulfActivity.collections && !ResulfActivity.favourites) {
                            ActionModeCallBack.this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), ActionModeCallBack.this.sortFlags);
                        }
                        if (!ResulfActivity.gridView) {
                            ActionModeCallBack.this.adapter.notifyDataSetChanged();
                        } else {
                            ActionModeCallBack.this.gadapter.notifyDataSetChanged();
                        }
                        actionMode.finish();
                    }
                });
                this.b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActionModeCallBack.this.alertDialog1.cancel();
                    }
                });
                break;
            case R.id.share:
                ArrayList<Uri> arrayList = new ArrayList<>();
                if (ResulfActivity.gridView) {
                    for (int i = 0; i < this.gadapter.getSelectedItemsFile().size(); i++) {
                        if (this.gadapter.getSelectedItemsFile().get(i).isDirectory()) {
                            Toast.makeText(this.context, "Sorry couldn`t share directories", Toast.LENGTH_SHORT).show();
                            z = false;
                        } else {
                            arrayList.add(FileProvider.getUriForFile(this.context, this.context.getPackageName()+".provider", this.gadapter.getSelectedItemsFile().get(i)));
                        }
                    }
                    z = true;
                } else {
                    for (int i2 = 0; i2 < this.adapter.getSelectedItemsFile().size(); i2++) {
                        if (this.adapter.getSelectedItemsFile().get(i2).isDirectory()) {
                            Toast.makeText(this.context, "Sorry couldn`t share directories", Toast.LENGTH_SHORT).show();
                            z = false;
                        } else {
                            arrayList.add(FileProvider.getUriForFile(this.context, this.context.getPackageName()+".provider", this.adapter.getSelectedItemsFile().get(i2)));
                        }
                    }
                    z = true;
                }
                if (z) {
                    try {
                        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                        intent.setType("images/*");
                        this.context.startActivity(Intent.createChooser(intent, "Send Via"));
                        break;
                    } catch (Exception unused) {
                        break;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (ResulfActivity.gridView) {
            this.gadapter.clearSelection();
        } else {
            this.adapter.clearSelection();
        }
        ResulfActivity.relativeLayout_head.setVisibility(View.VISIBLE);
        ResulfActivity.isPasteMode = false;
        ResulfActivity.isSelection = false;
        this.cut = false;
    }

    void remove(ActionMode actionMode) {
        int i = 0;
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("favourites", 0);
        HashSet hashSet = new HashSet(sharedPreferences.getStringSet("key", null));
        if (!ResulfActivity.gridView) {
            while (i < this.adapter.getSelectedItemCount()) {
                hashSet.remove(this.adapter.getSelectedItemsFile().get(i).getPath());
                i++;
            }
        } else {
            while (i < this.gadapter.getSelectedItemCount()) {
                hashSet.remove(this.gadapter.getSelectedItemsFile().get(i).getPath());
                i++;
            }
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet("key", hashSet);
        edit.apply();
        actionMode.finish();
        this.data_manager.setFavourites(this.context);
        if (ResulfActivity.gridView) {
            this.gadapter.notifyDataSetChanged();
        } else {
            this.adapter.notifyDataSetChanged();
        }
    }

    private void backGroundCopy(final ActionMode actionMode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ActionModeCallBack.this.sources.size(); i++) {
                    if (!ResulfActivity.sdCardmode) {
                        try {
                            FileOperations.copyFolder((File) ActionModeCallBack.this.sources.get(i), ResulfActivity.getCurrentPath());
                        } catch (IOException e) {
                            if (ResulfActivity.isExternalSD_available) {
                                ActionModeCallBack.this.fileOperations.pasteDoc((File) ActionModeCallBack.this.sources.get(i), ResulfActivity.getCurrentPath(), ActionModeCallBack.this.context);
                            }
                            e.printStackTrace();
                        }
                    } else if (ResulfActivity.sdCardmode) {
                        ActionModeCallBack.this.fileOperations.pasteDoc((File) ActionModeCallBack.this.sources.get(i), ResulfActivity.getCurrentPath(), ActionModeCallBack.this.context);
                    }
                }
                ActionModeCallBack.this.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        actionMode.finish();
                        ActionModeCallBack.this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), ActionModeCallBack.this.sortFlags);
                        if (!ResulfActivity.gridView) {
                            ActionModeCallBack.this.adapter.notifyDataSetChanged();
                        } else {
                            ActionModeCallBack.this.gadapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }
}
