package com.yoho.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SecondRecyclerAdapter extends RecyclerView.Adapter<SecondRecyclerAdapter.CustomListviewHolder> {


    Context cxt;
    List<ListviewPojo> listView;

    public SecondRecyclerAdapter(Context cxt, List<ListviewPojo> listView) {
        this.cxt = cxt;
        this.listView = listView;
    }
    @NonNull
    @Override
    public CustomListviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.listview_item_forcategory,null);
        return  new CustomListviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomListviewHolder customListviewHolder, int i) {
        ListviewPojo listviewPojoobject=listView.get(i);
        customListviewHolder.image.setImageDrawable(cxt.getDrawable(listviewPojoobject.getImages()));
        customListviewHolder.name.setText(listviewPojoobject.getName());

    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    public class CustomListviewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public CustomListviewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.roundimage);
            name=itemView.findViewById(R.id.image_description);
        }
    }
}
