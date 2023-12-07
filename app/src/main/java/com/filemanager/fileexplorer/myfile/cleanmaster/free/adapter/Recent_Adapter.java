package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.Intent;
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

import androidx.core.content.FileProvider;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.Edit_MediaActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.UpdateSearch;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.FavoritSongs;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class Recent_Adapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private Context context;
    private OnClickItem onClickItem;
    private UpdateSearch updateSearch;


    public interface OnClickItem {
        void itemclickFr(int i);

        void menuclickFr(int i);
    }

    public UpdateSearch getUpdateSearch() {
        return this.updateSearch;
    }

    public void setUpdateSearch(UpdateSearch updateSearch) {
        this.updateSearch = updateSearch;
    }

    public Recent_Adapter(Context context, OnClickItem onClickItem) {
        super(R.layout.custom_ittem_recent);
        this.context = context;
        this.onClickItem = onClickItem;
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.image);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_songitem);
        final ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.menu_button);
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.title);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.band);
        TextView textView3 = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_durian);
        if (file_DTO != null) {
            textView2.setText(file_DTO.getSize() + "-" + new Ultil(this.context).getDate(Long.parseLong(file_DTO.getLastmodified())));
            textView.setText(file_DTO.getName());
            textView3.setVisibility(View.GONE);
            String lowerCase = file_DTO.getPath().toLowerCase();
            if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains(".jpeg") || lowerCase.contains("webp")) {
                new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(".vob") || lowerCase.contains(".mov")) {
                try {
                    imageView.setImageBitmap(new Video_Ultil(this.context).getbitmap(file_DTO.getPath()));
                } catch (Exception unused) {
                    imageView.setImageResource(R.drawable.button_video);
                }
            } else if (lowerCase.contains(".txt")) {
                imageView.setImageResource(R.drawable.icon_txt);
            } else if (lowerCase.toLowerCase().contains(".pdf")) {
                imageView.setImageResource(R.drawable.icon_pdf);
            } else if (lowerCase.toLowerCase().contains(".ppt")) {
                imageView.setImageResource(R.drawable.icon_ppt);
            } else if (lowerCase.toLowerCase().contains(".csv") || lowerCase.contains(".csvs")) {
                imageView.setImageResource(R.drawable.icon_xls);
            } else if (lowerCase.toLowerCase().contains(".docx") || lowerCase.contains(".doc")) {
                imageView.setImageResource(R.drawable.icon_doc);
            } else if (lowerCase.toLowerCase().contains(".apk") || lowerCase.contains(".apks")) {
                imageView.setImageResource(R.drawable.icon_apk);
            } else if (lowerCase.toLowerCase().contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(".wma") || lowerCase.contains(".m4a")) {
                if (file_DTO.getArtistid() == null || file_DTO.getArtistid().equals("null")) {
                    imageView.setImageResource(R.drawable.button_music);
                } else {
                    new LoadImage_piscaso_glide(this.context).setAlbumArt(file_DTO.getArtistid(), imageView);
                }
            } else {
                imageView.setImageResource(R.drawable.unknowfile);
            }
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recent_Adapter.this.checkintent(file_DTO, baseViewHolder.getLayoutPosition());
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recent_Adapter.this.onClickItem.menuclickFr(baseViewHolder.getLayoutPosition());
                PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(Recent_Adapter.this.context, (int) R.style.AppTheme), imageView2);
                popupMenu.getMenuInflater().inflate(R.menu.popupmenufav, popupMenu.getMenu());
                popupMenu.getMenu().findItem(R.id.remove).setVisible(false);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.info) {
                            Log.d("TAG!", "onMenuItemClick: " + file_DTO.getSize());
                            new Ultil(Recent_Adapter.this.context).showdiloginfo(file_DTO, "def");
                            return true;
                        } else if (itemId == R.id.remove) {
                            Recent_Adapter.this.delete(file_DTO);
                            return true;
                        } else if (itemId != R.id.share) {
                            return true;
                        } else {
                            Recent_Adapter.this.sharefile(file_DTO);
                            return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }


    public void checkintent(File_DTO file_DTO, int i) {
        String path = file_DTO.getPath();
        String lowerCase = file_DTO.getPath().toLowerCase();
        if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains(".webp") || lowerCase.contains(".jpeg")) {
            Intent intent = new Intent(this.context, Edit_MediaActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("file_dto", file_DTO);
            intent.putExtra("hidefile", "recent");
            intent.putExtra("pos", i);
            this.context.startActivity(intent);
            Animatoo.animateSwipeLeft(this.context);
        } else if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(".wma") || lowerCase.contains(".m4a")) {
            Uri withAppendedPath = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, file_DTO.getId());
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(withAppendedPath);
            this.context.startActivity(intent2);
        } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(".vob") || lowerCase.contains(".mov")) {
            Uri withAppendedPath2 = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, file_DTO.getId());
            Intent intent3 = new Intent("android.intent.action.VIEW");
            intent3.setData(withAppendedPath2);
            this.context.startActivity(intent3);
        } else {
            new Ultil(this.context).openFile(new File(file_DTO.getPath()));
        }
    }


    public void sharefile(File_DTO file_DTO) {
        String path = file_DTO.getPath();
        Uri uriForFile = FileProvider.getUriForFile(this.context, this.context.getPackageName()+".provider", new File(file_DTO.getPath()));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (path.toLowerCase().contains(".jpg") || path.toLowerCase().contains(".png") || path.toLowerCase().contains(".gif") || path.toLowerCase().contains(".tiff")) {
            intent.setDataAndType(uriForFile, "image/*");
        } else if (path.toLowerCase().contains(".mp3") || path.contains(".wav") || path.contains(".flac") || path.contains(".wma") || path.contains(".mp4a")) {
            intent.setDataAndType(uriForFile, "audio/*");
        } else if (path.toLowerCase().contains(".mp4") || path.contains(".avi") || path.contains(".mkv") || path.contains(".vob") || path.contains(".mov")) {
            intent.setDataAndType(uriForFile, "video/*");
        } else {
            intent.setDataAndType(uriForFile, "document/*");
        }
        this.context.startActivity(Intent.createChooser(intent, "Remi_FileManger"));
    }


    public void delete(File_DTO file_DTO) {
        FavoritSongs favoritSongs = new FavoritSongs(this.context);
        favoritSongs.open();
        favoritSongs.deleteRowByPath(file_DTO.getPath());
        setList(new ArrayList());
        setList(favoritSongs.getAllRows());
        notifyDataSetChanged();
        favoritSongs.close();
    }

    public void search(String str) {
        new SearchBackground(str).execute(new String[0]);
    }


    public class SearchBackground extends AsyncTask<String, Integer, ArrayList<File_DTO>> {
        private String key;
        private ArrayList<File_DTO> listResulft;
        private ArrayList<File_DTO> savelist;

        public SearchBackground(String str) {
            this.key = str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.listResulft = new ArrayList<>();
            this.savelist = new Ultil(Recent_Adapter.this.context).getallfileRecent();
        }


        @Override
        public ArrayList<File_DTO> doInBackground(String... strArr) {
            Iterator<File_DTO> it = this.savelist.iterator();
            while (it.hasNext()) {
                File_DTO next = it.next();
                if (next.getName().toLowerCase().contains(this.key.toLowerCase())) {
                    this.listResulft.add(next);
                }
            }
            return this.listResulft;
        }


        @Override
        public void onPostExecute(ArrayList<File_DTO> arrayList) {
            super.onPostExecute(arrayList);
            if (this.key.length() == 0) {
                Recent_Adapter.this.setList(this.savelist);
            } else {
                Recent_Adapter.this.setList(this.listResulft);
            }
            Recent_Adapter.this.notifyDataSetChanged();
            Recent_Adapter.this.updateSearch.search();
        }
    }
}
