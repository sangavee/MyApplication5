package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Size_adpater extends RecyclerView.Adapter<Size_adpater.HomeCustomViewHolder>{
    private Context cxt;
    private List productList;
    RequestQueue requestQueue1;
    String id,img_url;
    Boolean b=true;

    int touch_count=0;

   static String size_result="";


    public Size_adpater(Context cxt, List productList) {
        this.cxt = cxt;
        this.productList = productList;
    }


    @NonNull
    @Override
    public HomeCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.detailed_dress_adapter,null);
        return new HomeCustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCustomViewHolder viewHolder, final int i) {

       // final GridviewProduct product=productList.get(i);

        viewHolder.size.setText(productList.get(i).toString());

        viewHolder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               size_result= (String) viewHolder.size.getText();
               if(touch_count%2==0)
               {
                   viewHolder.size.setBackgroundResource(R.drawable.textview_dark_border);
                   touch_count++;
               }
               else
               {
                   viewHolder.size.setBackgroundResource(R.drawable.textview_border);
                   touch_count++;
               }
              // background_change();
            }
        });


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HomeCustomViewHolder extends RecyclerView.ViewHolder
    {
        TextView size;

        public HomeCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            size=(TextView) itemView.findViewById(R.id.s_size);


        }


    }


}

