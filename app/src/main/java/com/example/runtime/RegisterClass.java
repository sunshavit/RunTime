package com.example.runtime;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterClass {


    interface SignUpStatusListener{
       void onSuccessSignUp(String userId);
        void onFailedSignUp(String problem);
    }

    interface SignInStatusListener{
        void onSuccessSignIn(String userId);
        void onFailedSignIn();
    }

    interface SignOutListener{
        void onSignOut();
    }

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private static RegisterClass registerClass = null;

    private SignUpStatusListener signUpCallback;
    private SignInStatusListener signInCallback;
    private SignOutListener signOutCallback;
    private FirebaseAuth.AuthStateListener authStateListener;

    public void setSignUpListener (SignUpStatusListener callback){
        signUpCallback = callback;
    }

    public void setSignInListener (SignInStatusListener callback){
        signInCallback = callback;
    }

    public void setSignOutListener (SignOutListener callback){
        signOutCallback = callback;
    }
    //2 methodes to connect and remove


    public static RegisterClass getInstance(){
        if(registerClass == null ){
            registerClass = new RegisterClass();
        }
        return registerClass;
    };

    private RegisterClass() {
    }

    public void signUpUser(String email , String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //tell listener success or fail
                if(task.isSuccessful()){
                    FirebaseUser user;
                    user = firebaseAuth.getCurrentUser();
                    signUpCallback.onSuccessSignUp(user.getUid());
                } else{
                    userRegisterFailed(task);
                }
            }
        });

    }

    public void signInUser(String email , String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user;
                    user = firebaseAuth.getCurrentUser();
                    signInCallback.onSuccessSignIn(user.getUid());
                }else{
                    signInCallback.onFailedSignIn();
                }
            }
        });

    }

    public void signOut(){
        firebaseAuth.signOut();
        signOutCallback.onSignOut();
    }

    private void userRegisterFailed(Task<AuthResult> task){
        try {
            throw task.getException();
        } catch(FirebaseAuthWeakPasswordException e) {
            signUpCallback.onFailedSignUp("Password must be at least 8 letters");
        } catch(FirebaseAuthInvalidCredentialsException e) {
            signUpCallback.onFailedSignUp("Invalid Credentials");
        } catch(FirebaseAuthUserCollisionException e) {
            signUpCallback.onFailedSignUp("User already exists");
        } catch(Exception e) {
            signUpCallback.onFailedSignUp("Error");
        }

    }

    public String getUserId(){
        return firebaseAuth.getCurrentUser().getUid();

    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }


}
