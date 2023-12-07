package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMusic;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateApp;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.MusicUltil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class MusicAdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private boolean check;
    private String ckeckicon;
    private Context context;
    private ArrayList<Drawable> drawables;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    protected OnClick onClick;
    public int pos;
    private ArrayList<String> posadd;
    public UpdateApp updateApp;
    private UpdateSearch updateSearch;


    public interface OnClick {
        void itemclick(int i);

        void menuclick(int i);

        void onlongclick();
    }

    public UpdateApp getUpdateApp() {
        return this.updateApp;
    }

    public void setUpdateApp(UpdateApp updateApp) {
        this.updateApp = updateApp;
    }

    public UpdateSearch getUpdateSearch() {
        return this.updateSearch;
    }

    public void setUpdateSearch(UpdateSearch updateSearch) {
        this.updateSearch = updateSearch;
    }

    public ArrayList<Drawable> getDrawables() {
        return this.drawables;
    }

    public void setDrawables(ArrayList<Drawable> arrayList) {
        this.drawables = arrayList;
    }

    public MusicAdapter(OnClick onClick, Context context, String str, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        super(R.layout.custom_ittemsong);
        this.pos = -1;
        this.posadd = new ArrayList<>();
        this.onClick = onClick;
        this.context = context;
        this.ckeckicon = str;
        this.launcher = activityResultLauncher;
        this.drawables = new Data_Manager(context).drawables();
    }

    public ArrayList<String> getPosadd() {
        return this.posadd;
    }

    public void setPosadd(ArrayList<String> arrayList) {
        this.posadd = arrayList;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void setCheck(boolean z) {
        this.check = z;
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.image);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_songitem);
        ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.menu_button);
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.title);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.band);
        ImageView imageView3 = (ImageView) baseViewHolder.findView(R.id.img_check);
        TextView textView3 = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_durian);
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.context, (int) R.style.AppTheme), imageView2, 5);
        if (file_DTO != null) {
            if (this.ckeckicon.equals("music")) {
                new LoadImage_piscaso_glide(this.context).setAlbumArt(file_DTO.getAbumid(), imageView);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("txt")) {
                imageView.setImageResource(R.drawable.icon_txt);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("docx")) {
                imageView.setImageResource(R.drawable.icon_doc);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("xls")) {
                imageView.setImageResource(R.drawable.icon_xls);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("ppt")) {
                imageView.setImageResource(R.drawable.icon_ppt);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("pdf")) {
                imageView.setImageResource(R.drawable.icon_pdf);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("app")) {
                imageView.setImageDrawable(this.drawables.get(baseViewHolder.getLayoutPosition()));
                popupMenu.getMenuInflater().inflate(R.menu.popmenuapp, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("dowload")) {
                imageView.setColorFilter(Color.parseColor("#09F169"));
                imageView.setImageResource(R.drawable.icon_dowload);
                popupMenu.getMenuInflater().inflate(R.menu.popmenuapp, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("apk")) {
                imageView.setImageResource(R.drawable.icon_apk);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else if (this.ckeckicon.equals("zip")) {
                imageView.setImageResource(R.drawable.icon_zip);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            } else {
                imageView.setImageResource(R.drawable.unknowfile);
                popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
            }
            textView2.setText(file_DTO.getSize() + "-" + file_DTO.getDate());
            textView.setText(file_DTO.getName());
            textView3.setText(file_DTO.getDuration());
        }
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAdapter.this.onClick.menuclick(baseViewHolder.getLayoutPosition());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.addfv:
                                new Ultil(MusicAdapter.this.context).addFav(file_DTO);
                                return true;
                            case R.id.appinfo:
                                new Ultil(MusicAdapter.this.context).showdiloginfo(file_DTO, "app");
                                return true;
                            case R.id.delete:
                                new Ultil(MusicAdapter.this.context).dialogdeletewihtpath(file_DTO);
                                return true;
                            case R.id.info:
                                new Ultil(MusicAdapter.this.context).showdiloginfo(file_DTO, "");
                                return true;
                            case R.id.open:
                                try {
                                    MusicAdapter.this.context.startActivity(MusicAdapter.this.context.getPackageManager().getLaunchIntentForPackage(file_DTO.getPackagename()));
                                    return true;
                                } catch (Exception e) {
                                    Log.d("TAG!", "onMenuItemClick: " + e.getMessage());
                                    return true;
                                }
                            case R.id.rename:
                                new Ultil(MusicAdapter.this.context).dialogRename(file_DTO, "");
                                return true;
                            case R.id.share:
                                new Ultil(MusicAdapter.this.context).sharefile(file_DTO);
                                return true;
                            case R.id.stop:
                                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.parse("package:" + file_DTO.getPackagename()));
                                MusicAdapter.this.context.startActivity(intent);
                                return true;
                            case R.id.uninstall:
                                MusicAdapter.this.updateApp.update(file_DTO.getPackagename());
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAdapter.this.onClick.itemclick(baseViewHolder.getLayoutPosition());
                if (!MusicAdapter.this.ckeckicon.equals("music")) {
                    if (MusicAdapter.this.ckeckicon.equals("app")) {
                        try {
                            MusicAdapter.this.context.startActivity(MusicAdapter.this.context.getPackageManager().getLaunchIntentForPackage(file_DTO.getPackagename()));
                        } catch (Exception e) {
                            Log.d("TAG!", "onMenuItemClick: " + e.getMessage());
                        }
                    } else {
                        try {
                            new Ultil(MusicAdapter.this.context).openFile(new File(file_DTO.getPath()));
                        } catch (Exception unused) {
                            Toast.makeText(MusicAdapter.this.context, "No activity for this action", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    MusicAdapter.this.playMedia(Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, file_DTO.getId()));
                }
                new Ultil(MusicAdapter.this.context).addRecent(file_DTO);
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MusicAdapter.this.onClick.onlongclick();
                MusicAdapter musicAdapter = MusicAdapter.this;
                musicAdapter.setCheck(!musicAdapter.isCheck());
                MusicAdapter.this.notifyDataSetChanged();
                return false;
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
                MusicAdapter.this.pos = baseViewHolder.getLayoutPosition();
                if (MusicAdapter.this.posadd.size() == 0) {
                    ArrayList arrayList2 = MusicAdapter.this.posadd;
                    arrayList2.add("" + MusicAdapter.this.pos);
                } else {
                    ArrayList arrayList3 = MusicAdapter.this.posadd;
                    if (arrayList3.contains("" + MusicAdapter.this.pos)) {
                        ArrayList arrayList4 = MusicAdapter.this.posadd;
                        arrayList4.remove("" + MusicAdapter.this.pos);
                    } else {
                        ArrayList arrayList5 = MusicAdapter.this.posadd;
                        arrayList5.add("" + MusicAdapter.this.pos);
                    }
                }
                MusicAdapter.this.notifyDataSetChanged();
                MusicAdapter.this.onClick.itemclick(baseViewHolder.getLayoutPosition());
            }
        });
        if (isCheck()) {
            imageView3.setVisibility(View.VISIBLE);
        } else {
            imageView3.setVisibility(View.GONE);
        }
        CallbackUpdateMusic.getInstance().setStateListen(new CallbackUpdateMusic.OncustomStateListen() {
            @Override
            public void statechange() {
                if (MusicAdapter.this.ckeckicon.equals("music")) {
                    MusicAdapter.this.setList(new MusicUltil(MusicAdapter.this.context).getdatafromDevice());
                } else {
                    Data_Manager data_Manager = new Data_Manager(MusicAdapter.this.context);
                    data_Manager.sysn();
                    if (!MusicAdapter.this.ckeckicon.equals("txt")) {
                        if (!MusicAdapter.this.ckeckicon.equals("docx")) {
                            if (!MusicAdapter.this.ckeckicon.equals("xls")) {
                                if (!MusicAdapter.this.ckeckicon.equals("ppt")) {
                                    if (!MusicAdapter.this.ckeckicon.equals("pdf")) {
                                        if (!MusicAdapter.this.ckeckicon.equals("apk")) {
                                            if (MusicAdapter.this.ckeckicon.equals("zip")) {
                                                MusicAdapter.this.setList(data_Manager.getzipwithMediastore());
                                            }
                                        } else {
                                            MusicAdapter.this.setList(data_Manager.getallapp());
                                        }
                                    } else {
                                        MusicAdapter.this.setList(data_Manager.getfilepdf());
                                    }
                                } else {
                                    data_Manager.setDocs();
                                    MusicAdapter.this.setList(data_Manager.getfileppt());
                                }
                            } else {
                                MusicAdapter.this.setList(data_Manager.getfilexls());
                            }
                        } else {
                            MusicAdapter.this.setList(data_Manager.getfidocx());
                        }
                    } else {
                        MusicAdapter.this.setList(data_Manager.getfiletxt());
                    }
                }
                MusicAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public void playMedia(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(uri);
        this.context.startActivity(intent);
    }

    public ArrayList<File_DTO> getlistchose() {
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

    public void clearlistchose() {
        this.posadd.clear();
        notifyDataSetChanged();
    }

    public static void ShareAudiofileWith_idfile(Context context, long j) {
        Uri withAppendedId = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, j);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("audio/*");
        intent.putExtra("android.intent.extra.STREAM", withAppendedId);
        context.startActivity(Intent.createChooser(intent, "Share Sound File"));
    }

    public void search(String str) {
        Log.e("~~~", "_" + str + ":rs");
        new searchbackground(str).execute(new String[0]);
    }


    public class searchbackground extends AsyncTask<String, Integer, ArrayList<File_DTO>> {
        private String key;
        private ArrayList<File_DTO> listResulf;
        private ArrayList<File_DTO> save_list;

        public searchbackground(String str) {
            this.key = str;
        }


        @Override
        public ArrayList<File_DTO> doInBackground(String... strArr) {
            data();
            if (!this.save_list.isEmpty()) {
                Iterator<File_DTO> it = this.save_list.iterator();
                while (it.hasNext()) {
                    File_DTO next = it.next();
                    if (next.getName().toLowerCase().contains(this.key)) {
                        this.listResulf.add(next);
                    } else {
                        this.listResulf.isEmpty();
                    }
                }
            }
            return this.listResulf;
        }

        @Override
        protected void onPreExecute() {
            this.listResulf = new ArrayList<>();
            super.onPreExecute();
        }


        @Override
        public void onPostExecute(ArrayList<File_DTO> arrayList) {
            super.onPostExecute(arrayList);
            if (this.key.length() == 0) {
                MusicAdapter.this.setList(this.save_list);
            } else {
                MusicAdapter.this.setList(this.listResulf);
            }
            MusicAdapter.this.updateSearch.search();
        }

        void data() {
            Data_Manager data_Manager = new Data_Manager(MusicAdapter.this.context);
            data_Manager.sysn();
            if (!MusicAdapter.this.ckeckicon.equals("music")) {
                if (!MusicAdapter.this.ckeckicon.equals("txt")) {
                    if (!MusicAdapter.this.ckeckicon.equals("docx")) {
                        if (!MusicAdapter.this.ckeckicon.equals("xls")) {
                            if (!MusicAdapter.this.ckeckicon.equals("ppt")) {
                                if (!MusicAdapter.this.ckeckicon.equals("pdf")) {
                                    if (MusicAdapter.this.ckeckicon.equals("app")) {
                                        this.save_list = data_Manager.readAllAppssss(MusicAdapter.this.context);
                                        return;
                                    } else if (!MusicAdapter.this.ckeckicon.equals("dowload")) {
                                        if (!MusicAdapter.this.ckeckicon.equals("apk")) {
                                            if (MusicAdapter.this.ckeckicon.equals("zip")) {
                                                this.save_list = data_Manager.getzipwithMediastore();
                                                return;
                                            }
                                            return;
                                        }
                                        this.save_list = data_Manager.getallapp();
                                        return;
                                    } else {
                                        this.save_list = data_Manager.filesDowload();
                                        return;
                                    }
                                }
                                this.save_list = data_Manager.getfilepdf();
                                return;
                            }
                            this.save_list = data_Manager.getfileppt();
                            return;
                        }
                        this.save_list = data_Manager.getfilexls();
                        return;
                    }
                    this.save_list = data_Manager.getfidocx();
                    return;
                }
                this.save_list = data_Manager.getfiletxt();
                return;
            }
            this.save_list = new MusicUltil(MusicAdapter.this.context).getdatafromDevice();
        }
    }
}
