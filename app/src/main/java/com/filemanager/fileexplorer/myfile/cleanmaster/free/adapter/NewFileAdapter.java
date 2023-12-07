package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class NewFileAdapter extends BaseQuickAdapter<File_DTO, BaseViewHolder> {
    private Context context;
    private Data_Manager data_manager;
    private Favourite_Adapter favourite_adapter;
    private ArrayList<File_DTO> file_dtos;
    private ActivityResultLauncher<IntentSenderRequest> laucher;
    private ArrayList<String> strings;

    public NewFileAdapter(Context context) {
        super(R.layout.item_adapternewfile);
        this.context = context;
        Data_Manager data_Manager = new Data_Manager(context);
        this.data_manager = data_Manager;
        this.file_dtos = data_Manager.getallfileindecive();
        this.favourite_adapter = new Favourite_Adapter(context, null, "");
        getTitleNgay();
    }


    @Override
    public void convert(BaseViewHolder baseViewHolder, File_DTO file_DTO) {
        RecyclerView recyclerView = (RecyclerView) baseViewHolder.itemView.findViewById(R.id.recycler_viewSM);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.VERTICAL, false));
        ((TextView) baseViewHolder.itemView.findViewById(R.id.txt_date)).setText(file_DTO.getTitle());
        this.favourite_adapter.setList(getlist().get(baseViewHolder.getLayoutPosition()).getFile_dtos());
        recyclerView.setAdapter(this.favourite_adapter);
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

    private ArrayList<File_DTO> list(List<String> list, int i) {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        String str = list.get(i);
        new ArrayList();
        for (int i2 = 0; i2 < this.file_dtos.size(); i2++) {
            if (str.equals(DateFormat.getDateInstance(1).format(Long.valueOf(Long.parseLong(this.file_dtos.get(i2).getLastmodified()))))) {
                arrayList.add(this.file_dtos.get(i2));
            }
        }
        return arrayList;
    }

    public ArrayList<File_DTO> getlist() {
        new ArrayList();
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
        }
        return arrayList;
    }
}
