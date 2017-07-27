package com.upadhyay.nilay.driver.UserInterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.upadhyay.nilay.driver.R;
import com.upadhyay.nilay.driver.UserSession.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {
    ImageView background;
    SessionManager session;
    String name;
    int dbid, pbid = 0;
    String dname ="",pname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String message = intent.getStringExtra("msg");
        TextView dnameEdit = (TextView) findViewById(R.id.dname);
        TextView aname = (TextView) findViewById(R.id.aname);
        TextView result = (TextView) findViewById(R.id.result);
        session =  new SessionManager(getApplicationContext());
        //textView.setText(id);
        background = (ImageView)findViewById(R.id.iv_backgroundW);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Driver app - "+"     "+"Auction Finished");
        setSupportActionBar(toolbar);
        initBackground();
        try {
            JSONArray ja = new JSONArray(message);

            JSONObject mainobj = ja.getJSONObject(0);
            name = mainobj.getString("name");
            dname = mainobj.getString("dbid");
            Log.e("name", String.valueOf(dbid));
            String  price = mainobj.getString("price");
            //Log.e("driver bid", String.valueOf(dbid));
            pname = mainobj.getString("pbid");
            String pl = mainobj.getString("pl");
            String pd = mainobj.getString("pd");
            aname.setText(String.valueOf(name) +" is finished \nprice for ride is "+price);
            dnameEdit.setText("Hello, "+dname.toString());
            result.setText("Passenger Name: "+String.valueOf(pname)+"\n\nPassenger Location "+String.valueOf(pl)+"\n\nPassenger Destination: "+String.valueOf(pd));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(session.checkLogin()){
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // name
            String type = user.get(SessionManager.KEY_TYPE);

            // email
            String email = user.get(SessionManager.KEY_EMAIL);
            if (type.equalsIgnoreCase("Driver")){

            }
        }
    }
    private void initBackground() {

        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
    }


}
