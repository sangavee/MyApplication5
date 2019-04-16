package com.yoho.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

public class CartViewActivity extends AppCompatActivity {

    DbHandler dbHandler;
    ArrayList<Integer> arrayList_cart;
    ArrayList<Integer> arrayList_cart_count;
    ArrayList<String> arrayList_cart_date;
    ArrayList<String> arrayList_cart_size;


    RequestQueue requestQueue;
    static TextView amountvalue;
   static int over_all_total=0;
    // first recycle view
    RecyclerView recycle1;
  //  RecyclerView.LayoutManager recycle_manager;
  CartViewAdapter adapter;
    ArrayList<CartPojo> carviewlist;
    String item_name[]={"saree","salwar comize","jeans","cotton salwar "};
    String price[]={"100","200","300","400"};
  //  String discount[]={"100%","200%","300%","400%"};
    String discount="450";
    int category_image=R.drawable.z1;
    String total[]={"1000","2000","3000","4000"};
    //int[] category_image={R.drawable.z1,R.drawable.a3,R.drawable.a4,R.drawable.saree14};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);

        dbHandler=new DbHandler(getApplicationContext());
        arrayList_cart=new ArrayList<>();
arrayList_cart_count=new ArrayList<>();
arrayList_cart_date=new ArrayList<>();
        arrayList_cart_size=new ArrayList<>();

        amountvalue=findViewById(R.id.amountvalue);

        //checkout button
        TextView checkout=findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountvalue.getText().toString().equalsIgnoreCase("0"))
                {
                    Toast.makeText(CartViewActivity.this, "No item is here to place order", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i=new Intent(getApplicationContext(),CheckoutActivity.class);
                    i.putExtra("total_amount",amountvalue.getText().toString());
                    getApplicationContext(). startActivity(i);
                    finish();
                }

            }
        });


        //back aero
        ImageView backAero=findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);*/

               over_all_total=0;
                finish();
            }
        });

        recycle1 = (RecyclerView) findViewById(R.id.my_recycler_view);

       // recycle_manager=new LinearLayoutManager(getApplicationContext());

     //   recycle1.setLayoutManager(recycle_manager);

        carviewlist = new ArrayList<>();
       /* for (int i=0;i<category_image.length;i++)
        {
            carviewlist.add(new CartPojo(category_image[i],price[i],discount[i],total[i],item_name[i]));

        }*/

       /* recycle1.setHasFixedSize(true);
        CartViewAdapter adapter=new CartViewAdapter(this,carviewlist);
        recycle1.setAdapter(adapter);
*/

        fetchCartItem();
       //fetct_cart_item();
        GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(manager);
        int spanCount = 1; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        recycle1.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));

    }

    //fetching cart item in sqlite
    public void fetchCartItem()
    {
        Cursor cursor=dbHandler.getCartItem();
        while (cursor.moveToNext())
        {
            int contentpath=cursor.getInt(0);
            int count_cart=cursor.getInt(1);
            String date=cursor.getString(2);
            String size_P=cursor.getString(3);

            arrayList_cart_date.add(date);
            arrayList_cart.add(contentpath);
            arrayList_cart_count.add(count_cart);
            arrayList_cart_size.add(size_P);



        }
        for(int i=arrayList_cart.size()-1;i>=0;i--)
        {
            fetct_cart_item(arrayList_cart.get(i),arrayList_cart_count.get(i),arrayList_cart_date.get(i),arrayList_cart_size.get(i));
        }

    }

    //fetching cart item in php
    private  void fetct_cart_item(final int num, final int count,final  String date,final String size) {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_CART_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {
                        CartPojo cartPojo = new CartPojo();
                        try
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            int price= Integer.parseInt(jsonObject.getString("price"));
                            int countval=count;

                            int total=(price*countval);
                            over_all_total=over_all_total+total;
                           //int count_arraylist= (int) count.get(i);
                           System.out.println("total  value is "+total);
                           // String c= (String) count.get(i);
                            cartPojo.setMainimage(jsonObject.getString("image"));
                            cartPojo.setSinglePrice(jsonObject.getString("price"));
                            cartPojo.setDiscount(discount);
                            cartPojo.setSize(size);
                            cartPojo.setMain_title(jsonObject.getString("name"));
                            cartPojo.setPrice(String.valueOf(total));
                            cartPojo.setItem_count(String.valueOf(count));

                            cartPojo.setTotal(date);
                            cartPojo.setId(String.valueOf(num));


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        carviewlist.add(cartPojo);
                        amountvalue.setText(String.valueOf(over_all_total) );
                    }

for (int i=0;i<carviewlist.size();i++)
{

}
                    adapter = new CartViewAdapter(getApplicationContext(), carviewlist);
                    recycle1.setAdapter(adapter);

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
