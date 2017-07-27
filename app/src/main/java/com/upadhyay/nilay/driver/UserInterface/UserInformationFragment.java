package com.upadhyay.nilay.driver.UserInterface;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upadhyay.nilay.driver.R;
import com.upadhyay.nilay.driver.UserInterface.Communicator;
import com.upadhyay.nilay.driver.UserInterface.LoginFragment;
import com.upadhyay.nilay.driver.UserSession.User;
import com.upadhyay.nilay.driver.UserSession.UserID;

import io.realm.Realm;

/**
 * Created by nilay on 4/5/2017.
 */

public class UserInformationFragment extends Fragment {
    private Button mSendUserInfo;
    private EditText mUserName;
    private EditText mUserPassword,nameedit;
    ImageView background;
    Communicator comm;
    String regID,type,name;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView  textViewSignIn;
    private DatabaseReference mDatabase;
    public UserInformationFragment(){
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
//           comm.loginResponse("Login test","Login test");
            Log.e("Userinfo","OnCreate");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("Userinfo","OnActivitycreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.mi_logout);
        item.setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Userinfo","OnCreateView");
       // Log.e("userInfo","onCreateView");
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Driver App");
       // MenuItem item = menu.findItem(R.id.addAction);
       // Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar.setTitle("Driver App");
        mUserName = (EditText) view.findViewById(R.id.editUsername);
        mUserPassword =(EditText)view.findViewById(R.id.editPassword);
        nameedit =(EditText)view.findViewById(R.id.name);

       // mRadioTraderGroup = (RadioGroup) view.findViewById(R.id.radioTrader);
        mSendUserInfo = (Button) view.findViewById(R.id.buttonUserInfo);
        //textViewSignIn = (TextView)view.findViewById(R.id.editPassword);
        background = (ImageView)view.findViewById(R.id.iv_Userbackground);
        textViewSignIn = (TextView) view.findViewById(R.id.textViewSignIn);
        initBackground();
        //getInfo();
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"TextView Sign in", Toast.LENGTH_SHORT).show();
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,loginFragment).commit();
            }
        });
        comm = (Communicator) getActivity();
        mSendUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  int selectedId= mRadioTraderGroup.getCheckedRadioButtonId();
                Realm.init(getActivity().getApplicationContext());
                Realm mRealm = Realm.getDefaultInstance();
                mRealm.beginTransaction();
                UserID uid = mRealm.createObject(UserID.class);
                uid.setUserId(mUserName.getText().toString());
                SharedPreferences prefs = getActivity().getSharedPreferences("com.example.myapp", Context.MODE_PRIVATE);
                name = nameedit.getText().toString();
                mRealm.commitTransaction();
                mRealm.close();
               // textViewSignIn.setOnClickListener(this);
                //Log.e("UserInfoType",type);
                String email = mUserName.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();
                registerUser(email,password);
                //comm.respond(type,mUserName.getText().toString());
            }
        });
        return view;
    }

   public void registerUser(String email, String password) {

       //creating a new user
       progressDialog.setMessage("Registering Please Wait...");
       progressDialog.show();
       firebaseAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       //checking if success
                       if(task.isSuccessful()){
                           //Toast.makeText(getActivity(),"Successful",Toast.LENGTH_LONG).show();
                           //Toast.makeText(getActivity(),"User ID" + firebaseAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();
                           String email = firebaseAuth.getCurrentUser().getEmail();
                           User user = new User(email,name);
                           mDatabase.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                           comm.loginResponse(email,name);
                          // finish();
                           //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                       }else{
                           Toast.makeText(getActivity(),"Registration Error",Toast.LENGTH_LONG).show();
                           //display some message here
                          // Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                       }

                   }
               });
   }
    private void initBackground() {
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Userinfo","onPause");
    }

    @Override
    public void onStop() {
        Log.e("Userinfo","onStop");
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
