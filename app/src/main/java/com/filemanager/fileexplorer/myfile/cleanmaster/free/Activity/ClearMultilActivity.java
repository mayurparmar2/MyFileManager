package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.BackgroundTask;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackFav;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdate;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.HandleLooper;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileUltils;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.ClearApdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.DowloadApdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class ClearMultilActivity extends AppCompatActivity implements ClearApdapter.Onclick {
    private boolean checkchose;
    private ClearApdapter clearApdapter;
    private Data_Manager data_manager;
    private DowloadApdapter dowloadApdapter;
    private ArrayList<File_DTO> file_dtos;
    private ImageView img_back;
    private ImageView img_choseall;
    private ImageView img_delete;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private String name_key = "dowload";
    private File path;
    private RecyclerView recyclerView;
    private TextView txt_count_filechose;
    private TextView txt_titile;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_clear_multil);
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(ClearMultilActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.img_choseall = (ImageView) findViewById(R.id.img_chose_all);
        this.txt_count_filechose = (TextView) findViewById(R.id.txt_count_file_chose);
        this.txt_titile = (TextView) findViewById(R.id.txt_titile);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view_delete);
        this.img_delete = (ImageView) findViewById(R.id.img_delete);
        this.file_dtos = new ArrayList<>();
        this.data_manager = new Data_Manager(this);
        Bundle extras = getIntent().getExtras();
        Objects.requireNonNull(extras);
        this.name_key = extras.getString("namekey");
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearMultilActivity.this.clearApdapter.clearFilechose();
                ClearMultilActivity.this.finish();
//                Animatoo.animateSwipeRight(view.getContext());
            }
        });
        this.path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        if (this.name_key.equals("filelagre")) {
            this.txt_titile.setText("Large Files");
            this.clearApdapter = new ClearApdapter(this, this.launcher, this, "largefile");
            ArrayList<File_DTO> arrayList = FileUltils.getallfilewithMediaconetnt(this);
            this.file_dtos = arrayList;
            this.clearApdapter.setList(arrayList);
        } else if (this.name_key.equals("dowload")) {
            this.clearApdapter = new ClearApdapter(this, this.launcher, this, "dowload");
            this.txt_titile.setText("Downloads");
            this.clearApdapter.setList(this.data_manager.getallfilewithpath(this.path));
        }
        this.clearApdapter.setAdapterAnimation(new AlphaInAnimation());
        this.recyclerView.setAdapter(this.clearApdapter);
        this.img_choseall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClearMultilActivity.this.clearApdapter.getfilechose().size() == ClearMultilActivity.this.clearApdapter.getData().size()) {
                    ClearMultilActivity.this.img_choseall.setImageResource(R.drawable.esclip);
                    ClearMultilActivity.this.clearApdapter.clearFilechose();
                } else {
                    ClearMultilActivity.this.img_choseall.setImageResource(R.drawable.blackcheck);
                    ClearMultilActivity.this.clearApdapter.choseall();
                }
                ClearMultilActivity.this.updateview();
            }
        });
        final HandleLooper handleLooper = new HandleLooper() {
            @Override
            public void update() {
                new Ultil(ClearMultilActivity.this).deleteSwithcontentResolver(ClearMultilActivity.this.clearApdapter.getfilechose());
                if (ClearMultilActivity.this.name_key.equals("filelagre")) {
                    ClearMultilActivity.this.clearApdapter.setList(FileUltils.getallfilewithMediaconetnt(ClearMultilActivity.this));
                } else if (ClearMultilActivity.this.name_key.equals("dowload")) {
                    ClearMultilActivity.this.clearApdapter.setList(ClearMultilActivity.this.data_manager.filesDowload());
                }
                ClearMultilActivity.this.clearApdapter.clearFilechose();
                ClearMultilActivity.this.updateview();
            }
        };
        final DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                new BackgroundTask(ClearMultilActivity.this).Handleloop(handleLooper);
            }
        };
        this.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(ClearMultilActivity.this, deleteCallback);
                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                customDeleteDialog.show();
                customDeleteDialog.setTextdeleteMultil();
            }
        });
        updateview();
        CallbackFav.getInstance().setListener(new CallbackFav.OnCustomStateListener() {
            @Override
            public void stateChanged() {
                ClearMultilActivity.this.clearApdapter.setList(CallbackFav.getInstance().getState());
                ClearMultilActivity.this.clearApdapter.notifyDataSetChanged();
                ClearMultilActivity.this.updateview();
            }
        });
        Callbackupdate.getInstance().setStateListen(new Callbackupdate.OncustomStateListen() {
            @Override
            public void statechange() {
                if (!ClearMultilActivity.this.name_key.equals("filelagre")) {
                    if (ClearMultilActivity.this.name_key.equals("dowload")) {
                        ClearMultilActivity.this.clearApdapter.setList(ClearMultilActivity.this.data_manager.getallfilewithpath(ClearMultilActivity.this.path));
                    }
                } else {
                    ClearMultilActivity clearMultilActivity = ClearMultilActivity.this;
                    clearMultilActivity.file_dtos = FileUltils.getallfilewithMediaconetnt(clearMultilActivity);
                    ClearMultilActivity.this.clearApdapter.setList(ClearMultilActivity.this.file_dtos);
                }
                ClearMultilActivity.this.clearApdapter.notifyDataSetChanged();
                ClearMultilActivity.this.updateview();
            }
        });
    }


    public void updateview() {
        if (this.clearApdapter.getPosadd().size() > 1) {
            TextView textView = this.txt_count_filechose;
            textView.setText(this.clearApdapter.getPosadd().size() + " files selected");
        } else {
            TextView textView2 = this.txt_count_filechose;
            textView2.setText(this.clearApdapter.getfilechose().size() + " file selected");
        }
        if (this.clearApdapter.getfilechose().size() < 1) {
            this.img_delete.setVisibility(View.GONE);
        } else {
            this.img_delete.setVisibility(View.VISIBLE);
        }
        if (this.clearApdapter.getfilechose().size() == this.clearApdapter.getData().size()) {
            this.img_choseall.setImageResource(R.drawable.blackcheck);
        } else {
            this.img_choseall.setImageResource(R.drawable.esclip);
        }
    }

    @Override
    public void iconchekClick(int i) {
        updateview();
    }

    @Override
    public void onBackPressed() {
        this.clearApdapter.clearFilechose();
        super.onBackPressed();
    }
}
