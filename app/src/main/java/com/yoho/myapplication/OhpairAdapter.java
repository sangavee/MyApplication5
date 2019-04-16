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

public class OhpairAdapter extends RecyclerView.Adapter<OhpairAdapter.HomeCustomViewHolder> {
    private Context cxt;
    private List<OhpairPojo> productList;
    RequestQueue requestQueue1;
    String id;

    public OhpairAdapter(Context cxt, List<OhpairPojo> productList) {
        this.cxt = cxt;
        this.productList = productList;
    }

    @NonNull
    @Override
    public HomeCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.home_grid,null);
        return new HomeCustomViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull HomeCustomViewHolder homeCustomViewHolder, final int i) {
        final OhpairPojo product=productList.get(i);

        Picasso.get().load(product.getProductimage()).into(homeCustomViewHolder.pimage);
        //viewHolder.pimage.setImageDrawable(cxt.getDrawable(product.getProductimage()));
        homeCustomViewHolder.prodname.setText(product.getProductname());
        homeCustomViewHolder.brand.setText(product.getBrand());
        //homeCustomViewHolder.productprice.setText(product.getProductprice());
        homeCustomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category=productList.get(i).getProductname();
              //  Config.THEME_NAME=product.getBrand();

                //System.out.println("id from recycler view"+id);
                Intent i=new Intent(cxt,SareesCategoryActivity.class);
               // i.putExtra("id",product.getId());
                Config.CATEGORY_NAME=category;
               i.putExtra("category",category);
                Config.THEME_NAME="ohpair";
                cxt.startActivity(i);

            }


        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HomeCustomViewHolder extends RecyclerView.ViewHolder
    {
        TextView prodname,brand;
        TextView productprice;
        ImageView pimage;
        CardView cardView;
        int quantity=0;

        public HomeCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            pimage=(ImageView)itemView.findViewById(R.id.productimage);
            prodname=(TextView)itemView.findViewById(R.id.productname);
            productprice=(TextView)itemView.findViewById(R.id.productprice);
            brand=(TextView)itemView.findViewById(R.id.brand);

            if(Config.THEME_NAME.equals("ohpair")) {
                productprice.setTextColor(Color.parseColor("#8CCB2E"));

            }
        }

    }
}
