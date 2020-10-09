package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class SignUp3Fragment extends Fragment {

    private SignUpVM viewModel;
    private int ageTo = 120;
    private int ageFrom = 0;
    private String gender;
    private String level;
    private ArrayList<Integer> fromAgesArray = new ArrayList<>();
    private ArrayList<Integer> toAgesArray = new ArrayList<>();

    interface OnSignUpLastListener{
        void onSignUpLast();
    }

    private OnSignUpLastListener callBack;

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
        //final EditText editTextFrom = root.findViewById(R.id.fromAge);
        //final EditText editTextTo = root.findViewById(R.id.toAge);
        viewModel.setProgressBar2LiveData(false);
        final RadioGroup radioGroupGender = root.findViewById(R.id.genderGroupPartner);
        RadioGroup radioGroupLevel = root.findViewById(R.id.levelGroupPartner);
        Button buttonSignUp = root.findViewById(R.id.signUpDone);
        RangeSlider slider = root.findViewById(R.id.slider_multiple_thumbs_signUp3);

        final ProgressBar progressBar = root.findViewById(R.id.signUp3_progressBar);
        viewModel.getProgressBar3LiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    progressBar.setVisibility(View.VISIBLE);
                } else{
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


        slider.setValues(0f,120f);


        slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                float fromLocal = slider.getValues().get(0);
                float toLocal = slider.getValues().get(1);
                ageFrom = (int) fromLocal;
                ageTo = (int) toLocal;
                viewModel.setEndAge(ageTo);
                viewModel.setStartAge(ageFrom);
            }
        });

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.maleRBPartner: {
                        gender="male" ;
                        viewModel.setPartnerGender(gender);
                        break;
                    }
                    case R.id.femaleRBPartner: {
                        gender="female" ;
                        viewModel.setPartnerGender(gender);
                        break;
                    }
                    case R.id.bothRBPartner:{
                        gender = "both";
                        viewModel.setPartnerGender(gender);
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
                        viewModel.setPartnerLevel(level);
                        break;
                    }
                    case R.id.mediumRBPartner:{
                        level = "medium";
                        viewModel.setPartnerLevel(level);
                        break;
                    }
                    case R.id.expertRBPartner:{
                        level = "expert";
                        viewModel.setPartnerLevel(level);
                        break;
                    }
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ageFrom =Integer.parseInt(editTextFrom.getText().toString());
                //ageTo =Integer.parseInt(editTextTo.getText().toString());

                if (viewModel.getEndAge() == 0 || viewModel.getStartAge() == 0 ||
                viewModel.getPartnerGender() == null || viewModel.getPartnerLevel() == null){
                    Toast.makeText(getActivity(),R.string.fill_all_details,Toast.LENGTH_LONG).show();
                }else {
                    viewModel.setDataNext3(ageFrom,ageTo,gender,level);

                    callBack.onSignUpLast();
                }


            }
        });
        return root;
    }

}
