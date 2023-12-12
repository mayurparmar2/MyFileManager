package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.core.os.EnvironmentCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.demo.example.R;
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


public class ResulfActivity2 extends AppCompatActivity implements ImageAlbumAdapter.OnClickImageAlbum {
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
                //  Eventback();
            }
        });
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(ResulfActivity2.this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        final HandleLooper handleLooper = new HandleLooper() {
//            @Override
//            public void update() {
//                musicAdapter.setList(data_manager.readAllAppssss(ResulfActivity2.this));
//                musicAdapter.setDrawables(data_manager.drawables());
//                musicAdapter.notifyDataSetChanged();
//            }
//        };
//        this.activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult activityResult) {
//                if (activityResult.getResultCode() == -1) {
//                    Toast.makeText(ResulfActivity2.this, "can't uninstall", Toast.LENGTH_SHORT).show();
//                } else {
//                    new BackgroundTask(ResulfActivity2.this).Handleloop(handleLooper);
//                }
//            }
//        });
        Contructer();
        data();
        this.name_key = getIntent().getExtras().getString("nameitem");
        final updatelist updatelistVar = new updatelist() {
            @Override
            public void update(int i) {
//                checkEmpty(i);
            }
        };
        switch (name_key) {
            case "SDcard":
                this.img_menu.setVisibility(View.VISIBLE);
                this.textView_title.setText(getString(R.string.sd_card));
                //  switchToSD();
                break;
            case "images":
                this.textView_title.setText(getString(R.string.images));
                this.txt_checklist.setText("No  Albums found!");
                this.img_checklist.setImageResource(R.drawable.icon_imgcheck);
                ImageAlbum();
                break;
            case "recent":
                this.textView_title.setText("Recent");
                this.txt_checklist.setText("No files found!");
                // recentfile();
                break;
            case "imagehide":
                this.textView_title.setText("Images Hide");
                this.txt_checklist.setText("No images found!");
                //   hidefile();
                break;
            case "apk":
                this.textView_title.setText(R.string.apks);
                this.txt_checklist.setText("No  apks found!");
                this.img_checklist.setImageResource(R.drawable.apkcheck);
                //  apkUltil();
                break;
            case "app":
                this.textView_title.setText(getString(R.string.Application));
                this.txt_checklist.setText("No apps found!");
                this.img_checklist.setImageResource(R.drawable.appcheck);
                //   app();
                break;
            case "doc":
                this.textView_title.setText("DOC");
                //    DocUi();
                break;
            case "fav":
                this.textView_title.setText("Favourite");
                this.txt_checklist.setText("No favourite files found!");
                this.img_checklist.setImageResource(R.drawable.favcheck);
                //   Favourites();
                break;
            case "pdf":
                this.textView_title.setText("PDF");
                //  PdfUi();
                break;
            case "ppt":
                this.textView_title.setText("PPT");
                //   PptUi();
                break;
            case "txt":
                this.textView_title.setText("TXT");
                //  txtUi();
                break;
            case "xls":
                this.textView_title.setText("XLS");
                //   XlstUi();
                break;
            case "zip":
                this.textView_title.setText(R.string.zipped);
                this.txt_checklist.setText("No  zips found!");
                this.img_checklist.setImageResource(R.drawable.zipcheck);
                //   zipUI();
                break;
            case "music":
                this.textView_title.setText(R.string.audios);
                this.txt_checklist.setText("No audios found!");
                this.img_checklist.setImageResource(R.drawable.iconmusic_check);
                //   MusicUi();
                break;
            case "video":
                this.textView_title.setText(getString(R.string.video));
                this.txt_checklist.setText("No  Albums found!");
                this.img_checklist.setImageResource(R.drawable.icon_videocheck);
                //    VideoAlbum();
                break;
            case "recycle":
                this.textView_title.setText(R.string.recycle);
                this.txt_checklist.setText("No files found!");
                this.img_checklist.setImageResource(R.drawable.recyclecheck);
                //    Recycledta();
                break;
            case "videohide":
                this.textView_title.setText("Videos Hide");
                this.txt_checklist.setText("No videos found!");
                //   hidefile();
                break;
            case "newfile":
                this.textView_title.setText("New File");
                this.txt_checklist.setText("No files found!");
                try {
                    //       NewFile();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            case "dowload":
                this.textView_title.setText(getString(R.string.dowdnloads));
                //    dowlaod();
                break;
            case "Instorage":
                this.img_menu.setVisibility(View.VISIBLE);
                this.textView_title.setText(getString(R.string.instorage));
                //   switchToInternal();
                this.search = new BackGroundSearch(this.data_manager, this.adapter, this.myRecyclerAdapter);
                break;
        }


    }


    private void data() {
        this.file_dtos_image = this.image_ultil.getallalbumImage();
        this.file_dtos_video = this.video_ultil.updateAllVidepAlbum();
    }

    private void Contructer() {
        this.musicUltil = new MusicUltil(this);
        this.image_ultil = new Image_Ultil(this);
        this.video_ultil = new Video_Ultil(this);
        this.data_manager = new Data_Manager(this);
        this.data_manager.setDocs();
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

    @Override
    public void itemclickimg_album(int i) {
        Intent intent = new Intent(this, Image_Video_Activity.class);
        intent.putExtra("namealbum", this.imageAlbumAdapter.getData().get(i).getAbumname());
        intent.putExtra("file_dto", this.imageAlbumAdapter.getData().get(i));
        intent.putExtra("check", "image");
        startActivity(intent);
    }

    @Override
    public void menuclick_album(int i) {

    }

    void ImageAlbum() {
        recyclerView.setLayoutManager(new GridLayoutManager(ResulfActivity2.this, 2, RecyclerView.VERTICAL, false));
        imageAlbumAdapter = new ImageAlbumAdapter(ResulfActivity2.this, ResulfActivity2.this, launcher);
        new ImageAlbum().execute();
    }

    class ImageAlbum extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                imageAlbumAdapter.setList(image_ultil.updatealbumimage());
                imageAlbumAdapter.setSave_list(file_dtos_image);
                checkEmpty(image_ultil.getallalbumImage().size());
            } catch (Exception e) {
                Log.e("MTAG", "doInBackground" +e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(imageAlbumAdapter);
                }
            });
        }
    }
}