package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header.PeopleRepo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header.PersonAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header.RecyclerSectionItemDecoration;

import java.io.File;
import java.util.ArrayList;


public class RecycleviewHeader extends AppCompatActivity {
    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.recyclerview_header);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new PersonAdapter(PeopleRepo.getPeopleSorted(), R.layout.list_item));
        this.recyclerView.addItemDecoration(new RecyclerSectionItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_section_header_height), true, getSectionCallback(new Data_Manager(this).getallfilewithpath(this.path))));
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final ArrayList<File_DTO> arrayList) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int i) {
                return i == 0;
            }

            @Override
            public CharSequence getSectionHeader(int i) {
                return ((File_DTO) arrayList.get(i)).getName();
            }
        };
    }
}
