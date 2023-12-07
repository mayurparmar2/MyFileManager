package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import androidx.core.content.FileProvider;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CustomDeleteDialog;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateAlbumVideo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMultilDelete;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMusic;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.Callbackupdatealbum;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.DeleteCallback;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class VideoAlbumAdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    public String albumName;
    private ArrayList<Bitmap> bitmaps;
    private boolean check;
    private Context context;
    private ArrayList<File_DTO> file_list;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    protected OnClickImageAlbum onClick;
    public int pos;
    public ArrayList<String> posadd;
    private ArrayList<File_DTO> save_list;
    private String type;
    private UpdateSearch updateSearch;


    public interface OnClickImageAlbum {
        void itemclickvideo_album(int i);

        void menuclick_albumvideo(int i);

        void onLongclick();
    }

    public ArrayList<File_DTO> getSave_list() {
        return this.save_list;
    }

    public UpdateSearch getUpdateSearch() {
        return this.updateSearch;
    }

    public void setUpdateSearch(UpdateSearch updateSearch) {
        this.updateSearch = updateSearch;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String str) {
        this.albumName = str;
    }

    public void setSave_list(ArrayList<File_DTO> arrayList) {
        this.save_list = arrayList;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void setCheck(boolean z) {
        this.check = z;
    }

    public VideoAlbumAdapter(OnClickImageAlbum onClickImageAlbum, Context context, String str, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        super(R.layout.custom_itemabumvideo);
        this.pos = -1;
        this.posadd = new ArrayList<>();
        this.onClick = onClickImageAlbum;
        this.context = context;
        this.type = str;
        this.launcher = activityResultLauncher;
        this.bitmaps = new ArrayList<>();
    }

    public ArrayList<Bitmap> getBitmaps() {
        return this.bitmaps;
    }

    public void setBitmaps(ArrayList<Bitmap> arrayList) {
        this.bitmaps = arrayList;
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_check);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_Image_Album);
        ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_info);
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_count);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_nameAlbum);
        ((ImageView) baseViewHolder.itemView.findViewById(R.id.img_album)).setImageBitmap(getBitmaps().get(baseViewHolder.getLayoutPosition()));
        if (file_DTO.getTotalitem() > 0) {
            textView.setText(String.valueOf(file_DTO.getTotalitem()));
        } else {
            textView.setText(String.valueOf(file_DTO.getSize()));
        }
        if (this.posadd.size() == 0) {
            imageView.setImageResource(R.drawable.esclip);
        }
        if (this.posadd.size() > 0) {
            for (int i = 0; i < this.posadd.size(); i++) {
                ArrayList<String> arrayList = this.posadd;
                if (arrayList.contains("" + baseViewHolder.getLayoutPosition())) {
                    imageView.setImageResource(R.drawable.blackcheck);
                } else {
                    imageView.setImageResource(R.drawable.esclip);
                }
            }
        }
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.context, (int) R.style.AppTheme), imageView2);
        if (this.type.equals("video")) {
            textView2.setText(file_DTO.getName());
            popupMenu.getMenuInflater().inflate(R.menu.popupmenu_video, popupMenu.getMenu());
        } else {
            textView2.setText(file_DTO.getAbumname());
            popupMenu.getMenuInflater().inflate(R.menu.popupmenuabumimage, popupMenu.getMenu());
        }
        final DeleteCallback deleteCallback = new DeleteCallback() {
            @Override
            public void update() {
                final String abumname = file_DTO.getAbumname();
                final Ultil ultil = new Ultil(VideoAlbumAdapter.this.context);
                final Dialog_thread dialog_thread = new Dialog_thread(VideoAlbumAdapter.this.context);
                dialog_thread.show();
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        ultil.createhidefile(file_DTO);
                        ultil.delete(file_DTO, VideoAlbumAdapter.this.launcher);
                        VideoAlbumAdapter.this.setList(new Video_Ultil(VideoAlbumAdapter.this.context).getVideoAlbum(abumname));
                        VideoAlbumAdapter.this.notifyDataSetChanged();
                        CallbackUpdateMultilDelete.getInstance().change();
                        Toast.makeText(VideoAlbumAdapter.this.context, VideoAlbumAdapter.this.context.getString(R.string.files_has_been_hide), Toast.LENGTH_SHORT).show();
                        dialog_thread.dissmiss();
                    }
                }.obtainMessage(111, "parameter").sendToTarget();
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
                                new Ultil(VideoAlbumAdapter.this.context).addFav(file_DTO);
                                return true;
                            case R.id.delete:
                                new Video_Ultil(VideoAlbumAdapter.this.context).dialogdelete(file_DTO, VideoAlbumAdapter.this.launcher);
                                return true;
                            case R.id.hide:
                                CustomDeleteDialog customDeleteDialog = new CustomDeleteDialog(VideoAlbumAdapter.this.context, deleteCallback);
                                customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                customDeleteDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                                customDeleteDialog.show();
                                customDeleteDialog.setview();
                                customDeleteDialog.set_titile_button_hide();
                                return true;
                            case R.id.info:
                                if (VideoAlbumAdapter.this.type.equals("video")) {
                                    new Ultil(VideoAlbumAdapter.this.context).showdiloginfo(file_DTO, "Video");
                                    return true;
                                }
                                new Ultil(VideoAlbumAdapter.this.context).showdiloginfo(file_DTO, "Videoalbum");
                                return true;
                            case R.id.rename:
                                if (VideoAlbumAdapter.this.type.equals("video")) {
                                    new Ultil(VideoAlbumAdapter.this.context).dialogRename(file_DTO, "video");
                                    return true;
                                }
                                new Ultil(VideoAlbumAdapter.this.context).dialogRenamAlbumImage(file_DTO.getPath());
                                return true;
                            case R.id.share:
                                VideoAlbumAdapter.this.shareimge(file_DTO.getPath());
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        if (this.type.equals("video")) {
            CallbackUpdateMusic.getInstance().setStateListen(new CallbackUpdateMusic.OncustomStateListen() {
                @Override
                public void statechange() {
                    VideoAlbumAdapter.this.setList(new Video_Ultil(VideoAlbumAdapter.this.context).getVideoAlbum(file_DTO.getAbumname()));
                    VideoAlbumAdapter.this.notifyDataSetChanged();
                }
            });
        } else {
            CallbackUpdateAlbumVideo.getInstance().setStateListen(new CallbackUpdateAlbumVideo.OncustomStateListen() {
                @Override
                public void statechange() {
                    VideoAlbumAdapter.this.setList(new Video_Ultil(VideoAlbumAdapter.this.context).updateAllVidepAlbum());
                    VideoAlbumAdapter.this.notifyDataSetChanged();
                }
            });
        }
        Callbackupdatealbum.getInstance().setStateListen(new Callbackupdatealbum.OncustomStateListen() {
            @Override
            public void statechange() {
                VideoAlbumAdapter.this.setList(new Video_Ultil(VideoAlbumAdapter.this.context).updateAllVidepAlbum());
                VideoAlbumAdapter.this.notifyDataSetChanged();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoAlbumAdapter.this.onClick.itemclickvideo_album(baseViewHolder.getLayoutPosition());
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                VideoAlbumAdapter.this.onClick.onLongclick();
                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoAlbumAdapter.this.pos = baseViewHolder.getLayoutPosition();
                if (VideoAlbumAdapter.this.posadd.size() == 0) {
                    ArrayList<String> arrayList2 = VideoAlbumAdapter.this.posadd;
                    arrayList2.add("" + VideoAlbumAdapter.this.pos);
                } else {
                    ArrayList<String> arrayList3 = VideoAlbumAdapter.this.posadd;
                    if (arrayList3.contains("" + VideoAlbumAdapter.this.pos)) {
                        ArrayList<String> arrayList4 = VideoAlbumAdapter.this.posadd;
                        arrayList4.remove("" + VideoAlbumAdapter.this.pos);
                    } else {
                        ArrayList<String> arrayList5 = VideoAlbumAdapter.this.posadd;
                        arrayList5.add("" + VideoAlbumAdapter.this.pos);
                    }
                }
                VideoAlbumAdapter.this.notifyDataSetChanged();
                VideoAlbumAdapter.this.onClick.menuclick_albumvideo(baseViewHolder.getLayoutPosition());
            }
        });
        if (isCheck()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public ArrayList<File_DTO> getlistChosed() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < this.posadd.size(); i++) {
            arrayList.add(getData().get(Integer.parseInt(this.posadd.get(i))));
        }
        return arrayList;
    }

    public void choseall() {
        this.posadd.clear();
        for (int i = 0; i < getData().size(); i++) {
            ArrayList<String> arrayList = this.posadd;
            arrayList.add("" + i);
        }
        notifyDataSetChanged();
    }

    public void cleanfilechose() {
        this.posadd.clear();
        notifyDataSetChanged();
    }


    public void shareimge(String str) {
        Uri uriForFile = FileProvider.getUriForFile(this.context, this.context.getPackageName()+".provider", new File(str));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.setType("video/*");
        intent.setDataAndType(uriForFile, "video/*");
        this.context.startActivity(Intent.createChooser(intent, "Remi_FileManger-Fileshare"));
    }

    public void search(String str) {
        this.save_list = new Video_Ultil(this.context).updateAllVidepAlbum();
        this.file_list = new ArrayList<>();
        Iterator<File_DTO> it = this.save_list.iterator();
        while (it.hasNext()) {
            File_DTO next = it.next();
            if (next.getAbumname().toLowerCase().contains(str.toLowerCase())) {
                this.file_list.add(next);
            }
        }
        if (str.length() == 0) {
            setList(this.save_list);
        } else {
            setList(this.file_list);
        }
        notifyDataSetChanged();
    }

    public void searchvideo(String str) {
        new BackgroundSearch().execute(str.toLowerCase());
    }


    public class BackgroundSearch extends AsyncTask<String, Integer, ArrayList<File_DTO>> {
        private ArrayList<File_DTO> fileDtos;
        private String key;
        private ArrayList<File_DTO> listResulft;
        private Video_Ultil video_ultil;

        public BackgroundSearch() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.listResulft = new ArrayList<>();
            this.video_ultil = new Video_Ultil(VideoAlbumAdapter.this.context);
            this.fileDtos = new ArrayList<>();
            VideoAlbumAdapter.this.save_list = new ArrayList();
        }


        @Override
        public void onPostExecute(ArrayList<File_DTO> arrayList) {
            super.onPostExecute(arrayList);
            if (this.key.length() == 0) {
                VideoAlbumAdapter videoAlbumAdapter = VideoAlbumAdapter.this;
                videoAlbumAdapter.setList(videoAlbumAdapter.save_list);
            } else {
                VideoAlbumAdapter.this.setList(arrayList);
            }
            VideoAlbumAdapter.this.updateSearch.search();
        }


        @Override
        public ArrayList<File_DTO> doInBackground(String... strArr) {
            this.key = strArr[0];
            this.fileDtos = this.video_ultil.getallvideo();
            for (int i = 0; i < this.fileDtos.size(); i++) {
                if (this.fileDtos.get(i).getAbumname().equals(VideoAlbumAdapter.this.getAlbumName())) {
                    VideoAlbumAdapter.this.save_list.add(this.fileDtos.get(i));
                }
            }
            if (!VideoAlbumAdapter.this.save_list.isEmpty()) {
                Iterator it = VideoAlbumAdapter.this.save_list.iterator();
                while (it.hasNext()) {
                    File_DTO file_DTO = (File_DTO) it.next();
                    if (file_DTO.getName().toLowerCase().contains(this.key.toLowerCase())) {
                        this.listResulft.add(file_DTO);
                    }
                }
            } else {
                Toast.makeText(VideoAlbumAdapter.this.context, "list empty", Toast.LENGTH_SHORT).show();
            }
            return this.listResulft;
        }
    }
}
