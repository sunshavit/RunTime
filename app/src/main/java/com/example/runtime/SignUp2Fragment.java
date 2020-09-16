package com.example.runtime;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SignUp2Fragment extends Fragment implements DataBaseClass.OnSaveImageListener{

    private SignUpVM viewModel;
    private String gender;
    private String level;
    int yearOfBirth;
    int monthOfBirth;
    int dayOfMonthOfBirth;
    final int PICK_IMAGE_REQUEST=1;
    private Uri filePath;
    private ImageView imageViewProfile;
    private DataBaseClass dataBaseClass;




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);
        dataBaseClass = DataBaseClass.getInstance();
        dataBaseClass.setCallBackImage(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == getActivity().RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            viewModel.saveProfileImage(filePath);
        }
       }

    @Override
    public void onSuccessImage() {
        Glide.with(getActivity()).load(filePath).into(imageViewProfile);
    }

    @Override
    public void onFailedImage() {
        Toast.makeText(getActivity(),"enter again",Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_up2,container,false);

        imageViewProfile = root.findViewById(R.id.profileImageIV);
        final EditText editTextDate = root.findViewById(R.id.dateET);
        final RadioGroup radioGroupGender = root.findViewById(R.id.genderGroup);
        RadioGroup radioGroupLevel = root.findViewById(R.id.levelGroup);
        Button buttonNext = root.findViewById(R.id.nextButton);

        final TextView ageView=root.findViewById(R.id.age);

       // final Calendar calendar = Calendar.getInstance();


        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });




        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year1=calendar.get(calendar.YEAR);
                int month1=calendar.get(calendar.MONTH);
                int dayOfMonth1=calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        yearOfBirth=year;
                        monthOfBirth=month+1;
                        dayOfMonthOfBirth=dayOfMonth;
                        editTextDate.setText(dayOfMonthOfBirth+"/"+monthOfBirth+'/'+yearOfBirth);

                    }
                },year1,month1,dayOfMonth1);
                datePickerDialog.show();




         /*  DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        localDate = LocalDate.of(year, month, dayOfMonth);
                        editTextDate.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();*/
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
                viewModel.setDataNext2(filePath,yearOfBirth,monthOfBirth,dayOfMonthOfBirth,gender,level);
            }
        });

        return root;
    }





}
