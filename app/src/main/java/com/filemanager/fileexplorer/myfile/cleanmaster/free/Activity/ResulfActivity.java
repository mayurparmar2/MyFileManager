package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
import androidx.core.os.EnvironmentCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.BackgroundTask;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackFav;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackNewFile;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateFavourite;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateHideFile;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateInternal;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMultilDelete;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateRecent;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdatehide;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdate;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdatealbum;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.HandleLooper;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateApp;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.updatelist;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Image_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.MusicUltil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Sharepre_Ulti;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.UStats;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.BaseApdapterNewfile;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Callback.ActionModeCallBack;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Callback.Listener_for_Recycler;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.DowloadApdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Favourite_Adapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.GirlAdapter.GridAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.ImageAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.ImageAlbumAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.MusicAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.MyRecyclerAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.NewFileAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Recent_Adapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.VideoAlbumAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.FavoritSongs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


public class ResulfActivity extends AppCompatActivity implements MusicAdapter.OnClick, ImageAlbumAdapter.OnClickImageAlbum, VideoAlbumAdapter.OnClickImageAlbum, Favourite_Adapter.OnClickItem, ImageAdapter.OnClickImageAlbum, Recent_Adapter.OnClickItem, DowloadApdapter.OnLongclick {
    public static final String CHANNEL_ID = "default";
    public static long audioSize = 0;
    public static boolean collections = false;
    public static long docsSize = 0;
    public static DocumentFile documentFile = null;
    public static File externalSD_root = null;
    public static boolean favourites = false;
    public static boolean gridView = false;
    public static long imagesSize = 0;
    public static boolean isExternalSD_available = false;
    public static boolean isPasteMode = false;
    public static boolean isSelection = false;
    public static File path = null;
    public static DocumentFile permadDocumentFile = null;
    public static RelativeLayout relativeLayout_head = null;
    public static boolean sdCardmode = false;
    public static long sdaudioSize = 0;
    public static long sddocsSize = 0;
    public static long sdimagesSize = 0;
    public static long sdvideoSize = 0;
    public static boolean searchMode = false;
    public static int sortFlag = 1;
    public static long videoSize;
    public static int whichCollection;
    private ActionMode actionMode;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    GridAdapter adapter;
    private BaseApdapterNewfile baseApdapterNewfile;
    private boolean check;
    private boolean checkchose;
    private Data_Manager data_manager;
    private DowloadApdapter dowloadApdapter;
    private EditText editText_search;
    private Favourite_Adapter favourite_adapter;
    private ArrayList<File_DTO> file_document;
    private ArrayList<File_DTO> file_dtos_image;
    private ArrayList<File_DTO> file_dtos_video;
    private ArrayList<File_DTO> fileshare;
    private LinearLayoutManager girdmanager;
    private ArrayList<File_DTO> hide_list;
    private ImageAdapter imageAdapter;
    private ImageAlbumAdapter imageAlbumAdapter;
    private ImageView imageView_back;
    private Image_Ultil image_ultil;
    private ImageView img_checklist;
    private ImageView img_choseall;
    private ImageView img_delete;
    private ImageView img_menu;
    private ImageView img_s;
    private ImageView img_share;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private LinearLayoutManager layoutManager;
    private LinearLayout linearLayout_check;
    private LinearLayout linearLayout_select;
    private MusicAdapter musicAdapter;
    private MusicUltil musicUltil;
    private MyRecyclerAdapter myRecyclerAdapter;
    private String name_key;
    private NewFileAdapter newFileAdapter;
    private Recent_Adapter recent_adapter;
    private RecyclerView recyclerView;
    private BackGroundSearch search;
    private TextView textView_title;
    private TextView txt_checklist;
    private TextView txt_count_file;
    private UpdateApp updateApp;
    private VideoAlbumAdapter videoAlbumAdapter;
    private Video_Ultil video_ultil;

    @Override
    public void menuclick(int i) {
    }

    @Override
    public void menuclickFr(int i) {
    }

    @Override
    public void menuclick_album(int i) {
    }

    @Override
    public void onLongclick() {
    }

