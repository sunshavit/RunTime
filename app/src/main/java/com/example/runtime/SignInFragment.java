package com.example.runtime;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SignInFragment extends Fragment {
    SignUpVM viewModel;
    RegisterClass registerClass;
    final int LOCATION_PERMISSION_REQUEST=0;
    String email;
    String password;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sign_in,container ,false);
        final EditText emailEt=root.findViewById(R.id.emailEtSignIn);
        final EditText passwordEt=root.findViewById(R.id.passwordEtSignIn);

        Button signInBtn = root.findViewById(R.id.signIn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClass=RegisterClass.getInstance();
                email=emailEt.getText().toString();
                password=passwordEt.getText().toString();

                if(Build.VERSION.SDK_INT>=23){
                    Log.d("tag","over 23");
                    int hasLocationPermission= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasLocationPermission1= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION);
                    if(hasLocationPermission!= PackageManager.PERMISSION_GRANTED || hasLocationPermission1!=PackageManager.PERMISSION_GRANTED){
                        Log.d("tag","no granted");
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST);
                    }
                    else {
                        registerClass.signInUser(email,password);
                    }
                }
                else{
                    Log.d("tag","less 23");
                    registerClass.signInUser(email,password);
                }


            }
        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==LOCATION_PERMISSION_REQUEST){
            Log.d("tag","requset code");
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Log.d("tag","result");
                registerClass.signInUser(email,password);
            }
        }
    }
}
