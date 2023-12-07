package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
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
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Image_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;

import java.util.ArrayList;
import java.util.Iterator;


public class ImageAlbumAdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private Context context;
    private Image_Ultil image_ultil;
    private ActivityResultLauncher<IntentSenderRequest> laucher;
    private ArrayList<File_DTO> listRemove;
    private ArrayList<File_DTO> list_resulf;
    protected OnClickImageAlbum onClick;
    private ArrayList<File_DTO> save_list;


    public interface OnClickImageAlbum {
        void itemclickimg_album(int i);

        void menuclick_album(int i);
    }

    public ArrayList<File_DTO> getSave_list() {
        return this.save_list;
    }

    public void setSave_list(ArrayList<File_DTO> arrayList) {
        this.save_list = arrayList;
    }

    public ImageAlbumAdapter(OnClickImageAlbum onClickImageAlbum, Context context, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher) {
        super(R.layout.custom_itemabumvideo);
        this.onClick = onClickImageAlbum;
        this.context = context;
        this.laucher = activityResultLauncher;
        this.image_ultil = new Image_Ultil(context);
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_info);
        final PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.context, (int) R.style.AppTheme), imageView);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenuabumimage, popupMenu.getMenu());
        ((ImageView) baseViewHolder.itemView.findViewById(R.id.img_play)).setVisibility(View.GONE);
        new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), (ImageView) baseViewHolder.itemView.findViewById(R.id.img_album));
        ((TextView) baseViewHolder.itemView.findViewById(R.id.txt_nameAlbum)).setText(file_DTO.getAbumname());
        ((TextView) baseViewHolder.itemView.findViewById(R.id.txt_count)).setText(String.valueOf(file_DTO.getTotalitem()));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageAlbumAdapter.this.onClick.menuclick_album(baseViewHolder.getLayoutPosition());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.delete) {
                            ImageAlbumAdapter.this.listRemove = new Image_Ultil(ImageAlbumAdapter.this.context).delteabum(file_DTO.getAbumname());
                            new Ultil(ImageAlbumAdapter.this.context).DeleteAlbum(ImageAlbumAdapter.this.listRemove);
                            return true;
                        } else if (itemId == R.id.info) {
                            new Ultil(ImageAlbumAdapter.this.context).showdiloginfo(file_DTO, "almumimage");
                            return true;
                        } else if (itemId != R.id.rename) {
                            return true;
                        } else {
                            new Ultil(ImageAlbumAdapter.this.context).dialogRenamAlbumImage(file_DTO.getPath());
                            return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        ((RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_Image_Album)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageAlbumAdapter.this.onClick.itemclickimg_album(baseViewHolder.getLayoutPosition());
            }
        });
    }

    public void search(String str) {
        this.save_list = this.image_ultil.updatealbumimage();
        this.list_resulf = new ArrayList<>();
        if (!this.save_list.isEmpty()) {
            Iterator<File_DTO> it = this.save_list.iterator();
            while (it.hasNext()) {
                File_DTO next = it.next();
                if (next.getAbumname().toLowerCase().contains(str.toLowerCase())) {
                    this.list_resulf.add(next);
                }
            }
            if (str.length() == 0) {
                setList(this.save_list);
            } else {
                setList(this.list_resulf);
            }
            notifyDataSetChanged();
            return;
        }
        Toast.makeText(this.context, "list empty", Toast.LENGTH_SHORT).show();
    }
}
