package com.yoho.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.List;

public class CatogryAction extends AppCompatActivity {
    // first recycle view
    RecyclerView recycle1;
    List<CategoryPojo> categorylist;
    CategoryAdapter categoryAdapter;
    RequestQueue requestQueue;

    ImageView ohpair_brand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catogry_action);


        ohpair_brand=findViewById(R.id.oh_pair_brand);
        ohpair_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),OhpairCategoryActivity.class);
                startActivity(i);
            }
        });


//cart icon
        ImageView cart_icon=findViewById(R.id.favorite_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        //back aero
        ImageView backAero=findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //recycleview with grid

        recycle1 = (RecyclerView) findViewById(R.id.my_recycler_view);
        categorylist = new ArrayList<>();
        recycle1.setHasFixedSize(true);
        fetch_yoshna_category();


        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(manager);
        int spanCount = 2; // 3 columns
        int spacing = 35; // 50px
        boolean includeEdge = true;
        recycle1.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));

    }

    private  void fetch_yoshna_category() {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_YOSHNA_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {
                        CategoryPojo categoryPojo = new CategoryPojo();
                        try
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            categoryPojo.setCategoryId(jsonObject.getString("id"));
                            categoryPojo.setCategoryimage(jsonObject.getString("category_images"));
                            categoryPojo.setCategoryname(jsonObject.getString("category_name"));

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        categorylist.add(categoryPojo);
                    }

                    // Toast.makeText(getContext(), "ARRAYLIST  "+orderlistPojoArrayList.toString() , Toast.LENGTH_SHORT).show();

                    categoryAdapter = new CategoryAdapter(getApplicationContext(), categorylist);
                    recycle1.setAdapter(categoryAdapter);

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

                ;
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

}
