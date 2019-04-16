package com.yoho.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.ULocale;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {

   static BottomNavigationView bottomNavigationView;
    private TextView mTextMessage;
SwipeRefreshLayout swipeRefreshLayout;
    DbHandler dbHandler;

    RelativeLayout search_layout;

    SharedPreferences sharedPreferences;
    //flip adapter
    private AdapterViewFlipper adapterViewFlipper;
    private int[] flipImages={R.drawable.banner,R.drawable.banner};
    int mposition=-1;

    //flipper images
    RequestQueue requestQueue_yoshna,requestQueue_store;
    RequestQueue requestQueue_data;

    //for yoshna recycle
    RequestQueue requestQueue;
    GridviewAdapter adapter;
    ArrayList<Integer> arrayList_cart;
    ArrayList<Integer> arrayList_cart_count;
    ArrayList<String> arrayList_cart_date;
    ArrayList<String> arrayList_cart_size;

    String phonenumber,name;

    boolean address_state;
    //for oh brand recycle
    RequestQueue requestQueue_two;
    GridviewAdapter adapter_two;

    // first recycle view
    RecyclerView recycle1;
    List<GridviewProduct> productList;

    //second recycler view
    RecyclerView second_recycler;
    List<GridviewProduct> ohbrand_list;

    //slider image view
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> yoshna_viewpager_urls;
    private ArrayList<String> ohpair_viewpager_urls;

    //ohpair brand
    private static ViewPager mPager2;
    private static int currentPage2 = 0;
    private static int NUM_PAGES2 = 0;

    private ArrayList<ImageModel> shoemodelarraylist;


    //array for slider images yoshnas
    private int[] myImageList = new int[]{R.drawable.banner, R.drawable.banner,
            R.drawable.banner,R.drawable.banner
            ,R.drawable.banner};

    //array for slide images for ohpair brand
    private int[] shooelist = new int[]{R.drawable.shoe1, R.drawable.shoes2,
            R.drawable.shoe1,R.drawable.shoes2
    };
    NavigationView navigationView;

   static View view;
   static Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout=findViewById(R.id.swipe);

        dbHandler=new DbHandler(getApplicationContext());
        arrayList_cart=new ArrayList<>();
        arrayList_cart_count=new ArrayList<>();
        arrayList_cart_date=new ArrayList<>();
        arrayList_cart_size=new ArrayList<>();

        yoshna_viewpager_urls= new ArrayList<>();

        ohpair_viewpager_urls= new ArrayList<>();
        //getting images for flipper
        fetchImage_yoshna();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        phonenumber=sharedPreferences.getString("phonenumber",null);
        name=sharedPreferences.getString("name",null);
        address_state=sharedPreferences.getBoolean("address",false);

        bottomNavigationView=findViewById(R.id.navigationView);

      //  bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
        case R.id.navigation_home:

        return true;


        case R.id.navigation_category:


        Intent i2=new Intent(getApplicationContext(),BasicCategoryActivity.class);

        startActivity(i2);
        boolean b=true;
        bottomNavigationView.getMenu().performIdentifierAction(R.id.navigation_home,0);
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        return true;

        case R.id.navigation_profile:
        loginCheck();
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);

        return true;

        case R.id.navigation_cart:
        Intent i4=new Intent(getApplicationContext(),CartViewActivity.class);
        startActivity(i4);
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        return true;
    }

        return false;
    }
});

search_layout=findViewById(R.id.search_layout);
search_layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(i);
    }
});

        //click event in banner image
        ImageView yoshna_img=findViewById(R.id.yoshna_brand);
        yoshna_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),CatogryAction.class);
                startActivity(i);
            }
        });

        //click event in banner image
        ImageView ohpair_img=findViewById(R.id.oh_pair_brand);
        ohpair_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),OhpairCategoryActivity.class);
                startActivity(i);
            }
        });

        //notification link
        ImageView notification_icon=findViewById(R.id.notify_icon);
        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent=new Intent(getApplicationContext(),NotificationActivity.class);
                startActivity(notificationIntent);
            }
        });

        //wishlist link
        ImageView wishlist=findViewById(R.id.wishlisticon);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wishlistintent=new Intent(getApplicationContext(),WishlistActivity.class);
                startActivity(wishlistintent);
            }
        });

        //recycleview with grid
        recycle1 = (RecyclerView) findViewById(R.id.my_recycler_view);
        productList = new ArrayList<>();
        recycle1.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(manager);
        fetch_yoshna_item();
        check_loginid_forCart();
        //second recycle view with grid
      /*  second_recycler=findViewById(R.id.my_recycler_view_two);
        ohbrand_list = new ArrayList<>();
        GridLayoutManager manager_two = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        second_recycler.setLayoutManager(manager_two);*/
      //  fetch_ohbrand_item();

//view pager data

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*if(sharedPreferences.getBoolean("loggedIn",false)) {
    Menu menu = navigationView.getMenu();
    menu.getItem(R.id.Logout).setEnabled(true);
}

