package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BasicCategoryActivity extends AppCompatActivity {

    String[] images_name={"Saree","Leggings","Jeans","Kids Wear"};
    String[] oh_pair_name={"Casuals","kids wear","High Heels","Sports"};
    int[] images={R.drawable.saree11,R.drawable.a3,R.drawable.a4,R.drawable.a1};
    int[] oh_pair_images={R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4};

    List<ListviewPojo> listviewlist;
    List<ListviewPojo> listviewlist2;
RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_category);

//cart icon
        ImageView cart_icon=findViewById(R.id.favorite_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartViewActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        //back aero
        ImageView backAero=findViewById(R.id.back_icon);
        backAero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        RecyclerView recyclerView=findViewById(R.id.my_recycler_view);
        RecyclerView recyclerView2=findViewById(R.id.my_recycler_view_two);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);

       // layoutManager=new LinearLayoutManager(getApplicationContext());
       // recyclerView.setLayoutManager(layoutManager);

        listviewlist=new ArrayList<>();
        for (int i=0;i<images.length;i++)
        {
            listviewlist.add(new ListviewPojo(images_name[i],images[i]));

        }

        listviewlist2=new ArrayList<>();
        for (int i=0;i<images.length;i++)
        {
            listviewlist2.add(new ListviewPojo(oh_pair_name[i],oh_pair_images[i]));

        }
        recyclerView.setHasFixedSize(true);
        ListViewAdapter adapter=new ListViewAdapter(this,listviewlist);
        recyclerView.setAdapter(adapter);
        recyclerView2.setHasFixedSize(true);
       SecondRecyclerAdapter adapter2=new SecondRecyclerAdapter(this,listviewlist2);
        recyclerView2.setAdapter(adapter2);


//set divide line in recycler view
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView2.addItemDecoration(itemDecoration);

        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        LinearLayoutManager manager2=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(manager2);
       // recyclerView2.setLayoutManager(manager);
       /* GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        int spanCount = 1; // 3 columns
        int spacing = 35; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));

        recyclerView2.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));*/

    }
}

