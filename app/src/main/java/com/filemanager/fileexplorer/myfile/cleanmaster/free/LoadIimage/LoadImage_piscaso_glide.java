package com.filemanager.fileexplorer.myfile.cleanmaster.free.LoadIimage;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LoadImage_piscaso_glide {
    private Context context;

    public void LoadImageAlbumwithBitmap(Bitmap bitmap, ImageView imageView) {
    }

    public LoadImage_piscaso_glide(Context context) {
        this.context = context;
    }

    public void setAlbumArt(String str, ImageView imageView) {
        try {
            Picasso.get().load(getSongUri(Long.valueOf(Long.parseLong(str)))).placeholder(this.context.getDrawable(R.drawable.button_music)).centerCrop().resize(300, 300).onlyScaleDown().into(imageView);
        } catch (Exception unused) {
        }
    }

    private Uri getSongUri(Long l) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), l.longValue());
    }

    public void setAlbumArt(ArrayList<File_DTO> arrayList, ImageView imageView) {
        if (arrayList.size() > 0) {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList2.add(arrayList.get(i).getAbumid());
                if (i == 20) {
                    break;
                }
            }
            setAlbumArt(arrayList2, imageView, 0, arrayList2.size() - 1);
            return;
        }
        imageView.setImageDrawable(this.context.getDrawable(R.drawable.button_music));
    }


    public void setAlbumArt(final List list, final ImageView imageView, final int i, final int i2) {
        try {
            if (i < i2) {
                Picasso.get().load(getSongUri(Long.valueOf(Long.parseLong(list.get(i).toString())))).placeholder(this.context.getDrawable(R.drawable.button_music)).centerCrop().resize(300, 300).onlyScaleDown().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception exc) {
                        LoadImage_piscaso_glide.this.setAlbumArt(list, imageView, i + 1, i2);
                    }
                });
            } else if (i != i2) {
            } else {
                Picasso.get().load(getSongUri(Long.valueOf(Long.parseLong(list.get(i).toString())))).placeholder(this.context.getDrawable(R.drawable.button_music)).into(imageView);
            }
        } catch (Exception unused) {
        }
    }

    public void LoadImageAlbum(String str, ImageView imageView) {
        try {
            Picasso.get().load(Uri.fromFile(new File(str))).placeholder(this.context.getDrawable(R.drawable.imageicon)).centerCrop().resize(300, 300).onlyScaleDown().into(imageView);
        } catch (Exception unused) {
        }
    }

    public void LoadZoomView(String str, PhotoView photoView) {
        try {
            Picasso.get().load(Uri.fromFile(new File(str))).placeholder(this.context.getDrawable(R.drawable.button_music)).centerCrop().resize(1000, 1000).onlyScaleDown().into(photoView);
        } catch (Exception unused) {
        }
    }
}
