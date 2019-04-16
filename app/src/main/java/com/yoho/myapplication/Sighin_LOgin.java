package com.yoho.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sighin_LOgin extends AppCompatActivity {

    TextView forget_id, new_user;

    EditText edit_loginid, edit_phnno;
    SharedPreferences sharedPreferences;
    ImageView pass_icon;

     boolean b=true;

    Button login;
    RequestQueue requestQueue;
    RequestQueue requestQueue_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighin__login);

        forget_id = findViewById(R.id.forget_id);
        new_user = findViewById(R.id.new_user);
        edit_loginid = findViewById(R.id.edit_loginid);
        edit_phnno = findViewById(R.id.edit_phnno);
        login = findViewById(R.id.btn_login);

        edit_phnno.setBackground(null);
        edit_loginid.setBackground(null);

        pass_icon=findViewById(R.id.pass_icon);
        pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b)
                {
                   // edit_loginid.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//used to hide password

                    edit_loginid.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass_icon.setImageResource(R.drawable.visibility_on);
                    b=false;

                }
                else
                {
                   edit_loginid.setTransformationMethod(PasswordTransformationMethod.getInstance());//used to show password
                    pass_icon.setImageResource(R.drawable.visibility_off);
                    b=true;
                }
            }
        });


        forget_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivity(i);
            }
        });


        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMethod();
               }
        });

    }

    //login method
    public void loginMethod()
    {
        if (edit_phnno.getText().length() == 0 || edit_loginid.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "PLEASE FILL ALL THE INFORMATION...!!!", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request=new StringRequest(Request.Method.POST, Config.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);

                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            if(jsonObject.getString("result").equals("success"))
                            {
                                String name=jsonObject.getString("name");
                                String phone=jsonObject.getString("phonenumber");
                                boolean address_state= Boolean.parseBoolean(jsonObject.getString("address_state"));

                                sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putBoolean("loggedIn",true);
                                editor.putString("pin",edit_loginid.getText().toString());
                                editor.putString("name",name);
                                editor.putBoolean("address",address_state);
                                editor.putString("phonenumber",edit_phnno.getText().toString());
                                editor.commit();

                                System.out.println(response);
                                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Sighin_LOgin.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   /* String s="error";
                    if(s.equalsIgnoreCase(response.trim().toString()))
                    {
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"php responce:" +response,Toast.LENGTH_LONG).show();
                        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("loggedIn",true);
                        editor.putString("pin",edit_loginid.getText().toString());
                        editor.putString("phonenumber",edit_phnno.getText().toString());
                        editor.commit();

                        System.out.println(response);
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Sighin_LOgin.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> Parameters = new HashMap<String, String>();

                    Parameters.put("contact", edit_phnno.getText().toString());

                    Parameters.put("token", edit_loginid.getText().toString());
                    return Parameters;

                }
            };
            requestQueue=Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }
    }


    }


