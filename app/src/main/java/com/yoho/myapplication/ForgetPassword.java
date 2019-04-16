package com.yoho.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity {
EditText edit_mblno;
ImageView img_enter;
RequestQueue requestQueue;
RequestQueue phonerequestque;
int number=0;
    JSONArray jsonArray;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

                edit_mblno=findViewById(R.id.edit_mblno);
                edit_mblno.setBackground(null);
                img_enter=findViewById(R.id.img_enterr);
                requestQueue=Volley.newRequestQueue(this);
                phonerequestque=Volley.newRequestQueue(this);

            img_enter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(edit_mblno.getText().toString().trim().length() !=0)
        {
            Intent i=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);

           // getphonenumber(edit_mblno.getText().toString().trim());
        }

    }
});

    }

    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data
        // username = editTextUsername.getText().toString().trim();
        //  password = editTextPassword.getText().toString().trim();
final String phone=edit_mblno.getText().toString().trim();

        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.FORGETPIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response" + response);
                        loading.dismiss();
                        try {
                            //Creating the json object from the response
                           JSONObject jsonResponse = new JSONObject(response);

                        //                            //If it is success
                          if(jsonResponse.getString(Config.TAG_RESPONSE).equalsIgnoreCase("Success")){
                                //Asking user to confirm otp
                               // confirmOtp();

                             /*   Intent i=new Intent(ForgetPassword.this,OTP.class);
                              i.putExtra("phone",phone);
                              edit_mblno.setText("");

                              startActivity(i);*/

                            }
                            else{
                                //If not successful user may already have registered
                                Toast.makeText(ForgetPassword.this, "Invalid  Phone Number", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ForgetPassword.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                // params.put(Config.KEY_USERNAME, username);
                //  params.put(Config.KEY_PASSWORD, password);
                params.put(Config.KEY_PHONE,phone);
                return params;
            }
        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    public  void getphonenumber(final String phn)
    {
  StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.FORGET_PHONE_GET_URL, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        System.out.println("phone Responce" +response.toString());
        // Toast.makeText(getApplicationContext(),"responce"+response,Toast.LENGTH_LONG).show();

        try {
            JSONObject jsonObject = new JSONObject(response);

            jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject j=jsonArray.getJSONObject(i);

              String contact=  j.getString("contact");
                System.out.println("contact Responce" +response.toString());
              if(phn.equalsIgnoreCase(contact))
              {
                 //
                  register();
              }
              else {
                  number++;
                  if(number==jsonArray.length())
                  {
                      Toast.makeText(getApplicationContext(),"Your contact is not Registered Before" ,Toast.LENGTH_LONG).show();
                  }

              }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
});
phonerequestque.add(stringRequest);
    }
}
