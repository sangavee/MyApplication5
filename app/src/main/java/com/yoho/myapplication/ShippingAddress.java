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

import com.android.volley.RequestQueue;

public class ShippingAddress extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radio_home, radio_work;
    EditText pincode, city, state, area, address, landmark, alternateno;
    Button confirmorder;


    RequestQueue requestQueue;
    String address_type;
    boolean address_state;

    //sqlite process
    DbHandler dbHandler;
    String phonenumber,total_amount;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        //sqlite process
        dbHandler = new DbHandler(getApplicationContext());
        total_amount=getIntent().getStringExtra("pay_amount");


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

                String shipping_address="Address Type : "+address_type+"\n"+"Phone Number : "+phonenumber+"\n"+"Street : "+address.getText().toString()+"\n"+"Area : "+area.getText().toString()+"\n"+"City : "+city.getText().toString()+"State : "+state.getText().toString()+"\n"+"Pincode : "+pincode.getText().toString();

             //  Payment.address_detail.setText(shipping_address);
                Intent i=new Intent(getApplicationContext(),Payment.class);
                i.putExtra("pay_amount",total_amount);
                i.putExtra("getdata","two");
                i.putExtra("shipping_address",shipping_address);
                startActivity(i);
                finish();

                // insertData();

               // register_address();
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
}
