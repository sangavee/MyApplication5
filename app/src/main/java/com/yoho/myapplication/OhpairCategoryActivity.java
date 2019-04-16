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

public class OhpairCategoryActivity extends AppCompatActivity {

    // first recycle view
    RecyclerView recycle1;
    RequestQueue requestQueue;
    OhpairAdapter ohpairAdapter;
    List<OhpairPojo> ohpairlist;

    ImageView yoshna_brand;
  /*  String category_name[]={"Fasttrack","Puma","Bata","Reebok"};
    int[] category_image={R.drawable.s1,R.drawable.s2,R.drawable.s4,R.drawable.s3};

*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohpair_category);


        yoshna_brand=findViewById(R.id.yoshna_brand);
        yoshna_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CatogryAction.class);
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
                finish();
            }
        });

        //back aero
        ImageView backAero=findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //recycleview with grid

        recycle1 = (RecyclerView) findViewById(R.id.my_recycler_view);
        ohpairlist = new ArrayList<>();

        recycle1.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(manager);
        int spanCount = 2; // 3 columns
        int spacing = 35; // 50px
        boolean includeEdge = true;
        recycle1.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));

        fetch_ohpair_category();
    }

    private  void fetch_ohpair_category() {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_OHPAIR_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {
                      OhpairPojo ohpairPojo=new OhpairPojo();
                        try
                        {

                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            {
                                ohpairPojo.setId(jsonObject.getString("id"));
                                ohpairPojo.setProductimage(jsonObject.getString("category_images"));
                                ohpairPojo.setProductname(jsonObject.getString("category_name"));

                            }

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        ohpairlist.add(ohpairPojo);
                    }

                    // Toast.makeText(getContext(), "ARRAYLIST  "+orderlistPojoArrayList.toString() , Toast.LENGTH_SHORT).show();

                    ohpairAdapter = new OhpairAdapter(getApplicationContext(), ohpairlist);
                    recycle1.setAdapter(ohpairAdapter);

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

