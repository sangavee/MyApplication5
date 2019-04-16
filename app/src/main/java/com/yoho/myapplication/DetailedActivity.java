package com.yoho.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Target;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DetailedActivity extends AppCompatActivity {

    DbHandler dbHandler;

    RequestQueue requestQueue;
    RequestQueue requestQueue_two;
    RequestQueue flipper_requestqueue;

    //current time
    DateTimeFormatter dtf = null;
    LocalDateTime now = null;

    TextView image_title,price,description,first_price;
    CheckBox favorite_icon;

    RelativeLayout cartlayout;

String desc,count_item;
    //slider image view
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> viewpager_urls;

    int count=1;

    TextView counting;



    //array for slider images
    private int[] myImageList = new int[]{R.drawable.pro1, R.drawable.pro2,
            R.drawable.pro3,R.drawable.pro4
            };

    String id,img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Config.THEME_NAME.equalsIgnoreCase("ohpair"))
        {
            setTheme(R.style.OHAppTheme);
            // title.setBackgroundColor(Color.parseColor("#E0E34A"));
        }
        setContentView(R.layout.activity_detailed);

        dbHandler=new DbHandler(getApplicationContext());


        //changing theme of action bar

        viewpager_urls= new ArrayList<>();
        fetch_flipper_images();

        //fetch data from db
        fetch_category_item();

        id=getIntent().getStringExtra("id");
        img_url=getIntent().getStringExtra("img_url");
      //  Toast.makeText(this, "id is "+id+"image url is "+img_url, Toast.LENGTH_SHORT).show();
        System.out.println("id from  Detailed  view"+id);
       // RelativeLayout addtocart=findViewById(R.id.left_layout);

        requestQueue=Volley.newRequestQueue(getApplicationContext());
        description=findViewById(R.id.description);
        image_title=findViewById(R.id.image_title);
        price=findViewById(R.id.second_price);
        first_price=findViewById(R.id.first_price);
        favorite_icon=findViewById(R.id.heart);
        ImageView backAero=findViewById(R.id.back_icon);
        ImageView cart_icon=findViewById(R.id.favorite_icon);
        TextView title=(TextView)findViewById(R.id.titles);
        TextView qty=(TextView)findViewById(R.id.qty);
        TextView addtocart=(TextView)findViewById(R.id.addtocart);
        ImageView add=findViewById(R.id.add);
        final ImageView plus=findViewById(R.id.plus);
        ImageView remove=findViewById(R.id.remove);

        counting=findViewById(R.id.counting);
        final ImageView minus=findViewById(R.id.minus);

        first_price.setPaintFlags(first_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        cartlayout=findViewById(R.id.cartlayout);

        if(Config.THEME_NAME.equals("ohpair"))
        {
            title.setBackgroundColor(Color.parseColor("#8CCB2E"));
            title.setText("OhPair");
           /* title.setTextColor(Color.BLACK);
            qty.setTextColor(Color.BLACK);
            addtocart.setTextColor(Color.BLACK);
            counting.setTextColor(Color.BLACK);


            cart.setImageDrawable(getApplicationContext().getDrawable(R.drawable.cart_black));
            cart_icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.cart_black));
            add.setImageDrawable(getApplicationContext().getDrawable(R.drawable.plus_black));
            remove.setImageDrawable(getApplicationContext().getDrawable(R.drawable.minus_black));

            backAero.setImageDrawable(getApplicationContext().getDrawable(R.drawable.back_icon_black));*/
            cartlayout.setBackgroundColor(Color.parseColor("#8CCB2E"));
           // favorite_icon.setBackgroundColor(R.drawable.like_green);
            plus.setImageDrawable(getApplicationContext().getDrawable(R.drawable.plus_black));
            minus.setImageDrawable(getApplicationContext().getDrawable(R.drawable.minus_black));

            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                favorite_icon.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.like_green) );
            } else {
                favorite_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.like_green));
            }


        }


        //getting current time


favorite_icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        insertWishlist();
    }
});


//add to cart
        addtocart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     //   desc=description.getText().toString();
       // count_item=counting.getText().toString();
       // Toast.makeText(getApplicationContext(),"op "+description.getText(),Toast.LENGTH_SHORT).show();
        insertCartItem();
      //  store();
      /*  Intent i=new Intent(getApplicationContext(),CartViewActivity.class);
        startActivity(i);*/
    }
});

        //back aero
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

