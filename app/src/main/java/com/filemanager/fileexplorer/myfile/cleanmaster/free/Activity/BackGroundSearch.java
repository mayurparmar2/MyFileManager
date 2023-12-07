package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.os.AsyncTask;
import android.util.Log;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.updatelist;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.GirlAdapter.GridAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.MyRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BackGroundSearch extends AsyncTask<String, Integer, List<File>> {
    private Data_Manager data_manager;
    private GridAdapter gridAdapter;
    private MyRecyclerAdapter myRecyclerAdapter;
    public List<File> searchResults;
    public updatelist updatelist;

    public updatelist getUpdatelist() {
        return this.updatelist;
    }

    public void setUpdatelist(updatelist updatelistVar) {
        this.updatelist = updatelistVar;
    }

    public List<File> getSearchResults() {
        return this.searchResults;
    }

    public void setSearchResults(List<File> list) {
        this.searchResults = list;
    }


    public BackGroundSearch(Data_Manager data_Manager, GridAdapter gridAdapter, MyRecyclerAdapter myRecyclerAdapter) {
        this.data_manager = data_Manager;
        this.gridAdapter = gridAdapter;
        this.myRecyclerAdapter = myRecyclerAdapter;
    }


    @Override
    public List<File> doInBackground(String... strArr) {
        findFile(strArr[1], new File(strArr[0]));
        return this.searchResults;
    }

    @Override
    protected void onPreExecute() {
        this.searchResults = new ArrayList();
        super.onPreExecute();
    }


    @Override
    public void onPostExecute(List<File> list) {
        this.data_manager.setSearchResults(list);
        if (ResulfActivity.gridView) {
            this.gridAdapter.notifyDataSetChanged();
        } else {
            this.myRecyclerAdapter.notifyDataSetChanged();
        }
        this.updatelist.update(this.searchResults.size());
    }

    private void findFile(String str, File file) {
        int i = 0;
        if (str.length() == 0) {
            File[] listFiles = ResulfActivity.path.listFiles();
            while (i < listFiles.length) {
                this.searchResults.add(listFiles[i]);
                i++;
            }
            return;
        }
        File[] listFiles2 = file.listFiles();
        int length = listFiles2.length;
        while (i < length) {
            File file2 = listFiles2[i];
            if (file2.isDirectory()) {
                if (file2.getPath().toLowerCase().contains(str)) {
                    this.searchResults.add(file2);
                    if (this.searchResults.size() > 250) {
                        return;
                    }
                }
                try {
                    findFile(str, file2);
                } catch (Exception e) {
                    Log.d("TAG", "findFile: " + e.getMessage());
                }
            } else if (file2.getPath().toLowerCase().contains(str)) {
                this.searchResults.add(file2);
                if (this.searchResults.size() > 250) {
                    return;
                }
            } else {
                continue;
            }
            i++;
        }
    }
}
