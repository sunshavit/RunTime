package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements WelcomeFragment.OnRegisterClick, DataBaseClass.OnUserCreateListener
        ,SignUp3Fragment.OnSignUpLastListener, RegisterClass.SignUpStatusListener, DataBaseClass.OnUserPreferenceCreateListener,
        RegisterClass.SignInStatusListener,DataBaseClass.OnUserListsListener{
    // where to do the user authentication
    // local time and local date require sdk 26
//    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
//    FirebaseAuth.AuthStateListener authStateListener;
//    String userName;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    final String WELCOMEFRAGMENTTAG="welcomefregmenttag";
    final String SIGNUP1TAG="signup1tag";
    final String SIGNUP2TAG="signup2tag";
    final String SIGNUP3TAG="signup3tag";
    final String HOME_TAG="homeTag";
    private boolean isFirstFragment=true;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase ;
    private boolean isPreferencesCreated;
    private boolean isUserListsCreated;




    // private FirebaseStorage firebaseStorage;
    RegisterClass registerClass;
    DataBaseClass dataBaseClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerClass = RegisterClass.getInstance();
        dataBaseClass = DataBaseClass.getInstance();
        registerClass.setSignUpListener(this);
        dataBaseClass.setCallBackCreate(this);
        dataBaseClass.setCallBackPreferenceCreate(this);
        registerClass.setSignInListener(this);
        dataBaseClass.setCallBackUserLists(this);

        //registerClass.stateListener();



        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null) { //sign up or sign in
                    getLocationUpdates();
                    Toast.makeText(MainActivity.this,user.getUid(),Toast.LENGTH_LONG).show();
                }
                else { //sign out

                }

            }
        };

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().add(R.id.rootLayout,new WelcomeFragment(),WELCOMEFRAGMENTTAG);
        fragmentTransaction.commit();


    }

    @Override
    public void onSignInClick() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignInFragment(),SIGNUP1TAG).commit();
    }

    @Override
    public void onSignUpClick() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp1Fragment(),SIGNUP1TAG).commit();
    }



    @Override
    public void onSignUpLast() {
        Toast.makeText(MainActivity.this,"sign up successful",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerClass.addStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
       registerClass.removeStateListener(authStateListener);
    }

    @Override
    public void onSuccessSignUp(String userId) {
        fragmentManager.beginTransaction().replace(R.id.rootLayout, new SignUp2Fragment(), SIGNUP2TAG).commit();
        Toast.makeText(this, "welcome" + userId, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessUserLists() {
        isUserListsCreated=true;
        moveToHomeFragment();
    }

    @Override
    public void onFailedUserLists() {
        isUserListsCreated=false;
        moveToHomeFragment();

    }

    //    private void getLocationUpdates() {
//        CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
//            @Override
//            public void onChanged(@Nullable Location location) {
//                if (location != null) {
//                    Log.d(MainActivity.class.getSimpleName(),
//                            "Location Changed " + location.getLatitude() + " : " + location.getLongitude());
//                    Toast.makeText(MainActivity.this, "Location Changed", Toast.LENGTH_SHORT).show();
//                    builder.append(location.getLatitude()).append(" : ").append(location.getLongitude()).append("\n");
//                    textView.setText(builder.toString());
//                }
//            }
//        });
//    }

    @Override
    public void onFailedSignUp(String problem) {
        Toast.makeText(this, problem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessCreate() {

        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp3Fragment(),SIGNUP3TAG).commit();
    }

    @Override
    public void onFailedCreate(String problem) {
        Toast.makeText(this, problem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessPreferenceCreate() {
        Toast.makeText(MainActivity.this,"hi",Toast.LENGTH_LONG).show();
        isPreferencesCreated=true;
        //getLocationUpdates();
        //fragmentManager.beginTransaction().replace(R.id.rootLayout,new HomeFragment(),HOME_TAG).commit();
    }

    @Override
    public void onFailedPreferenceCreate() {
        isPreferencesCreated=false;

    }

    @Override
    public void onSuccessSignIn(String userId) {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new CreateEventFragment(),HOME_TAG).commit();
        Toast.makeText(MainActivity.this,"sign up successful",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedSignIn(String problem) {
        Toast.makeText(MainActivity.this,problem,Toast.LENGTH_LONG).show();
    }

    public void getLocationUpdates(){
        Toast.makeText(MainActivity.this,"fun",Toast.LENGTH_LONG).show();
        CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if(location!=null){
                    Toast.makeText(MainActivity.this,location.toString(),Toast.LENGTH_LONG).show();
                    double longitude=location.getLongitude();
                    double latitude=location.getLatitude();
                    dataBaseClass.updateLocation(longitude,latitude);

                }
                else
                    Toast.makeText(MainActivity.this,"no location",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void moveToHomeFragment(){
        if (isPreferencesCreated && isUserListsCreated){
            fragmentManager.beginTransaction().replace(R.id.rootLayout,new HomeFragment(),HOME_TAG).commit();
        }
        else
            Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
    }


}






