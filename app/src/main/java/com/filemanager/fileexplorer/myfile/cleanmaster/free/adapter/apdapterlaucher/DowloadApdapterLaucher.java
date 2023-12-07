package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.apdapterlaucher;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;


public class DowloadApdapterLaucher extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private Context context;

    public DowloadApdapterLaucher(Context context) {
        super(R.layout.custom_dowloadadapter_laucher);
        this.context = context;
    }


    @Override
    public void convert(BaseViewHolder baseViewHolder, File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_e);
        if (file_DTO != null) {
            String path = file_DTO.getPath();
            if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac") || path.endsWith(".wma") || path.endsWith(".mp4a")) {
                new LoadImage_piscaso_glide(this.context).setAlbumArt(file_DTO.getAbumid(), imageView);
            } else if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".tiff") || path.endsWith(".jpeg")) {
                new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            } else if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".mkv") || path.endsWith(".vob") || path.endsWith(".mov")) {
                imageView.setImageBitmap(new Video_Ultil(this.context).getbitmap(file_DTO.getPath()));
            } else if (path.endsWith(".txt")) {
                imageView.setImageResource(R.drawable.icon_txt);
            } else if (path.endsWith(".doc") || path.endsWith(".docx")) {
                imageView.setImageResource(R.drawable.icon_doc);
            } else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
                imageView.setImageResource(R.drawable.icon_ppt);
            } else if (path.endsWith(".pdf")) {
                imageView.setImageResource(R.drawable.icon_pdf);
            } else if (path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".csv") || path.endsWith(".csvx")) {
                imageView.setImageResource(R.drawable.icon_xls);
            } else if (path.endsWith(".zip") || path.endsWith(".rar")) {
                imageView.setImageResource(R.drawable.icon_zip);
            } else if (path.endsWith(".apk") || path.endsWith(".apks")) {
                imageView.setImageResource(R.drawable.icon_apk);
            } else {
                imageView.setImageResource(R.drawable.unknowfile);
            }
        }
    }
}
