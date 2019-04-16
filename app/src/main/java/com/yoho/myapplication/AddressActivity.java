package com.yoho.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    //sqlite process
    DbHandler dbHandler;
String phonenumber;
    SharedPreferences sharedPreferences;

    RadioGroup radioGroup;
    RadioButton radio_home, radio_work;
    EditText  pincode, city, state, area, address, landmark, alternateno;
    Button confirmorder;


    RequestQueue requestQueue;
    String address_type;
    boolean address_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        //sqlite process
        dbHandler = new DbHandler(getApplicationContext());


        radioGroup = findViewById(R.id.radiogroup);
        radio_home = findViewById(R.id.home);
        radio_work = findViewById(R.id.work);


        pincode = findViewById(R.id.pincode);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        area = findViewById(R.id.area);
        address = findViewById(R.id.address);
        landmark = findViewById(R.id.landmark);
        alternateno = findViewById(R.id.alternateno);
        confirmorder = findViewById(R.id.confirmorder);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        phonenumber=sharedPreferences.getString("phonenumber",null);
        address_state=sharedPreferences.getBoolean("address",false);
if(address_state)
{
    getAddress();
}



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        address_type = radio_home.getText().toString();
                    case R.id.work:
                        address_type = radio_work.getText().toString();
                }
            }
        });


        //conform order
        confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // insertData();

                register_address();
            }
        });

        //back aero
        ImageView backAero = findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }



   // storing address to db
    public void register_address() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADD_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("address",true);
                editor.commit();

                onBackPressed();
/*
                System.out.println("Address page response " + response);
                Intent i = new Intent(getApplicationContext(), Payment.class);
                startActivity(i);*/
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("address_type", address_type);
                params.put("phone_number",phonenumber);
                params.put("pincode", pincode.getText().toString());
                params.put("city", city.getText().toString());
                params.put("state", state.getText().toString());
                params.put("area", area.getText().toString());
                params.put("address", address.getText().toString());
                params.put("landmark", landmark.getText().toString());
                params.put("alternate_no", alternateno.getText().toString());
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

//inserting data to sqlite
   /* private void insertData()
    {
        boolean isInserted=dbHandler.insertData(address_type,name.getText().toString(),phonenumber.getText().toString(),pincode.getText().toString(),city.getText().toString(),state.getText().toString(),area.getText().toString(),address.getText().toString(),landmark.getText().toString(),alternateno.getText().toString());
        if (isInserted)
        {
            Toast.makeText(this, "INserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Failed to INserted ", Toast.LENGTH_SHORT).show();

        }
    }*/

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
                            String address_type1=jsonObject.getString("addresstype");
                            String address1=jsonObject.getString("address");
                            String area1=jsonObject.getString("area");
                            String city1=jsonObject.getString("city");
                            String state1=jsonObject.getString("state");
                            String pincode1=jsonObject.getString("pincode");
                            String landmark1=jsonObject.getString("landmark");
                            String alternateno1=jsonObject.getString("alternateno");

                            if(address_type1.equals("Work"))
                            {
                                radio_work.setChecked(true);
                            }
                            else
                            {
                                radio_home.setChecked(true);
                            }
                            landmark.setText(landmark1);
                            alternateno.setText(alternateno1);
                            address.setText(address1);
                            area.setText(area1);
                            city.setText(city1);
                            state.setText(state1);
                            pincode.setText(pincode1);
/*
                            landmark.setFocusable(false);
                            alternateno.setFocusable(false);
                            address.setFocusable(false);
                            pincode.setFocusable(false);
                            area.setFocusable(false);
                            city.setFocusable(false);
                            state.setFocusable(false);*/


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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
