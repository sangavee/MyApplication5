package com.yoho.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutActivity extends AppCompatActivity {

    String total_amount;
    TextView totalvalue,payablevalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        total_amount=getIntent().getStringExtra("total_amount");

        totalvalue=findViewById(R.id.totalvalue);
        totalvalue.setText(total_amount);

        payablevalue=findViewById(R.id.payablevalue);
        final int pay_amount=Integer.parseInt(total_amount)+50;
        Toast.makeText(this, "total amount is "+pay_amount, Toast.LENGTH_SHORT).show();
        if(pay_amount>0)
        {
            payablevalue.setText(String.valueOf(pay_amount));
        }

        //back aero
        ImageView backAero=findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //conform order
        Button conformorder=findViewById(R.id.confirmorder);
        conformorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Payment.class);
                i.putExtra("getdata","one");
               i.putExtra("pay_amount",payablevalue.getText().toString());
                i.putExtra("shipping_address","");

                getApplicationContext().startActivity(i);
                finish();
            }
        });
    }
}
