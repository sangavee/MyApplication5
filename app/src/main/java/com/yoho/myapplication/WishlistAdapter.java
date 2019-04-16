package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.CustomwishlistHolder> {

Context cxt;
    private List<WishlistPojo> wishlist;

    //current time
    DateTimeFormatter dtf = null;
    LocalDateTime now = null;


    public WishlistAdapter(Context cxt, List<WishlistPojo> wishlist) {
        this.cxt = cxt;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public CustomwishlistHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.wishlist_design,null);
        return  new CustomwishlistHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomwishlistHolder customwishlistHolder, int i) {


        WishlistPojo wishlistPojoObject=wishlist.get(i);
        Picasso.get().load(wishlistPojoObject.getImage()).into(customwishlistHolder.overviewImage);
customwishlistHolder.description.setText(wishlistPojoObject.getDescription());
        customwishlistHolder.second_price.setText(wishlistPojoObject.getPrice());
customwishlistHolder.id.setText(wishlistPojoObject.getId());
      /* customwishlistHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(cxt,SareesCategoryActivity.class);

               cxt.startActivity(i);
           }
       });*/

       // cartCustomViewHolder.discount.setText(cartPojoObject.getDiscount());
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    //getting current time
    public String current_time()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println(dtf.format(now)); //2016/11/16 12:08:43
        }

        return String.valueOf(now);
    }

    //inserting cart data to sqlite
    public void insertCartItem(int id)
    {
        String datetime=current_time();
DbHandler dbHandler=new DbHandler(cxt);
        int cart_id=id;
        int cart_count=1;
        if(cart_count==0)
        {
            cart_count=1;
        }

        if(Size_adpater.size_result.isEmpty())
        {
            Toast.makeText(cxt, "Please Select the Size", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean isInserted = dbHandler.insert_cartitem(cart_id, cart_count, datetime,Size_adpater.size_result);
            if (isInserted) {
                //Toast.makeText(this, "Counting is "+cart_count, Toast.LENGTH_SHORT).show();

                Toast.makeText(cxt, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(cxt, "Failed to INserted ", Toast.LENGTH_SHORT).show();

            }
        }
    }


    class CustomwishlistHolder extends RecyclerView.ViewHolder
    {
        ImageView overviewImage;
        TextView description,second_price,addbutton,id;


        public CustomwishlistHolder(@NonNull View itemView) {

            super(itemView);

            overviewImage=itemView.findViewById(R.id.overviewimage);
            description=itemView.findViewById(R.id.productname);
            addbutton=itemView.findViewById(R.id.addbutton);
            id=itemView.findViewById(R.id.id);
            second_price=itemView.findViewById(R.id.second_price);

            //add item to cart
            addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertCartItem(Integer.parseInt(id.getText().toString()));
                }
            });
        }

    }
}
