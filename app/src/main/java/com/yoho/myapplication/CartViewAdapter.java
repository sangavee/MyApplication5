package com.yoho.myapplication;

import android.content.Context;
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

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartCustomViewHolder>{

String id_data;
    int total_price_value=0;
    private Context cxt;
    public List<CartPojo> cartlist;
    RequestQueue requestQueue;

    public CartViewAdapter(Context cxt, List<CartPojo> cartlist) {
        this.cxt = cxt;
        this.cartlist = cartlist;
    }

    @NonNull
    @Override
    public CartCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.cart_pagge_design,null);
        return  new CartCustomViewHolder(v);
        }

    @Override
    public void onBindViewHolder(@NonNull CartCustomViewHolder cartCustomViewHolder, final int i) {
        final CartPojo cartPojoObject=cartlist.get(i);



       Picasso.get().load(cartPojoObject.getMainimage()).into(cartCustomViewHolder.mainImage);

      //  cartCustomViewHolder.mainImage.setImageDrawable(cxt.getDrawable(cartPojoObject.getMainimage()));
        cartCustomViewHolder.price.setText(cartPojoObject.getPrice());
        cartCustomViewHolder.title.setText(cartPojoObject.getMain_title());
        cartCustomViewHolder.currentdatetime.setText(cartPojoObject.getTotal());
        cartCustomViewHolder.counting.setText(cartPojoObject.getItem_count());
        cartCustomViewHolder.id_value.setText(cartPojoObject.getId());
cartCustomViewHolder.single_amount.setText(cartPojoObject.getSinglePrice());
cartCustomViewHolder.size.setText(cartPojoObject.getSize());


        cartCustomViewHolder.removecart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               id_data=cartlist.get(i).getId();
               DbHandler dbHandler=new DbHandler(cxt);

               if( dbHandler.deleteCartItem(Integer.parseInt(id_data)))
               {
                   Toast.makeText(cxt, "id from cart view adapter"+id_data, Toast.LENGTH_SHORT).show();
                   // removedata_db(id_data);
                   int p= Integer.parseInt(cartPojoObject.getPrice());
                   CartViewActivity.over_all_total=CartViewActivity.over_all_total-p;
                   CartViewActivity.amountvalue.setText(String.valueOf(CartViewActivity.over_all_total));

                   cartlist.remove(cartlist.get(i));
                   notifyDataSetChanged();
               }

           }
       });

    }

    //removing datafrom cart item
    private void removedata_db(final String id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.DELETE_CART_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              System.out.println("response of cart for removing data "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
                // params.put("name", (String) image_title.getText());

                params.put("id",id);
                return params;
            }
        };
        requestQueue=Volley.newRequestQueue(cxt);
        requestQueue.add(stringRequest);


    }
    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class CartCustomViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,price,discount,total,id_value,size;
        ImageView add,remove;
        ImageView mainImage,removecart;
        int count;
        TextView counting,currentdatetime,single_amount;


        public CartCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage=(ImageView)itemView.findViewById(R.id.overviewimage);
            removecart=(ImageView)itemView.findViewById(R.id.removecart);
            id_value=(TextView)itemView.findViewById(R.id.id_value);
            single_amount=(TextView)itemView.findViewById(R.id.single_amount);
            price=(TextView)itemView.findViewById(R.id.amount);
            title=(TextView)itemView.findViewById(R.id.title);
            add=(ImageView)itemView.findViewById(R.id.add);
            remove=(ImageView)itemView.findViewById(R.id.remove);
            counting=(TextView)itemView.findViewById(R.id.counting);
            currentdatetime=(TextView)itemView.findViewById(R.id.currentdatetime);
size=(TextView)itemView.findViewById(R.id.size);

            //add count
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id= Integer.parseInt(id_value.getText().toString());
                    count=Integer.parseInt(counting.getText().toString());
                    Toast.makeText(cxt, "count  :  "+count, Toast.LENGTH_SHORT).show();
                    count=count+1;

                    updatecpunt(id,count);
                    int cash= Integer.parseInt(single_amount.getText().toString());
                    cash=cash*count;
                    CartViewActivity.over_all_total=CartViewActivity.over_all_total+cash;
                    CartViewActivity.amountvalue.setText(String.valueOf(CartViewActivity.over_all_total));
                    price.setText(String.valueOf(cash));
                }
            });
            //removind count
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count=Integer.parseInt(counting.getText().toString());
                    Toast.makeText(cxt, "count  :  "+count, Toast.LENGTH_SHORT).show();
                    int id= Integer.parseInt(id_value.getText().toString());
                    if(count==0)
                    {
                        count=0;
                        counting.setText(String.valueOf(count));

                    }
                    else
                    {
                        count=count-1;

                        updatecpunt(id,count);
                        int cash= Integer.parseInt(single_amount.getText().toString());
              cash=cash*count;
                        CartViewActivity.over_all_total=CartViewActivity.over_all_total-cash;
                        CartViewActivity.amountvalue.setText(String.valueOf(CartViewActivity.over_all_total));
                        price.setText(String.valueOf(cash));
                      //  counting.setText(String.valueOf(count));
                    }

                }
            });

            for (int i=0;i<cartlist.size();i++)
            {
                total_price_value= Integer.parseInt((cartlist.get(i).getPrice()))+total_price_value;

            }

        }

        //inserting cart data to sqlite
        public void updatecpunt(int id,int count)
        {

            int cart_id=id;
            int cart_count=count;
            if(cart_count==0)
            {
                cart_count=1;
            }
            DbHandler dbHandler=new DbHandler(cxt);
            boolean isInserted=dbHandler.updateCount(cart_id,cart_count);
            if (isInserted)
            {
                //Toast.makeText(this, "Counting is "+cart_count, Toast.LENGTH_SHORT).show();
                counting.setText(String.valueOf(count));


                Toast.makeText(cxt, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(cxt, "Failed to INserted ", Toast.LENGTH_SHORT).show();

            }
        }

    }
}


