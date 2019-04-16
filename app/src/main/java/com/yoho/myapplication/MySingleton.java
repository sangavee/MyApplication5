package com.yoho.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mInstance;
    private static Context mctx;
    private RequestQueue requestQueue;

    //constructor

    private  MySingleton(Context context){
        mctx =context;
        requestQueue =getRequestQueue();
    }

//method to reurn request queue

    private  RequestQueue getRequestQueue(){
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());

        }
    return requestQueue;
    }

    //create instance class

    public static synchronized MySingleton getInstance(Context context)
    {

        if(mInstance==null)
        {
            mInstance = new MySingleton(context);

        }
        return  mInstance;
    }

    //add request to request queue
    public<T> void addToRequestque(Request<T> request){
        getRequestQueue().add(request);
    }
}
