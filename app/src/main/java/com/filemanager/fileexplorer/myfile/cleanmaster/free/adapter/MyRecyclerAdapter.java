package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
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


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private Data_Manager dataManager;
    private int n;
    private SparseBooleanArray selectionState = new SparseBooleanArray();

    public MyRecyclerAdapter(Data_Manager data_Manager, Context context) {
        this.dataManager = data_Manager;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_itemfolder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        String str;
        myViewHolder.imageView.setImageResource(this.dataManager.getIconId(i));
        myViewHolder.title.setText(this.dataManager.getName(i));
        int totalItem = this.dataManager.getTotalItem(i);
        this.n = totalItem;
        if (totalItem > 1) {
            str = this.n + " files";
        } else {
            str = this.n + " file";
        }
        try {
            myViewHolder.date.setText(this.dataManager.getsize(i) + "-" + this.dataManager.getDate_and_time(i) + "-" + str);
        } catch (Exception unused) {
            Log.e("AAAA", "onBindViewHolder: ");
        }
        myViewHolder.itemView.setActivated(this.selectionState.get(i, false));
        new BackImageLoading(i).execute(myViewHolder);
        if (this.selectionState.get(i, false)) {
            myViewHolder.img_check.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.img_check.setVisibility(View.GONE);
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


    public class BackImageLoading extends AsyncTask<MyViewHolder, MyViewHolder, Object> {
        Bitmap bitmap;
        private int i;

        BackImageLoading(int i) {
            this.i = i;
        }


        @Override
        public Object doInBackground(MyViewHolder... myViewHolderArr) {
            if (MyRecyclerAdapter.this.dataManager.getFiles(this.i).getPath().toLowerCase().contains(".jpg") || MyRecyclerAdapter.this.dataManager.getFiles(this.i).getPath().toLowerCase().endsWith(".png") || MyRecyclerAdapter.this.dataManager.getFiles(this.i).getPath().toLowerCase().contains(".jpeg") || MyRecyclerAdapter.this.dataManager.getFiles(this.i).getPath().endsWith(".JPEG")) {
                if (MyRecyclerAdapter.this.dataManager.getFiles(this.i).length() > 120000) {
                    long j = 0;
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    Cursor query = MyRecyclerAdapter.this.context.getContentResolver().query(uri, new String[]{"_data", "_id"}, null, null, null);
                    query.moveToFirst();
                    while (true) {
                        if (query.getString(query.getColumnIndexOrThrow("_data")).equals(MyRecyclerAdapter.this.dataManager.getFiles(this.i).getPath())) {
                            j = query.getLong(query.getColumnIndexOrThrow("_id"));
                            break;
                        } else if (!query.moveToNext()) {
                            break;
                        }
                    }
                    query.close();
                    this.bitmap = MediaStore.Images.Thumbnails.getThumbnail(MyRecyclerAdapter.this.context.getContentResolver(), j, 1, null);
                }
                if (this.bitmap == null) {
                    this.bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(MyRecyclerAdapter.this.dataManager.getFiles(this.i).getPath()), 192, 192);
                }
                publishProgress(myViewHolderArr);
            }
            return null;
        }


        @Override
        public void onProgressUpdate(MyViewHolder... myViewHolderArr) {
            myViewHolderArr[0].imageView.setImageBitmap(this.bitmap);
        }
    }
}
