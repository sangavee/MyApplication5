package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.HomeCustomViewHolder>{
    private Context cxt;
    private List<CategoryPojo> productList;

    public CategoryAdapter(Context cxt, List<CategoryPojo> productList) {
        this.cxt = cxt;
        this.productList = productList;
    }

    @NonNull
    @Override
    public HomeCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.category_list,null);
        return new HomeCustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCustomViewHolder homeCustomViewHolder, final int i) {
        CategoryPojo product=productList.get(i);
        Picasso.get().load(product.getCategoryimage()).into(homeCustomViewHolder.categoryimage);
        homeCustomViewHolder.categoryname.setText(product.getCategoryname());

        homeCustomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=productList.get(i).getCategoryname();
                Intent i=new Intent(cxt,SareesCategoryActivity.class);

                Config.CATEGORY_NAME=category;
                Config.THEME_NAME="yoshna";
              //  i.putExtra("class","yoshna");
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

        TextView categoryname;
        ImageView categoryimage;

        public HomeCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryimage=(ImageView)itemView.findViewById(R.id.category_images);
            categoryname=(TextView)itemView.findViewById(R.id.category_name);



        }

    }


}
