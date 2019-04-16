package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SortingDialogeFragment extends DialogFragment {
RadioGroup radioGroup;
RadioButton relavance,desc,lowtohigh,hightolow,sortingname;
    List<GridviewProduct> productlist;
GridviewAdapter mAdapter;
RequestQueue requestQueue;
Context context;
String option;
int id=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sorting_dialoge, container, false);

         radioGroup=v.findViewById(R.id.radiogroup);
context=getActivity().getApplicationContext();
      /*   relavance=v.findViewById(R.id.relavance);
       */
        lowtohigh=v.findViewById(R.id.Pricelowtohigh);
        hightolow=v.findViewById(R.id.pricehightolow);
        sortingname=v.findViewById(R.id.sortbyname);
        desc=v.findViewById(R.id.desc);
productlist=new ArrayList<>();
        //Radiogroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    /*case R.id.relavance:
                        option=relavance.getText().toString();
                        break;
                    */
                    case R.id.Pricelowtohigh:
                        option=lowtohigh.getText().toString();
                        break;
                    case R.id.pricehightolow:
                        option=hightolow.getText().toString();
                        break;
                    case R.id.sortbyname:
                        option=sortingname.getText().toString();
                        break;
                    case R.id.desc:
                        option=desc.getText().toString();
                        break;

                }
            }
        });
//finish button
        final TextView finish=v.findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toast.makeText(getContext()," value is "+option,Toast.LENGTH_SHORT).show();

                //  String value=getRadioValue();
                Intent i=new Intent(getActivity(),SareesCategoryActivity.class);
               // i.putExtra("radiobutton_data",value);
                dismiss();*/

                switch (id)
                {
                    case 0:
                        SareesCategoryActivity.sortingtext.setText(option);
                        fetchJson();
                        break;
                    case 1:
                        SareesCategoryActivity.sortingtext.setText(option);
                        fetchJson();
                        break;
                    case 2:
                        SareesCategoryActivity.sortingtext.setText(option);
                        fetchJson();

                        break;
                    case 3:
                        SareesCategoryActivity.sortingtext.setText(option);
                        fetchJson();
                        break;
                }
                dismiss();

            }
        });

       return v;
    }






    public void fetchJson()
    {

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Config.SORTING,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("Response" +response);
                try {
                   JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        GridviewProduct gridviewProduct = new GridviewProduct();

                        //removeSimpleProgressDialog();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //
                        gridviewProduct.setId(jsonObject.getString("id"));
                        gridviewProduct.setProductimage(jsonObject.getString("images"));
                        gridviewProduct.setProductname(jsonObject.getString("image_name"));
                        gridviewProduct.setProductprice(jsonObject.getString("price"));
                        gridviewProduct.setBrand(jsonObject.getString("brand"));
                        gridviewProduct.setProductdescription(jsonObject.getString("description"));

                        productlist.add(gridviewProduct);
                    }
                    mAdapter  = new GridviewAdapter(context, productlist);
                    SareesCategoryActivity.recycle1.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.print(error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters=new HashMap<String, String>();
                parameters.put("sort",option);
                parameters.put("category",Config.CATEGORY_NAME);
                return parameters;
            }
        }
        ;
        requestQueue=Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

}
