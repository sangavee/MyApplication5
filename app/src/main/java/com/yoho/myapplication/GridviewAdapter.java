package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GridviewAdapter extends RecyclerView.Adapter<GridviewAdapter.HomeCustomViewHolder>{
    private Context cxt;
    private List<GridviewProduct> productList;
    RequestQueue requestQueue1;
String id,img_url;

    public GridviewAdapter(Context cxt, List<GridviewProduct> productList) {
        this.cxt = cxt;
        this.productList = productList;
    }


    @NonNull
    @Override
    public HomeCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.home_grid,null);
        return new HomeCustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCustomViewHolder viewHolder, final int i) {

        final GridviewProduct product=productList.get(i);
        Picasso.get().load(product.getProductimage()).into(viewHolder.pimage);
        //viewHolder.pimage.setImageDrawable(cxt.getDrawable(product.getProductimage()));
    viewHolder.prodname.setText(product.getProductname());
       viewHolder.productprice.setText(product.getProductprice());
       viewHolder.brand.setText(product.getBrand());

       viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               id = productList.get(i).getId();
               img_url = productList.get(i).getProductimage();
               Config.THEME_NAME = product.getBrand();
               System.out.println("id from recycler view" + id);
               if (productList.get(i).getCategories().isEmpty())
               {
                   Toast.makeText(cxt, "No items", Toast.LENGTH_SHORT).show();
               }
               else {

                   if (productList.get(i).getCategories().equalsIgnoreCase("Saree")) {
                       Intent i1 = new Intent(cxt, DetailedActivity.class);
                       i1.putExtra("id", id);
                       i1.putExtra("img_url", img_url);
                       cxt.startActivity(i1);
                   } else {

                       Intent i = new Intent(cxt, DetailedDressActivity.class);
                       i.putExtra("id", id);
                       i.putExtra("img_url", img_url);
                       cxt.startActivity(i);
                   }

               }
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
