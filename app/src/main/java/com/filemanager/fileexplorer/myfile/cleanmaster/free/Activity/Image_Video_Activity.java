package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMultilDelete;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMusic;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdate;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Image_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.ImageAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.VideoAlbumAdapter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;


public class Image_Video_Activity extends AppCompatActivity implements ImageAdapter.OnClickImageAlbum, VideoAlbumAdapter.OnClickImageAlbum {
    private static File_DTO file_dto;
    private static String namealbum;
    private static String str_check;
    private boolean back;
    private boolean check;
    private EditText editText_search;
    public ArrayList<File_DTO> file_dtos;
    private boolean flag_endthread;
    private ImageAdapter imageAdapter;
    private Image_Ultil image_ultil;
    private ImageView img_back;
    private ImageView img_check;
    private ImageView img_choseall;
    private ImageView img_delete;
    private ImageView img_s;
    private ImageView img_share;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private LinearLayout linearLayout_check;
    private LinearLayout linearLayout_select;
    private RecyclerView recyclerView;
    private Runnable runnable;
    private TextView textView_titile;
    private TextView txt_count_file;
    private TextView txt_find;
    private VideoAlbumAdapter videoAlbumAdapter;
    private Video_Ultil video_ultil;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_image_video);
        file_dto = new File_DTO();
        this.img_delete = (ImageView) findViewById(R.id.img_delete);
        this.img_share = (ImageView) findViewById(R.id.img_share);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.img_check = (ImageView) findViewById(R.id.img_check);
        this.img_choseall = (ImageView) findViewById(R.id.img_choseall);
        this.img_s = (ImageView) findViewById(R.id.img_s);
        this.txt_find = (TextView) findViewById(R.id.txt_title_find);
        this.txt_count_file = (TextView) findViewById(R.id.txt_count_file);
        this.textView_titile = (TextView) findViewById(R.id.title);
        this.linearLayout_check = (LinearLayout) findViewById(R.id.check_layout_empty);
        this.editText_search = (EditText) findViewById(R.id.edt_search);
        this.linearLayout_select = (LinearLayout) findViewById(R.id.linear_select);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview_result);
        Bundle extras = getIntent().getExtras();
        Objects.requireNonNull(extras);
        namealbum = extras.getString("namealbum");
        Bundle extras2 = getIntent().getExtras();
        Objects.requireNonNull(extras2);
        str_check = extras2.getString("check");
        file_dto = (File_DTO) getIntent().getSerializableExtra("file_dto");
        this.image_ultil = new Image_Ultil(this);
        this.video_ultil = new Video_Ultil(this);
        setdata();
        this.textView_titile.setText(file_dto.getAbumname());
        this.recyclerView.setLayoutManager(new GridLayoutManager((Context) this, 3, 1, false));
        if (str_check.equals("image")) {
            this.img_check.setImageResource(R.drawable.icon_imgcheck);
        } else {
            this.img_check.setImageResource(R.drawable.icon_videocheck);
        }
        checkview_delete_share(0);
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Image_Video_Activity.this.back) {
                    if (Image_Video_Activity.str_check.equals("image")) {
                        Image_Video_Activity.this.imageAdapter.setIscheck(false);
                        Image_Video_Activity.this.imageAdapter.cleanfilechose();
                    } else {
                        Image_Video_Activity.this.videoAlbumAdapter.setCheck(false);
                        Image_Video_Activity.this.videoAlbumAdapter.cleanfilechose();
                    }
                    Image_Video_Activity.this.updateUI(false);
                    Image_Video_Activity.this.back = false;
                    return;
                }
                Image_Video_Activity.this.finish();
                Animatoo.animateSwipeRight(Image_Video_Activity.this);
            }
        });
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(Image_Video_Activity.this, "Delete", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Image_Video_Activity.this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Callbackupdate.getInstance().setStateListen(new Callbackupdate.OncustomStateListen() {
            @Override
            public void statechange() {
                Image_Video_Activity.this.setdata();
                Image_Video_Activity.this.recyclerView.setAdapter(Image_Video_Activity.this.imageAdapter);
                Image_Video_Activity.this.imageAdapter.notifyDataSetChanged();
            }
        });
        final DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                final Dialog_thread dialog_thread = new Dialog_thread(Image_Video_Activity.this);
                dialog_thread.show();
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        if (Image_Video_Activity.str_check.equals("image")) {
                            new Ultil(Image_Video_Activity.this).DeleteMultil(Image_Video_Activity.this.imageAdapter.getListchosed(), Image_Video_Activity.this.launcher);
                            ArrayList<File_DTO> updatedata = Image_Video_Activity.this.image_ultil.updatedata();
                            Image_Video_Activity.this.file_dtos = new ArrayList<>();
                            for (int i = 0; i < updatedata.size(); i++) {
                                if (updatedata.get(i).getAbumname().equals(Image_Video_Activity.namealbum)) {
                                    Image_Video_Activity.this.file_dtos.add(updatedata.get(i));
                                }
                            }
                            Image_Video_Activity.this.imageAdapter.setIscheck(false);
                            Image_Video_Activity.this.imageAdapter.cleanfilechose();
                            Image_Video_Activity.this.imageAdapter.setList(Image_Video_Activity.this.file_dtos);
                        } else {
                            new Ultil(Image_Video_Activity.this).DeleteMultil(Image_Video_Activity.this.videoAlbumAdapter.getlistChosed(), Image_Video_Activity.this.launcher);
                            Image_Video_Activity.this.file_dtos = new ArrayList<>();
                            new ArrayList();
                            ArrayList<File_DTO> arrayList = Image_Video_Activity.this.video_ultil.getallvideo();
                            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                                if (arrayList.get(i2).getAbumname().equals(Image_Video_Activity.namealbum)) {
                                    Image_Video_Activity.this.file_dtos.add(arrayList.get(i2));
                                }
                            }
                            Image_Video_Activity.this.videoAlbumAdapter.setCheck(false);
                            Image_Video_Activity.this.videoAlbumAdapter.setList(Image_Video_Activity.this.file_dtos);
                            Image_Video_Activity.this.videoAlbumAdapter.cleanfilechose();
                        }
                        Image_Video_Activity.this.back = false;
                        Image_Video_Activity.this.updateUI(false);
                        dialog_thread.dissmiss();
                        CallbackUpdateMultilDelete.getInstance().change();
                    }
                }.obtainMessage(111, "parameter").sendToTarget();
                Toast.makeText(Image_Video_Activity.this, "Moved to trash", Toast.LENGTH_SHORT).show();
            }
        };
        CallbackUpdateMusic.getInstance().setStateListen(new CallbackUpdateMusic.OncustomStateListen() {
            @Override
            public void statechange() {
                final Dialog_thread dialog_thread = new Dialog_thread(Image_Video_Activity.this);
                dialog_thread.show();
                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        if (Image_Video_Activity.str_check.equals("image")) {
                            Image_Video_Activity.this.setdata();
                            Image_Video_Activity.this.imageAdapter.notifyDataSetChanged();
                            Image_Video_Activity.this.recyclerView.setAdapter(Image_Video_Activity.this.imageAdapter);
                            dialog_thread.dissmiss();
                        }
                    }
                };
                handler.removeCallbacksAndMessages(null);
                handler.obtainMessage(111, "parameter").sendToTarget();
            }
        });
        final UpdateSearch updateSearch = new UpdateSearch() {
            @Override
            public void search() {
                if (!Image_Video_Activity.str_check.equals("image")) {
                    if (Image_Video_Activity.str_check.equals("video")) {
                        Image_Video_Activity image_Video_Activity = Image_Video_Activity.this;
                        image_Video_Activity.checkEmpty(image_Video_Activity.videoAlbumAdapter.getData().size());
                        return;
                    }
                    return;
                }
                Image_Video_Activity image_Video_Activity2 = Image_Video_Activity.this;
                image_Video_Activity2.checkEmpty(image_Video_Activity2.imageAdapter.getData().size());
            }
        };
        this.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Image_Video_Activity.this.imageAdapter != null ? Image_Video_Activity.this.imageAdapter.posadd : Image_Video_Activity.this.videoAlbumAdapter.posadd).size() == 0) {
                    Image_Video_Activity image_Video_Activity = Image_Video_Activity.this;
                    Toast.makeText(image_Video_Activity, image_Video_Activity.getResources().getString(R.string.atention_delemiultil), Toast.LENGTH_SHORT).show();
                    return;
                }
                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(Image_Video_Activity.this, deleteCallback);
                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                customDeleteDialog.show();
                if (Image_Video_Activity.str_check.equals("image")) {
                    if (Image_Video_Activity.this.imageAdapter.posadd.size() > 1) {
                        customDeleteDialog.setTextmul_file();
                    } else {
                        customDeleteDialog.settextdeleteone_file();
                    }
                } else if (Image_Video_Activity.this.videoAlbumAdapter.posadd.size() > 1) {
                    customDeleteDialog.delete_videos();
                } else {
                    customDeleteDialog.delete_video();
                }
            }
        });
        this.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Image_Video_Activity.str_check.equals("image")) {
                    if (Image_Video_Activity.this.imageAdapter.getListchosed().size() > 0) {
                        new Ultil(Image_Video_Activity.this).shareMultil_file(Image_Video_Activity.this.imageAdapter.getListchosed());
                        return;
                    }
                    Image_Video_Activity image_Video_Activity = Image_Video_Activity.this;
                    Toast.makeText(image_Video_Activity, image_Video_Activity.getResources().getString(R.string.no_file_have_select), Toast.LENGTH_SHORT).show();
                } else if (Image_Video_Activity.str_check.equals("video")) {
                    if (Image_Video_Activity.this.videoAlbumAdapter.getlistChosed().size() > 0) {
                        new Ultil(Image_Video_Activity.this).shareMultil_file(Image_Video_Activity.this.videoAlbumAdapter.getlistChosed());
                        return;
                    }
                    Image_Video_Activity image_Video_Activity2 = Image_Video_Activity.this;
                    Toast.makeText(image_Video_Activity2, image_Video_Activity2.getResources().getString(R.string.no_file_have_select), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (Image_Video_Activity.str_check.equals("image")) {
                    Image_Video_Activity.this.imageAdapter.setUpdateSearch(updateSearch);
                    Image_Video_Activity.this.imageAdapter.search(charSequence.toString());
                } else if (Image_Video_Activity.str_check.equals("video")) {
                    Image_Video_Activity.this.videoAlbumAdapter.setUpdateSearch(updateSearch);
                    Image_Video_Activity.this.videoAlbumAdapter.searchvideo(charSequence.toString());
                }
            }
        });
        this.linearLayout_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Image_Video_Activity.str_check.equals("image")) {
                    if (Image_Video_Activity.this.imageAdapter.posadd.size() == Image_Video_Activity.this.imageAdapter.getData().size()) {
                        Image_Video_Activity.this.imageAdapter.cleanfilechose();
                        Image_Video_Activity.this.img_choseall.setImageResource(R.drawable.esclip);
                        Image_Video_Activity.this.checkview_delete_share(0);
                    } else {
                        Image_Video_Activity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                        Image_Video_Activity.this.imageAdapter.choseall();
                        Image_Video_Activity.this.checkview_delete_share(10);
                    }
                } else if (Image_Video_Activity.this.videoAlbumAdapter.posadd.size() == Image_Video_Activity.this.videoAlbumAdapter.getData().size()) {
                    Image_Video_Activity.this.videoAlbumAdapter.cleanfilechose();
                    Image_Video_Activity.this.img_choseall.setImageResource(R.drawable.esclip);
                    Image_Video_Activity.this.checkview_delete_share(0);
                } else {
                    Image_Video_Activity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                    Image_Video_Activity.this.videoAlbumAdapter.choseall();
                    Image_Video_Activity.this.checkview_delete_share(10);
                }
                if (Image_Video_Activity.str_check.equals("image")) {
                    TextView textView = Image_Video_Activity.this.txt_count_file;
                    textView.setText("Select all(" + Image_Video_Activity.this.imageAdapter.posadd.size() + ")");
                    return;
                }
                TextView textView2 = Image_Video_Activity.this.txt_count_file;
                textView2.setText("Select all(" + Image_Video_Activity.this.videoAlbumAdapter.getlistChosed().size() + ")");
            }
        });
    }


    public void checkview_delete_share(int i) {
        if (i > 0) {
            this.img_delete.setVisibility(View.VISIBLE);
            this.img_share.setVisibility(View.VISIBLE);
            return;
        }
        this.img_delete.setVisibility(View.GONE);
        this.img_share.setVisibility(View.GONE);
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


    public void setdata() {
        this.file_dtos = new ArrayList<>();
        this.txt_find.setText(R.string.no_image_found);
        if (str_check.equals("image")) {
            ArrayList<File_DTO> updatedata = this.image_ultil.updatedata();
            for (int i = 0; i < updatedata.size(); i++) {
                if (updatedata.get(i).getAbumname().equals(namealbum)) {
                    this.file_dtos.add(updatedata.get(i));
                }
            }
            ImageAdapter imageAdapter = new ImageAdapter(this, this, " ");
            this.imageAdapter = imageAdapter;
            imageAdapter.setList(this.file_dtos);
            this.imageAdapter.setNamealbum(namealbum);
            this.recyclerView.setAdapter(this.imageAdapter);
            checkEmpty(this.file_dtos.size());
        } else if (str_check.equals("video")) {
            this.txt_find.setText(R.string.no_video_found);
            this.videoAlbumAdapter = new VideoAlbumAdapter(this, this, "video", this.launcher);
            new videoUibackground().execute(new String[0]);
        }
    }


    public class videoUibackground extends AsyncTask<String, Void, String> {
        ArrayList<Bitmap> bitmaps;
        Dialog_thread dialog_thread;
        ArrayList<File_DTO> fileDtos;

        public videoUibackground() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.bitmaps = new ArrayList<>();
            Image_Video_Activity.this.file_dtos = new ArrayList<>();
            Dialog_thread dialog_thread = new Dialog_thread(Image_Video_Activity.this);
            this.dialog_thread = dialog_thread;
            dialog_thread.show();
        }


        @Override
        public String doInBackground(String... strArr) {
            this.fileDtos = Image_Video_Activity.this.video_ultil.getallvideo();
            for (int i = 0; i < this.fileDtos.size(); i++) {
                if (this.fileDtos.get(i).getAbumname().equals(Image_Video_Activity.namealbum)) {
                    Image_Video_Activity.this.file_dtos.add(this.fileDtos.get(i));
                }
            }
            this.bitmaps = Image_Video_Activity.this.video_ultil.bitmaps(Image_Video_Activity.this.file_dtos);
            return null;
        }


        @Override
        public void onPostExecute(String str) {
            Image_Video_Activity.this.videoAlbumAdapter.setList(Image_Video_Activity.this.file_dtos);
            Image_Video_Activity.this.videoAlbumAdapter.setBitmaps(this.bitmaps);
            Image_Video_Activity.this.videoAlbumAdapter.setAlbumName(Image_Video_Activity.namealbum);
            Image_Video_Activity.this.recyclerView.setAdapter(Image_Video_Activity.this.videoAlbumAdapter);
            Image_Video_Activity image_Video_Activity = Image_Video_Activity.this;
            image_Video_Activity.checkEmpty(image_Video_Activity.file_dtos.size());
            this.dialog_thread.dissmiss();
            super.onPostExecute(str);
        }
    }

    @Override
    public void itemclickimg_image(int i) {
        Intent intent = new Intent(this, Edit_MediaActivity.class);
        intent.putExtra("path", this.imageAdapter.getData().get(i).getPath());
        intent.putExtra("file_dto", this.imageAdapter.getData().get(i));
        intent.putExtra("hidefile", "image");
        intent.putExtra("pos", i);
        new Ultil(this).addRecent(this.imageAdapter.getData().get(i));
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
    }

    @Override
    public void menuclick_image(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all(" + this.imageAdapter.posadd.size() + ")");
        if (this.imageAdapter.posadd.size() == 0) {
            updateUI(false);
            this.back = false;
            this.imageAdapter.setIscheck(false);
            this.imageAdapter.cleanfilechose();
        } else {
            updateUI(true);
        }
        if (this.imageAdapter.posadd.size() - this.imageAdapter.getData().size() == 0) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }

    @Override
    public void onlongclickimage() {
        this.back = true;
        updateUI(true);
        this.imageAdapter.setIscheck(true);
        TextView textView = this.txt_count_file;
        textView.setText("Select all(" + this.imageAdapter.posadd.size() + ")");
        this.imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemclickvideo_album(int i) {
        URL url;
        new Ultil(this).addRecent(this.file_dtos.get(i));
        String path = this.file_dtos.get(i).getPath();
        URLConnection uRLConnection = null;
        try {
            url = new URL("file://" + path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }
        try {
            uRLConnection = url.openConnection();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        String contentType = uRLConnection.getContentType();
        Intent intent = new Intent();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        Uri fromFile = Uri.fromFile(new File(path));
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(fromFile, contentType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void menuclick_albumvideo(int i) {
        TextView textView = this.txt_count_file;
        textView.setText("Select all(" + this.videoAlbumAdapter.posadd.size() + ")");
        if (this.videoAlbumAdapter.posadd.size() == 0) {
            updateUI(false);
            this.back = false;
            this.videoAlbumAdapter.setCheck(false);
            this.videoAlbumAdapter.cleanfilechose();
        } else {
            updateUI(true);
        }
        if (this.videoAlbumAdapter.posadd.size() - this.videoAlbumAdapter.getData().size() == 0) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }

    @Override
    public void onLongclick() {
        this.back = true;
        updateUI(true);
        this.videoAlbumAdapter.setCheck(true);
        this.videoAlbumAdapter.notifyDataSetChanged();
    }


    public void updateUI(boolean z) {
        if (z) {
            checkview_delete_share(10);
            this.img_s.setVisibility(View.GONE);
            this.editText_search.setVisibility(View.GONE);
            return;
        }
        checkview_delete_share(0);
        this.img_s.setVisibility(View.VISIBLE);
        this.editText_search.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getRepeatCount() == 0) {
            if (this.back) {
                if (str_check.equals("image")) {
                    this.imageAdapter.setIscheck(false);
                    this.imageAdapter.cleanfilechose();
                } else {
                    this.videoAlbumAdapter.setCheck(false);
                    this.videoAlbumAdapter.cleanfilechose();
                }
                updateUI(false);
                this.back = false;
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
