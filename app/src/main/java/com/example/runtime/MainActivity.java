package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button signUp=findViewById(R.id.sign_up);
        Button signIn=findViewById(R.id.sign_in);
        Button signOut=findViewById(R.id.sign_out);

        final EditText userNameEt=findViewById(R.id.full_name);
        final EditText passwordEt=findViewById(R.id.password);
        final EditText emailEt=findViewById(R.id.email);

        final TextView userNameTv=findViewById(R.id.username);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=userNameEt.getText().toString();
                final String password=passwordEt.getText().toString();
                final String email=emailEt.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"sign up successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this,"sign up field",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=passwordEt.getText().toString();
                String email=emailEt.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"sign in successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this,"sign in field",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

            }
        });

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    if(userName!=null){//sign up
                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(userName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userName=null;
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,user.getDisplayName()+"welcome",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    userNameTv.setText(user.getDisplayName()+"logged in");

                    //read the user database.

                }
                else{

                }
            }
        };




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
}

