package com.yoho.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    DbHandler dbHandler;
    ArrayList<Integer> arrayList_id;

    RecyclerView recyclerView;
    WishlistAdapter adapter;
    RequestQueue requestQueue;

    ImageView overviewImage;
    TextView description,second_price;


    List<WishlistPojo> carviewlist;
    String price[]={"100","200","300","400"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        overviewImage=findViewById(R.id.overviewimage);
        description=findViewById(R.id.productname);
        second_price=findViewById(R.id.second_price);

        dbHandler=new DbHandler(getApplicationContext());
        arrayList_id=new ArrayList<>();

//cart icon
        ImageView cart_icon=findViewById(R.id.favorite_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView=findViewById(R.id.my_recycler_view);

        carviewlist=new ArrayList<>();
       /* for (int i=0;i<price.length;i++)
        {
            carviewlist.add(new WishlistPojo(price[i]));

        }*/
        recyclerView.setHasFixedSize(true);



        GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        int spanCount = 1; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));

        //back aero
        ImageView backAero=findViewById(R.id.back_icon

         );
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

        fetchCartItem();
    }

    //fetching cart item in sqlite
    public void fetchCartItem()
    {
        Cursor cursor=dbHandler.getWishlistItem();
        while (cursor.moveToNext())
        {
            int contentpath=cursor.getInt(0);


            arrayList_id.add(contentpath);




        }
        for(int i=arrayList_id.size()-1;i>=0;i--)
        {
            fetct_cart_item(arrayList_id.get(i));
        }

    }


    //fetching wishlist item in php
    private  void fetct_cart_item(final int num) {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_CART_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {
                        WishlistPojo cartPojo = new WishlistPojo();
                        try
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            cartPojo.setId(String.valueOf(num));
                            cartPojo.setImage(jsonObject.getString("image"));
                            cartPojo.setDescription(jsonObject.getString("description"));
                            cartPojo.setPrice(jsonObject.getString("price"));



                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        carviewlist.add(cartPojo);
                    }


                    adapter = new WishlistAdapter(getApplicationContext(), carviewlist);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id",String.valueOf(num));
                return params;
            }
        }

                ;
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
