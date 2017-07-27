package com.upadhyay.nilay.driver.Messanging;

/**
 * Created by nilay on 2/5/2017.
 */

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static String TAG ="MyFirebaseInstanceIDService";
    String refreshedToken;
    public static final String MyPREFERENCES = "com.upadhyay.nilay.driver.regis" ;
    @Override
    public void onTokenRefresh() {
        Log.e("sada","on refresh");
        // super.onTokenRefresh();
        refreshedToken= FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("firebaseToken", refreshedToken);
        editor.commit();
    }

   }
