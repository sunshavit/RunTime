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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

public class SignUp1Fragment extends Fragment {

    /*interface OnNext1Listener{
        void onClickNext1(String email , String password);
    }*/
    SignUpVM viewModel;
    final int LOCATION_PERMISSION_REQUEST=0;
    TextInputEditText fullNameEt;
    TextInputEditText passwordEt;
    TextInputEditText emailEt;
    TextInputEditText passwordConfirmEt;

    String fullName;
    String email;
    String password;
    String passwordConfirm;

    //OnNext1Listener callBackMainActivity;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*try {
            callBackMainActivity= (OnNext1Listener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnNextListener");
        }*/
        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sign_up1,container ,false);
        fullNameEt=root.findViewById(R.id.fullNameEt);
        passwordEt=root.findViewById(R.id.passwordEt);
        emailEt=root.findViewById(R.id.emailEt);
        passwordConfirmEt=root.findViewById(R.id.passwordConfirmEt);
        Button nextButton = root.findViewById(R.id.next1);

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
*/


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullName=fullNameEt.getText().toString();
                password=passwordEt.getText().toString();
                passwordConfirm=passwordConfirmEt.getText().toString();
                email=emailEt.getText().toString();

                if(Build.VERSION.SDK_INT>=23){
                    Log.d("tag","over 23");
                    int hasLocationPermission= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasLocationPermission1= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION);
                    if(hasLocationPermission!=PackageManager.PERMISSION_GRANTED || hasLocationPermission1!=PackageManager.PERMISSION_GRANTED){
                        Log.d("tag","no granted");
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST);
                    }
                    else {
                        if(!viewModel.setDataNext1(fullName,password,passwordConfirm,email)){
                            Log.d("tag","granted");
                            Toast.makeText(getActivity(),"passwords not identical",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Log.d("tag","less 23");
                    if(!viewModel.setDataNext1(fullName,password,passwordConfirm,email)){
                        Toast.makeText(getActivity(),"passwords not identical",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==LOCATION_PERMISSION_REQUEST){
            Log.d("tag","requset code");
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Log.d("tag","result");
                if(!viewModel.setDataNext1(fullName,password,passwordConfirm,email)){
                    Log.d("tag",fullName);
                    Toast.makeText(getActivity(),"passwords not identical",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
