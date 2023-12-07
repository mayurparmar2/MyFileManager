package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private Data_Manager data_manager;
    ArrayList<File_DTO> list;
    private ArrayList<String> strings;

    private boolean isPositionHeader(int i) {
        return true;
    }

    public MyRecyclerAdapter(Context context) {
        this.context = context;
        Data_Manager data_Manager = new Data_Manager(context);
        this.data_manager = data_Manager;
        this.list = data_Manager.getallfileindecive();
        this.strings = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            return new VHHeader(from.inflate(R.layout.custom_item_header, viewGroup, false));
        }
        return new VHItem(from.inflate(R.layout.custom_ittem_recent, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        getTitleNgay(this.list);
        if (viewHolder instanceof VHHeader) {
            ((VHHeader) viewHolder).txtTitle.setText(this.strings.get(i));
        } else if (viewHolder instanceof VHItem) {
            VHItem vHItem = (VHItem) viewHolder;
        }
    }

    @Override
    public int getItemViewType(int i) {
        return isPositionHeader(i) ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return this.strings.size();
    }

    public ArrayList<File_DTO> getallfile() {
        ArrayList<File_DTO> arrayList = this.data_manager.getallfileindecive();
        this.list = arrayList;
        return arrayList;
    }


    class VHHeader extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public VHHeader(View view) {
            super(view);
            this.txtTitle = (TextView) view.findViewById(R.id.list_item_section_text);
        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        TextView txtName;

        public VHItem(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.title);
        }
    }

    public List<String> getTitleNgay(List<File_DTO> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String format = DateFormat.getDateInstance(1).format(Long.valueOf(Long.parseLong(list.get(i).getLastmodified())));
            if (!arrayList.contains(format)) {
                arrayList.add(format);
            }
        }
        this.strings = arrayList;
        return arrayList;
    }

    public ArrayList<File_DTO> getlist() {
        ArrayList arrayList = new ArrayList();
        List<String> titleNgay = getTitleNgay(this.list);
        new ArrayList();
        ArrayList<File_DTO> arrayList2 = new ArrayList<>();
        for (int i = 0; i < titleNgay.size(); i++) {
            File_DTO file_DTO = new File_DTO();
            String str = titleNgay.get(i);
            ArrayList<File_DTO> arrayList3 = new ArrayList<>();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (DateFormat.getDateInstance(1).format(Long.valueOf(Long.parseLong(((File_DTO) arrayList.get(i2)).getLastmodified()))).equals(str)) {
                    arrayList3.add((File_DTO) arrayList.get(i2));
                }
            }
            file_DTO.setTitle(str);
            file_DTO.setFile_dtos(arrayList3);
            arrayList2.add(file_DTO);
        }
        return arrayList2;
    }
}
