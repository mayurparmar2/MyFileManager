package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.Edit_MediaActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMusic;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileOperations;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.UStats;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class DowloadApdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private String check;
    private Context context;
    private boolean ischeck;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private OnLongclick onLongclick;
    public int pos;
    public ArrayList<String> posadd;
    private String type;
    private UpdateSearch updateSearch;


    public interface OnLongclick {
        void onclick_item(int i);

        void onlongClickRecent();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public UpdateSearch getUpdateSearch() {
        return this.updateSearch;
    }

    public void setUpdateSearch(UpdateSearch updateSearch) {
        this.updateSearch = updateSearch;
    }

    public DowloadApdapter(Context context, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, OnLongclick onLongclick) {
        super(R.layout.custom_ittemsong);
        this.check = "";
        this.pos = -1;
        this.posadd = new ArrayList<>();
        this.context = context;
        this.launcher = activityResultLauncher;
        this.onLongclick = onLongclick;
    }

    public boolean isIscheck() {
        return this.ischeck;
    }

    public void setIscheck(boolean z) {
        this.ischeck = z;
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.image);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_songitem);
        ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.menu_button);
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.title);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.band);
        TextView textView3 = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_durian);
        ImageView imageView3 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_check);
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.context, (int) R.style.AppTheme), imageView2);
        popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
        if (file_DTO != null) {
            String lowerCase = file_DTO.getPath().toLowerCase();
            if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(".wma") || lowerCase.contains(".mp4a")) {
                imageView.setImageResource(R.drawable.button_music);
            } else if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains(".jpeg") || lowerCase.contains("webp")) {
                new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(".vob") || lowerCase.contains(".mov")) {
                imageView.setImageBitmap(new Video_Ultil(this.context).getbitmap(file_DTO.getPath()));
            } else if (lowerCase.contains(".txt")) {
                imageView.setImageResource(R.drawable.icon_txt);
            } else if (lowerCase.contains(".doc") || lowerCase.contains(".docx")) {
                imageView.setImageResource(R.drawable.icon_doc);
            } else if (lowerCase.contains(".ppt") || lowerCase.contains(".pptx")) {
                imageView.setImageResource(R.drawable.icon_ppt);
            } else if (lowerCase.contains(".pdf")) {
                imageView.setImageResource(R.drawable.icon_pdf);
            } else if (lowerCase.contains(".xls") || lowerCase.contains(".xlsx") || lowerCase.contains(".csv") || lowerCase.contains(".csvx")) {
                imageView.setImageResource(R.drawable.icon_xls);
            } else if (lowerCase.contains(".zip") || lowerCase.contains(".rar")) {
                imageView.setImageResource(R.drawable.icon_zip);
            } else if (lowerCase.contains(".apk") || lowerCase.contains(".apks")) {
                imageView.setImageResource(R.drawable.icon_apk);
            } else {
                imageView.setImageResource(R.drawable.unknowfile);
            }
            textView2.setText(file_DTO.getSize() + ":" + file_DTO.getDate());
            textView.setText(file_DTO.getName());
            textView3.setText(file_DTO.getDuration());
            final DeleteCallback deleteCallback = new DeleteCallback() {
                @Override
                public void update() {
                    new Ultil(DowloadApdapter.this.context).copyFile(file_DTO.getPath());
                    try {
                        FileOperations.delete(new File(file_DTO.getPath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DowloadApdapter.this.setList(new Data_Manager(DowloadApdapter.this.context).getallfilewithpath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                }
            };
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.addfv:
                                    Log.d(UStats.TAG, "onMenuItemClick: " + file_DTO.getId() + file_DTO.getPath());
                                    new Ultil(DowloadApdapter.this.context).addFav(file_DTO);
                                    return true;
                                case R.id.delete:
                                    CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(DowloadApdapter.this.context, deleteCallback);
                                    customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                    customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                                    customDeleteDialog.show();
                                    return true;
                                case R.id.info:
                                    new Ultil(DowloadApdapter.this.context).showdiloginfo(file_DTO, "");
                                    return true;
                                case R.id.rename:
                                    new Ultil(DowloadApdapter.this.context).dialogRename(file_DTO, "def");
                                    Toast.makeText(DowloadApdapter.this.context, "rename", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.share:
                                    new Ultil(DowloadApdapter.this.context).sharefile(file_DTO);
                                    return true;
                                default:
                                    return true;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
            if (this.posadd.size() == 0) {
                imageView3.setImageResource(R.drawable.esclip);
            }
            for (int i = 0; i < this.posadd.size(); i++) {
                ArrayList<String> arrayList = this.posadd;
                if (arrayList.contains("" + baseViewHolder.getLayoutPosition())) {
                    imageView3.setImageResource(R.drawable.blackcheck);
                } else {
                    imageView3.setImageResource(R.drawable.esclip);
                }
            }
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DowloadApdapter.this.pos = baseViewHolder.getAdapterPosition();
                    if (DowloadApdapter.this.posadd.size() == 0) {
                        ArrayList<String> arrayList2 = DowloadApdapter.this.posadd;
                        arrayList2.add("" + DowloadApdapter.this.pos);
                    } else {
                        ArrayList<String> arrayList3 = DowloadApdapter.this.posadd;
                        if (arrayList3.contains("" + DowloadApdapter.this.pos)) {
                            ArrayList<String> arrayList4 = DowloadApdapter.this.posadd;
                            arrayList4.remove("" + DowloadApdapter.this.pos);
                        } else {
                            ArrayList<String> arrayList5 = DowloadApdapter.this.posadd;
                            arrayList5.add("" + DowloadApdapter.this.pos);
                        }
                    }
                    DowloadApdapter.this.notifyDataSetChanged();
                    DowloadApdapter.this.onLongclick.onclick_item(baseViewHolder.getLayoutPosition());
                }
            });
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(file_DTO.getPath());
                    if (file_DTO.getPath().toLowerCase().contains(".jpg") || file_DTO.getPath().toLowerCase().contains(".png") || file_DTO.getPath().toLowerCase().contains(".gif") || file_DTO.getPath().toLowerCase().contains(".tiff") || file_DTO.getPath().toLowerCase().contains("webp")) {
                        Intent intent = new Intent(DowloadApdapter.this.context, Edit_MediaActivity.class);
                        intent.putExtra("path", file);
                        intent.putExtra("file_dto", file_DTO);
                        intent.putExtra("hidefile", "download");
                        intent.putExtra("pos", DowloadApdapter.this.pos);
                        DowloadApdapter.this.context.startActivity(intent);
                        Animatoo.animateSwipeLeft(DowloadApdapter.this.context);
                        return;
                    }
                    new Ultil(DowloadApdapter.this.context).openFile(file);
                }
            });
            CallbackUpdateMusic.getInstance().setStateListen(new CallbackUpdateMusic.OncustomStateListen() {
                @Override
                public void statechange() {
                    DowloadApdapter.this.setList(new Data_Manager(DowloadApdapter.this.context).getallfilewithpath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                }
            });
            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DowloadApdapter.this.onLongclick.onlongClickRecent();
                    return false;
                }
            });
            if (isIscheck()) {
                imageView3.setVisibility(View.VISIBLE);
            } else {
                imageView3.setVisibility(View.GONE);
            }
        }
    }

    public void choseall() {
        this.posadd.clear();
        for (int i = 0; i < getData().size(); i++) {
            ArrayList<String> arrayList = this.posadd;
            arrayList.add("" + i);
        }
        notifyDataSetChanged();
    }

    public ArrayList<File_DTO> getlistchose() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < this.posadd.size(); i++) {
            arrayList.add(getData().get(Integer.parseInt(this.posadd.get(i))));
        }
        return arrayList;
    }

    public void clearListchose() {
        this.posadd.clear();
        notifyDataSetChanged();
    }

    public void search(String str) {
        new Searchbackground(str).execute(new String[0]);
    }


    public class Searchbackground extends AsyncTask<String, Integer, ArrayList<File_DTO>> {
        private String key;
        private ArrayList<File_DTO> listResulft;
        private ArrayList<File_DTO> saveList;

        public Searchbackground(String str) {
            this.key = str;
        }


        @Override
        public void onPostExecute(ArrayList<File_DTO> arrayList) {
            super.onPostExecute(arrayList);
            if (this.key.length() == 0) {
                DowloadApdapter.this.setList(this.saveList);
            } else {
                DowloadApdapter.this.setList(this.listResulft);
            }
            DowloadApdapter.this.notifyDataSetChanged();
            DowloadApdapter.this.updateSearch.search();
        }


        @Override
        public ArrayList<File_DTO> doInBackground(String... strArr) {
            this.listResulft = new ArrayList<>();
            if (this.saveList.isEmpty()) {
                Toast.makeText(DowloadApdapter.this.context, "List empty", Toast.LENGTH_SHORT).show();
            } else {
                Iterator<File_DTO> it = this.saveList.iterator();
                while (it.hasNext()) {
                    File_DTO next = it.next();
                    if (next.getName().toLowerCase().contains(this.key.toLowerCase())) {
                        this.listResulft.add(next);
                    }
                }
            }
            return this.listResulft;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.listResulft = new ArrayList<>();
            this.saveList = new Data_Manager(DowloadApdapter.this.context).filesDowload();
        }
    }
}
