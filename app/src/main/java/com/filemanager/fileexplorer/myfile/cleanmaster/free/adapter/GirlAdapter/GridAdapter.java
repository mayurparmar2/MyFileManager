package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.GirlAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {
    Context context;
    private Data_Manager dataManager;
    private SparseBooleanArray selectionState = new SparseBooleanArray();

    public GridAdapter(Data_Manager data_Manager, Context context) {
        this.dataManager = data_Manager;
        this.context = context;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GridViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_girl, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(GridViewHolder gridViewHolder, int i) {
        if (this.dataManager.getName(i).length() > 12) {
            gridViewHolder.textView.setText(this.dataManager.getName(i).substring(0, 11));
        } else {
            gridViewHolder.textView.setText(this.dataManager.getName(i));
        }
        gridViewHolder.imageView.setImageResource(this.dataManager.getIconId(i));
        gridViewHolder.itemView.setActivated(this.selectionState.get(i, false));
        new BackImageLoading(i).execute(gridViewHolder);
        if (this.selectionState.get(i, false)) {
            gridViewHolder.img_check.setVisibility(View.VISIBLE);
        } else {
            gridViewHolder.img_check.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.dataManager.name.size();
    }

    public void toggleSelection(int i) {
        if (this.selectionState.get(i, false)) {
            this.selectionState.delete(i);
        } else {
            this.selectionState.put(i, true);
        }
        notifyItemChanged(i);
    }

    public void clearSelection() {
        this.selectionState.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return this.selectionState.size();
    }

    public List<File> getSelectedItemsFile() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.selectionState.size(); i++) {
            arrayList.add(this.dataManager.getFiles(this.selectionState.keyAt(i)));
        }
        return arrayList;
    }


    public class BackImageLoading extends AsyncTask<GridViewHolder, GridViewHolder, Object> {
        Bitmap bitmap;
        private int i;

        BackImageLoading(int i) {
            this.i = i;
        }


        @Override
        public Object doInBackground(GridViewHolder... gridViewHolderArr) {
            String lowerCase = GridAdapter.this.dataManager.getFiles(this.i).getPath().toLowerCase();
            if (lowerCase.contains(".jpg") || lowerCase.contains(".png") || lowerCase.contains(".jpeg") || lowerCase.contains(".gif") || lowerCase.contains(".webp")) {
                if (GridAdapter.this.dataManager.getFiles(this.i).length() > 120000) {
                    long j = 0;
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    Cursor query = GridAdapter.this.context.getContentResolver().query(uri, new String[]{"_data", "_id"}, null, null, null);
                    query.moveToFirst();
                    while (true) {
                        if (query.getString(query.getColumnIndexOrThrow("_data")).equals(GridAdapter.this.dataManager.getFiles(this.i).getPath())) {
                            j = query.getLong(query.getColumnIndexOrThrow("_id"));
                            break;
                        } else if (!query.moveToNext()) {
                            break;
                        }
                    }
                    this.bitmap = MediaStore.Images.Thumbnails.getThumbnail(GridAdapter.this.context.getContentResolver(), j, 1, null);
                }
                if (this.bitmap == null) {
                    this.bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(GridAdapter.this.dataManager.getFiles(this.i).getPath()), 192, 192);
                }
                publishProgress(gridViewHolderArr);
            }
            return null;
        }


        @Override
        public void onProgressUpdate(GridViewHolder... gridViewHolderArr) {
            gridViewHolderArr[0].imageView.setImageBitmap(this.bitmap);
        }
    }
}
