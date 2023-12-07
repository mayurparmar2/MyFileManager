package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;

import java.util.List;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private final List<Person> people;
    private final int rowLayout;

    public PersonAdapter(List<Person> list, int i) {
        this.people = list;
        this.rowLayout = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.rowLayout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.fullName.setText(this.people.get(i).getFullName());
    }

    @Override
    public int getItemCount() {
        return this.people.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullName;

        public ViewHolder(View view) {
            super(view);
            this.fullName = (TextView) view.findViewById(R.id.tv_text);
        }
    }
}
