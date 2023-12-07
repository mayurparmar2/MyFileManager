package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.Edit_MediaActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage.LoadImage_piscaso_glide;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;

import java.io.File;
import java.util.ArrayList;


public class ClearApdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private String check;
    private String checkintent;
    private Context context;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private Onclick onClick;
    public String path;
    private int pos;
    public ArrayList<String> posadd;


    public interface Onclick {
        void iconchekClick(int i);
    }

    public ArrayList<String> getPosadd() {
        return this.posadd;
    }

    public void setPosadd(ArrayList<String> arrayList) {
        this.posadd = arrayList;
    }

    public ClearApdapter(Context context, ActivityResultLauncher<IntentSenderRequest> activityResultLauncher, Onclick onclick, String str) {
        super(R.layout.custom_ittemsong);
        this.posadd = new ArrayList<>();
        this.check = "";
        this.context = context;
        this.launcher = activityResultLauncher;
        this.onClick = onclick;
        this.checkintent = str;
    }


    @Override
    public void convert(final BaseViewHolder baseViewHolder, final File_DTO file_DTO) {
        ImageView imageView = (ImageView) baseViewHolder.itemView.findViewById(R.id.image);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.itemView.findViewById(R.id.rela_songitem);
        ImageView imageView2 = (ImageView) baseViewHolder.itemView.findViewById(R.id.menu_button);
        ImageView imageView3 = (ImageView) baseViewHolder.itemView.findViewById(R.id.img_check);
        imageView3.setVisibility(View.VISIBLE);
        TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.title);
        TextView textView2 = (TextView) baseViewHolder.itemView.findViewById(R.id.band);
        TextView textView3 = (TextView) baseViewHolder.itemView.findViewById(R.id.txt_durian);
        imageView3.setImageResource(R.drawable.esclip);
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
        if (file_DTO != null) {
            final String lowerCase = file_DTO.getPath().toLowerCase();
            if (lowerCase.contains(".mp3") || lowerCase.contains(".wav") || lowerCase.contains(".flac") || lowerCase.contains(".wma") || lowerCase.contains(".mp4a")) {
                imageView.setImageResource(R.drawable.button_music);
            } else if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".gif") || lowerCase.contains(".tiff") || lowerCase.contains(".jpeg") || this.checkintent.contains(".webp")) {
                new LoadImage_piscaso_glide(this.context).LoadImageAlbum(file_DTO.getPath(), imageView);
            } else if (lowerCase.contains(".mp4") || lowerCase.contains(".avi") || lowerCase.contains(".mkv") || lowerCase.contains(".vob") || lowerCase.contains(".mov")) {
                new loadbitmap(file_DTO).execute(baseViewHolder);
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
            if (this.checkintent.equals("largefile")) {
                textView2.setText(new Ultil(this.context).bytesToHuman(Long.parseLong(file_DTO.getSize())) + ":" + file_DTO.getDate());
            } else {
                textView2.setText(file_DTO.getSize() + ":" + file_DTO.getDate());
            }
            textView.setText(file_DTO.getName());
            textView3.setText("");
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClearApdapter.this.pos = baseViewHolder.getLayoutPosition();
                    if (ClearApdapter.this.posadd.size() == 0) {
                        ArrayList<String> arrayList2 = ClearApdapter.this.posadd;
                        arrayList2.add("" + ClearApdapter.this.pos);
                    } else {
                        ArrayList<String> arrayList3 = ClearApdapter.this.posadd;
                        if (arrayList3.contains("" + ClearApdapter.this.pos)) {
                            ArrayList<String> arrayList4 = ClearApdapter.this.posadd;
                            arrayList4.remove("" + ClearApdapter.this.pos);
                        } else {
                            ArrayList<String> arrayList5 = ClearApdapter.this.posadd;
                            arrayList5.add("" + ClearApdapter.this.pos);
                        }
                    }
                    ClearApdapter.this.notifyDataSetChanged();
                    ClearApdapter.this.onClick.iconchekClick(baseViewHolder.getLayoutPosition());
                }
            });
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(file_DTO.getPath());
                    if (lowerCase.toLowerCase().contains(".jpg") || lowerCase.toLowerCase().contains(".png") || lowerCase.toLowerCase().contains(".gif") || lowerCase.contains(".tiff") || lowerCase.toLowerCase().contains(".jpeg")) {
                        Intent intent = new Intent(ClearApdapter.this.context, Edit_MediaActivity.class);
                        intent.putExtra("path", "recycle");
                        intent.putExtra("file_dto", file_DTO);
                        if (ClearApdapter.this.checkintent.equals("largefile")) {
                            intent.putExtra("hidefile", "largefile");
                        } else {
                            intent.putExtra("hidefile", "download");
                        }
                        intent.putExtra("pos", baseViewHolder.getLayoutPosition());
                        new Ultil(ClearApdapter.this.context).addRecent(file_DTO);
                        ClearApdapter.this.context.startActivity(intent);
                        Animatoo.animateSwipeLeft(ClearApdapter.this.context);
                        return;
                    }
                    new Ultil(ClearApdapter.this.context).openFile(file);
                }
            });
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

    public void clearFilechose() {
        this.posadd.clear();
        notifyDataSetChanged();
    }

    public ArrayList<File_DTO> getfilechose() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        for (int i = 0; i < this.posadd.size(); i++) {
            arrayList.add(getData().get(Integer.parseInt(this.posadd.get(i))));
        }
        Log.d("TAG", "getfilechose: " + arrayList.size());
        return arrayList;
    }


    public class loadbitmap extends AsyncTask<BaseViewHolder, BaseViewHolder, Object> {
        Bitmap bitmap;
        File_DTO file_dto;

        public loadbitmap(File_DTO file_DTO) {
            this.file_dto = file_DTO;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        public Bitmap doInBackground(BaseViewHolder... baseViewHolderArr) {
            this.bitmap = new Video_Ultil(ClearApdapter.this.context).getbitmap(this.file_dto.getPath());
            publishProgress(baseViewHolderArr);
            return null;
        }


        @Override
        public void onProgressUpdate(BaseViewHolder... baseViewHolderArr) {
            super.onProgressUpdate( baseViewHolderArr);
            ((ImageView) baseViewHolderArr[0].itemView.findViewById(R.id.image)).setImageBitmap(this.bitmap);
        }
    }
}
