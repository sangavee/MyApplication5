package com.yoho.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyorderOverviewActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    String order_id,product_id,delivery_state;

    TextView orderplacedvalue,orderidvalue,title,qtvalue,estimationvalue;
    ImageView overviewimage,placed_btn,shipped_btn,deliver_btn;
    View view2,view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_detail_page);

        order_id=getIntent().getStringExtra("order_id");
        product_id=getIntent().getStringExtra("product_id");

        orderplacedvalue=findViewById(R.id.orderplacedvalue);
        orderidvalue=findViewById(R.id.orderidvalue);
        title=findViewById(R.id.title);
        qtvalue=findViewById(R.id.qtvalue);
        estimationvalue=findViewById(R.id.estimationvalue);
        overviewimage=findViewById(R.id.overviewimage);
        placed_btn=findViewById(R.id.placed_btn);
        deliver_btn=findViewById(R.id.deliverd_btn);
        shipped_btn=findViewById(R.id.shipped_btn);
        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);

        show_details();
        //back aero
        ImageView backAero = findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

   public void show_details()
   {
       StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.MYORDER_OVERVIEW, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               System.out.println("RERSPONCE OF MYORDER OVERVIEW "+response);

               try {
                   JSONArray jsonArray=new JSONArray(response);
                   for(int i=0;i<jsonArray.length();i++){

                       JSONObject jsonObject=jsonArray.getJSONObject(i);
                       orderplacedvalue.setText(jsonObject.getString("datetime"));
                       orderidvalue.setText(jsonObject.getString("rand_num"));
                       title.setText(jsonObject.getString("image_name"));
                       qtvalue.setText(jsonObject.getString("count"));
                       Picasso.get().load(jsonObject.getString("image_path")).into(overviewimage);
                       estimationvalue.setText(jsonObject.getString("estimate_time"));
                       delivery_state=jsonObject.getString("delivery_state");
                       if(delivery_state.equalsIgnoreCase("Placed"))
                       {
                           placed_btn.setImageResource(R.drawable.radio);
                           blink(placed_btn);
                       }
                       else if(delivery_state.equals("shipped"))
                       {
                           shipped_btn.setImageResource(R.drawable.radio);
                           placed_btn.setImageResource(R.drawable.radio);
                           view1.setBackgroundColor(Color.parseColor("#25CA04"));
                           blink(shipped_btn);

                       }
                       else
                       {
                           shipped_btn.setImageResource(R.drawable.radio);
                           placed_btn.setImageResource(R.drawable.radio);
                           deliver_btn.setImageResource(R.drawable.radio);
                           view1.setBackgroundColor(Color.parseColor("#25CA04"));
                           view2.setBackgroundColor(Color.parseColor("#25CA04"));
                           blink(deliver_btn);
                       }

                   }


               } catch (JSONException e) {
                   e.printStackTrace();
                   Toast.makeText(MyorderOverviewActivity.this, "e error"+e, Toast.LENGTH_SHORT).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){

           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params=new HashMap<>();
               params.put("rand_num",order_id);
               params.put("product_id",product_id);
               return params;
           }
       };
       requestQueue=Volley.newRequestQueue(getApplicationContext());
       requestQueue.add(stringRequest);
   }



    private void blink(final ImageView obj) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 500;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //  TextView txt = (TextView) findViewById(R.id.order_id);
                        // ImageView txt=(ImageView)findViewById(R.id.pickup);

                       // ImageView txt=

                                ;
                        /*if (txt.getVisibility() == View.VISIBLE) {
                            txt.setVisibility(View.INVISIBLE);
                        } else {
                            txt.setVisibility(View.VISIBLE);
                        }*/
                        blink(obj);
                    }
                });
            }
        }).start();
    }
}