    @Override
    public void onPointerCaptureChanged(boolean z) {
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_resulf);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview_result);
        this.imageView_back = (ImageView) findViewById(R.id.img_back);
        this.textView_title = (TextView) findViewById(R.id.title);
        this.img_checklist = (ImageView) findViewById(R.id.img_checklist);
        this.txt_checklist = (TextView) findViewById(R.id.txt_checklist);
        this.linearLayout_check = (LinearLayout) findViewById(R.id.check_layout_empty);
        this.linearLayout_select = (LinearLayout) findViewById(R.id.linear_select);
        this.img_delete = (ImageView) findViewById(R.id.img_delete_multil);
        this.img_share = (ImageView) findViewById(R.id.img_share);
        this.img_choseall = (ImageView) findViewById(R.id.img_chose_all);
        this.img_menu = (ImageView) findViewById(R.id.img_menu);
        this.img_s = (ImageView) findViewById(R.id.img_s);
        this.editText_search = (EditText) findViewById(R.id.edt_search);
        relativeLayout_head = (RelativeLayout) findViewById(R.id.l1);
        this.txt_count_file = (TextView) findViewById(R.id.txt_count_file);
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this, (int) R.style.AppTheme), this.img_menu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_filestorage, popupMenu.getMenu());
        this.imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResulfActivity.this.Eventback();
            }
        });
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(ResulfActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final HandleLooper handleLooper = new HandleLooper() {
            @Override
            public void update() {
                ResulfActivity.this.musicAdapter.setList(ResulfActivity.this.data_manager.readAllAppssss(ResulfActivity.this));
                ResulfActivity.this.musicAdapter.setDrawables(ResulfActivity.this.data_manager.drawables());
                ResulfActivity.this.musicAdapter.notifyDataSetChanged();
            }
        };
        this.activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(ResulfActivity.this, "can't uninstall", Toast.LENGTH_SHORT).show();
                } else {
                    new BackgroundTask(ResulfActivity.this).Handleloop(handleLooper);
                }
            }
        });
        Contructer();
        data();
        Bundle extras = getIntent().getExtras();
        Objects.requireNonNull(extras);
        this.name_key = extras.getString("nameitem");
        final updatelist updatelistVar = new updatelist() {
            @Override
            public void update(int i) {
                ResulfActivity.this.checkEmpty(i);
            }
        };
        String str = this.name_key;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1852902175:
                if (str.equals("SDcard")) {
                    c = 0;
                    break;
                }
                break;
            case -1185250696:
                if (str.equals("images")) {
                    c = 1;
                    break;
                }
                break;
            case -934918565:
                if (str.equals("recent")) {
                    c = 2;
                    break;
                }
                break;
            case -877567715:
                if (str.equals("imagehide")) {
                    c = 3;
                    break;
                }
                break;
            case 96796:
                if (str.equals("apk")) {
                    c = 4;
                    break;
                }
                break;
            case 96801:
                if (str.equals("app")) {
                    c = 5;
                    break;
                }
                break;
            case 99640:
                if (str.equals("doc")) {
                    c = 6;
                    break;
                }
                break;
            case 101147:
                if (str.equals("fav")) {
                    c = 7;
                    break;
                }
                break;
            case 110834:
                if (str.equals("pdf")) {
                    c = '\b';
                    break;
                }
                break;
            case 111220:
                if (str.equals("ppt")) {
                    c = '\t';
                    break;
                }
                break;
            case 115312:
                if (str.equals("txt")) {
                    c = '\n';
                    break;
                }
                break;
            case 118783:
                if (str.equals("xls")) {
                    c = 11;
                    break;
                }
                break;
            case 120609:
                if (str.equals("zip")) {
                    c = '\f';
                    break;
                }
                break;
            case 104263205:
                if (str.equals("music")) {
                    c = '\r';
                    break;
                }
                break;
            case 112202875:
                if (str.equals("video")) {
                    c = 14;
                    break;
                }
                break;
            case 1082880659:
                if (str.equals("recycle")) {
                    c = 15;
                    break;
                }
                break;
            case 1333541949:
                if (str.equals("videohide")) {
                    c = 16;
                    break;
                }
                break;
            case 1845743388:
                if (str.equals("newfile")) {
                    c = 17;
                    break;
                }
                break;
            case 1847116850:
                if (str.equals("dowload")) {
                    c = 18;
                    break;
                }
                break;
            case 2046112150:
                if (str.equals("Instorage")) {
                    c = 19;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.img_menu.setVisibility(View.VISIBLE);
                this.textView_title.setText(getString(R.string.sd_card));
                switchToSD();
                break;
            case 1:
                this.textView_title.setText(getString(R.string.images));
                this.txt_checklist.setText("No  Albums found!");
                this.img_checklist.setImageResource(R.drawable.icon_imgcheck);
                ImageAlbum();
                break;
            case 2:
                this.textView_title.setText("Recent");
                this.txt_checklist.setText("No files found!");
                recentfile();
                break;
            case 3:
                this.textView_title.setText("Images Hide");
                this.txt_checklist.setText("No images found!");
                hidefile();
                break;
            case 4:
                this.textView_title.setText(R.string.apks);
                this.txt_checklist.setText("No  apks found!");
                this.img_checklist.setImageResource(R.drawable.apkcheck);
                apkUltil();
                break;
            case 5:
                this.textView_title.setText(getString(R.string.Application));
                this.txt_checklist.setText("No apps found!");
                this.img_checklist.setImageResource(R.drawable.appcheck);
                app();
                break;
            case 6:
                this.textView_title.setText("DOC");
                DocUi();
                break;
            case 7:
                this.textView_title.setText("Favourite");
                this.txt_checklist.setText("No favourite files found!");
                this.img_checklist.setImageResource(R.drawable.favcheck);
                Favourites();
                break;
            case '\b':
                this.textView_title.setText("PDF");
                PdfUi();
                break;
            case '\t':
                this.textView_title.setText("PPT");
                PptUi();
                break;
            case '\n':
                this.textView_title.setText("TXT");
                txtUi();
                break;
            case 11:
                this.textView_title.setText("XLS");
                XlstUi();
                break;
            case '\f':
                this.textView_title.setText(R.string.zipped);
                this.txt_checklist.setText("No  zips found!");
                this.img_checklist.setImageResource(R.drawable.zipcheck);
                zipUI();
                break;
            case '\r':
                this.textView_title.setText(R.string.audios);
                this.txt_checklist.setText("No audios found!");
                this.img_checklist.setImageResource(R.drawable.iconmusic_check);
                MusicUi();
                break;
            case 14:
                this.textView_title.setText(getString(R.string.video));
                this.txt_checklist.setText("No  Albums found!");
                this.img_checklist.setImageResource(R.drawable.icon_videocheck);
                VideoAlbum();
                break;
            case 15:
                this.textView_title.setText(R.string.recycle);
                this.txt_checklist.setText("No files found!");
                this.img_checklist.setImageResource(R.drawable.recyclecheck);
                Recycledta();
                break;
            case 16:
                this.textView_title.setText("Videos Hide");
                this.txt_checklist.setText("No videos found!");
                hidefile();
                break;
            case 17:
                this.textView_title.setText("New File");
                this.txt_checklist.setText("No files found!");
                try {
                    NewFile();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            case 18:
                this.textView_title.setText(getString(R.string.dowdnloads));
                dowlaod();
                break;
            case 19:
                this.img_menu.setVisibility(View.VISIBLE);
                this.textView_title.setText(getString(R.string.instorage));
                switchToInternal();
                this.search = new BackGroundSearch(this.data_manager, this.adapter, this.myRecyclerAdapter);
                break;
        }
        Callbackupdate.getInstance().setstateListenuodatecout(new Callbackupdate.Updatecount() {
            @Override
            public void updatecount() {
                ResulfActivity.this.updateAlbumImage();
            }
        });
        CallbackUpdateHideFile.getInstance().setStateListen(new CallbackUpdateHideFile.OncustomStateListen() {
            @Override
            public void statechange() {
                ResulfActivity.this.hidefile();
            }
        });
        CallbackUpdatehide.getInstance().setStateListen(new CallbackUpdatehide.OncustomStateListen() {
            @Override
            public void statechange() {
                if (ResulfActivity.this.name_key.equals("imagehide")) {
                    ResulfActivity.this.imageAdapter.setList(ResulfActivity.this.imagehide());
                } else if (ResulfActivity.this.name_key.equals("videohide")) {
                    ResulfActivity.this.imageAdapter.setList(ResulfActivity.this.videohide());
                }
                ResulfActivity.this.recyclerView.setAdapter(ResulfActivity.this.imageAdapter);
                ResulfActivity.this.imageAdapter.notifyDataSetChanged();
            }
        });
        CallbackUpdateFavourite.getInstance().setStateListen(new CallbackUpdateFavourite.OncustomStateListen() {
            @Override
            public void statechange() {
                if (ResulfActivity.this.name_key.equals("fav")) {
                    ResulfActivity.this.Favourites();
                }
            }
        });
        CallbackUpdateMultilDelete.getInstance().setStateListen(new CallbackUpdateMultilDelete.OncustomStateListen() {
            @Override
            public void statechange() {
                if (ResulfActivity.this.imageAlbumAdapter != null) {
                    ResulfActivity.this.imageAlbumAdapter.setList(ResulfActivity.this.image_ultil.updatealbumimage());
                    ResulfActivity.this.imageAlbumAdapter.notifyDataSetChanged();
                    return;
                }
                ResulfActivity.this.videoAlbumAdapter.setList(ResulfActivity.this.video_ultil.updateAllVidepAlbum());
                ResulfActivity.this.videoAlbumAdapter.notifyDataSetChanged();
            }
        });
        CallbackFav.getInstance().setListener(new CallbackFav.OnCustomStateListener() {
            @Override
            public void stateChanged() {
                ResulfActivity.this.file_document = new ArrayList();
                ResulfActivity.this.file_document = CallbackFav.getInstance().getState();
                if (ResulfActivity.this.favourite_adapter != null) {
                    ResulfActivity.this.favourite_adapter.setList(ResulfActivity.this.file_document);
                    ResulfActivity.this.favourite_adapter.notifyDataSetChanged();
                } else if (ResulfActivity.this.dowloadApdapter != null) {
                    ResulfActivity.this.dowloadApdapter.setList(ResulfActivity.this.file_document);
                    ResulfActivity.this.dowloadApdapter.notifyDataSetChanged();
                } else if (ResulfActivity.this.recent_adapter != null) {
                    ResulfActivity.this.recent_adapter.setList(ResulfActivity.this.file_document);
                    ResulfActivity.this.recent_adapter.notifyDataSetChanged();
                }
            }
        });
        CallbackNewFile.getInstance().setListener(new CallbackNewFile.OnCustomStateListener() {
            @Override
            public void stateChanged() {
                ResulfActivity.this.baseApdapterNewfile.setdata();
                ResulfActivity.this.baseApdapterNewfile.setSave_list(ResulfActivity.this.baseApdapterNewfile.getArrAll());
                ResulfActivity.this.baseApdapterNewfile.notifyDataSetChanged();
            }
        });
        CallbackUpdateRecent.getInstance().setStateListen(new CallbackUpdateRecent.OncustomStateListen() {
            @Override
            public void statechange() {
            }
        });
        final DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                final Dialog_thread dialog_thread = new Dialog_thread(ResulfActivity.this);
                dialog_thread.show();
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        String str2 = ResulfActivity.this.name_key;
                        str2.hashCode();
                        char c2 = 65535;
                        switch (str2.hashCode()) {
                            case -877567715:
                                if (str2.equals("imagehide")) {
                                    c2 = 0;
                                    break;
                                }
                                break;
                            case 96796:
                                if (str2.equals("apk")) {
                                    c2 = 1;
                                    break;
                                }
                                break;
                            case 99640:
                                if (str2.equals("doc")) {
                                    c2 = 2;
                                    break;
                                }
                                break;
                            case 101147:
                                if (str2.equals("fav")) {
                                    c2 = 3;
                                    break;
                                }
                                break;
                            case 110834:
                                if (str2.equals("pdf")) {
                                    c2 = 4;
                                    break;
                                }
                                break;
                            case 111220:
                                if (str2.equals("ppt")) {
                                    c2 = 5;
                                    break;
                                }
                                break;
                            case 115312:
                                if (str2.equals("txt")) {
                                    c2 = 6;
                                    break;
                                }
                                break;
                            case 118783:
                                if (str2.equals("xls")) {
                                    c2 = 7;
                                    break;
                                }
                                break;
                            case 120609:
                                if (str2.equals("zip")) {
                                    c2 = '\b';
                                    break;
                                }
                                break;
                            case 104263205:
                                if (str2.equals("music")) {
                                    c2 = '\t';
                                    break;
                                }
                                break;
                            case 1082880659:
                                if (str2.equals("recycle")) {
                                    c2 = '\n';
                                    break;
                                }
                                break;
                            case 1333541949:
                                if (str2.equals("videohide")) {
                                    c2 = 11;
                                    break;
                                }
                                break;
                            case 1847116850:
                                if (str2.equals("dowload")) {
                                    c2 = '\f';
                                    break;
                                }
                                break;
                        }
                        switch (c2) {
                            case 0:
                                new Ultil(ResulfActivity.this).restoreMultil(ResulfActivity.this.imageAdapter.getListchosed());
                                ResulfActivity.this.deletefile(ResulfActivity.this.imageAdapter.getListchosed());
                                ResulfActivity.this.imageAdapter.setList(ResulfActivity.this.imagehide());
                                ResulfActivity.this.imageAdapter.setIscheck(false);
                                ResulfActivity.this.imageAdapter.cleanfilechose();
                                ResulfActivity.this.updateview(false);
                                Toast.makeText(ResulfActivity.this, "Restored", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getfileApkdowload();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case 2:
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getfidocx();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case 3:
                                ResulfActivity.this.favourite_adapter.deleteMultil(ResulfActivity.this.favourite_adapter.getlistchose());
                                Toast.makeText(ResulfActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getfilepdf();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case 5:
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getfileppt();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case 6:
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getfiletxt();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case 7:
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getfilexls();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case '\b':
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.getzipwithMediastore();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case '\t':
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.musicAdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.musicUltil.updateData();
                                ResulfActivity.this.callbackdelete(ResulfActivity.this.file_document);
                                break;
                            case '\n':
                                ResulfActivity.this.deletefile(ResulfActivity.this.favourite_adapter.getlistchose());
                                ResulfActivity.this.favourite_adapter.setList(new Ultil(ResulfActivity.this).setRecylerView());
                                ResulfActivity.this.favourite_adapter.setCheckview(false);
                                ResulfActivity.this.favourite_adapter.clearlistchose();
                                ResulfActivity.this.updateview(false);
                                Toast.makeText(ResulfActivity.this, "Deleted from device", Toast.LENGTH_SHORT).show();
                                break;
                            case 11:
                                new Ultil(ResulfActivity.this).restoreMultil(ResulfActivity.this.imageAdapter.getListchosed());
                                ResulfActivity.this.deletefile(ResulfActivity.this.imageAdapter.getListchosed());
                                ResulfActivity.this.imageAdapter.setList(ResulfActivity.this.videohide());
                                ResulfActivity.this.imageAdapter.setIscheck(false);
                                ResulfActivity.this.imageAdapter.cleanfilechose();
                                ResulfActivity.this.updateview(false);
                                Toast.makeText(ResulfActivity.this, "Restored", Toast.LENGTH_SHORT).show();
                                break;
                            case '\f':
                                ResulfActivity.this.deleCallBack(ResulfActivity.this.dowloadApdapter.getlistchose());
                                ResulfActivity.this.file_document = ResulfActivity.this.data_manager.filesDowload();
                                ResulfActivity.this.dowloadApdapter.setList(ResulfActivity.this.file_document);
                                ResulfActivity.this.dowloadApdapter.setIscheck(false);
                                ResulfActivity.this.dowloadApdapter.clearListchose();
                                ResulfActivity.this.updateview(false);
                                break;
                        }
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        dialog_thread.dissmiss();
                    }
                }.obtainMessage(111, "parameter").sendToTarget();
                ResulfActivity.this.check = false;
            }
        };
        Callbackupdatealbum.getInstance().setStateListen(new Callbackupdatealbum.OncustomStateListen() {
            @Override
            public void statechange() {
                if (ResulfActivity.this.name_key.equals("images")) {
                    ResulfActivity.this.image_ultil.updatealbumimage();
                    ResulfActivity.this.imageAlbumAdapter.setList(ResulfActivity.this.image_ultil.updatealbumimage());
                } else {
                    ResulfActivity.this.video_ultil.updateAllVidepAlbum();
                    ResulfActivity.this.imageAlbumAdapter.setList(ResulfActivity.this.video_ultil.updateAllVidepAlbum());
                }
                ResulfActivity.this.imageAlbumAdapter.notifyDataSetChanged();
            }
        });
        CallbackUpdateInternal.getInstance().setStateListen(new CallbackUpdateInternal.OncustomStateListen() {
            @Override
            public void statechange() {
                if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.clearSelection();
                } else {
                    ResulfActivity.this.adapter.clearSelection();
                }
                ResulfActivity.isPasteMode = false;
                ResulfActivity.isSelection = false;
                if (ResulfActivity.collections) {
                    if (ResulfActivity.whichCollection == 1) {
                        ResulfActivity.this.data_manager.setImagesData();
                    }
                    if (ResulfActivity.whichCollection == 2) {
                        ResulfActivity.this.data_manager.setAudio(ResulfActivity.this);
                    }
                    if (ResulfActivity.whichCollection == 3) {
                        ResulfActivity.this.data_manager.setDocs();
                    }
                }
                if (!ResulfActivity.collections && !ResulfActivity.favourites) {
                    ResulfActivity.this.data_manager.setRecycler(ResulfActivity.getCurrentPath(), new Sharepre_Ulti(ResulfActivity.this).readSharedPrefsInt("Chosed", 1));
                }
                if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ResulfActivity.this.adapter.notifyDataSetChanged();
                }
            }
        });
        this.img_delete.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View view) {
                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(ResulfActivity.this, deleteCallback);
                char c2 = 0;
                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                customDeleteDialog.show();
                String str2 = ResulfActivity.this.name_key;
                str2.hashCode();
                switch (str2.hashCode()) {
                    case -877567715:
                        break;
                    case 104263205:
                        if (str2.equals("music")) {
                            c2 = 1;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1082880659:
                        if (str2.equals("recycle")) {
                            c2 = 2;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1333541949:
                        if (str2.equals("videohide")) {
                            c2 = 3;
                            break;
                        }
                        c2 = 65535;
                        break;
                    default:
                        c2 = 65535;
                        break;
                }
                switch (c2) {
                    case 0:
                        if (ResulfActivity.this.imageAdapter.getListchosed().size() > 1) {
                            customDeleteDialog.restore_imgs();
                            return;
                        } else {
                            customDeleteDialog.restore_img();
                            return;
                        }
                    case 1:
                        if (ResulfActivity.this.musicAdapter.getlistchose().size() > 1) {
                            customDeleteDialog.set_titile_multil_audio();
                            return;
                        } else {
                            customDeleteDialog.settitle_Music();
                            return;
                        }
                    case 2:
                        if (ResulfActivity.this.favourite_adapter.getlistchose().size() > 1) {
                            customDeleteDialog.delete_permanerly();
                            return;
                        } else {
                            customDeleteDialog.delete_onefile_permanenly();
                            return;
                        }
                    case 3:
                        if (ResulfActivity.this.imageAdapter.getListchosed().size() > 1) {
                            customDeleteDialog.set_tiltile_restore_mulvideo();
                            return;
                        } else {
                            customDeleteDialog.set_titile_restore_oneVideo();
                            return;
                        }
                    default:
                        return;
                }
            }
        });
        this.img_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                char c2;
                ResulfActivity.this.fileshare = new ArrayList();
                String str2 = ResulfActivity.this.name_key;
                switch (str2.hashCode()) {
                    case -877567715:
                        if (str2.equals("imagehide")) {
                            c2 = '\n';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 96796:
                        if (str2.equals("apk")) {
                            c2 = 5;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 99640:
                        if (str2.equals("doc")) {
                            c2 = 2;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 101147:
                        if (str2.equals("fav")) {
                            c2 = '\b';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 110834:
                        if (str2.equals("pdf")) {
                            c2 = 7;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 111220:
                        if (str2.equals("ppt")) {
                            c2 = 3;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 115312:
                        if (str2.equals("txt")) {
                            c2 = 1;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 118783:
                        if (str2.equals("xls")) {
                            c2 = 6;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 120609:
                        if (str2.equals("zip")) {
                            c2 = 4;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 104263205:
                        if (str2.equals("music")) {
                            c2 = 0;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1082880659:
                        if (str2.equals("recycle")) {
                            c2 = '\t';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1333541949:
                        if (str2.equals("videohide")) {
                            c2 = 11;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1845743388:
                        if (str2.equals("newfile")) {
                            c2 = '\r';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1847116850:
                        if (str2.equals("dowload")) {
                            c2 = '\f';
                            break;
                        }
                        c2 = 65535;
                        break;
                    default:
                        c2 = 65535;
                        break;
                }
                switch (c2) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        ResulfActivity resulfActivity = ResulfActivity.this;
                        resulfActivity.fileshare = resulfActivity.musicAdapter.getlistchose();
                        break;
                    case '\b':
                    case '\t':
                        ResulfActivity resulfActivity2 = ResulfActivity.this;
                        resulfActivity2.fileshare = resulfActivity2.favourite_adapter.getlistchose();
                        break;
                    case '\n':
                    case 11:
                        ResulfActivity.this.imageAdapter.shareVideoHide(ResulfActivity.this.imageAdapter.getListchosed());
                        break;
                    case '\f':
                        ResulfActivity resulfActivity3 = ResulfActivity.this;
                        resulfActivity3.fileshare = resulfActivity3.dowloadApdapter.getlistchose();
                        break;
                }
                if (ResulfActivity.this.fileshare.size() > 0) {
                    new Ultil(ResulfActivity.this).shareMultil_file(ResulfActivity.this.fileshare);
                    return;
                }
                ResulfActivity resulfActivity4 = ResulfActivity.this;
                Toast.makeText(resulfActivity4, resulfActivity4.getResources().getString(R.string.no_file_have_select), Toast.LENGTH_SHORT).show();
            }
        });
        this.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = popupMenu.getMenu();
                if (ResulfActivity.gridView) {
                    menu.findItem(R.id.mode_girdview).setTitle("List View");
                } else {
                    menu.findItem(R.id.mode_girdview).setTitle("Gird View");
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.mode_girdview:
                                if (ResulfActivity.gridView) {
                                    ResulfActivity.this.recyclerView.setLayoutManager(new LinearLayoutManager(ResulfActivity.this, RecyclerView.VERTICAL, false));
                                    ResulfActivity.this.myRecyclerAdapter = new MyRecyclerAdapter(ResulfActivity.this.data_manager, ResulfActivity.this);
                                    ResulfActivity.this.recyclerView.setAdapter(ResulfActivity.this.myRecyclerAdapter);
                                    ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                                    ResulfActivity.gridView = false;
                                    break;
                                } else {
                                    ResulfActivity.this.recyclerView.setLayoutManager(new GridLayoutManager((Context) ResulfActivity.this, 4, RecyclerView.VERTICAL, false));
                                    ResulfActivity.this.recyclerView.setAdapter(ResulfActivity.this.adapter);
                                    ResulfActivity.this.adapter.notifyDataSetChanged();
                                    ResulfActivity.gridView = true;
                                    break;
                                }
                            case R.id.new_folder:
                                ResulfActivity.this.createfolder();
                                break;
                            case R.id.sort:
                                ResulfActivity.this.ChoseSort();
                                break;
                            case R.id.statistic:
                                ResulfActivity.this.startActivity(new Intent(ResulfActivity.this, StatisticActivity.class));
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        final UpdateSearch updateSearch = new UpdateSearch() {
            @Override
            public void search() {
                if (!ResulfActivity.this.name_key.equals("images")) {
                    if (!ResulfActivity.this.name_key.equals("video")) {
                        if (!ResulfActivity.this.name_key.equals("dowload")) {
                            if (!ResulfActivity.this.name_key.equals("recent")) {
                                if (ResulfActivity.this.name_key.equals("newfile")) {
                                    return;
                                }
                                if (!ResulfActivity.this.name_key.equals("fav")) {
                                    if (!ResulfActivity.this.name_key.equals("recycle")) {
                                        if (ResulfActivity.this.name_key.equals("imagehide") || ResulfActivity.this.name_key.equals("videohide")) {
                                            ResulfActivity resulfActivity = ResulfActivity.this;
                                            resulfActivity.checkEmpty(resulfActivity.imageAdapter.getData().size());
                                            return;
                                        }
                                        ResulfActivity resulfActivity2 = ResulfActivity.this;
                                        resulfActivity2.checkEmpty(resulfActivity2.musicAdapter.getData().size());
                                        return;
                                    }
                                    ResulfActivity resulfActivity3 = ResulfActivity.this;
                                    resulfActivity3.checkEmpty(resulfActivity3.favourite_adapter.getData().size());
                                    return;
                                }
                                ResulfActivity resulfActivity4 = ResulfActivity.this;
                                resulfActivity4.checkEmpty(resulfActivity4.favourite_adapter.getData().size());
                                return;
                            }
                            ResulfActivity resulfActivity5 = ResulfActivity.this;
                            resulfActivity5.checkEmpty(resulfActivity5.recent_adapter.getData().size());
                            return;
                        }
                        ResulfActivity resulfActivity6 = ResulfActivity.this;
                        resulfActivity6.checkEmpty(resulfActivity6.dowloadApdapter.getData().size());
                        return;
                    }
                    ResulfActivity resulfActivity7 = ResulfActivity.this;
                    resulfActivity7.checkEmpty(resulfActivity7.videoAlbumAdapter.getData().size());
                    return;
                }
                ResulfActivity resulfActivity8 = ResulfActivity.this;
                resulfActivity8.checkEmpty(resulfActivity8.imageAlbumAdapter.getData().size());
            }
        };
        this.editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (ResulfActivity.this.name_key.equals("Instorage") || ResulfActivity.this.name_key.equals("SDcard")) {
                    try {
                        ResulfActivity.this.search.cancel(true);
                    } catch (Exception unused) {
                    }
                    ResulfActivity.this.search = new BackGroundSearch(ResulfActivity.this.data_manager, ResulfActivity.this.adapter, ResulfActivity.this.myRecyclerAdapter);
                    ResulfActivity.this.search.setUpdatelist(updatelistVar);
                    ResulfActivity.this.search.execute(ResulfActivity.path.getPath(), ResulfActivity.this.editText_search.getText().toString().toLowerCase().trim());
                } else if (ResulfActivity.this.name_key.equals("images")) {
                    ResulfActivity.this.imageAlbumAdapter.search(ResulfActivity.this.editText_search.getText().toString().trim());
                    ResulfActivity resulfActivity = ResulfActivity.this;
                    resulfActivity.checkEmpty(resulfActivity.imageAlbumAdapter.getData().size());
                } else if (ResulfActivity.this.name_key.equals("video")) {
                    ResulfActivity.this.videoAlbumAdapter.search(charSequence.toString().trim());
                    ResulfActivity resulfActivity2 = ResulfActivity.this;
                    resulfActivity2.checkEmpty(resulfActivity2.videoAlbumAdapter.getData().size());
                } else if (ResulfActivity.this.name_key.equals("dowload")) {
                    ResulfActivity.this.dowloadApdapter.setUpdateSearch(updateSearch);
                    ResulfActivity.this.dowloadApdapter.search(charSequence.toString());
                } else if (ResulfActivity.this.name_key.equals("recent")) {
                    ResulfActivity.this.recent_adapter.setUpdateSearch(updateSearch);
                    ResulfActivity.this.recent_adapter.search(charSequence.toString().trim());
                } else if (ResulfActivity.this.name_key.equals("newfile")) {
                    ResulfActivity.this.baseApdapterNewfile.search(charSequence.toString().trim());
                } else if (ResulfActivity.this.name_key.equals("fav")) {
                    ResulfActivity.this.favourite_adapter.search(charSequence.toString().trim());
                    ResulfActivity resulfActivity3 = ResulfActivity.this;
                    resulfActivity3.checkEmpty(resulfActivity3.favourite_adapter.getData().size());
                } else if (ResulfActivity.this.name_key.equals("recycle")) {
                    ResulfActivity.this.favourite_adapter.searchRecycle(charSequence.toString().trim());
                    ResulfActivity resulfActivity4 = ResulfActivity.this;
                    resulfActivity4.checkEmpty(resulfActivity4.favourite_adapter.getData().size());
                } else if (ResulfActivity.this.name_key.equals("imagehide") || ResulfActivity.this.name_key.equals("videohide")) {
                    ResulfActivity.this.imageAdapter.search(charSequence.toString().trim());
                    ResulfActivity.this.imageAdapter.setUpdateSearch(updateSearch);
                } else {
                    ResulfActivity.this.musicAdapter.setUpdateSearch(updateSearch);
                    ResulfActivity.this.musicAdapter.search(charSequence.toString().toLowerCase().trim());
                    ResulfActivity resulfActivity5 = ResulfActivity.this;
                    resulfActivity5.checkEmpty(resulfActivity5.musicAdapter.getData().size());
                }
            }
        });
        this.linearLayout_select.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View view) {
                ResulfActivity resulfActivity = ResulfActivity.this;
                char c2 = 1;
                resulfActivity.checkchose = !resulfActivity.checkchose;
                String str2 = ResulfActivity.this.name_key;
                str2.hashCode();
                switch (str2.hashCode()) {
                    case -877567715:
                        if (str2.equals("imagehide")) {
                            c2 = 0;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 96796:
                        break;
                    case 99640:
                        if (str2.equals("doc")) {
                            c2 = 2;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 101147:
                        if (str2.equals("fav")) {
                            c2 = 3;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 110834:
                        if (str2.equals("pdf")) {
                            c2 = 4;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 111220:
                        if (str2.equals("ppt")) {
                            c2 = 5;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 115312:
                        if (str2.equals("txt")) {
                            c2 = 6;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 118783:
                        if (str2.equals("xls")) {
                            c2 = 7;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 120609:
                        if (str2.equals("zip")) {
                            c2 = '\b';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 104263205:
                        if (str2.equals("music")) {
                            c2 = '\t';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1082880659:
                        if (str2.equals("recycle")) {
                            c2 = '\n';
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1333541949:
                        if (str2.equals("videohide")) {
                            c2 = 11;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1847116850:
                        if (str2.equals("dowload")) {
                            c2 = '\f';
                            break;
                        }
                        c2 = 65535;
                        break;
                    default:
                        c2 = 65535;
                        break;
                }
                switch (c2) {
                    case 0:
                    case 11:
                        if (ResulfActivity.this.imageAdapter.posadd.size() == ResulfActivity.this.imageAdapter.getData().size()) {
                            ResulfActivity.this.imageAdapter.cleanfilechose();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.esclip);
                        } else {
                            ResulfActivity.this.imageAdapter.choseall();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                        }
                        TextView textView = ResulfActivity.this.txt_count_file;
                        textView.setText("Select all( " + ResulfActivity.this.imageAdapter.posadd.size() + " )");
                        return;
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case '\b':
                    case '\t':
                        if (ResulfActivity.this.musicAdapter.getPosadd().size() == ResulfActivity.this.musicAdapter.getData().size()) {
                            ResulfActivity.this.musicAdapter.setPosadd(new ArrayList<>());
                            ResulfActivity.this.musicAdapter.notifyDataSetChanged();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.esclip);
                        } else {
                            ResulfActivity.this.musicAdapter.choseall();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                        }
                        TextView textView2 = ResulfActivity.this.txt_count_file;
                        textView2.setText("Select all( " + ResulfActivity.this.musicAdapter.getPosadd().size() + " )");
                        return;
                    case 3:
                    case '\n':
                        if (ResulfActivity.this.favourite_adapter.posadd.size() == ResulfActivity.this.favourite_adapter.getData().size()) {
                            ResulfActivity.this.favourite_adapter.clearlistchose();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.esclip);
                        } else {
                            ResulfActivity.this.favourite_adapter.chosseall();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                        }
                        TextView textView3 = ResulfActivity.this.txt_count_file;
                        textView3.setText("Select all( " + ResulfActivity.this.favourite_adapter.posadd.size() + " )");
                        return;
                    case '\f':
                        if (ResulfActivity.this.dowloadApdapter.posadd.size() == ResulfActivity.this.dowloadApdapter.getData().size()) {
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.esclip);
                            ResulfActivity.this.dowloadApdapter.clearListchose();
                        } else {
                            ResulfActivity.this.dowloadApdapter.choseall();
                            ResulfActivity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                        }
                        TextView textView4 = ResulfActivity.this.txt_count_file;
                        textView4.setText("Select all( " + ResulfActivity.this.dowloadApdapter.posadd.size() + " )");
                        return;
                    default:
                        return;
                }
            }
        });
    }


    public void deletefile(ArrayList<File_DTO> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            new File(arrayList.get(i).getPath()).delete();
        }
        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"));
    }


    public void deleCallBack(ArrayList<File_DTO> arrayList) {
        new Ultil(this).deleteSwithcontentResolver(arrayList);
        this.data_manager.sysn();
        this.file_document = new ArrayList<>();
        Toast.makeText(this, "Moved to trash", Toast.LENGTH_SHORT).show();
    }

    public void callbackdelete(ArrayList<File_DTO> arrayList) {
        this.musicAdapter.setList(new ArrayList());
        this.musicAdapter.setList(arrayList);
        this.musicAdapter.setCheck(false);
        updateview(false);
        this.musicAdapter.clearlistchose();
    }


    public void checkEmpty(int i) {
        if (i == 0) {
            this.linearLayout_check.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
            return;
        }
        this.linearLayout_check.setVisibility(View.GONE);
        this.recyclerView.setVisibility(View.VISIBLE);
    }

    private void data() {
        this.file_dtos_image = this.image_ultil.getallalbumImage();
        this.file_dtos_video = this.video_ultil.updateAllVidepAlbum();
    }

    @Override
    public void onBackPressed() {
        Eventback();
    }


    public void Eventback() {
        if (this.check) {
            if (this.name_key.equals("dowload")) {
                this.dowloadApdapter.setIscheck(false);
                this.dowloadApdapter.clearListchose();
                this.dowloadApdapter.notifyDataSetChanged();
            } else if (this.name_key.equals("fav") || this.name_key.equals("recycle")) {
                this.favourite_adapter.setCheckview(false);
                this.favourite_adapter.clearlistchose();
                this.favourite_adapter.notifyDataSetChanged();
            } else if (this.name_key.equals("imagehide") || this.name_key.equals("videohide")) {
                this.imageAdapter.setIscheck(false);
                this.imageAdapter.cleanfilechose();
                this.imageAdapter.notifyDataSetChanged();
            } else if (this.name_key.equals("Instorage") || this.name_key.equals("SDcard")) {
                relativeLayout_head.setVisibility(View.VISIBLE);
            } else {
                this.musicAdapter.setCheck(false);
                this.musicAdapter.clearlistchose();
                this.musicAdapter.notifyDataSetChanged();
            }
            this.txt_count_file.setText("Select all ()");
            updateview(false);
            this.img_choseall.setImageResource(R.drawable.esclip);
            this.check = false;
            return;
        }
        if (collections) {
            collections = false;
            finish();
            Animatoo.animateSwipeRight(this);
        }
        if (isSelection) {
            contentPerent();
        }
        try {
            if (sdCardmode && path.getPath().equals(externalSD_root.getPath())) {
                sdCardmode = false;
                finish();
                Animatoo.animateSwipeRight(this);
            } else if (path.getPath().equals(Environment.getExternalStorageDirectory().getPath())) {
                finish();
                Animatoo.animateSwipeRight(this);
            } else {
                contentPerent();
            }
        } catch (Exception unused) {
            super.onBackPressed();
        }
    }

    private void contentPerent() {
        File file = new File(path.getParent());
        path = file;
        this.textView_title.setText(file.toString());
        checkEmpty(path.listFiles().length);
        this.data_manager.setRecycler(file, sortFlag);
        if (sdCardmode) {
            documentFile = documentFile.getParentFile();
        }
        if (isPasteMode) {
            this.actionMode.setTitle(path.getName());
        }
        if (gridView) {
            this.adapter.notifyDataSetChanged();
        } else {
            this.myRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public static File getCurrentPath() {
        return path;
    }

    private void Contructer() {
        this.musicUltil = new MusicUltil(this);
        this.image_ultil = new Image_Ultil(this);
        this.video_ultil = new Video_Ultil(this);
        Data_Manager data_Manager = new Data_Manager(this);
        this.data_manager = data_Manager;
        data_Manager.setDocs();
    }


    public void updateview(boolean z) {
        if (z) {
            this.img_share.setVisibility(View.VISIBLE);
            this.img_delete.setVisibility(View.VISIBLE);
            this.img_s.setVisibility(View.GONE);
            this.editText_search.setVisibility(View.GONE);
            return;
        }
        this.img_share.setVisibility(View.GONE);
        this.img_delete.setVisibility(View.GONE);
        this.img_s.setVisibility(View.VISIBLE);
        this.editText_search.setVisibility(View.VISIBLE);
    }

    private void MusicUi() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "music", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.musicUltil.allsong());
        this.musicAdapter.setAdapterAnimation(new ScaleInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        checkEmpty(this.musicUltil.allsong().size());
    }

    private void ImageAlbum() {
        this.recyclerView.setLayoutManager(new GridLayoutManager((Context) this, 2, RecyclerView.VERTICAL, false));
        ImageAlbumAdapter imageAlbumAdapter = new ImageAlbumAdapter(this, this, this.launcher);
        this.imageAlbumAdapter = imageAlbumAdapter;
        imageAlbumAdapter.setList(this.image_ultil.updatealbumimage());
        this.imageAlbumAdapter.setSave_list(this.file_dtos_image);
        this.recyclerView.setAdapter(this.imageAlbumAdapter);
        checkEmpty(this.image_ultil.getallalbumImage().size());
    }

    private void VideoAlbum() {
        this.recyclerView.setLayoutManager(new GridLayoutManager((Context) this, 2, RecyclerView.VERTICAL, false));
        VideoAlbumAdapter videoAlbumAdapter = new VideoAlbumAdapter(this, this, "videoalbum", this.launcher);
        this.videoAlbumAdapter = videoAlbumAdapter;
        videoAlbumAdapter.setList(this.file_dtos_video);
        this.videoAlbumAdapter.setSave_list(this.file_dtos_video);
        this.videoAlbumAdapter.setBitmaps(this.video_ultil.bitmaps(this.file_dtos_video));
        this.recyclerView.setAdapter(this.videoAlbumAdapter);
        checkEmpty(this.file_dtos_video.size());
    }

    private void txtUi() {
        this.file_document = this.data_manager.getfiletxt();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "txt", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getfiletxt().size());
    }

    private void DocUi() {
        this.file_document = this.data_manager.getfidocx();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "docx", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getfidocx().size());
    }

    private void PdfUi() {
        this.file_document = this.data_manager.getfilepdf();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "pdf", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getfilepdf().size());
    }

    private void PptUi() {
        this.file_document = this.data_manager.getfileppt();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "ppt", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getfileppt().size());
    }

    private void XlstUi() {
        this.file_document = this.data_manager.getfilexls();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "xls", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getfilexls().size());
    }

    private void app() {
        this.updateApp = new UpdateApp() {
            @Override
            public void update(String str) {
                Intent intent = new Intent("android.intent.action.DELETE");
                intent.setData(Uri.parse("package:" + str));
                ResulfActivity.this.activityResultLauncher.launch(intent);
            }
        };
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "app", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setUpdateApp(this.updateApp);
        this.musicAdapter.setList(this.data_manager.readAllAppssss(this));
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.readAllAppssss(this).size());
    }

    private void dowlaod() {
        this.file_document = new ArrayList<>();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.file_document = this.data_manager.filesDowload();
        DowloadApdapter dowloadApdapter = new DowloadApdapter(this, this.launcher, this);
        this.dowloadApdapter = dowloadApdapter;
        dowloadApdapter.setList(this.file_document);
        this.dowloadApdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.dowloadApdapter);
        this.dowloadApdapter.notifyDataSetChanged();
        checkEmpty(this.file_document.size());
    }

    private void zipUI() {
        this.file_document = this.data_manager.getzipwithMediastore();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "zip", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getzipwithMediastore().size());
    }

    private void apkUltil() {
        this.file_document = this.data_manager.getallapp();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MusicAdapter musicAdapter = new MusicAdapter(this, this, "apk", this.launcher);
        this.musicAdapter = musicAdapter;
        musicAdapter.setList(this.file_document);
        this.musicAdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.musicAdapter);
        this.musicAdapter.notifyDataSetChanged();
        checkEmpty(this.data_manager.getfileApkdowload().size());
    }


    public void Favourites() {
        new Ultil(this).checkfav_fileExist();
        new ArrayList();
        FavoritSongs favoritSongs = new FavoritSongs(this);
        favoritSongs.open();
        ArrayList<File_DTO> allRows = favoritSongs.getAllRows();
        Log.d(UStats.TAG, "Favourites: " + allRows.size());
        favoritSongs.close();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Favourite_Adapter favourite_Adapter = new Favourite_Adapter(this, this, "fav");
        this.favourite_adapter = favourite_Adapter;
        favourite_Adapter.setList(allRows);
        this.favourite_adapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.favourite_adapter);
        checkEmpty(allRows.size());
    }

    private void NewFile() throws IOException {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        BaseApdapterNewfile baseApdapterNewfile = new BaseApdapterNewfile(this);
        this.baseApdapterNewfile = baseApdapterNewfile;
        this.recyclerView.setAdapter(baseApdapterNewfile);
        this.baseApdapterNewfile.notifyDataSetChanged();
        checkEmpty(this.baseApdapterNewfile.getArrAll().size());
    }

    private void Recycledta() {
        new Ultil(this).check_dialog_recyclebin();
        new ArrayList();
        ArrayList<File_DTO> recylerView = new Ultil(this).setRecylerView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        Favourite_Adapter favourite_Adapter = new Favourite_Adapter(this, this, "recycler");
        this.favourite_adapter = favourite_Adapter;
        favourite_Adapter.setList(recylerView);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.favourite_adapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.favourite_adapter);
        this.favourite_adapter.notifyDataSetChanged();
        checkEmpty(recylerView.size());
    }


    public void hidefile() {
        new ArrayList();
        if (this.name_key.equals("imagehide")) {
            ImageAdapter imageAdapter = new ImageAdapter(this, this, "hide");
            this.imageAdapter = imageAdapter;
            imageAdapter.setList(imagehide());
            this.imageAdapter.setSave_list(imagehide());
        } else if (this.name_key.equals("videohide")) {
            ImageAdapter imageAdapter2 = new ImageAdapter(this, this, "videohide");
            this.imageAdapter = imageAdapter2;
            imageAdapter2.setList(videohide());
            this.imageAdapter.setSave_list(videohide());
        }
        checkEmpty(this.hide_list.size());
        this.recyclerView.setLayoutManager(new GridLayoutManager((Context) this, 3, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(this.imageAdapter);
        this.imageAdapter.notifyDataSetChanged();
    }

    private void recentfile() {
        new Ultil(this).checkrecent_fileExist();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.file_document = new Ultil(this).getallfileRecent();
        Recent_Adapter recent_Adapter = new Recent_Adapter(this, this);
        this.recent_adapter = recent_Adapter;
        recent_Adapter.setList(this.file_document);
        this.recent_adapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.recent_adapter);
        checkEmpty(this.file_document.size());
    }

    private void UItabfile() {
        this.adapter = new GridAdapter(this.data_manager, this);
        this.myRecyclerAdapter = new MyRecyclerAdapter(this.data_manager, this);
        this.data_manager.setRecycler(path, sortFlag);
        if (gridView) {
            this.recyclerView.setLayoutManager(new GridLayoutManager((Context) this, 4, RecyclerView.VERTICAL, false));
            this.recyclerView.setAdapter(this.adapter);
        } else {
            this.recyclerView.setAdapter(this.myRecyclerAdapter);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        registerForContextMenu(this.recyclerView);
        this.recyclerView.addOnItemTouchListener(new Listener_for_Recycler(getApplicationContext(), this.recyclerView, new Listener_for_Recycler.ClickListener() {


            @Override

            public void onClick(View view, int i) {
                Intent intent = null;
                if (!ResulfActivity.isSelection) {
                    ResulfActivity.path = ResulfActivity.this.data_manager.getFiles(i);
                    if (ResulfActivity.favourites) {
                        ResulfActivity.favourites = false;
                    }
                    if (ResulfActivity.isPasteMode) {
                        ResulfActivity.this.actionMode.setTitle(ResulfActivity.path.getName());
                    }
                    if (ResulfActivity.path.isDirectory()) {
                        if (ResulfActivity.collections) {
                            ResulfActivity.collections = false;
                        }
                        if (ResulfActivity.sdCardmode) {
                            ResulfActivity.documentFile = ResulfActivity.documentFile.findFile(ResulfActivity.this.data_manager.getName(i));
                        }
                        if (ResulfActivity.path.getName().equals("data") && Build.VERSION.SDK_INT >= 30) {
                            ResulfActivity.this.restrict_dialog();
                        }
                        ResulfActivity.this.data_manager.setRecycler(ResulfActivity.path, ResulfActivity.sortFlag);
                        ResulfActivity.this.recyclerView.scrollToPosition(0);
                        if (!ResulfActivity.gridView) {
                            ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            ResulfActivity.this.adapter.notifyDataSetChanged();
                        }
                    } else {
                        try {
                            intent = new Intent();
                        } catch (Exception unused) {
                            Toast.makeText(ResulfActivity.this, "Couldn`t open the specified file", Toast.LENGTH_SHORT).show();
                        }
                        if (!ResulfActivity.path.toString().toLowerCase().contains("jpg") && !ResulfActivity.path.toString().toLowerCase().contains("jpeg") && !ResulfActivity.path.toString().toLowerCase().contains("png") && !ResulfActivity.path.toString().toLowerCase().contains("giff")) {
                            if (!ResulfActivity.path.toString().toLowerCase().contains(".apks") && !ResulfActivity.path.toString().toLowerCase().contains(".apk")) {
                                String contentType = null;
                                try {
                                    contentType = new URL("file://" + ResulfActivity.path.getPath()).openConnection().getContentType();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
                                Uri fromFile = Uri.fromFile(ResulfActivity.path);
                                intent.setAction("android.intent.action.VIEW");
                                intent.setDataAndType(fromFile, contentType);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ResulfActivity.this.startActivity(intent);
                                if (!ResulfActivity.collections) {
                                    ResulfActivity.path = ResulfActivity.path.getParentFile();
                                }
                            }
                            new Ultil(ResulfActivity.this).installAPK(ResulfActivity.path.getPath());
                            ResulfActivity.this.startActivity(intent);
                            if (!ResulfActivity.collections) {
                            }
                        }
                        File_DTO file_DTO = new File_DTO();
                        file_DTO.setName(ResulfActivity.path.getName());
                        file_DTO.setPath(ResulfActivity.path.getPath());
                        file_DTO.setSize(new Ultil(ResulfActivity.this).bytesToHuman(ResulfActivity.path.length()));
                        file_DTO.setDate(new Ultil(ResulfActivity.this).getDate(ResulfActivity.path.lastModified()));
                        Intent intent2 = new Intent(ResulfActivity.this, Edit_MediaActivity.class);
                        intent2.putExtra("path", "" + ResulfActivity.path.getPath());
                        intent2.putExtra("file_dto", file_DTO);
                        intent2.putExtra("hidefile", "internal");
                        intent2.putExtra("pos", i);
                        intent = intent2;
                        ResulfActivity.this.startActivity(intent);
                        if (!ResulfActivity.collections) {
                        }
                    }
                } else if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.toggleSelection(i);
                    if (ResulfActivity.this.myRecyclerAdapter.getSelectedItemCount() > 1) {
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.rename).setEnabled(false);
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.properties).setEnabled(false);
                    }
                    if (ResulfActivity.this.myRecyclerAdapter.getSelectedItemCount() == 1) {
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.rename).setEnabled(true);
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.properties).setEnabled(true);
                    }
                    ActionMode actionMode = ResulfActivity.this.actionMode;
                    actionMode.setTitle(ResulfActivity.this.myRecyclerAdapter.getSelectedItemCount() + " Selected");
                    if (ResulfActivity.this.myRecyclerAdapter.getSelectedItemCount() == 0) {
                        ResulfActivity.this.myRecyclerAdapter.clearSelection();
                        ResulfActivity.isSelection = false;
                        ResulfActivity.this.actionMode.finish();
                    }
                } else {
                    ResulfActivity.this.adapter.toggleSelection(i);
                    if (ResulfActivity.this.adapter.getSelectedItemCount() > 1) {
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.rename).setEnabled(false);
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.properties).setEnabled(false);
                    }
                    if (ResulfActivity.this.adapter.getSelectedItemCount() == 1) {
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.rename).setEnabled(true);
                        ResulfActivity.this.actionMode.getMenu().findItem(R.id.properties).setEnabled(true);
                    }
                    ActionMode actionMode2 = ResulfActivity.this.actionMode;
                    actionMode2.setTitle(ResulfActivity.this.adapter.getSelectedItemCount() + " Selected");
                    if (ResulfActivity.this.adapter.getSelectedItemCount() == 0) {
                        ResulfActivity.this.adapter.clearSelection();
                        ResulfActivity.isSelection = false;
                        ResulfActivity.this.actionMode.finish();
                    }
                }
                try {
                    ResulfActivity.this.checkEmpty(ResulfActivity.path.listFiles().length);
                } catch (Exception unused2) {
                    ResulfActivity.this.checkEmpty(0);
                }
                ResulfActivity.this.textView_title.setText(ResulfActivity.path.toString());
            }

            @Override
            public void onLongClick(View view, int i) {
                if (ResulfActivity.isSelection) {
                    return;
                }
                if (ResulfActivity.gridView) {
                    ResulfActivity.relativeLayout_head.setVisibility(View.GONE);
                    ResulfActivity.this.adapter.toggleSelection(i);
                    ResulfActivity resulfActivity = ResulfActivity.this;
                    GridAdapter gridAdapter = resulfActivity.adapter;
                    ResulfActivity resulfActivity2 = ResulfActivity.this;
                    resulfActivity.actionMode = resulfActivity.startActionMode(new ActionModeCallBack(gridAdapter, resulfActivity2, resulfActivity2.data_manager, ResulfActivity.sortFlag));
                    ResulfActivity.this.actionMode.setTitle("1 Selected");
                    ResulfActivity.isSelection = true;
                    return;
                }
                ResulfActivity.relativeLayout_head.setVisibility(View.GONE);
                ResulfActivity.this.myRecyclerAdapter.toggleSelection(i);
                ResulfActivity resulfActivity3 = ResulfActivity.this;
                MyRecyclerAdapter myRecyclerAdapter = resulfActivity3.myRecyclerAdapter;
                ResulfActivity resulfActivity4 = ResulfActivity.this;
                resulfActivity3.actionMode = resulfActivity3.startActionMode(new ActionModeCallBack(myRecyclerAdapter, resulfActivity4, resulfActivity4.data_manager, ResulfActivity.sortFlag));
                ResulfActivity.this.actionMode.setTitle("1 Selected");
                ResulfActivity.isSelection = true;
            }
        }));
    }

    @SuppressLint("ResourceType")
    void createOpenAs(File file, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.open_as).setTitle("Open As");
        final AlertDialog create = builder.create();
        create.show();
        ListView listView = (ListView) create.findViewById(R.id.list);
        listView.setAdapter((ListAdapter) new ArrayAdapter(this, 17367043, new String[]{"Text", "Audio", "Video", "Image"}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                Intent intent = new Intent("android.intent.action.VIEW");
                Uri fromFile = Uri.fromFile(ResulfActivity.path);
                if (i2 == 0) {
                    intent.setDataAndType(fromFile, "text/plain");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else if (i2 == 1) {
                    intent.setDataAndType(fromFile, "audio/wav");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else if (i2 == 2) {
                    intent.setDataAndType(fromFile, "video/*");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else if (i2 == 3) {
                    File_DTO file_DTO = new File_DTO();
                    file_DTO.setName(ResulfActivity.path.getName());
                    file_DTO.setPath(ResulfActivity.path.getPath());
                    Intent intent2 = new Intent(ResulfActivity.this, Edit_MediaActivity.class);
                    intent2.putExtra("path", "" + ResulfActivity.path.getPath());
                    intent2.putExtra("file_dto", file_DTO);
                    intent2.putExtra("hidefile", "internal");
                    intent2.putExtra("pos", i);
                    ResulfActivity.this.startActivity(intent2);
                    Animatoo.animateSwipeLeft(ResulfActivity.this);
                }
                try {
                    ResulfActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    Toast.makeText(ResulfActivity.this, "No App found to open requested file type", Toast.LENGTH_SHORT).show();
                }
                create.cancel();
            }
        });
    }

    void refresh() {
        this.data_manager.setRecycler(path, sortFlag);
    }

    void switchToSD() {
        setExternalSD_root();
        path = new File(getExternalStorageDirectories()[0]);
        refresh();
        Log.d("TAGW", "switchToSD: " + path);
        UItabfile();
    }

    public String[] getExternalStorageDirectories() {
        byte[] bArr = new byte[0];
        boolean equals;
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 19) {
            File[] externalFilesDirs = getExternalFilesDirs(null);
            String lowerCase = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();
            for (File file : externalFilesDirs) {
                if (file != null) {
                    String str = file.getPath().split("/Android")[0];
                    if (!str.toLowerCase().startsWith(lowerCase)) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            equals = Environment.isExternalStorageRemovable(file);
                        } else {
                            equals = "mounted".equals(EnvironmentCompat.getStorageState(file));
                        }
                        if (equals) {
                            arrayList.add(str);
                        }
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            String str2 = "";
            try {
                Process start = new ProcessBuilder(new String[0]).command("mount | grep /dev/block/vold").redirectErrorStream(true).start();
                start.waitFor();
                InputStream inputStream = start.getInputStream();
                while (inputStream.read(new byte[1024]) != -1) {
                    str2 = str2 + new String(bArr);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!str2.trim().isEmpty()) {
                for (String str3 : str2.split("\n")) {
                    arrayList.add(str3.split(" ")[2]);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int i = 0;
            while (i < arrayList.size()) {
                if (!((String) arrayList.get(i)).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d("LOG_TAG", ((String) arrayList.get(i)) + " might not be extSDcard");
                    arrayList.remove(i);
                    i += -1;
                }
                i++;
            }
        } else {
            int i2 = 0;
            while (i2 < arrayList.size()) {
                if (!((String) arrayList.get(i2)).toLowerCase().contains("ext") && !((String) arrayList.get(i2)).toLowerCase().contains("sdcard")) {
                    Log.d("LOG_TAG", ((String) arrayList.get(i2)) + " might not be extSDcard");
                    arrayList.remove(i2);
                    i2 += -1;
                }
                i2++;
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            strArr[i3] = (String) arrayList.get(i3);
        }
        return strArr;
    }

    void switchToInternal() {
        path = Environment.getExternalStorageDirectory();
        refresh();
        UItabfile();
    }

    void setExternalSD_root() {
        String absolutePath;
        if (Build.DEVICE.contains("samsung") || Build.MANUFACTURER.contains("samsung")) {
            absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(Environment.getExternalStorageDirectory().getParent() + "/extSdCard/myDirectory");
            if (file.exists() && file.isDirectory()) {
                absolutePath = Environment.getExternalStorageDirectory().getParent() + "/extSdCard";
            } else {
                File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/external_sd/myDirectory");
                if (file2.exists() && file2.isDirectory()) {
                    absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/external_sd";
                }
            }
        } else {
            absolutePath = Environment.getExternalStorageDirectory().getPath();
        }
        externalSD_root = new File(absolutePath);
    }

    @Override
    public void itemclick(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all( " + this.musicAdapter.getPosadd().size() + " )");
        if (this.musicAdapter.getPosadd().size() == 0) {
            this.musicAdapter.setCheck(false);
            this.musicAdapter.clearlistchose();
            updateview(false);
        } else {
            updateview(true);
        }
        if (this.musicAdapter.getPosadd().size() - this.musicAdapter.getData().size() == 0) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }

    @Override
    public void onlongclick() {
        this.check = true;
        updateview(true);
    }

    @Override
    public void itemclickimg_album(int i) {
        Intent intent = new Intent(this, Image_Video_Activity.class);
        intent.putExtra("namealbum", this.imageAlbumAdapter.getData().get(i).getAbumname());
        intent.putExtra("file_dto", this.imageAlbumAdapter.getData().get(i));
        intent.putExtra("check", "image");
        startActivity(intent);
    }

    @Override
    public void itemclickimg_image(int i) {
        Intent intent = new Intent(this, Edit_MediaActivity.class);
        intent.putExtra("path", this.hide_list.get(i).getPath());
        intent.putExtra("file_dto", this.hide_list.get(i));
        intent.putExtra("hidefile", "hidefile");
        intent.putExtra("pos", i);
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
    }

    @Override
    public void menuclick_image(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all( " + this.imageAdapter.posadd.size() + " )");
        if (this.imageAdapter.posadd.size() == 0) {
            this.imageAdapter.setIscheck(false);
            this.imageAdapter.cleanfilechose();
            updateview(false);
        } else {
            updateview(true);
        }
        if (this.imageAdapter.posadd.size() - this.imageAdapter.getData().size() == 0) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }

    @Override
    public void onlongclickimage() {
        this.check = true;
        this.imageAdapter.setIscheck(true);
        this.imageAdapter.notifyDataSetChanged();
        updateview(this.check);
        this.img_delete.setImageResource(R.drawable.restorefile);
    }

    @Override
    public void itemclickvideo_album(int i) {
        Intent intent = new Intent(this, Image_Video_Activity.class);
        intent.putExtra("namealbum", this.videoAlbumAdapter.getData().get(i).getAbumname());
        intent.putExtra("file_dto", this.videoAlbumAdapter.getData().get(i));
        intent.putExtra("check", "video");
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
    }

    @Override
    public void menuclick_albumvideo(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all( " + this.favourite_adapter.posadd.size() + " )");
        if (this.videoAlbumAdapter.posadd.size() == 0) {
            this.videoAlbumAdapter.setCheck(false);
            this.videoAlbumAdapter.cleanfilechose();
            updateview(false);
            return;
        }
        updateview(true);
    }

    @Override
    public void itemclickFr(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all( " + this.favourite_adapter.posadd.size() + " )");
        if (this.favourite_adapter.posadd.size() == 0) {
            this.favourite_adapter.setCheckview(false);
            this.favourite_adapter.clearlistchose();
            updateview(false);
        } else {
            updateview(true);
        }
        if (this.favourite_adapter.posadd.size() - this.favourite_adapter.getData().size() == 0) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }

    public ArrayList<File_DTO> gethidefile() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        File file = new File(MainActivity.getStore(this) + "/remiImagehide");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                String absolutePath = listFiles[i].getAbsolutePath();
                long lastModified = listFiles[i].lastModified();
                String substring = listFiles[i].getName().substring(listFiles[i].getName().lastIndexOf("%") + 1);
                long length = listFiles[i].length();
                File_DTO file_DTO = new File_DTO();
                file_DTO.setId("1");
                file_DTO.setPath(absolutePath);
                file_DTO.setDate(new Ultil(this).getDate(lastModified));
                file_DTO.setName(substring);
                file_DTO.setSize(new Ultil(this).bytesToHuman(length));
                arrayList.add(file_DTO);
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> videohide() {
        this.hide_list = new ArrayList<>();
        for (int i = 0; i < gethidefile().size(); i++) {
            String path2 = gethidefile().get(i).getPath();
            if (path2.toLowerCase().contains(".mp4") || path2.toLowerCase().contains(".avi") || path2.toLowerCase().contains(".mkv") || path2.toLowerCase().contains(".vob") || path2.toLowerCase().contains("mov")) {
                this.hide_list.add(gethidefile().get(i));
            }
        }
        return this.hide_list;
    }

    public ArrayList<File_DTO> imagehide() {
        this.hide_list = new ArrayList<>();
        for (int i = 0; i < gethidefile().size(); i++) {
            String path2 = gethidefile().get(i).getPath();
            if (path2.toLowerCase().contains(".jpg") || path2.toLowerCase().contains(".png") || path2.toLowerCase().contains(".gif") || path2.toLowerCase().contains(".tiff") || path2.toLowerCase().contains("jpeg")) {
                this.hide_list.add(gethidefile().get(i));
            }
        }
        return this.hide_list;
    }

    private void dialogdelete(final String str, String str2) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pink)));
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
                new File(str).delete();
                dialog.cancel();
            }
        });
        dialog.show();
    }


    public void updateAlbumImage() {
        ImageAlbumAdapter imageAlbumAdapter = this.imageAlbumAdapter;
        if (imageAlbumAdapter != null) {
            imageAlbumAdapter.setList(new ArrayList());
            this.imageAlbumAdapter.setList(this.image_ultil.updatealbumimage());
            this.imageAlbumAdapter.notifyDataSetChanged();
        }
    }

    private void openFiles(File file) {
        Uri uriForFile = FileProvider.getUriForFile(this, getPackageName()+".provider", file);
        String mimeType = getMimeType(uriForFile.toString());
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uriForFile, "*/*");
        Log.d("TAG>", "openFiles: " + mimeType);
        startActivity(intent);
    }

    private String getMimeType(String str) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(str));
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }


    public void ChoseSort() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_chose_sort);
        dialog.setCancelable(false);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.img_check_date);
        ImageView imageView2 = (ImageView) dialog.findViewById(R.id.img_check);
        ImageView imageView3 = (ImageView) dialog.findViewById(R.id.img_check_type);
        ImageView imageView4 = (ImageView) dialog.findViewById(R.id.img_check_size);
        RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.name);
        RelativeLayout relativeLayout2 = (RelativeLayout) dialog.findViewById(R.id.date);
        RelativeLayout relativeLayout3 = (RelativeLayout) dialog.findViewById(R.id.size);
        RelativeLayout relativeLayout4 = (RelativeLayout) dialog.findViewById(R.id.type);
        final Sharepre_Ulti sharepre_Ulti = new Sharepre_Ulti(this);
        int readSharedPrefsInt = sharepre_Ulti.readSharedPrefsInt("Chosed", 1);
        if (readSharedPrefsInt == 1) {
            imageView2.setColorFilter(0xffff0000);
        } else if (readSharedPrefsInt == 2) {
            imageView3.setColorFilter(0xffff0000);
        } else if (readSharedPrefsInt == 3) {
            imageView4.setColorFilter(0xffff0000);
        } else if (readSharedPrefsInt == 4) {
            imageView.setColorFilter(0xffff0000);
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResulfActivity.sortFlag = 1;
                if (ResulfActivity.collections) {
                    ResulfActivity.this.data_manager.sortCollectionsByName();
                } else {
                    ResulfActivity.sortFlag = 1;
                }
                sharepre_Ulti.writeSharedPrefs("Chosed", 1);
                ResulfActivity.this.refresh();
                if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ResulfActivity.this.adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResulfActivity.sortFlag = 2;
                if (ResulfActivity.collections) {
                    ResulfActivity.this.data_manager.sortCollectionsByDate();
                } else {
                    ResulfActivity.sortFlag = 2;
                }
                sharepre_Ulti.writeSharedPrefs("Chosed", 4);
                ResulfActivity.this.refresh();
                if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ResulfActivity.this.adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharepre_Ulti.writeSharedPrefs("Chosed", 2);
                ResulfActivity.this.refresh();
                if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ResulfActivity.this.adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResulfActivity.sortFlag = 3;
                if (ResulfActivity.collections) {
                    ResulfActivity.this.data_manager.sortCollectionsBySize();
                } else {
                    ResulfActivity.sortFlag = 3;
                }
                sharepre_Ulti.writeSharedPrefs("Chosed", 3);
                ResulfActivity.this.refresh();
                if (!ResulfActivity.gridView) {
                    ResulfActivity.this.myRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ResulfActivity.this.adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void createfolder() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialogrename);
        final EditText editText = (EditText) dialog.findViewById(R.id.edt_filename);
        ((TextView) dialog.findViewById(R.id.img_info)).setText(R.string.newfolder);
        ((TextView) dialog.findViewById(R.id.txt_Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        ((TextView) dialog.findViewById(R.id.txt_OK)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ResulfActivity.sdCardmode) {
                    if (!new File(ResulfActivity.path, editText.getText().toString()).mkdirs()) {
                        Toast.makeText(ResulfActivity.this, "Sorry, could not create the Folder", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ResulfActivity.documentFile.createDirectory(editText.getText().toString());
                }
                ResulfActivity.this.refresh();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onlongClickRecent() {
        this.dowloadApdapter.setIscheck(true);
        this.dowloadApdapter.notifyDataSetChanged();
        this.check = true;
        updateview(true);
    }

    @Override
    public void onclick_item(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all( " + this.dowloadApdapter.posadd.size() + " )");
        if (this.dowloadApdapter.posadd.size() == 0) {
            this.dowloadApdapter.setIscheck(false);
            this.dowloadApdapter.clearListchose();
            updateview(false);
        } else {
            updateview(true);
        }
        if (this.dowloadApdapter.posadd.size() - this.dowloadApdapter.getData().size() == 0) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }


    public void restrict_dialog() {
        CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(this, new DeleteCallback() {
            @Override
            public void update() {
                new Ultil(ResulfActivity.this).intent_tree();
            }
        });
        customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        customDeleteDialog.show();
        customDeleteDialog.titile_retrict_access();
    }

    @Override
    protected void onResume() {
        File[] listFiles;
        super.onResume();
        if (this.imageAdapter != null) {
            File file = new File(MainActivity.getStore(this) + "/Video");
            if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    if (!new Ultil(this).deletewithPath(file2)) {
                        file2.delete();
                    }
                }
            }
        }
    }
}
