package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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
        ,SignUp3Fragment.OnSignUpLastListener, RegisterClass.SignUpStatusListener, DataBaseClass.OnUserPreferenceCreateListener {
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
    private boolean isFirstFragment=true;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase ;



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

       // registerClass.setSignInListener(this);
//        Button signUp=findViewById(R.id.sign_up);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //sign up / sign out
                }
                else{
                    //sign out
                }
            }
        };

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().add(R.id.rootLayout,new WelcomeFragment(),WELCOMEFRAGMENTTAG);
        fragmentTransaction.commit();

//        Button signIn=findViewById(R.id.sign_in);
//        Button signOut=findViewById(R.id.sign_out);
//
//        final EditText userNameEt=findViewById(R.id.full_name);
//        final EditText passwordEt=findViewById(R.id.password);
//        final EditText emailEt=findViewById(R.id.email);
//
//        final TextView userNameTv=findViewById(R.id.username);
//
//
//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userName=userNameEt.getText().toString();
//                final String password=passwordEt.getText().toString();
//                final String email=emailEt.getText().toString();
//                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(MainActivity.this,"sign up successful",Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                            Toast.makeText(MainActivity.this,"sign up field",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
//
//        signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String password=passwordEt.getText().toString();
//                String email=emailEt.getText().toString();
//
//                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(MainActivity.this,"sign in successful",Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                            Toast.makeText(MainActivity.this,"sign in field",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
//
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.signOut();
//
//            }
//        });
//
//        authStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                final FirebaseUser user=firebaseAuth.getCurrentUser();
//                if(user!=null){
//                    if(userName!=null){//sign up
//                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(userName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                userName=null;
//                                if(task.isSuccessful()){
//                                    Toast.makeText(MainActivity.this,user.getDisplayName()+"welcome",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                    }
//
//                    userNameTv.setText(user.getDisplayName()+" logged in");
//
//                    //read the user database.
//
//                }
//                else{
//
                }
/*
    @Override
    public void onClickNext1(String email,String password) {
            *//*firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp2Fragment(),SIGNUP2TAG).commit();
                    }
                    else Toast.makeText(MainActivity.this,"enter again",Toast.LENGTH_LONG).show();
                }
            });*//*
    }*/

    @Override
    public void onSignInClick() {

    }

    @Override
    public void onSignUpClick() {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp1Fragment(),SIGNUP1TAG).commit();
    }



    @Override
    public void onSignUpLast() {
        Toast.makeText(MainActivity.this,"sun shavit",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onSuccessSignUp(String userId) {
        fragmentManager.beginTransaction().replace(R.id.rootLayout,new SignUp2Fragment(),SIGNUP2TAG).commit();
        Toast.makeText(this, "welcome" + userId, Toast.LENGTH_SHORT).show();
    }

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
        //move to home fragment;
    }

    @Override
    public void onFailedPreferenceCreate() {

    }
}






