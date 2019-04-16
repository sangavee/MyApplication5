package com.yoho.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.NotifiViewHolder> {

    Context ctx;
    ArrayList<Pojo> arrayList;
    String title,message;

    public NotifiAdapter(Context ctx, ArrayList<Pojo> arrayList) {
        this.ctx = ctx;
        this.arrayList = arrayList;
    }

    @Override
    public NotifiViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(ctx);
        View v =inflater.inflate(R.layout.detail,null);
        NotifiViewHolder notifiViewHolder=new NotifiViewHolder(v);
        return notifiViewHolder;
    }

    @Override
    public void onBindViewHolder( NotifiViewHolder notifiViewHolder, int i) {
notifiViewHolder.title.setText(arrayList.get(i).getTitle());
notifiViewHolder.message.setText(arrayList.get(i).getMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NotifiViewHolder extends  RecyclerView.ViewHolder {

       TextView title,message;
        public NotifiViewHolder( View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            message=itemView.findViewById(R.id.message);
        }
    }
}
