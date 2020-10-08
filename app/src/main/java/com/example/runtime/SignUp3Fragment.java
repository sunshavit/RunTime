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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i;
        for (i = 0; i < 121 ; i++) {
            Integer age = i;
           fromAgesArray.add(age);
           toAgesArray.add(age);
        }
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


        ArrayAdapter<Integer> adapterFrom = new ArrayAdapter<>(getContext(), R.layout.spinner_item, fromAgesArray);
        Spinner spinnerFrom = root.findViewById(R.id.spinnerFrom);
        spinnerFrom.setAdapter(adapterFrom);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if ((int)parent.getItemAtPosition(position) == 0){

                }else{
                    ageFrom = (int) parent.getItemAtPosition(position);
                    viewModel.setStartAge(ageFrom);

                    toAgesArray.clear();
                    int i;
                    for (i = ageFrom; i < 121 ; i++) {
                        Integer age = i;
                        toAgesArray.add(age);
                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<Integer> adapterTo = new ArrayAdapter<>(getContext(), R.layout.spinner_item, toAgesArray);
        final Spinner spinnerTo = root.findViewById(R.id.spinnerTo);
        spinnerTo.setAdapter(adapterTo);
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if((int)parent.getItemAtPosition(position) == 0){

                }else{
                    ageTo = (int) parent.getItemAtPosition(position);
                    viewModel.setEndAge(ageTo);
                    fromAgesArray.clear();
                    int i;
                    for (i = 1; i < ageTo ; i++) {
                        Integer age = i;
                        fromAgesArray.add(age);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
