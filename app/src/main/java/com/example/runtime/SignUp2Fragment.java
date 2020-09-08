package com.example.runtime;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class SignUp2Fragment extends Fragment {

    private SignUpVM viewModel;
    private OnNext2Listener callBack;
    private String gender;
    private String level;
    private LocalDate localDate;

    interface OnNext2Listener{
        void onClickNext2();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callBack = (OnNext2Listener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnNextListener");
        }
        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_up2,container,false);

        ImageView imageViewProfile = root.findViewById(R.id.profileImageIV);
        final EditText editTextDate = root.findViewById(R.id.dateET);
        final RadioGroup radioGroupGender = root.findViewById(R.id.genderGroup);
        RadioGroup radioGroupLevel = root.findViewById(R.id.levelGroup);
        Button buttonNext = root.findViewById(R.id.nextButton);

        final Calendar calendar = Calendar.getInstance();




        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        localDate = LocalDate.of(year, month, dayOfMonth);
                        editTextDate.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.maleRB: {
                      gender="male" ;
                      break;
                    }
                    case R.id.femaleRB: {
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
                    case R.id.easyRB:{
                        level = "easy";
                        break;
                    }
                    case R.id.mediumRB:{
                        level = "medium";
                        break;
                    }
                    case R.id.expertRB:{
                        level = "expert";
                        break;
                    }
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                viewModel.setDataNext2("imagePath",localDate,gender,level);
                callBack.onClickNext2();
            }
        });





        return root;
    }
}
