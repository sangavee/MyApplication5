package com.yoho.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    DbHandler dbHandler;

    RequestQueue requestQueue;
    RequestQueue requestQueue_store;
    RequestQueue requestQueue_store_address;
    String phonenumber,randnumber;
    boolean address_state;

    //current time
    DateTimeFormatter dtf = null;
    LocalDateTime now = null;


    static TextView address_detail,edit_address,totalvalue;
   Button place_order;
    SharedPreferences sharedPreferences;
    String pay_amount;
    ArrayList<Integer> arrayList_cart;
    ArrayList<Integer> arrayList_cart_count;
    ArrayList<String> arrayList_cart_date;
    ArrayList<String> arrayList_cart_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

         pay_amount=getIntent().getStringExtra("pay_amount");
        String getdata_state=getIntent().getStringExtra("getdata");
        String shipping_address=getIntent().getStringExtra("shipping_address");


        dbHandler=new DbHandler(getApplicationContext());
        arrayList_cart=new ArrayList<>();
        arrayList_cart_count=new ArrayList<>();
        arrayList_cart_date=new ArrayList<>();
        arrayList_cart_size=new ArrayList<>();


        totalvalue=findViewById(R.id.totalvalue);
        address_detail=findViewById(R.id.address_detail);
        place_order=findViewById(R.id.place_order);
        edit_address=findViewById(R.id.edit_address);
        ImageView addaddress=findViewById(R.id.addaddress);

        totalvalue.setText(pay_amount);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        phonenumber=sharedPreferences.getString("phonenumber",null);
        address_state=sharedPreferences.getBoolean("address",false);

        if(address_state)
        {
            addaddress.setVisibility(View.GONE);
            address_detail.setVisibility(View.VISIBLE);
            if(getdata_state.equals("one"))
            {
                getAddress();
            }
            else
            {
                address_detail.setText(shipping_address);
                address_detail.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            edit_address.setVisibility(View.GONE);
            address_detail.setVisibility(View.GONE);
            addaddress.setVisibility(View.VISIBLE);
        }


        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              loginCheck();
            }
        });

        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginCheck();
            }
        });

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address_detail.getText().toString().isEmpty())
                {
                    Toast.makeText(Payment.this, "Please fill the address..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    randnumber=Config.rand_number();
                    storeAddressDetails();
                    fetchCartItem();
                }

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

       // viewData();

    }


    //showing data from sqlite db
    public void viewData()
    {
        Cursor cursor=dbHandler.getallData();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "Query not executed", Toast.LENGTH_SHORT).show();
        }

        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext())
        {
            stringBuffer.append("address_type : "+cursor.getString(1)+"\n");
            stringBuffer.append("name"+cursor.getString(2)+"\n");
            stringBuffer.append("phone"+cursor.getString(3 )+"\n");
            stringBuffer.append("pincode4"+cursor.getString(4)+"\n");
            stringBuffer.append("city"+cursor.getString(5)+"\n");
            stringBuffer.append("state"+cursor.getString(6)+"\n");
            stringBuffer.append("area"+cursor.getString(7)+"\n");
            stringBuffer.append("address"+cursor.getString(8)+"\n");
            stringBuffer.append("landmark"+cursor.getString(9)+"\n");
            stringBuffer.append("alternate_no"+cursor.getString(10)+"\n");

            address_detail.setText(stringBuffer.toString());
        }

    }

    //checking user loged_in or not while editing address
public void loginCheck()
{
    sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    if(sharedPreferences.getBoolean("loggedIn",false))
    {
        if(sharedPreferences.getBoolean("address",false))
        {
            Intent i=new Intent(Payment.this,ShippingAddress.class);
            i.putExtra("pay_amount",pay_amount);
            startActivity(i);
            address_detail.setText("");
            finish();
        }
        else
        {
            Intent i=new Intent(Payment.this,AddressActivity.class);
            startActivity(i);
            finish();
        }

    }
    else
    {
        Intent intent=new Intent(Payment.this,Sighin_LOgin.class);
        startActivity(intent);
        finish();
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
                        address_detail.setText(address+"\n"+area+"\n"+city+"\n"+state+"\n"+pincode);
                        address_detail.setVisibility(View.VISIBLE);
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
        /*@Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params=new HashMap<>();
            params.put("phone_number",phonenumber);
            return params;
        }*/
    };

    requestQueue=Volley.newRequestQueue(getApplicationContext());
    requestQueue.add(stringRequest);
}



//storing address and total amount
    public  void storeAddressDetails()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.STORE_MYORDER_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
System.out.println("Response from STORE MYORDER ADDRESS "+response);
if(response.equalsIgnoreCase("success"))
{
    Intent i=new Intent(getApplicationContext(),MyOrderActivity.class);
    startActivity(i);
    finish();

}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
System.out.println("ERROR from STORE MYORDER ADDRESS"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("user_id",phonenumber);
                params.put("rand_num",randnumber);
                params.put("total_amount",pay_amount);
                params.put("address",address_detail.getText().toString());

                return params;
            }
        };
        requestQueue_store_address=Volley.newRequestQueue(getApplicationContext());
        requestQueue_store_address.add(stringRequest);

    }
    //storing existing cart data to server;
    public void storeCartToServer(final int id, final int count, final String date,final String size)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.STORE_MY_ORDERS, new Response.Listener<String>() {
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
                params.put("rand_num",randnumber);
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
        String datetime=current_time();
        Cursor cursor=dbHandler.getCartItem();
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                int contentpath=cursor.getInt(0);
                int count_cart=cursor.getInt(1);
String size=cursor.getString(3);



                arrayList_cart.add(contentpath);
                arrayList_cart_count.add(count_cart);

                arrayList_cart_size.add(size);


            }
            for(int i=arrayList_cart.size()-1;i>=0;i--)
            {

                storeCartToServer(arrayList_cart.get(i),arrayList_cart_count.get(i),datetime,arrayList_cart_size.get(i));
                if(i==0)
                {
                    if(dbHandler.delete_db())
                    {
                        Toast.makeText(this, "item placed Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else
        {
            Toast.makeText(this, "No item is there to place order", Toast.LENGTH_SHORT).show();
        }



    }

    //current time
    public String current_time()
    {

        Calendar c=Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());


        return String.valueOf(datetime);
    }

}