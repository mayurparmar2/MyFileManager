package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.Edit_MediaActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.Dialog_thread;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.ItemTest;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackUpdateMusic;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class BaseApdapterNewfile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ItemTest> arrAll;
    private ArrayList<ArrayList<File_DTO>> arrayLists;
    private Context context;
    private Data_Manager data_manager;
    private Favourite_Adapter favourite_adapter;
    private ArrayList<File_DTO> file_dtos;
    private ArrayList<File_DTO> flist;
    private ArrayList<ItemTest> list_resulf;
    private ArrayList<File_DTO> s_list;
    private ArrayList<ItemTest> save_list;
    private ArrayList<String> strings;

    public BaseApdapterNewfile(Context context) {
        this.context = context;
        this.data_manager = new Data_Manager(context);
        this.favourite_adapter = new Favourite_Adapter(context, null, "");
        setdata();
        this.save_list = new ArrayList<>();
        this.save_list = this.arrAll;
    }

    public ArrayList<ItemTest> getArrAll() {
        return this.arrAll;
    }

    public void setArrAll(ArrayList<ItemTest> arrayList) {
        this.arrAll = arrayList;
    }

    public ArrayList<ItemTest> getSave_list() {
        return this.save_list;
    }

    public void setSave_list(ArrayList<ItemTest> arrayList) {
        this.save_list = arrayList;
    }

    public void setdata() {
        ArrayList<File_DTO> arrayList = this.data_manager.getallfileindecive();
        this.file_dtos = arrayList;
        Collections.sort(arrayList, new Comparator<File_DTO>() {
            @Override
            public int compare(File_DTO file_DTO, File_DTO file_DTO2) {
                return file_DTO2.getLastmodified().compareTo(file_DTO.getLastmodified());
            }
        });
        getTitleNgay();
        this.flist = getlist();
        this.arrAll = new ArrayList<>();
        Iterator<File_DTO> it = this.flist.iterator();
        while (it.hasNext()) {
            File_DTO next = it.next();
            this.arrAll.add(new ItemTest(next.getTitle(), null));
            Iterator<File_DTO> it2 = next.getFile_dtos().iterator();
            while (it2.hasNext()) {
                this.arrAll.add(new ItemTest(null, it2.next()));
            }
        }
    }

    @Override
    public int getItemViewType(int i) {
        return this.save_list.get(i).getTime() != null ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 1) {
            return new HolderTitle(from.inflate(R.layout.custom_item_header, viewGroup, false));
        }
        return new ViewHolder(from.inflate(R.layout.custom_ittemsong, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final ItemTest itemTest = this.save_list.get(i);
        if (viewHolder instanceof HolderTitle) {
            ((HolderTitle) viewHolder).textView.setText(this.save_list.get(i).getTime());
            return;
        }
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.txt_titile.setText(itemTest.getFile_dto().getName());
        viewHolder2.txt_size.setText(itemTest.getFile_dto().getSize());
        viewHolder2.txt_durian.setVisibility(View.GONE);
        final String lowerCase = itemTest.getFile_dto().getPath().toLowerCase();
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.context, (int) R.style.AppTheme), viewHolder2.imageView);
        popupMenu.getMenuInflater().inflate(R.menu.popmenumusic, popupMenu.getMenu());
        if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains(".jpeg") || lowerCase.contains(".webp")) {
            new LoadImage_piscaso_glide(this.context).LoadImageAlbum(itemTest.getFile_dto().getPath(), viewHolder2.imageView);
        } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(".vob") || lowerCase.contains(".mov")) {
            viewHolder2.imageView.setImageBitmap(new Video_Ultil(this.context).getbitmap(itemTest.getFile_dto().getPath()));
        } else if (lowerCase.contains(".txt")) {
            viewHolder2.imageView.setImageResource(R.drawable.icon_txt);
        } else if (lowerCase.contains(".pdf")) {
            viewHolder2.imageView.setImageResource(R.drawable.icon_pdf);
        } else if (lowerCase.contains(".ppt")) {
            viewHolder2.imageView.setImageResource(R.drawable.icon_ppt);
        } else if (lowerCase.contains(".csv") || lowerCase.contains(".csvs")) {
            viewHolder2.imageView.setImageResource(R.drawable.icon_xls);
        } else if (lowerCase.contains(".docx") || lowerCase.contains(".doc")) {
            viewHolder2.imageView.setImageResource(R.drawable.icon_doc);
        } else if (lowerCase.contains(".apk") || lowerCase.contains(".apks")) {
            viewHolder2.imageView.setImageResource(R.drawable.icon_apk);
        } else if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(".wma") || lowerCase.contains(".m4a")) {
            if (itemTest.getFile_dto().getArtistid() == null || itemTest.getFile_dto().getArtistid().equals("null")) {
                viewHolder2.imageView.setImageResource(R.drawable.button_music);
            } else {
                new LoadImage_piscaso_glide(this.context).setAlbumArt(itemTest.getFile_dto().getArtistid(), viewHolder2.imageView);
            }
        } else {
            viewHolder2.imageView.setImageResource(R.drawable.unknowfile);
        }
        viewHolder2.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.addfv:
                                new Ultil(BaseApdapterNewfile.this.context).addFav(itemTest.getFile_dto());
                                return false;
                            case R.id.delete:
                                new Ultil(BaseApdapterNewfile.this.context).dialogdeletewihtpath(itemTest.getFile_dto());
                                return false;
                            case R.id.info:
                                new Ultil(BaseApdapterNewfile.this.context).showdiloginfo(itemTest.getFile_dto(), "");
                                return false;
                            case R.id.rename:
                                new Ultil(BaseApdapterNewfile.this.context).dialogRename(itemTest.getFile_dto(), "def");
                                return false;
                            case R.id.share:
                                new Ultil(BaseApdapterNewfile.this.context).sharefile(itemTest.getFile_dto());
                                return false;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        viewHolder2.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lowerCase.toLowerCase().contains(".jpg") || lowerCase.toLowerCase().contains(".png") || lowerCase.toLowerCase().contains(".gif") || lowerCase.contains(".tiff") || lowerCase.toLowerCase().contains(".jpeg")) {
                    Intent intent = new Intent(BaseApdapterNewfile.this.context, Edit_MediaActivity.class);
                    intent.putExtra("path", itemTest.getFile_dto().getPath());
                    intent.putExtra("file_dto", itemTest.getFile_dto());
                    intent.putExtra("pos", i);
                    intent.putExtra("hidefile", "newfile");
                    new Ultil(BaseApdapterNewfile.this.context).addRecent(itemTest.getFile_dto());
                    BaseApdapterNewfile.this.context.startActivity(intent);
                    Animatoo.animateSwipeLeft(BaseApdapterNewfile.this.context);
                    return;
                }
                new Ultil(BaseApdapterNewfile.this.context).openFile(new File(itemTest.getFile_dto().getPath()));
            }
        });
        CallbackUpdateMusic.getInstance().setStateListen(new CallbackUpdateMusic.OncustomStateListen() {
            @Override
            public void statechange() {
                final Dialog_thread dialog_thread = new Dialog_thread(BaseApdapterNewfile.this.context);
                dialog_thread.show();
                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        BaseApdapterNewfile.this.file_dtos = BaseApdapterNewfile.this.data_manager.getallfileindecive();
                        Collections.sort(BaseApdapterNewfile.this.file_dtos, new Comparator<File_DTO>() {
                            @Override
                            public int compare(File_DTO file_DTO, File_DTO file_DTO2) {
                                return file_DTO2.getLastmodified().compareTo(file_DTO.getLastmodified());
                            }
                        });
                        BaseApdapterNewfile.this.setdata();
                        BaseApdapterNewfile.this.save_list = BaseApdapterNewfile.this.arrAll;
                        BaseApdapterNewfile.this.notifyDataSetChanged();
                        dialog_thread.dissmiss();
                    }
                };
                handler.removeCallbacksAndMessages(null);
                handler.obtainMessage(111, "parameter").sendToTarget();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.save_list.size();
    }


    class HolderTitle extends RecyclerView.ViewHolder {
        TextView textView;

        public HolderTitle(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.list_item_section_text);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView img_menu;
        RelativeLayout recyclerView;
        TextView txt_durian;
        TextView txt_size;
        TextView txt_titile;

        public ViewHolder(View view) {
            super(view);
            this.txt_titile = (TextView) view.findViewById(R.id.title);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.txt_durian = (TextView) view.findViewById(R.id.txt_durian);
            this.txt_size = (TextView) view.findViewById(R.id.band);
            this.img_menu = (ImageView) view.findViewById(R.id.menu_button);
            this.recyclerView = (RelativeLayout) view.findViewById(R.id.rela_songitem);
        }
    }

    public List<String> getTitleNgay() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < this.file_dtos.size(); i++) {
            String format = DateFormat.getDateInstance(1).format(Long.valueOf(Long.parseLong(this.file_dtos.get(i).getLastmodified())));
            if (!arrayList.contains(format)) {
                arrayList.add(format);
            }
        }
        this.strings = arrayList;
        return arrayList;
    }

    public ArrayList<File_DTO> getlist() {
        this.arrayLists = new ArrayList<>();
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < this.strings.size(); i++) {
            String str = this.strings.get(i);
            ArrayList<File_DTO> arrayList2 = new ArrayList<>();
            for (int i2 = 0; i2 < this.file_dtos.size(); i2++) {
                if (DateFormat.getDateInstance(1).format(Long.valueOf(Long.parseLong(this.file_dtos.get(i2).getLastmodified()))).equals(str)) {
                    arrayList2.add(this.file_dtos.get(i2));
                }
            }
            File_DTO file_DTO = new File_DTO();
            file_DTO.setTitle(str);
            file_DTO.setFile_dtos(arrayList2);
            arrayList.add(file_DTO);
            this.arrayLists.add(arrayList2);
        }
        return arrayList;
    }

    public void search(String str) {
        new BackgroundSearch(str.toLowerCase()).execute(new String[0]);
    }


    public class BackgroundSearch extends AsyncTask<String, Integer, ArrayList<File_DTO>> {
        private String key;
        private ArrayList<ItemTest> listResulf;

        public BackgroundSearch(String str) {
            this.key = str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.listResulf = new ArrayList<>();
            BaseApdapterNewfile.this.setdata();
        }


        @Override
        public ArrayList<File_DTO> doInBackground(String... strArr) {
            BaseApdapterNewfile.this.getTitleNgay();
            BaseApdapterNewfile baseApdapterNewfile = BaseApdapterNewfile.this;
            baseApdapterNewfile.flist = baseApdapterNewfile.getlist();
            this.listResulf = new ArrayList<>();
            Iterator it = BaseApdapterNewfile.this.flist.iterator();
            while (it.hasNext()) {
                File_DTO file_DTO = (File_DTO) it.next();
                if (file_DTO.getTitle().toLowerCase().contains(this.key)) {
                    this.listResulf.add(new ItemTest(file_DTO.getTitle(), null));
                }
                Iterator<File_DTO> it2 = file_DTO.getFile_dtos().iterator();
                while (it2.hasNext()) {
                    File_DTO next = it2.next();
                    if (next.getName().toLowerCase().contains(this.key)) {
                        this.listResulf.add(new ItemTest(null, next));
                    }
                }
            }
            return null;
        }


        @Override
        public void onPostExecute(ArrayList<File_DTO> arrayList) {
            super.onPostExecute(arrayList);
            BaseApdapterNewfile.this.save_list = this.listResulf;
            BaseApdapterNewfile.this.notifyDataSetChanged();
        }
    }
}