else
{

}*/
       /* Menu menu = navigationView.getMenu();
        menu.getItem(R.id.Logout).setEnabled(false);*/
       /* Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.getItem(R.id.logout);
        nav_item2.setVisible(false);
*/
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Menu menu=navigationView.getMenu();
        if(sharedPreferences.getBoolean("loggedIn",false)) {
            item = menu.getItem(R.id.logout);
            item.setVisible(true);
        }

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_home) {

            // Handle the camera action
        } else if (id == R.id.navigation_category) {

        } else if (id == R.id.navigation_profile) {
           loginCheck();

        } else if (id == R.id.MyOrders) {
            Intent myorder=new Intent(getApplicationContext(), MyOrderActivity.class);
            startActivity(myorder);


        } else if (id == R.id.Wishlist) {
            Intent wishlist=new Intent(getApplicationContext(), WishlistActivity.class);
            startActivity(wishlist);

        } else if (id == R.id.NNotofication) {
            Intent navi=new Intent(getApplicationContext(), NotificationActivity.class);
            startActivity(navi);

        }else if (id == R.id.terms) {

            Intent terms=new Intent(getApplicationContext(), TermsActivity.class);
            startActivity(terms);


        } else if (id == R.id.aboutus) {
            Intent aboutus=new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(aboutus);

        } else if (id == R.id.contactus) {
            Intent contactus=new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(contactus);

        } else if (id == R.id.Logout) {

            fetchCartItem();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
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

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(HomeActivity.this,yoshna_viewpager_urls));

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(3 * density);
        NUM_PAGES =yoshna_viewpager_urls.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
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
    //setting adapter for view pager for oh pair brand
    /*private void init2() {

        mPager2 = (ViewPager) findViewById(R.id.pager2);
        mPager2.setAdapter(new SlidingImage_Adapter(HomeActivity.this,ohpair_viewpager_urls));


        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator2);
        indicator.setViewPager(mPager2);
        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(3 * density);
        NUM_PAGES2 =ohpair_viewpager_urls.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage2 == NUM_PAGES2) {
                    currentPage2 = 0;
                }
                mPager2.setCurrentItem(currentPage2++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
                                              @Override
                                              public void onPageSelected(int position) {
                                                  currentPage2 = position;
                                              }
                                              @Override
                                              public void onPageScrolled(int pos, float arg1, int arg2) {
                                              }
                                              @Override
                                              public void onPageScrollStateChanged(int pos) {
                                              }
                                          }
        );

    }*/

    private  void fetch_yoshna_item() {

        //   showSimpleProgressDialog(getActivity(), "Loading...","Fetching Json",false);


        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_YOSHNA_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response from home activity "+response);
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

                    adapter = new GridviewAdapter(getApplicationContext(), productList);
                    recycle1.setAdapter(adapter);

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

                ;
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    private  void fetch_ohbrand_item() {

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.GET_OHPAIR_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=jsonArray.length()-1;i>=0;i--)
                    {
                        GridviewProduct ohpairPojo = new GridviewProduct();
                        try
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            ohpairPojo.setId(jsonObject.getString("id"));
                            ohpairPojo.setProductimage(jsonObject.getString("images"));
                            ohpairPojo.setProductname(jsonObject.getString("image_name"));

                            ohpairPojo.setProductprice(jsonObject.getString("price"));
                            ohpairPojo.setBrand(jsonObject.getString("brand"));
                            ohpairPojo.setProductdescription(jsonObject.getString("description"));
                            ohpairPojo.setCategories(jsonObject.getString("categories"));

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        ohbrand_list.add(ohpairPojo);
                    }

                    // Toast.makeText(getContext(), "ARRAYLIST  "+orderlistPojoArrayList.toString() , Toast.LENGTH_SHORT).show();

                    adapter_two=new GridviewAdapter(getApplicationContext(),ohbrand_list);

                    second_recycler.setAdapter(adapter_two);

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

                ;
        requestQueue_two=Volley.newRequestQueue(getApplicationContext());
        requestQueue_two.add(jsonArrayRequest);
    }


    // fetching data for view pager flipper
    private void fetchImage_yoshna()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.FETCH_IMAGE_YOSHNA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Flipper image response is "+response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        if(jsonObject.getString("brand").equalsIgnoreCase("YOSHNA"))
                        {
                            yoshna_viewpager_urls.add(jsonObject.getString("images"));
                        }
                        else
                        {
                            ohpair_viewpager_urls.add(jsonObject.getString("images"));
                        }


                    }

                    init();
                  //  init2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue_yoshna=Volley.newRequestQueue(getApplicationContext());
        requestQueue_yoshna.add(stringRequest);
    }


    //checking user loged_in or not
    public void loginCheck()
    {
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPreferences.getBoolean("loggedIn",false))
        {
            Intent i=new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(i);

        }
        else
        {
            Intent intent=new Intent(HomeActivity.this,Sighin_LOgin.class);
            startActivity(intent);

        }

    }


    public  void check_loginid_forCart()
    {
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String id=sharedPreferences.getString("phonenumber",null);
        if(sharedPreferences.getBoolean("loggedIn",false))
        {
            getCartData(id);

        }

    }

    //getting existing cart item
    public void getCartData(final String id)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.GET_CART_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("GETTING CART ITEM "+response);
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String user_id=jsonObject.getString("user_id");
                        if(user_id.equals(id))
                        {
                            int id1= Integer.parseInt(jsonObject.getString("id"));
                            int count= Integer.parseInt(jsonObject.getString("count"));
                            String date=jsonObject.getString("datetime");
                            String size=jsonObject.getString("size");
                            DbHandler dbHandler=new DbHandler(getApplicationContext());
                            dbHandler.insert_cartitem(id1,count,date,size);
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
                params.put("id",id);
                return params;
            }
        };
        requestQueue_data=Volley.newRequestQueue(getApplicationContext());
        requestQueue_data.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
