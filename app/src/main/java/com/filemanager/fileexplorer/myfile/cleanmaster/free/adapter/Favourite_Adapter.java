package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.Edit_MediaActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.BackgroundTask;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.HandleLooper;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.FavoritSongs;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class Favourite_Adapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private String check;
    private boolean checkview;
    private Context context;
    private OnClickItem onClickItem;
    public int pos;
    public ArrayList<String> posadd;
    public ArrayList<File_DTO> savelisrrecycle;
    public ArrayList<File_DTO> savelist;


    public interface OnClickItem {
        void itemclickFr(int i);

        void menuclickFr(int i);

        void onlongclick();
    }

    public boolean isCheckview() {
        return this.checkview;
    }

    public void setCheckview(boolean z) {
        this.checkview = z;
    }

    public Favourite_Adapter(Context context, OnClickItem onClickItem, String str) {
        super(R.layout.custom_ittemsong);
        this.pos = -1;
        this.posadd = new ArrayList<>();
        this.context = context;
        this.onClickItem = onClickItem;
        this.check = str;
        this.savelist = new Ultil(context).getListfav();
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.image);
        ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_check);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_songitem);
        ImageView imageView3 = (ImageView) baseViewHolder.itemView.findViewById(R.id.menu_button);
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.title);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.band);
        TextView textView3 = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_durian);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.context, (int) R.style.AppTheme);
        if (file_DTO != null) {
            textView2.setText(file_DTO.getSize() + ":" + file_DTO.getDate());
            textView.setText(file_DTO.getName());
            textView3.setVisibility(View.GONE);
            String lowerCase = file_DTO.getPath().toLowerCase();
            if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains(".jpeg") || lowerCase.contains("webp")) {
                new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(".vob") || lowerCase.contains(".mov")) {
                imageView.setImageBitmap(new Video_Ultil(this.context).getbitmap(file_DTO.getPath()));
            } else if (lowerCase.contains(".txt")) {
                imageView.setImageResource(R.drawable.icon_txt);
            } else if (lowerCase.contains(".pdf")) {
                imageView.setImageResource(R.drawable.icon_pdf);
            } else if (lowerCase.contains(".ppt") || lowerCase.contains(".pptx")) {
                imageView.setImageResource(R.drawable.icon_ppt);
            } else if (lowerCase.contains(".csv") || lowerCase.contains(".csvs")) {
                imageView.setImageResource(R.drawable.icon_xls);
            } else if (lowerCase.contains(".docx") || lowerCase.contains(".doc")) {
                imageView.setImageResource(R.drawable.icon_doc);
            } else if (lowerCase.contains(".apk") || lowerCase.contains(".apks")) {
                imageView.setImageResource(R.drawable.icon_apk);
            } else if (lowerCase.contains(".xls") || lowerCase.contains(".xlsx")) {
                imageView.setImageResource(R.drawable.icon_xls);
            } else if (lowerCase.contains(".zip") || lowerCase.contains(".rar")) {
                imageView.setImageResource(R.drawable.icon_zip);
            } else if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(".wma") || lowerCase.contains(".m4a")) {
                if (file_DTO.getArtistid() == null || file_DTO.getArtistid().equals("null")) {
                    imageView.setImageResource(R.drawable.button_music);
                } else {
                    new LoadImage_piscaso_glide(this.context).setAlbumArt(file_DTO.getArtistid(), imageView);
                }
            } else {
                imageView.setImageResource(R.drawable.unknowfile);
            }
        }
        final PopupMenu popupMenu = new PopupMenu(contextThemeWrapper, imageView3);
        if (this.check.equals("fav")) {
            popupMenu.getMenuInflater().inflate(R.menu.popupmenufav, popupMenu.getMenu());
        } else if (this.check.equals("recycler")) {
            popupMenu.getMenuInflater().inflate(R.menu.recycle_menu, popupMenu.getMenu());
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Favourite_Adapter.this.checkintent(file_DTO, baseViewHolder.getLayoutPosition());
                new Ultil(Favourite_Adapter.this.context).addRecent(file_DTO);
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Favourite_Adapter.this.onClickItem.onlongclick();
                Favourite_Adapter favourite_Adapter = Favourite_Adapter.this;
                favourite_Adapter.setCheckview(!favourite_Adapter.isCheckview());
                Favourite_Adapter.this.notifyDataSetChanged();
                return false;
            }
        });
        if (isCheckview()) {
            imageView2.setVisibility(View.VISIBLE);
        } else {
            imageView2.setVisibility(View.GONE);
        }
        if (this.posadd.size() == 0) {
            imageView2.setImageResource(R.drawable.esclip);
        }
        for (int i = 0; i < this.posadd.size(); i++) {
            ArrayList<String> arrayList = this.posadd;
            if (arrayList.contains("" + baseViewHolder.getLayoutPosition())) {
                imageView2.setImageResource(R.drawable.blackcheck);
            } else {
                imageView2.setImageResource(R.drawable.esclip);
            }
        }
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Favourite_Adapter.this.pos = baseViewHolder.getLayoutPosition();
                if (Favourite_Adapter.this.posadd.size() == 0) {
                    ArrayList<String> arrayList2 = Favourite_Adapter.this.posadd;
                    arrayList2.add("" + Favourite_Adapter.this.pos);
                } else {
                    ArrayList<String> arrayList3 = Favourite_Adapter.this.posadd;
                    if (arrayList3.contains("" + Favourite_Adapter.this.pos)) {
                        ArrayList<String> arrayList4 = Favourite_Adapter.this.posadd;
                        arrayList4.remove("" + Favourite_Adapter.this.pos);
                    } else {
                        ArrayList<String> arrayList5 = Favourite_Adapter.this.posadd;
                        arrayList5.add("" + Favourite_Adapter.this.pos);
                    }
                }
                Favourite_Adapter.this.notifyDataSetChanged();
                Favourite_Adapter.this.onClickItem.itemclickFr(baseViewHolder.getLayoutPosition());
            }
        });
        final HandleLooper handleLooper = new HandleLooper() {
            @Override
            public void update() {
                new Ultil(Favourite_Adapter.this.context).restore(file_DTO);
                Favourite_Adapter.this.setList(new ArrayList());
                Favourite_Adapter favourite_Adapter = Favourite_Adapter.this;
                favourite_Adapter.setList(new Ultil(favourite_Adapter.context).setRecylerView());
                Favourite_Adapter.this.notifyDataSetChanged();
                Toast.makeText(Favourite_Adapter.this.context, Favourite_Adapter.this.context.getResources().getString(R.string.restore), Toast.LENGTH_SHORT).show();
            }
        };
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Favourite_Adapter.this.onClickItem.menuclickFr(baseViewHolder.getLayoutPosition());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.info:
                                Log.d("TAG!", "onMenuItemClick: " + file_DTO.getSize());
                                new Ultil(Favourite_Adapter.this.context).showdiloginfo(file_DTO, "def");
                                return true;
                            case R.id.origin:
                                new BackgroundTask(Favourite_Adapter.this.context).Handleloop(handleLooper);
                                return true;
                            case R.id.remove:
                                if (Favourite_Adapter.this.check.equals("fav")) {
                                    Favourite_Adapter.this.delete(file_DTO);
                                    Favourite_Adapter.this.notifyDataSetChanged();
                                    return true;
                                } else if (Favourite_Adapter.this.check.equals("recycler")) {
                                    new File(file_DTO.getPath()).delete();
                                    Favourite_Adapter.this.setList(new Ultil(Favourite_Adapter.this.context).setRecylerView());
                                    Favourite_Adapter.this.notifyDataSetChanged();
                                    return true;
                                } else {
                                    return true;
                                }
                            case R.id.share:
                                new Ultil(Favourite_Adapter.this.context).sharefile(file_DTO);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void chosseall() {
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


    public void checkintent(File_DTO file_DTO, int i) {
        String path = file_DTO.getPath();
        String lowerCase = file_DTO.getPath().toLowerCase();
        if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains("jpeg") || lowerCase.contains("webp")) {
            Intent intent = new Intent(this.context, Edit_MediaActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("file_dto", file_DTO);
            if (this.check.equals("fav")) {
                intent.putExtra("hidefile", "fav");
            } else if (this.check.equals("recycler")) {
                intent.putExtra("hidefile", "recycle");
            }
            intent.putExtra("pos", i);
            this.context.startActivity(intent);
            Animatoo.animateSwipeLeft(this.context);
            return;
        }
        new Ultil(this.context).openFile(new File(file_DTO.getPath()));
    }

    public ArrayList<File_DTO> getlistchose() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < this.posadd.size(); i++) {
            arrayList.add(getData().get(Integer.parseInt(this.posadd.get(i))));
        }
        return arrayList;
    }


    public void delete(File_DTO file_DTO) {
        FavoritSongs favoritSongs = new FavoritSongs(this.context);
        favoritSongs.open();
        favoritSongs.deleteRowByPath(file_DTO.getPath());
        setList(new ArrayList());
        setList(favoritSongs.getAllRows());
        favoritSongs.close();
    }

    public void deleteMultil(ArrayList<File_DTO> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            delete(arrayList.get(i));
        }
        this.posadd.clear();
        notifyDataSetChanged();
    }

    public void search(String str) {
        this.savelist = new Ultil(this.context).getListfav();
        ArrayList arrayList = new ArrayList();
        if (!this.savelist.isEmpty()) {
            Iterator<File_DTO> it = this.savelist.iterator();
            while (it.hasNext()) {
                File_DTO next = it.next();
                if (next.getName().toLowerCase().contains(str.toLowerCase())) {
                    arrayList.add(next);
                }
            }
            if (str.length() == 0) {
                setList(this.savelist);
            } else {
                setList(arrayList);
            }
            notifyDataSetChanged();
            return;
        }
        Toast.makeText(this.context, "list empty", Toast.LENGTH_SHORT).show();
    }

    public void searchRecycle(String str) {
        this.savelisrrecycle = new Ultil(this.context).setRecylerView();
        ArrayList arrayList = new ArrayList();
        Iterator<File_DTO> it = this.savelisrrecycle.iterator();
        while (it.hasNext()) {
            File_DTO next = it.next();
            if (next.getName().toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(next);
            }
        }
        if (str.length() == 0) {
            setList(this.savelisrrecycle);
        } else {
            setList(arrayList);
        }
        notifyDataSetChanged();
    }
}
