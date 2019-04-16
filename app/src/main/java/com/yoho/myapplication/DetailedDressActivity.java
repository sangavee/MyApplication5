package com.yoho.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedDressActivity extends AppCompatActivity implements View.OnClickListener {

Handler handler=new Handler();
    DbHandler dbHandler;
    //slider image view
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> viewpager_urls;
    public int touch_count=0;
    RecyclerView size_recycler;
    Size_adpater size_adpater;


    RequestQueue flipper_requestqueue,requestQueue_two;
    String id;
    String size;
    LinearLayout shoelinear,dresslinear;
    TextView image_title,description,price,first_price,counting,s_size,m_size,l_size,xl_size,xxl_size,XXXl_size,shoesiz_36,shoesiz_38,shoesiz_40,
            shoesiz_42,shoesiz_44;
    ImageView add,remove;
    CheckBox favorite_icon;
    RelativeLayout addtocart;
    int count=1;

    //array for slider images
    private int[] myImageList = new int[]{R.drawable.saree11, R.drawable.saree12,
            R.drawable.saree13
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_dress);
        dbHandler=new DbHandler(getApplicationContext());

        image_title=findViewById(R.id.p_name);
        description=findViewById(R.id.productname);
        price=findViewById(R.id.second_price);
        first_price=findViewById(R.id.first_price);
        favorite_icon=findViewById(R.id.fav);
        add=findViewById(R.id.add);
        remove=findViewById(R.id.remove);
        counting=findViewById(R.id.counting);
        addtocart=findViewById(R.id.left_layout);


         shoelinear=(LinearLayout) findViewById(R.id.shoelayout);
        dresslinear=(LinearLayout) findViewById(R.id.dresssize);
        //dress size
       s_size=findViewById(R.id.s_size);
        l_size=findViewById(R.id.l_size);
        m_size=findViewById(R.id.M_size);
        xl_size=findViewById(R.id.xl_size);
        xxl_size=findViewById(R.id.xXl_size);
       XXXl_size =findViewById(R.id.XXXl_size);

       //shoe size
        shoesiz_36=findViewById(R.id.shoesiz_36);
        shoesiz_38=findViewById(R.id.shoesiz_38);
        shoesiz_40=findViewById(R.id.shoesiz_40);
        shoesiz_42=findViewById(R.id.shoesiz_42);
        shoesiz_44=findViewById(R.id.shoesiz_44);


        s_size.setOnClickListener(this);
        l_size.setOnClickListener(this);
        m_size.setOnClickListener(this);
        xl_size.setOnClickListener(this);
        xxl_size.setOnClickListener(this);
        XXXl_size.setOnClickListener(this);

        shoesiz_36.setOnClickListener(this);
        shoesiz_38.setOnClickListener(this);
        shoesiz_40.setOnClickListener(this);
        shoesiz_42.setOnClickListener(this);
        shoesiz_44.setOnClickListener(this);


        first_price.setPaintFlags(first_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        fetch_category_item();

        id=getIntent().getStringExtra("id");

        viewpager_urls= new ArrayList<>();

        fetch_flipper_images();

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCartItem();
            }
        });

        favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertWishlist();
            }
        });
        //expandable textview
        final TextView description=findViewById(R.id.productname);
        final ImageView plus=findViewById(R.id.plus);

        //before expand
        final ImageView minus=findViewById(R.id.minus);
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



//cart icon
        ImageView cart_icon=findViewById(R.id.favorite_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
               // finish();
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


        //slide imageview for first flipper
       /* imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();*/
        //init();

    }