//cart icon
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  store();
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
              //  finish();
            }
        });

        //add item to cart

        counting.setText(String.valueOf(count));
        //adding cart
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=count+1;
                counting.setText(String.valueOf(count));
            }
        });


        //removind cart
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==1)
                {
                    count=1;
                    counting.setText(String.valueOf(count));

                }
                else
                {
                    count=count-1;
                    counting.setText(String.valueOf(count));
                }

            }
        });



        //count_text.setText(count);

        //slide imageview for first flipper
       /* imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();*/



        //expandable textview
        final TextView description=findViewById(R.id.description);
        //before expand
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.VISIBLE);
                description.setMaxLines(25);
            }
        });
        //after expand
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.VISIBLE);
                description.setMaxLines(1);
            }
        });

    }

    //converting integer array for images to arraylist
    private ArrayList<ImageModel> populateList(){
        ArrayList<ImageModel> list = new ArrayList<>();
        for(int i = 0; i <myImageList.length; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }
        return list;
    }

    //setting the adapter to slide view pager
    private void init() {

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(new SlidingImage_Adapter(getApplicationContext(),viewpager_urls));

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(3 * density);
        NUM_PAGES =viewpager_urls.size();


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                              @Override
                                              public void onPageSelected(int position) {
                                                  currentPage = position;
                                              }

            @Override
                                              public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
                                              public void onPageScrollStateChanged(int pos) {


            }
                                          }
        );


}

//storing cart item to php
    private void store() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.INSERT_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response of cart",response);
                Toast.makeText(getApplicationContext(),"Response is "+response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();
               // params.put("name", (String) image_title.getText());
                params.put("name", image_title.getText().toString());
                params.put("price",price.getText().toString());
                params.put("item_count",counting.getText().toString());
                params.put("description",description.getText().toString());
                params.put("image",img_url);
                params.put("id",id);
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    //get details from db
    private  void fetch_category_item() {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.DETAILED_CATEGORY_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {

                        try
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                         //   Picasso.get().load(jsonObject.getString("images")).into((Target) mPager);
                           image_title.setText(jsonObject.getString("image_name"));
                           description.setText(jsonObject.getString("description"));
                            price.setText(jsonObject.getString("price"));
                            first_price.setText(jsonObject.getString("first_price"));
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                    }

                    // Toast.makeText(getContext(), "ARRAYLIST  "+orderlistPojoArrayList.toString() , Toast.LENGTH_SHORT).show();

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
                params.put("id",id);

                return params;
            }
        }
                ;
        requestQueue_two=Volley.newRequestQueue(getApplicationContext());
        requestQueue_two.add(jsonArrayRequest);
    }


//fetching image for flip adapter
    private void fetch_flipper_images()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.DETAILED_IMAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("DEtailed image response is "+response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                       viewpager_urls.add(jsonObject.getString("image_url"));


                    }

                    init();

                } catch (JSONException e) {
                    e.printStackTrace();
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

                params.put("id",id);
                return params;

            }
        };
        flipper_requestqueue=Volley.newRequestQueue(getApplicationContext());
        flipper_requestqueue.add(stringRequest);
    }

//inserting data to wishlist in sqlite
    public void insertWishlist()
    {

        int cart_id=Integer.parseInt(id);

        boolean isInserted=dbHandler.insert_wishlistitem(cart_id);
        if (isInserted)
        {
            //Toast.makeText(this, "Counting is "+cart_count, Toast.LENGTH_SHORT).show();
            System.out.println("WISH LIST ITEM ADDED SUCCESSFULLY");
            Toast.makeText(DetailedActivity.this, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println(" ADDED WISH LIST ITEM IS FAILED");

        }
    }

    //inserting cart data to sqlite
    public void insertCartItem()
    {
       String datetime=current_time();
       // Date date=Calendar.getInstance().getTime();
        int cart_id=Integer.parseInt(id);
        int cart_count=Integer.parseInt(counting.getText().toString());
        if(cart_count==0)
        {
          cart_count=1;
        }

        if(Size_adpater.size_result.isEmpty())
        {
            Toast.makeText(this, "Please Select the Size", Toast.LENGTH_SHORT).show();
        }
        else
        {


        boolean isInserted=dbHandler.insert_cartitem(cart_id,cart_count, String.valueOf(datetime),Size_adpater.size_result);
        if (isInserted)
        {
            //Toast.makeText(this, "Counting is "+cart_count, Toast.LENGTH_SHORT).show();

            Toast.makeText(DetailedActivity.this, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Failed to INserted ", Toast.LENGTH_SHORT).show();

        }}
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
