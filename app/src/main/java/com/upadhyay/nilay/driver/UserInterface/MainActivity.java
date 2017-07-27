package com.upadhyay.nilay.driver.UserInterface;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.nilay.myapplication.backend.auctionTimeApi.model.AuctionTime;
import com.upadhyay.nilay.driver.backend.AuctionDetailsEndpoint;
import com.upadhyay.nilay.driver.R;
import com.upadhyay.nilay.driver.UserSession.SessionManager;
import com.upadhyay.nilay.driver.beans.AuctionMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements Communicator,DisplayAuctionFragment.OnFragmentInteractionListener,PlaceBid.OnFragmentInteractionListener {


    Toolbar mToolbar;
    String regID;
    String userID;
    String traderType;
    // Session Manager Class
   SessionManager session;
    SharedPreferences sharedPref;
    public static final String MyPREFERENCES = "com.upadhyay.nilay.driver.regis" ;
    public final static String appurl = "https://app-auction-17.appspot.com/_ah/api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getApplicationContext().getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);
        session =  new SessionManager(getApplicationContext());
       // setSupportActionBar(mToolbar);
       // listview.setAdapter(myAdapter);
        if(session.checkLogin()){
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // name
            String type = user.get(SessionManager.KEY_TYPE);

            // email
            String email = user.get(SessionManager.KEY_EMAIL);
            loginResponse(email,type);
        }
        else{
            UserInformationFragment userInformationFragment = new UserInformationFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,userInformationFragment).commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void auctionClicked(View view, int position, String[] auctionDetail) {
        //FragmentTransaction fragmentTransaction = ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("AuctionDetails");
        Bundle args = new Bundle();

        args.putString("username",userID);
        args.putString("type",traderType);
        args.putStringArray("ad",auctionDetail);
        args.putString("regID",regID);
        PlaceBid placeBid = new PlaceBid();
        placeBid.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container,placeBid).commit();
    }

    @Override
    public void loginResponse(String email, String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        userID = name;
        traderType = "Driver";
        regID =sharedPref.getString("firebaseToken","NotFound");
        AuctionDetailsEndpoint auctionDetailsEndpoint = new AuctionDetailsEndpoint(new AsyncRespose() {
            int k=0;
            JSONArray auctiondataarr = new JSONArray();
            JSONObject auctiondata  = new JSONObject();
            @Override
            public void progressFinish(List<AuctionTime> auctionTime) {
                if(auctionTime.size() > 0) {
                    //Log.e("size","10");
                    try {
                        for (k = 0; k < auctionTime.size(); k++) {
                            JSONObject auction = new JSONObject();
                            auction.put("ID", auctionTime.get(k).getId());
                            auction.put("startTime", auctionTime.get(k).getStartTime());
                            auction.put("endTime", auctionTime.get(k).getEndTime());
                            auction.put("name",auctionTime.get(k).getAuctionName());
                            long currtime= System.currentTimeMillis() - Long.parseLong(String.valueOf(auctionTime.get(k).getEndTime()));
                            if (currtime < 0){
                                auctiondataarr.put(auction);
                            }else
                            {

                            }
                        }
                        auctiondata.put("auction", auctiondataarr);
                        // Log.e("data", String.valueOf(auctiondataarr.length()));
                    } catch (JSONException je) {
                    }
                    Realm.init(getApplicationContext());
                    Realm realm = Realm.getDefaultInstance();
                    // can be more optimized here.. by not deleting data every time
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<AuctionMain> auctionMains= realm.where(AuctionMain.class).findAll();
                            auctionMains.where().findAll().deleteAllFromRealm();

                            realm.createObjectFromJson(AuctionMain.class, auctiondata);
                        }
                    });
                }
                // realm.close();
                DisplayAuctionFragment displayAuctionFragment = new DisplayAuctionFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,displayAuctionFragment).commit();
            }
        });
        auctionDetailsEndpoint.execute();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawerListener.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_logout:
                session.logoutUser();
                UserInformationFragment userInformationFragment = new UserInformationFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,userInformationFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //drawerListener.onConfigurationChanged(newConfig);
    }
    class Counter extends CountDownTimer{

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("time !!!!!!!!!", String.valueOf(millisUntilFinished/1000));
         }
        @Override
        public void onFinish() {
        }
    }
    public interface AsyncRespose {
        void progressFinish(List<AuctionTime> output);
    }
    public interface AsyncRegistraion {
        void onSuccess();
    }

}
