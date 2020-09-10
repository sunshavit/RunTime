package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

public class SignUp3Fragment extends Fragment {

    private SignUpVM viewModel;
    private int ageTo;
    private int ageFrom;
    private String gender;
    private String level;

    interface OnSignUpLastListener{
        void onSignUpLast();
    }

    OnSignUpLastListener callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callBack = (OnSignUpLastListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnNextListener");
        }
        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.sign_up3,container,false);
        final EditText editTextFrom = root.findViewById(R.id.fromAge);
        final EditText editTextTo = root.findViewById(R.id.toAge);
        final RadioGroup radioGroupGender = root.findViewById(R.id.genderGroupPartner);
        RadioGroup radioGroupLevel = root.findViewById(R.id.levelGroupPartner);
        Button buttonSignUp = root.findViewById(R.id.signUpDone);

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.maleRBPartner: {
                        gender="male" ;
                        break;
                    }
                    case R.id.femaleRBPartner: {
                        gender="female" ;
                        break;
                    }
                }
            }
        });

        radioGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.easyRBPartner:{
                        level = "easy";
                        break;
                    }
                    case R.id.mediumRBPartner:{
                        level = "medium";
                        break;
                    }
                    case R.id.expertRBPartner:{
                        level = "expert";
                        break;
                    }
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageFrom =Integer.parseInt(editTextFrom.getText().toString());
                ageTo =Integer.parseInt(editTextTo.getText().toString());
                viewModel.setDataNext3(ageFrom,ageTo,gender,level);

                callBack.onSignUpLast();

            }
        });



        return root;
    }
}
