package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.FileUltils;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Image_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Iterator;


public class ImageAdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private String checkhide;
    private Context context;
    private ArrayList<File> files;
    public boolean ischeck;
    public boolean ischeck2;
    private ArrayList<File_DTO> listuse;
    private String namealbum;
    protected OnClickImageAlbum onClick;
    private File path_delete;
    public int pos;
    public ArrayList<String> posadd;
    private ArrayList<File_DTO> save_list;
    private Ultil ultil;
    private UpdateSearch updateSearch;


    public interface OnClickImageAlbum {
        void itemclickimg_image(int i);

        void menuclick_image(int i);

        void onlongclickimage();
    }

    public ArrayList<File_DTO> getSave_list() {
        return this.save_list;
    }

    public ArrayList<File> getFiles() {
        return this.files;
    }

    public void setFiles(ArrayList<File> arrayList) {
        this.files = arrayList;
    }

    public File getPath_delete() {
        return this.path_delete;
    }

    public void setPath_delete(File file) {
        this.path_delete = file;
    }

    public UpdateSearch getUpdateSearch() {
        return this.updateSearch;
    }

    public void setUpdateSearch(UpdateSearch updateSearch) {
        this.updateSearch = updateSearch;
    }

    public String getNamealbum() {
        return this.namealbum;
    }

    public void setNamealbum(String str) {
        this.namealbum = str;
    }

    public void setSave_list(ArrayList<File_DTO> arrayList) {
        this.save_list = arrayList;
    }

    public ImageAdapter(OnClickImageAlbum onClickImageAlbum, Context context, String str) {
        super(R.layout.custom_itemimage);
        this.pos = -1;
        this.posadd = new ArrayList<>();
        this.checkhide = "";
        this.onClick = onClickImageAlbum;
        this.context = context;
        this.checkhide = str;
        this.ultil = new Ultil(context);
        this.files = new ArrayList<>();
        this.listuse = new ArrayList<>();
        this.save_list = new ArrayList<>();
    }

    public boolean isIscheck() {
        return this.ischeck;
    }

    public void setIscheck(boolean z) {
        this.ischeck = z;
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_album);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_Image_Album);
        ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_info);
        ImageView imageView3 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_frame);
        ImageView imageView4 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_check);
        ((TextView) baseViewHolder.itemView.findViewById(R.id.txt_nameAlbum)).setText(file_DTO.getName());
        ((TextView) baseViewHolder.itemView.findViewById(R.id.txt_count)).setText(file_DTO.getSize());
        if (this.posadd.size() == 0) {
            imageView4.setImageResource(R.drawable.esclip);
        }
        for (int i = 0; i < this.posadd.size(); i++) {
            ArrayList<String> arrayList = this.posadd;
            if (arrayList.contains("" + baseViewHolder.getLayoutPosition())) {
                imageView4.setImageResource(R.drawable.blackcheck);
                Log.d("TAG", "convert: " + baseViewHolder.getLayoutPosition());
            } else {
                imageView4.setImageResource(R.drawable.esclip);
            }
        }
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.context, (int) R.style.AppTheme), imageView, 0);
        if (this.checkhide.equals("hide")) {
            new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            popupMenu.getMenuInflater().inflate(R.menu.popuphidemenu, popupMenu.getMenu());
            imageView3.setVisibility(View.GONE);
        } else if (this.checkhide.equals("videohide")) {
            imageView.setImageBitmap(new Video_Ultil(this.context).getbitmap(file_DTO.getPath()));
            imageView3.setVisibility(View.VISIBLE);
            popupMenu.getMenuInflater().inflate(R.menu.popuphidemenu, popupMenu.getMenu());
        } else {
            new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            popupMenu.getMenuInflater().inflate(R.menu.popmenu, popupMenu.getMenu());
            imageView3.setVisibility(View.GONE);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addfv:
                        new Ultil(ImageAdapter.this.context).addFav(file_DTO);
                        return true;
                    case R.id.appinfo:
                        ImageAdapter.this.ultil.showdiloginfo(file_DTO, "appinfo");
                        return true;
                    case R.id.delete:
                        if (ImageAdapter.this.checkhide.equals("hide") || ImageAdapter.this.checkhide.equals("videohide")) {
                            return true;
                        }
                        ImageAdapter.this.dialogdelete(file_DTO.getPath(), baseViewHolder.getLayoutPosition());
                        return true;
                    case R.id.hideinfo:
                        ImageAdapter.this.ultil.showdiloginfo(file_DTO, "def");
                        return true;
                    case R.id.info:
                        ImageAdapter.this.ultil.showdiloginfo(file_DTO, "def");
                        return true;
                    case R.id.rename:
                        ImageAdapter.this.ultil.dialogRename(file_DTO, "img");
                        return true;
                    case R.id.resfile:
                        final Dialog_thread dialog_thread = new Dialog_thread(ImageAdapter.this.context);
                        dialog_thread.show();
                        new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message message) {
                                ImageAdapter.this.ultil.restorefilehide(file_DTO);
                                if (ImageAdapter.this.checkhide.equals("hide")) {
                                    ImageAdapter.this.setList(ImageAdapter.this.ultil.imagehide());
                                } else if (ImageAdapter.this.checkhide.equals("videohide")) {
                                    ImageAdapter.this.setList(ImageAdapter.this.ultil.videohide());
                                }
                                ImageAdapter.this.notifyDataSetChanged();
                                dialog_thread.dissmiss();
                            }
                        }.obtainMessage(111, "parameter").sendToTarget();
                        return true;
                    case R.id.share:
                        new Ultil(ImageAdapter.this.context).sharefile(file_DTO);
                        return true;
                    default:
                        return true;
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageAdapter.this.onClick.menuclick_image(baseViewHolder.getLayoutPosition());
                popupMenu.show();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageAdapter.this.pos = baseViewHolder.getLayoutPosition();
                if (ImageAdapter.this.posadd.size() == 0) {
                    ArrayList<String> arrayList2 = ImageAdapter.this.posadd;
                    arrayList2.add("" + ImageAdapter.this.pos);
                } else {
                    ArrayList<String> arrayList3 = ImageAdapter.this.posadd;
                    if (arrayList3.contains("" + ImageAdapter.this.pos)) {
                        ArrayList<String> arrayList4 = ImageAdapter.this.posadd;
                        arrayList4.remove("" + ImageAdapter.this.pos);
                    } else {
                        ArrayList<String> arrayList5 = ImageAdapter.this.posadd;
                        arrayList5.add("" + ImageAdapter.this.pos);
                    }
                }
                ImageAdapter.this.notifyDataSetChanged();
                ImageAdapter.this.onClick.menuclick_image(baseViewHolder.getLayoutPosition());
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageAdapter.this.checkhide.equals("videohide")) {
                    ImageAdapter.this.opendVideo(new File(file_DTO.getPath()));
                } else {
                    ImageAdapter.this.onClick.itemclickimg_image(baseViewHolder.getLayoutPosition());
                }
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageAdapter.this.onClick.onlongclickimage();
                return true;
            }
        });
        if (isIscheck()) {
            imageView4.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
            relativeLayout.setEnabled(false);
            return;
        }
        imageView4.setVisibility(View.GONE);
        imageView2.setVisibility(View.VISIBLE);
        relativeLayout.setEnabled(true);
    }

    public void update(int i) {
        getData().remove(i);
        notifyDataSetChanged();
    }

    public void cleanfilechose() {
        this.posadd.clear();
        notifyDataSetChanged();
    }

    private void lock(String str) {
        FileLock fileLock = null;
        try {
            try {
                try {
                    fileLock = new RandomAccessFile(str, "rw").getChannel().tryLock();
                    if (fileLock != null) {
                        System.out.println("File is locked");
                        accessTheLockedFile();
                    }
                } catch (Throwable th) {
                    if (fileLock != null) {
                        try {
                            fileLock.release();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
                if (fileLock == null) {
                    return;
                }
                fileLock.release();
            } catch (IOException e3) {
                e3.printStackTrace();
                if (fileLock == null) {
                    return;
                }
                fileLock.release();
            }
            if (fileLock != null) {
                fileLock.release();
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    void accessTheLockedFile() {
        try {
            System.out.println(new FileInputStream("FileToBeLocked").read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dialogdelete(final String str, final int i) {
        final Dialog dialog = new Dialog(this.context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(this.context.getResources().getColor(R.color.pink)));
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
                ImageAdapter.this.getData().remove(i);
                new Image_Ultil(ImageAdapter.this.context).updatedata();
                new Image_Ultil(ImageAdapter.this.context).getallalbumImage();
                ImageAdapter.this.notifyDataSetChanged();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public ArrayList<File_DTO> getListchosed() {
        this.listuse = new ArrayList<>();
        for (int i = 0; i < this.posadd.size(); i++) {
            this.listuse.add(getData().get(Integer.parseInt(this.posadd.get(i))));
        }
        return this.listuse;
    }

    public void choseall() {
        this.posadd.clear();
        for (int i = 0; i < getData().size(); i++) {
            ArrayList<String> arrayList = this.posadd;
            arrayList.add("" + i);
        }
        notifyDataSetChanged();
    }

    public void search(String str) {
        new Searchbackground(str).execute(new String[0]);
    }


    public class Searchbackground extends AsyncTask<String, Integer, ArrayList<File_DTO>> {
        private ArrayList<File_DTO> fileDtos;
        private String key;
        private ArrayList<File_DTO> listResulf;
        private ArrayList<File_DTO> save_list;

        public Searchbackground(String str) {
            this.key = str;
        }


        @Override
        public ArrayList<File_DTO> doInBackground(String... strArr) {
            data();
            if (!this.save_list.isEmpty()) {
                Iterator<File_DTO> it = this.save_list.iterator();
                while (it.hasNext()) {
                    File_DTO next = it.next();
                    if (next.getName().toLowerCase().contains(this.key.toLowerCase())) {
                        this.listResulf.add(next);
                    }
                }
            }
            return this.listResulf;
        }

        @Override
        protected void onPreExecute() {
            this.listResulf = new ArrayList<>();
            this.fileDtos = new Image_Ultil(ImageAdapter.this.context).allImage();
            this.save_list = new ArrayList<>();
            super.onPreExecute();
        }


        @Override
        public void onPostExecute(ArrayList<File_DTO> arrayList) {
            super.onPostExecute(arrayList);
            if (this.key.length() == 0) {
                ImageAdapter.this.setList(this.save_list);
            } else {
                ImageAdapter.this.setList(this.listResulf);
            }
            ImageAdapter.this.updateSearch.search();
        }

        void data() {
            if (!ImageAdapter.this.checkhide.equals("hide")) {
                if (ImageAdapter.this.checkhide.equals("videohide")) {
                    this.save_list = new Ultil(ImageAdapter.this.context).videohide();
                } else {
                    for (int i = 0; i < this.fileDtos.size(); i++) {
                        if (this.fileDtos.get(i).getAbumname().equals(ImageAdapter.this.getNamealbum())) {
                            this.save_list.add(this.fileDtos.get(i));
                        }
                    }
                }
            } else {
                this.save_list = new Ultil(ImageAdapter.this.context).imagehide();
            }
            this.listResulf = new ArrayList<>();
        }
    }


    public void opendVideo(File file) {
        String substring = file.getName().substring(file.getName().lastIndexOf("&") + 1, file.getName().lastIndexOf(FileUltils.HIDDEN_PREFIX));
        File file2 = new File(MainActivity.getStore(this.context) + "/Video", substring);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            MainActivity.copyStream(fileInputStream, fileOutputStream);
            fileOutputStream.close();
            fileInputStream.close();
            Uri uriForFile = FileProvider.getUriForFile(this.context, this.context.getPackageName()+".provider", file2);
            setPath_delete(file2);
            Intent intent = new Intent();
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(uriForFile, "video/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            this.context.startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void shareVideoHide(ArrayList<File_DTO> arrayList) {
        ArrayList<Uri> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            File file = new File(arrayList.get(i).getPath());
            String substring = arrayList.get(i).getName().substring(arrayList.get(i).getName().lastIndexOf("&") + 1, arrayList.get(i).getName().lastIndexOf(FileUltils.HIDDEN_PREFIX));
            File file2 = new File(MainActivity.getStore(this.context) + "/Video", substring);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                MainActivity.copyStream(fileInputStream, fileOutputStream);
                fileOutputStream.close();
                fileInputStream.close();
                arrayList2.add(FileProvider.getUriForFile(this.context, this.context.getPackageName()+".provider", file2));
                getFiles().add(file2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList2);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (this.checkhide.equals("videohide")) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        this.context.startActivity(Intent.createChooser(intent, "Share File with My File Manager"));
    }
}