//add to cart


    //inserting data to wishlist in sqlite
    public void insertWishlist()
    {

        int cart_id=Integer.parseInt(id);

        boolean isInserted=dbHandler.insert_wishlistitem(cart_id);
        if (isInserted)
        {
            //Toast.makeText(this, "Counting is "+cart_count, Toast.LENGTH_SHORT).show();
            System.out.println("WISH LIST ITEM ADDED SUCCESSFULLY");
            Toast.makeText(DetailedDressActivity.this, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println(" ADDED WISH LIST ITEM IS FAILED");

        }
    }

    //setting the adapter to slide view pager
    private void init() {

        mPager = (ViewPager) findViewById(R.id.overviewimage);
        mPager.setAdapter(new SlidingImage_Adapter(getApplicationContext(),viewpager_urls));


        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(3 * density);
        NUM_PAGES =viewpager_urls.size();


        // Pager listener over indicator
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

        if(size.trim().isEmpty())
        {
            Toast.makeText(this, "Please Select the Size", Toast.LENGTH_SHORT).show();
        }
        else
        {
            boolean isInserted=dbHandler.insert_cartitem(cart_id,cart_count, String.valueOf(datetime),Size_adpater.size_result);
            if (isInserted)
            {
                //Toast.makeText(this, "Counting is "+cart_count, Toast.LENGTH_SHORT).show();

                Toast.makeText(DetailedDressActivity.this, "Item Inserted Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Failed to INserted ", Toast.LENGTH_SHORT).show();

            }
        }

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
                            String s=jsonObject.getString("size");
                            String brand=jsonObject.getString("brand");
                           // Toast.makeText(DetailedDressActivity.this, "Size value is "+s, Toast.LENGTH_SHORT).show();

                            JSONObject jsonObject1=new JSONObject(s);


                            JSONArray jsonArray1=jsonObject1.getJSONArray("size");

                           // JSONArray jsonArray1=jsonObject.getJSONArray("size");
                            for(int j=0;j<jsonArray1.length();j++) {
                                // JSONObject jsonObject1=jsonArray1.getJSONObject(j);

                                size = jsonArray1.getString(j);
                                if (brand.equalsIgnoreCase("yoshna")) {


                                    switch (size)
                                    {
                                        case "s":
                                        case "S": s_size.setVisibility(View.VISIBLE);
                                        break;
                                        case "l":
                                        case "L": l_size.setVisibility(View.VISIBLE);
                                            break;
                                        case "m":
                                        case "M": m_size.setVisibility(View.VISIBLE);
                                            break;
                                        case "xl":
                                        case "XL": xl_size.setVisibility(View.VISIBLE);
                                            break;
                                        case "xxl":
                                        case "XXL": xxl_size.setVisibility(View.VISIBLE);
                                            break;
                                        case "xxxl":
                                        case "XXXL": XXXl_size.setVisibility(View.VISIBLE);
                                            break;
                                    }


                                }
                                else {
                                    shoelinear.setVisibility(View.VISIBLE);
                                    dresslinear.setVisibility(View.INVISIBLE);
                                    switch (size)
                                    {
                                        case "36": shoesiz_36.setVisibility(View.VISIBLE);
                                        break;
                                        case "38": shoesiz_38.setVisibility(View.VISIBLE);
                                            break;
                                        case "40": shoesiz_40.setVisibility(View.VISIBLE);
                                            break;
                                        case "42": shoesiz_42.setVisibility(View.VISIBLE);
                                            break;
                                        case "44": shoesiz_44.setVisibility(View.VISIBLE);
                                            break;
                                    }

                                }
                            }


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

    //current time
    public String current_time()
    {
        Calendar c=Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());

        return String.valueOf(datetime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.s_size:
               // l_size.setBackgroundResource(R.drawable.textview_border);
                if(touch_count%2==0)
                {
                    s_size.setTextColor(Color.parseColor("#ffffff"));
                    xxl_size.setTextColor(Color.parseColor("#a92578"));
                    l_size.setTextColor(Color.parseColor("#a92578"));
                    m_size.setTextColor(Color.parseColor("#a92578"));
                    xl_size.setTextColor(Color.parseColor("#a92578"));
                    XXXl_size.setTextColor(Color.parseColor("#a92578"));

                    s_size.setBackgroundResource(R.drawable.textview_dark_border);
                    l_size.setBackgroundResource(R.drawable.textview_border);
                    m_size.setBackgroundResource(R.drawable.textview_border);
                    xl_size.setBackgroundResource(R.drawable.textview_border);
                    xxl_size.setBackgroundResource(R.drawable.textview_border);
                    XXXl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                else
                {
                    s_size.setTextColor(Color.parseColor("#a92578"));
                    s_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }

                break;
            case R.id.l_size:
               // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    l_size.setTextColor(Color.parseColor("#ffffff"));
                    xxl_size.setTextColor(Color.parseColor("#a92578"));
                    s_size.setTextColor(Color.parseColor("#a92578"));
                    m_size.setTextColor(Color.parseColor("#a92578"));
                    xl_size.setTextColor(Color.parseColor("#a92578"));
                    XXXl_size.setTextColor(Color.parseColor("#a92578"));

                    l_size.setBackgroundResource(R.drawable.textview_dark_border);
                    s_size.setBackgroundResource(R.drawable.textview_border);
                    m_size.setBackgroundResource(R.drawable.textview_border);
                    xl_size.setBackgroundResource(R.drawable.textview_border);
                    xxl_size.setBackgroundResource(R.drawable.textview_border);
                    XXXl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                else
                {
                    l_size.setTextColor(Color.parseColor("#a92578"));
                    l_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.M_size:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    m_size.setTextColor(Color.parseColor("#ffffff"));
                    xxl_size.setTextColor(Color.parseColor("#a92578"));
                    s_size.setTextColor(Color.parseColor("#a92578"));
                    l_size.setTextColor(Color.parseColor("#a92578"));
                    xl_size.setTextColor(Color.parseColor("#a92578"));
                    XXXl_size.setTextColor(Color.parseColor("#a92578"));

                    m_size.setBackgroundResource(R.drawable.textview_dark_border);
                    s_size.setBackgroundResource(R.drawable.textview_border);
                    l_size.setBackgroundResource(R.drawable.textview_border);
                    xl_size.setBackgroundResource(R.drawable.textview_border);
                    xxl_size.setBackgroundResource(R.drawable.textview_border);
                    XXXl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                else
                {
                    m_size.setTextColor(Color.parseColor("#a92578"));
                    m_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.xl_size:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {

                    xl_size.setTextColor(Color.parseColor("#ffffff"));
                    xxl_size.setTextColor(Color.parseColor("#a92578"));
                    s_size.setTextColor(Color.parseColor("#a92578"));
                    l_size.setTextColor(Color.parseColor("#a92578"));
                    m_size.setTextColor(Color.parseColor("#a92578"));
                    XXXl_size.setTextColor(Color.parseColor("#a92578"));

                    xl_size.setBackgroundResource(R.drawable.textview_dark_border);
                    s_size.setBackgroundResource(R.drawable.textview_border);
                    m_size.setBackgroundResource(R.drawable.textview_border);
                    l_size.setBackgroundResource(R.drawable.textview_border);
                    xxl_size.setBackgroundResource(R.drawable.textview_border);
                    XXXl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                else
                {
                    xl_size.setTextColor(Color.parseColor("#a92578"));
                    xl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.xXl_size:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    xxl_size.setTextColor(Color.parseColor("#ffffff"));
                    m_size.setTextColor(Color.parseColor("#a92578"));
                    s_size.setTextColor(Color.parseColor("#a92578"));
                    l_size.setTextColor(Color.parseColor("#a92578"));
                    xl_size.setTextColor(Color.parseColor("#a92578"));
                    XXXl_size.setTextColor(Color.parseColor("#a92578"));

                    xxl_size.setBackgroundResource(R.drawable.textview_dark_border);
                    s_size.setBackgroundResource(R.drawable.textview_border);
                    m_size.setBackgroundResource(R.drawable.textview_border);
                    l_size.setBackgroundResource(R.drawable.textview_border);
                    xl_size.setBackgroundResource(R.drawable.textview_border);
                    XXXl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                else
                {
                    xxl_size.setTextColor(Color.parseColor("#a92578"));
                    xxl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.XXXl_size:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    XXXl_size.setTextColor(Color.parseColor("#ffffff"));
                    m_size.setTextColor(Color.parseColor("#a92578"));
                    s_size.setTextColor(Color.parseColor("#a92578"));
                    l_size.setTextColor(Color.parseColor("#a92578"));
                    xl_size.setTextColor(Color.parseColor("#a92578"));
                    xxl_size.setTextColor(Color.parseColor("#a92578"));


                    XXXl_size.setBackgroundResource(R.drawable.textview_dark_border);
                    s_size.setBackgroundResource(R.drawable.textview_border);
                    m_size.setBackgroundResource(R.drawable.textview_border);
                    l_size.setBackgroundResource(R.drawable.textview_border);
                    xl_size.setBackgroundResource(R.drawable.textview_border);
                    xxl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                else
                {
                    XXXl_size.setTextColor(Color.parseColor("#a92578"));
                    XXXl_size.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.shoesiz_36:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    shoesiz_36.setTextColor(Color.parseColor("#ffffff"));
                    shoesiz_38.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_40.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_42.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_44.setTextColor(Color.parseColor("#a92578"));


                    shoesiz_36.setBackgroundResource(R.drawable.textview_dark_border);
                    shoesiz_38.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_40.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_42.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_44.setBackgroundResource(R.drawable.textview_border);

                    touch_count++;
                }
                else
                {
                    shoesiz_36.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_36.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;

            case R.id.shoesiz_38:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    shoesiz_38.setTextColor(Color.parseColor("#ffffff"));
                    shoesiz_36.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_40.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_42.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_44.setTextColor(Color.parseColor("#a92578"));


                    shoesiz_38.setBackgroundResource(R.drawable.textview_dark_border);
                    shoesiz_36.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_40.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_42.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_44.setBackgroundResource(R.drawable.textview_border);

                    touch_count++;
                }
                else
                {
                    shoesiz_38.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_38.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.shoesiz_40:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {


                    shoesiz_40.setTextColor(Color.parseColor("#ffffff"));
                    shoesiz_36.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_38.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_42.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_44.setTextColor(Color.parseColor("#a92578"));

                    shoesiz_40.setBackgroundResource(R.drawable.textview_dark_border);
                    shoesiz_36.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_38.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_42.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_44.setBackgroundResource(R.drawable.textview_border);

                    touch_count++;
                }
                else
                {
                    shoesiz_40.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_40.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.shoesiz_42:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    shoesiz_42.setTextColor(Color.parseColor("#ffffff"));
                    shoesiz_36.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_38.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_40.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_44.setTextColor(Color.parseColor("#a92578"));

                    shoesiz_42.setBackgroundResource(R.drawable.textview_dark_border);
                    shoesiz_36.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_40.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_38.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_44.setBackgroundResource(R.drawable.textview_border);

                    touch_count++;
                }
                else
                {
                    shoesiz_42.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_42.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;
            case R.id.shoesiz_44:
                // s_size.setBackgroundResource(R.drawable.textview_border);

                if(touch_count%2==0)
                {
                    shoesiz_44.setTextColor(Color.parseColor("#ffffff"));
                    shoesiz_36.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_38.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_40.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_42.setTextColor(Color.parseColor("#a92578"));

                    shoesiz_44.setBackgroundResource(R.drawable.textview_dark_border);
                    shoesiz_36.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_40.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_42.setBackgroundResource(R.drawable.textview_border);
                    shoesiz_38.setBackgroundResource(R.drawable.textview_border);

                    touch_count++;
                }
                else
                {
                    shoesiz_44.setTextColor(Color.parseColor("#a92578"));
                    shoesiz_44.setBackgroundResource(R.drawable.textview_border);
                    touch_count++;
                }
                break;









        }
    }



}
