package com.yoho.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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

public class SareesCategoryActivity extends AppCompatActivity {

   static RecyclerView recycle1;
    List<GridviewProduct> sareelist;
    RequestQueue requestQueue;
    GridviewAdapter adapter;
    static TextView title,sortingtext,productcount;



String radio_value,category_name,class_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Config.THEME_NAME.equals("ohpair"))
        {
            setTheme(R.style.OHAppTheme);
           // title.setBackgroundColor(Color.parseColor("#E0E34A"));
        }
        setContentView(R.layout.activity_sarees_category);

        title=findViewById(R.id.titles);
        ImageView cart_icon=findViewById(R.id.cart_icon);
        ImageView sortingimage=findViewById(R.id.sortingimage);
        sortingtext=findViewById(R.id.sortingtext);
        productcount=findViewById(R.id.productcount);
        ImageView backAero=findViewById(R.id.back_icon);

//changing theme of action bar
        if(Config.THEME_NAME.equals("ohpair"))
        {
            title.setBackgroundColor(Color.parseColor("#8CCB2E"));
            title.setText("OhPair");


            sortingimage.setImageDrawable(getApplicationContext().getDrawable(R.drawable.sorting_green));

            cart_icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.cart));
            backAero.setImageDrawable(getApplicationContext().getDrawable(R.drawable.back_icon));

        }
       // category_name=getIntent().getStringExtra("category");


//cart icon

        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        //back aero
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent=new Intent(getApplicationContext(),CatogryAction.class);
                startActivity(intent);*/
                finish();
            }
        });

        //getting value from Sorting Dialouge Fragment
        Intent intent = getIntent();
        radio_value = intent.getStringExtra("radiobutton_data");

       // Toast.makeText(getApplicationContext(),"Radio Button Value "+radio_value,Toast.LENGTH_LONG).show();

        //Sorting page
        TextView sortingtext=findViewById(R.id.sortingtext);
        sortingtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortingDialogeFragment sortingDialogeFragment=new SortingDialogeFragment();
                //showing fragment dialouge....
                sortingDialogeFragment.show(getSupportFragmentManager(),"mydialouge");

            }
        });

        //recycleview with grid

        recycle1 = (RecyclerView) findViewById(R.id.my_recycler_view);
        sareelist = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(manager);
        fetch_category_item();

    }

    private  void fetch_category_item() {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.CATEGORY_ITEM_PARTICULAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {
                        GridviewProduct gridviewProduct = new GridviewProduct();
                        try
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            gridviewProduct.setId(jsonObject.getString("id"));
                            gridviewProduct.setProductimage(jsonObject.getString("images"));
                            gridviewProduct.setProductname(jsonObject.getString("image_name"));
                            gridviewProduct.setProductprice(jsonObject.getString("price"));
                            gridviewProduct.setBrand(jsonObject.getString("brand"));
                            gridviewProduct.setProductdescription(jsonObject.getString("description"));
                            gridviewProduct.setCategories(jsonObject.getString("categories"));


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        sareelist.add(gridviewProduct);
                    }

                    // Toast.makeText(getContext(), "ARRAYLIST  "+orderlistPojoArrayList.toString() , Toast.LENGTH_SHORT).show();

                    adapter = new GridviewAdapter(getApplicationContext(), sareelist);
                    productcount.setText(String.valueOf(sareelist.size()));
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
                params.put("category",Config.CATEGORY_NAME);
                return params;
            }
        }
                ;
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}
