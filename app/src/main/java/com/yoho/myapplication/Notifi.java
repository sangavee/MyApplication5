package com.yoho.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Notifi extends AppCompatActivity {
//TextView title,msg;
RecyclerView recyclerView;
NotifiAdapter notifiAdapter;
String a,b;
ArrayList<Pojo> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi);
      /*  title=findViewById(R.id.title);
        msg=findViewById(R.id.message);
        Intent i = getIntent();
     a=   i.getStringExtra("title");
     b=i.getStringExtra("message");
     title.setText(a);
     msg.setText(b);*/
      recyclerView=findViewById(R.id.recycler);
        Intent i = getIntent();
        a=   i.getStringExtra("title");
        b=i.getStringExtra("message");
        arrayList=new ArrayList<>();
        Pojo pojo = new Pojo();
        pojo.setTitle(a);
        pojo.setMessage(b);
        arrayList.add(pojo);
        notifiAdapter=new NotifiAdapter(getApplicationContext(),arrayList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notifiAdapter);

    }
}
