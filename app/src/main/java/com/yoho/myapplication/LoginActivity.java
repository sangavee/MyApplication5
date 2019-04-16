package com.yoho.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    DbHandler dbHandler;
    ArrayList<Integer> arrayList_cart;
    ArrayList<Integer> arrayList_cart_count;
    ArrayList<String> arrayList_cart_date;
    ArrayList<String> arrayList_cart_size;

    RequestQueue requestQueue_store;
    int add_address_count=0;
    RelativeLayout hiding_layout;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    String phonenumber,name;
    boolean address_state;

    TextView address_view,addaddress,edit,logout,enter_address,phonenumber_tv,name_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHandler=new DbHandler(getApplicationContext());
        arrayList_cart=new ArrayList<>();
        arrayList_cart_count=new ArrayList<>();
        arrayList_cart_date=new ArrayList<>();
        arrayList_cart_size=new ArrayList<>();

        phonenumber_tv=findViewById(R.id.phonenumber);
        hiding_layout=findViewById(R.id.hidelayout);
        address_view=findViewById(R.id.address_view);
        addaddress=findViewById(R.id.addaddress);
        edit=findViewById(R.id.edit);
        logout=findViewById(R.id.logout);
        enter_address=findViewById(R.id.enter_address);
name_tv=findViewById(R.id.name);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        phonenumber=sharedPreferences.getString("phonenumber",null);
        name=sharedPreferences.getString("name",null);
        address_state=sharedPreferences.getBoolean("address",false);

        name_tv.setText(name);
        phonenumber_tv.setText(phonenumber);

        if(address_state)
        {
            addaddress.setVisibility(View.GONE);
            enter_address.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            address_view.setVisibility(View.VISIBLE);
            getAddress();
        }
        else
        {

            enter_address.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            address_view.setVisibility(View.GONE);

            addaddress.setVisibility(View.VISIBLE);
        }

//edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });


        //myorder button
        TextView myorder=findViewById(R.id.myorderbtn);
        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MyOrderActivity.class);
                startActivity(i);
            }
        });

        //logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchCartItem();//fetching item in sqlite



            }
        });

        //wishlist button
        TextView wishlist=findViewById(R.id.wishlist);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),WishlistActivity.class);
                startActivity(i);
            }
        });

        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent i=new Intent(getApplicationContext(),AddressActivity.class);
             startActivity(i);
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

//cart icon
        ImageView cart_icon=findViewById(R.id.favorite_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
                //  finish();
            }
        }); }

    //checking user loged_in or not while editing address
    public void loginCheck() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("loggedIn", false)) {
            Intent i = new Intent(LoginActivity.this, AddressActivity.class);
            startActivity(i);

        } else {
            Intent intent = new Intent(LoginActivity.this, Sighin_LOgin.class);
            startActivity(intent);

        }
    }

    //get the address from db
    public void getAddress()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.GET_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Payment get address" +response);


                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(jsonObject.getString("phone_number").equalsIgnoreCase(phonenumber))
                        {
                            String address=jsonObject.getString("address");
                            String area=jsonObject.getString("area");
                            String city=jsonObject.getString("city");
                            String state=jsonObject.getString("state");
                            String pincode=jsonObject.getString("pincode");
                            address_view.setText(address+"\n"+area+"\n"+city+"\n"+state+"\n"+pincode);
                            hiding_layout.setVisibility(View.VISIBLE);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("phone_number",phonenumber);
                return params;
            }
        };

        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    //logout functionality
    public void logoutmethod(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set a title for alert dialog
        builder.setTitle("Logout");

        // Show a message on alert dialog
        builder.setMessage("Are you sure you want to logout?");

        // Set the positive button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("loggedIn",false);
                editor.putBoolean("address",false);

                editor.apply();
                editor.commit();
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set the negative button
        builder.setNegativeButton("CANCEL", null);
        AlertDialog dialog = builder.create();

        dialog.show();
        // Get the alert dialog buttons reference
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(Color.parseColor("#a92578"));
        negativeButton.setTextColor(Color.parseColor("#a92578"));

    }


    //storing existing cart data to server;
    public void storeCartToServer(final int id, final int count, final String date,final String size)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.STORE_EXIST_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              System.out.println("DELETED ITEM STATUS :"+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("count", String.valueOf(count));
                params.put("datetime",date);
                params.put("user_id",phonenumber);
                params.put("size",size);
               return params;
            }
        };
        requestQueue_store=Volley.newRequestQueue(getApplicationContext());
        requestQueue_store.add(stringRequest);

    }

    //fetching cart item in sqlite
    public void fetchCartItem()
    {
        Cursor cursor=dbHandler.getCartItem();
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int contentpath=cursor.getInt(0);
                int count_cart=cursor.getInt(1);
                String date=cursor.getString(2);
String size=cursor.getString(3);

                arrayList_cart_date.add(date);
                arrayList_cart.add(contentpath);
                arrayList_cart_count.add(count_cart);
                arrayList_cart_size.add(size);



            }
            for(int i=arrayList_cart.size()-1;i>=0;i--)
            {

                storeCartToServer(arrayList_cart.get(i),arrayList_cart_count.get(i),arrayList_cart_date.get(i),arrayList_cart_size.get(i));
                if(i==0)
                {
                    if(dbHandler.delete_db())
                    {
                        logoutmethod();
                    }
                }
            }
        }
        else
        {
            logoutmethod();
        }



    }
}
