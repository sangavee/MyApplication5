package com.yoho.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;

import static com.android.volley.VolleyLog.TAG;

public class FcmInstanceIdService extends FirebaseInstanceIdService {
    String token;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
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
                                       String msg = token;
                                       Log.d(TAG, msg);
                                       //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                   }
                               });


    //Shared preference

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences(getString(R.string.fcmpreference),Context.MODE_PRIVATE);
    SharedPreferences.Editor editor=sharedPreferences.edit();
    editor.putString(getString(R.string.fcmtoken),token);
    editor.commit();
    }
}
