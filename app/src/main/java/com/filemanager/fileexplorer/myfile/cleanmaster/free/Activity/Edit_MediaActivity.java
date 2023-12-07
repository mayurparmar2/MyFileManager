package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.FragmentActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.BackgroundTask;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackFav;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackNewFile;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateFavourite;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateHideFile;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateInternal;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdatehide;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdate;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdatealbum;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.HandleLooper;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.AndroidXI;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileUltils;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.BaseApdapterNewfile;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.FavoritSongs;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class Edit_MediaActivity extends AppCompatActivity {
    private File_DTO file_dto;
    private String hidefile;
    private ImageView imageView_giff;
    private ImageView img_back;
    private ImageView img_hide;
    private ImageView img_info;
    private ImageView img_like;
    private ImageView img_recycle;
    private ImageView img_setwall;
    private ImageView img_share;
    private ActivityResultLauncher<IntentSenderRequest> intentSenderRequestActivityResultLauncher;
    private String path;
    private int pos;
    private TextView textView_title;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_media);
        this.img_hide = (ImageView) findViewById(R.id.img_hide);
        this.img_like = (ImageView) findViewById(R.id.img_like);
        this.img_recycle = (ImageView) findViewById(R.id.img_recycle);
        this.img_share = (ImageView) findViewById(R.id.img_share);
        this.img_setwall = (ImageView) findViewById(R.id.img_setwall);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.img_info = (ImageView) findViewById(R.id.img_info);
        this.textView_title = (TextView) findViewById(R.id.txt_title);
        this.imageView_giff = (ImageView) findViewById(R.id.img_giff);
        Bundle extras = getIntent().getExtras();
        Objects.requireNonNull(extras);
        this.path = extras.getString("path");
        Bundle extras2 = getIntent().getExtras();
        Objects.requireNonNull(extras2);
        this.hidefile = extras2.getString("hidefile");
        Bundle extras3 = getIntent().getExtras();
        Objects.requireNonNull(extras3);
        this.pos = extras3.getInt("pos");
        this.file_dto = (File_DTO) getIntent().getSerializableExtra("file_dto");
        PhotoView photoView = (PhotoView) findViewById(R.id.photoview);
        photoView.setImageURI(Uri.parse(this.file_dto.getPath()));
        Glide.with((FragmentActivity) this).load(this.file_dto.getPath()).into(this.imageView_giff);
        if (this.file_dto.getPath().contains(".gif")) {
            photoView.setVisibility(View.GONE);
            this.imageView_giff.setVisibility(View.VISIBLE);
        } else {
            photoView.setVisibility(View.VISIBLE);
            this.imageView_giff.setVisibility(View.GONE);
        }
        this.textView_title.setText(this.file_dto.getName());
        this.img_setwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit_MediaActivity.this.dialogsetWall();
            }
        });
        this.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit_MediaActivity.this.shareimge();
            }
        });
        updateUI();
        this.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edit_MediaActivity.this.file_dto.getPath().toLowerCase().endsWith(".txt")) {
                    Edit_MediaActivity edit_MediaActivity = Edit_MediaActivity.this;
                    Toast.makeText(edit_MediaActivity, edit_MediaActivity.getResources().getString(R.string.hide_title_atention), Toast.LENGTH_SHORT).show();
                    return;
                }
                Edit_MediaActivity edit_MediaActivity2 = Edit_MediaActivity.this;
                if (!edit_MediaActivity2.checkfav(edit_MediaActivity2.file_dto)) {
                    Edit_MediaActivity edit_MediaActivity3 = Edit_MediaActivity.this;
                    edit_MediaActivity3.addFav(edit_MediaActivity3.file_dto);
                } else {
                    Edit_MediaActivity edit_MediaActivity4 = Edit_MediaActivity.this;
                    if (edit_MediaActivity4.delete(edit_MediaActivity4.file_dto)) {
                        Edit_MediaActivity edit_MediaActivity5 = Edit_MediaActivity.this;
                        Toast.makeText(edit_MediaActivity5, edit_MediaActivity5.getResources().getString(R.string.remove_from_fv), Toast.LENGTH_SHORT).show();
                    }
                    Edit_MediaActivity.this.updateUI();
                }
                try {
                    CallbackUpdateFavourite.getInstance().change();
                } catch (Exception unused) {
                    Log.d("AAAA", "onClick: ");
                }
            }
        });
        final DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                if (Edit_MediaActivity.this.hidefile.equals("recycle")) {
                    Edit_MediaActivity edit_MediaActivity = Edit_MediaActivity.this;
                    Toast.makeText(edit_MediaActivity, edit_MediaActivity.getResources().getString(R.string.file_already_in_recycle), Toast.LENGTH_SHORT).show();
                } else {
                    Edit_MediaActivity edit_MediaActivity2 = Edit_MediaActivity.this;
                    edit_MediaActivity2.copyFile(edit_MediaActivity2.file_dto.getPath());
                    new File(Edit_MediaActivity.this.file_dto.getPath()).delete();
                    if (!Edit_MediaActivity.this.hidefile.equals("hidefile")) {
                        if (Edit_MediaActivity.this.hidefile.equals("fav")) {
                            new Ultil(Edit_MediaActivity.this).deletefav(Edit_MediaActivity.this.file_dto.getPath());
                            CallbackFav.getInstance().changeState(new Ultil(Edit_MediaActivity.this).getListfav());
                        } else if (!Edit_MediaActivity.this.hidefile.equals("largefile")) {
                            if (!Edit_MediaActivity.this.hidefile.equals("download")) {
                                if (!Edit_MediaActivity.this.hidefile.equals("internal")) {
                                    if (!Edit_MediaActivity.this.hidefile.equals("newfile")) {
                                        if (Edit_MediaActivity.this.hidefile.equals("recent")) {
                                            new Ultil(Edit_MediaActivity.this).checkrecent_fileExist();
                                            CallbackFav.getInstance().changeState(new Ultil(Edit_MediaActivity.this).getallfileRecent());
                                        } else {
                                            Edit_MediaActivity edit_MediaActivity3 = Edit_MediaActivity.this;
                                            Toast.makeText(edit_MediaActivity3, edit_MediaActivity3.getResources().getString(R.string.file_already_in_recycle), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        CallbackNewFile.getInstance().changeState(new BaseApdapterNewfile(Edit_MediaActivity.this).getSave_list());
                                    }
                                } else {
                                    CallbackUpdateInternal.getInstance().change();
                                }
                            } else {
                                CallbackFav.getInstance().changeState(new Data_Manager(Edit_MediaActivity.this).filesDowload());
                            }
                        } else {
                            CallbackFav.getInstance().changeState(FileUltils.getallfilewithMediaconetnt(Edit_MediaActivity.this));
                        }
                    } else {
                        CallbackUpdateHideFile.getInstance().change();
                    }
                    Edit_MediaActivity.this.showToast();
                }
                Edit_MediaActivity.this.finish();
            }
        };
        this.img_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edit_MediaActivity.this.hidefile.equals("image")) {
                    Edit_MediaActivity.this.dialogdelete("delete");
                    return;
                }
                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(Edit_MediaActivity.this, deleteCallback);
                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                customDeleteDialog.show();
                if (Edit_MediaActivity.this.hidefile.equals("")) {
                    customDeleteDialog.setTextdelete();
                }
            }
        });
        if (this.hidefile.equals("hidefile")) {
            this.img_hide.setImageResource(R.drawable.icon_restore);
        } else {
            this.img_hide.setImageResource(R.drawable.edit_hide);
        }
        this.intentSenderRequestActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(Edit_MediaActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit_MediaActivity.this.finish();
                Animatoo.animateSwipeRight(Edit_MediaActivity.this);
            }
        });
        this.img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Ultil(Edit_MediaActivity.this).showdiloginfo(Edit_MediaActivity.this.file_dto, "def");
            }
        });
        final DeleteCallback deleteCallback2 = new DeleteCallback() {
            @Override
            public void update() {
                Edit_MediaActivity edit_MediaActivity = Edit_MediaActivity.this;
                edit_MediaActivity.createhidefile(edit_MediaActivity.file_dto);
                new Ultil(Edit_MediaActivity.this).delewithcontentResolver(Edit_MediaActivity.this.file_dto);
                if (Edit_MediaActivity.this.hidefile.equals("fav")) {
                    new Ultil(Edit_MediaActivity.this).deletefav(Edit_MediaActivity.this.file_dto.getPath());
                    CallbackFav.getInstance().changeState(new Ultil(Edit_MediaActivity.this).getListfav());
                } else if (!Edit_MediaActivity.this.hidefile.equals("largefile")) {
                    if (!Edit_MediaActivity.this.hidefile.equals("download")) {
                        if (!Edit_MediaActivity.this.hidefile.equals("internal")) {
                            if (!Edit_MediaActivity.this.hidefile.equals("newfile")) {
                                if (!Edit_MediaActivity.this.hidefile.equals("recycle")) {
                                    if (Edit_MediaActivity.this.hidefile.equals("recent")) {
                                        CallbackFav.getInstance().changeState(new Ultil(Edit_MediaActivity.this).getallfileRecent());
                                    }
                                } else {
                                    new File(Edit_MediaActivity.this.file_dto.getPath()).delete();
                                    CallbackFav.getInstance().changeState(new Ultil(Edit_MediaActivity.this).setRecylerView());
                                }
                            } else {
                                CallbackNewFile.getInstance().changeState(new BaseApdapterNewfile(Edit_MediaActivity.this).getSave_list());
                            }
                        } else {
                            CallbackUpdateInternal.getInstance().change();
                        }
                    } else {
                        CallbackFav.getInstance().changeState(new Data_Manager(Edit_MediaActivity.this).filesDowload());
                    }
                } else {
                    CallbackFav.getInstance().changeState(FileUltils.getallfilewithMediaconetnt(Edit_MediaActivity.this));
                }
                Edit_MediaActivity.this.finish();
            }
        };
        this.img_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Edit_MediaActivity.this.hidefile.equals("hidefile")) {
                    if (Edit_MediaActivity.this.hidefile.equals("download") || Edit_MediaActivity.this.hidefile.equals("largefile") || Edit_MediaActivity.this.hidefile.equals("fav") || Edit_MediaActivity.this.hidefile.equals("internal") || Edit_MediaActivity.this.hidefile.equals("recycle") || Edit_MediaActivity.this.hidefile.equals("recent") || Edit_MediaActivity.this.hidefile.equals("newfile")) {
                        Edit_MediaActivity.this.hidefile(deleteCallback2);
                        return;
                    } else {
                        Edit_MediaActivity.this.dialogdelete("hide");
                        return;
                    }
                }
                Edit_MediaActivity edit_MediaActivity = Edit_MediaActivity.this;
                edit_MediaActivity.restorefile(edit_MediaActivity.file_dto);
                CallbackUpdatehide.getInstance().change();
                Edit_MediaActivity.this.finish();
            }
        });
    }


    public void updateUI() {
        if (checkfav(this.file_dto)) {
            this.img_like.setColorFilter(0xffff0000);
        } else {
            this.img_like.setColorFilter(Color.parseColor("#737373"));
        }
    }


    public void dialogsetWall() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_setwallpaper);
        ((TextView) dialog.findViewById(R.id.titile)).setText(R.string.usethispictrueforwallpaper);
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final HandleLooper handleLooper = new HandleLooper() {
            @Override
            public void update() {
                Bitmap decodeFile = BitmapFactory.decodeFile(Edit_MediaActivity.this.file_dto.getPath());
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(Edit_MediaActivity.this.getApplicationContext());
                try {
                    Edit_MediaActivity edit_MediaActivity = Edit_MediaActivity.this;
                    wallpaperManager.setBitmap(FileUltils.modifyOrientation(edit_MediaActivity, decodeFile, edit_MediaActivity.file_dto.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                Edit_MediaActivity edit_MediaActivity2 = Edit_MediaActivity.this;
                Toast.makeText(edit_MediaActivity2, edit_MediaActivity2.getString(R.string.setted), Toast.LENGTH_SHORT).show();
            }
        };
        ((TextView) dialog.findViewById(R.id.txt_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask(Edit_MediaActivity.this).Handleloop(handleLooper);
            }
        });
        dialog.show();
    }


    public void hidefile(DeleteCallback deleteCallback) {
        CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(this, deleteCallback);
        customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        customDeleteDialog.show();
        customDeleteDialog.set_title_hide();
    }


    public void dialogdelete(final String str) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_setwallpaper);
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.txt_Cancel);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt_yes);
        TextView textView3 = (TextView) dialog.findViewById(R.id.titile);
        if (str.equals("delete")) {
            textView3.setText(R.string.edit_delete_photo);
            textView2.setText(R.string.delete);
        } else if (str.equals("hide")) {
            textView3.setText(R.string.titile_hide);
            textView2.setText(R.string.hide);
        } else {
            textView3.setText(R.string.delete_dialog_title);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str.equals("delete")) {
                    new AndroidXI(Edit_MediaActivity.this);
                    Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Edit_MediaActivity.this.file_dto.getId());
                    Edit_MediaActivity edit_MediaActivity = Edit_MediaActivity.this;
                    edit_MediaActivity.copyFile(edit_MediaActivity.file_dto.getPath());
                    new Ultil(Edit_MediaActivity.this).delewithcontentResolver(Edit_MediaActivity.this.file_dto);
                } else if (str.equals("hide")) {
                    new AndroidXI(Edit_MediaActivity.this);
                    Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Edit_MediaActivity.this.file_dto.getId());
                    Edit_MediaActivity edit_MediaActivity2 = Edit_MediaActivity.this;
                    edit_MediaActivity2.createhidefile(edit_MediaActivity2.file_dto);
                    new Ultil(Edit_MediaActivity.this).delewithcontentResolver(Edit_MediaActivity.this.file_dto);
                }
                Callbackupdate.getInstance().change();
                Callbackupdatealbum.getInstance().change();
                Edit_MediaActivity.this.showToast();
                Edit_MediaActivity.this.finish();
            }
        });
        dialog.show();
    }


    public void copyFile(String str) {
        String replaceAll = str.replaceAll("/", "%");
        File file = new File(MainActivity.getStore(this) + "/Bin", replaceAll);
        try {
            InputStream openInputStream = getContentResolver().openInputStream(Uri.fromFile(new File(str)));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MainActivity.copyStream(openInputStream, fileOutputStream);
            fileOutputStream.close();
            openInputStream.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    public void shareimge() {
        Uri uriForFile = FileProvider.getUriForFile(this, getPackageName()+".provider", new File(this.file_dto.getPath()));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.setType("video/*");
        intent.setDataAndType(uriForFile, "image/*");
        startActivity(Intent.createChooser(intent, "Remi_FileManger"));
    }


    public void addFav(File_DTO file_DTO) {
        FavoritSongs favoritSongs = new FavoritSongs(this);
        favoritSongs.open();
        favoritSongs.addRow(file_DTO);
        favoritSongs.close();
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        this.img_like.setColorFilter(0xffff0000);
    }


    public boolean checkfav(File_DTO file_DTO) {
        if (file_DTO != null) {
            FavoritSongs favoritSongs = new FavoritSongs(this);
            favoritSongs.open();
            ArrayList<File_DTO> allRows = favoritSongs.getAllRows();
            favoritSongs.close();
            boolean z = false;
            for (int i = 0; i < allRows.size(); i++) {
                if (file_DTO.getPath().equals(allRows.get(i).getPath())) {
                    z = true;
                }
            }
            return z;
        }
        return false;
    }


    public void createhidefile(File_DTO file_DTO) {
        String replaceAll = String.format("%s.txt", file_DTO.getPath()).replaceAll("/", "&");
        File file = new File(MainActivity.getStore(this) + "/remiImagehide", replaceAll);
        try {
            InputStream openInputStream = getContentResolver().openInputStream(Uri.fromFile(new File(file_DTO.getPath())));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            MainActivity.copyStream(openInputStream, fileOutputStream);
            fileOutputStream.close();
            openInputStream.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private ArrayList<File_DTO> restorefileorigin(ArrayList<File_DTO> arrayList) {
        ArrayList<File_DTO> arrayList2 = new ArrayList<>();
        File file = null;
        File file2 = null;
        int i = 0;
        while (i < arrayList.size()) {
            File_DTO file_DTO = new File_DTO();
            File file3 = new File(arrayList.get(i).getPath());
            String substring = file3.getName().replaceAll("&", "/").substring(0, file3.getName().length() - 4);
            String substring2 = file3.getName().substring(file3.getName().lastIndexOf("&") + 1, file3.getName().lastIndexOf(FileUltils.HIDDEN_PREFIX));
            long length = file3.length();
            long lastModified = file3.lastModified();
            file_DTO.setName(substring2);
            file_DTO.setPath(substring);
            file_DTO.setSize(new Ultil(this).bytesToHuman(length));
            file_DTO.setDate(new Ultil(this).getDate(lastModified));
            arrayList2.add(file_DTO);
            i++;
            file2 = new File(substring, substring2);
            file = file3;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            new MainActivity();
            MainActivity.copyStream(fileInputStream, fileOutputStream);
            fileOutputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return arrayList2;
    }


    public void restorefile(File_DTO file_DTO) {
        File file = new File(file_DTO.getPath());
        File file2 = new File(file.getName().replaceAll("&", "/").substring(0, file.getName().lastIndexOf("&")), file.getName().substring(file.getName().lastIndexOf("&") + 1, file.getName().lastIndexOf(FileUltils.HIDDEN_PREFIX)));
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            copyStream(fileInputStream, fileOutputStream);
            fileOutputStream.close();
            fileInputStream.close();
            file.delete();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
            MediaScannerConnection.scanFile(this, new String[]{file2.toString()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        showToast();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    }
                }).start();
                return;
            }
        }
    }


    public boolean delete(File_DTO file_DTO) {
        FavoritSongs favoritSongs = new FavoritSongs(this);
        favoritSongs.open();
        boolean deleteRowByPath = favoritSongs.deleteRowByPath(file_DTO.getPath());
        favoritSongs.close();
        return deleteRowByPath;
    }

    public void showToast() {
        View inflate = getLayoutInflater().inflate(R.layout.toast_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        ((TextView) inflate.findViewById(R.id.txt_title)).setText("Completed!");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(0);
        toast.setView(inflate);
        toast.show();
    }
}
