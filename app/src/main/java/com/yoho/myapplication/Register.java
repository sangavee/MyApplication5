package com.yoho.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.volley.VolleyLog.TAG;


public class Register extends AppCompatActivity  {
    String msg ="";
    String app_server_url="http://192.168.1.7/yoshna/insert.php";
    boolean b=true;
    boolean bb=true;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText edit_name, edit_phnno, edit_email,edit_password,edit_repassword;
    Button Register;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    ImageView pass_icon,re_pass_icon;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        edit_name = findViewById(R.id.edit_name);
        edit_phnno = findViewById(R.id.edit_phnno);
        edit_email = findViewById(R.id.edit_mailid);
        edit_password = findViewById(R.id.edit_password);
        edit_repassword = findViewById(R.id.edit_repassword);
        Register = findViewById(R.id.btn_register);

        pass_icon=findViewById(R.id.pass_icon);
        re_pass_icon=findViewById(R.id.re_pass_icon);

        pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bb)
                {
                    // edit_loginid.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//used to hide password

                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass_icon.setImageResource(R.drawable.visibility_on);
                    bb=false;

                }
                else
                {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//used to show password
                    pass_icon.setImageResource(R.drawable.visibility_off);
                    bb=true;
                }
            }
        });

        re_pass_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b)
                {
                    // edit_loginid.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//used to hide password

                    edit_repassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass_icon.setImageResource(R.drawable.visibility_on);
                    b=false;

                }
                else
                {
                    edit_repassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//used to show password
                    pass_icon.setImageResource(R.drawable.visibility_off);
                    b=true;
                }
            }
        });

        edit_name.setBackground(null);
        edit_email.setBackground(null);
        edit_password.setBackground(null);
        edit_repassword.setBackground(null);
        edit_phnno.setBackground(null);


        requestQueue=Volley.newRequestQueue(getApplicationContext());


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                method1();

                if(edit_password.getText().toString().trim().equals(edit_repassword.getText().toString().trim()) && !edit_password.getText().toString().isEmpty())
                {
                    boolean state=  validatePassword();
                    if(state)
                    {
                        boolean state_email=validateEmail();
                        if(state_email)
                        {
                            register_data();

                        }
                    }

                }
                else 
                {
                    Toast.makeText(Register.this, "Password and confirm password should be same", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //registering new useer data to php
    public void register_data()
    {
        if (edit_name.getText().length() == 0 || edit_phnno.getText().length() == 0 || edit_repassword.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "PLEASE FILL ALL THE INFORMATION...!!!", Toast.LENGTH_SHORT).show();
        } else if(edit_phnno.getText().length()<10) {
            Toast.makeText(getApplication(),"Enter correct Phone Number",Toast.LENGTH_LONG).show();

        } else {

            StringRequest request = new StringRequest(Request.Method.POST, Config.REGISTER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String res=response.toString();
                    System.out.println(res);

if(res.equals("Successe"))
{
    Toast.makeText(getApplicationContext(), "User Registered Successfully...", Toast.LENGTH_SHORT).show();

    Intent i = new Intent(Register.this, Sighin_LOgin.class);
    startActivity(i);
    finish();
}

                    else
                    {
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> Parameters = new HashMap<String, String>();

                    Parameters.put("name", edit_name.getText().toString());
                    Parameters.put("contact", edit_phnno.getText().toString());
                    Parameters.put("email", edit_email.getText().toString());
                    Parameters.put("token", edit_password.getText().toString());
                    return Parameters;
                }
            };

            requestQueue.add(request);

        }
    }

//password validation
    private boolean validatePassword() {
        String passwordInput =edit_repassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            shoemessege("Password Rule :","Password should contain atleast minimum 8 characters which include one special character and one digit");
           // Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //Email validation
    private boolean validateEmail() {
        String emailInput =edit_email.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailInput);

        if (emailInput.isEmpty()) {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();

            return false;
        }
        else if (!matcher.matches()){

      //  else if (!EMAIL_PATTERN.matches(emailInput)) {


         //   return matcher.matches();
            shoemessege("Email Rule : ","Please enter a valid email address");
            // Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //alert message
    private void shoemessege(String title,String msg)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }


    public void method1()
    {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        msg = token;
                       // Log.d(TAG, msg);
                        //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        StringRequest stringRequest= new StringRequest(Request.Method.POST, app_server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> paramas=new HashMap<String, String>();
                paramas.put("token", msg);
                return paramas;
            }
        };
        MySingleton.getInstance(Register.this).addToRequestque(stringRequest);
    }


}
