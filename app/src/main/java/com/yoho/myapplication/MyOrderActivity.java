package com.yoho.myapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

public class MyOrderActivity extends AppCompatActivity {

    // first recycle view
    RecyclerView recycle1;
    RequestQueue requestQueue;
    RequestQueue requestQueue_image;

    SharedPreferences sharedPreferences;
    String phonenumber;
    //  RecyclerView.LayoutManager recycle_manager;

    List<MyorderPojo> myorderlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //back aero
        ImageView backAero=findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


     //recycler view setting
        recycle1=findViewById(R.id.my_recycler_view);
        myorderlist = new ArrayList<>();

        fetch_myorder_id();
        recycle1.setHasFixedSize(true);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        phonenumber=sharedPreferences.getString("phonenumber",null);

        GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(manager);
        int spanCount = 1; // 3 columns
        int spacing = 35; // 50px
        boolean includeEdge = true;
        recycle1.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));

    }

    private void fetch_myorder_id()
    {

        //showSimpleProgressDialog(this, "Loading...","Fetching Json",false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_MYORDER, new Response.Listener<String>() {
            // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                System.out.println("RESPONSE" + response);
                // Toast.makeText(getApplicationContext(), "RESPONSE"+response, Toast.LENGTH_LONG).show();
                try {
                    //JSONObject jsonObject1=new JSONObject(response);

                   JSONArray jsonArray = new JSONArray(response);
                    // System.out.println(jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        MyorderPojo servicesPojo = new MyorderPojo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                         {
                            servicesPojo.setOverimage( jsonObject.getString("image_path"));
                            servicesPojo.setId( jsonObject.getString("rand_num"));
                            servicesPojo.setProduct_id(jsonObject.getString("product_id"));
                            servicesPojo.setDatetime( jsonObject.getString("datetime"));
                            servicesPojo.setP_name( jsonObject.getString("image_name"));

                        }

                        myorderlist.add(servicesPojo);


                    }

                    MyorderAdapter adapter=new MyorderAdapter(getApplicationContext(),myorderlist);
                    recycle1.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("user_id",phonenumber);
                return params;
            }
        }

                ;
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }



}
