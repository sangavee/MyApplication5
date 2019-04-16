package com.yoho.myapplication;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchView;
    RecyclerView recyclerView;
    GridviewAdapter gridviewAdapter;
    RequestQueue requestQueue;
    List<GridviewProduct> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView=findViewById(R.id.recyclerview);
        productList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               fetch_item(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public  void fetch_item(final String name)
    {
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_SEARCH_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response from search activity "+response);
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

                        productList.add(gridviewProduct);
                    }

                    // Toast.makeText(getContext(), "ARRAYLIST  "+orderlistPojoArrayList.toString() , Toast.LENGTH_SHORT).show();

                    gridviewAdapter = new GridviewAdapter(getApplicationContext(), productList);
                    recyclerView.setAdapter(gridviewAdapter);

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
                params.put("name",name);
                return params;
            }
        }
                ;
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

}
