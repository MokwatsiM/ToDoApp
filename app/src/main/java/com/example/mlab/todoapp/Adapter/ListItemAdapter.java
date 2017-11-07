package com.example.mlab.todoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mlab.todoapp.MainActivity;
import com.example.mlab.todoapp.R;
import com.example.mlab.todoapp.model.ToDo;

import java.util.List;

/**
 * Created by mLab on 2017/11/06.
 */


class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    ItemCLickListener itemCLickListener;
    TextView item_title, item_description;


    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = itemView.findViewById(R.id.item_title);
        item_description = itemView.findViewById(R.id.item_description);

    }

    @Override
    public void onClick(View v) {
        itemCLickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setItemCLickListener(ItemCLickListener itemCLickListener) {
        this.itemCLickListener = itemCLickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select A title");
        menu.add(0, 0, getAdapterPosition(), "DELETE");
    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    List<ToDo> toDoList;

    MainActivity mainActivity;

    public ListItemAdapter(MainActivity mainActivity, List<ToDo> toDoList) {
        this.mainActivity = mainActivity;
        this.toDoList = toDoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity.getApplicationContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);

        ListItemViewHolder holder = new ListItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        ToDo toDo = toDoList.get(position);

        holder.item_title.setText(toDo.getTitle());
        holder.item_description.setText(toDo.getDescription());


        holder.setItemCLickListener(new ItemCLickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                ToDo toDo = toDoList.get(position);

                mainActivity.title.setText(toDo.getTitle());
                mainActivity.description.setText(toDo.getDescription());


                mainActivity.isUpdate = true; //set flag is update = true
                mainActivity.idUpdate = toDoList.get(position).getId();

            }
        });

    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
