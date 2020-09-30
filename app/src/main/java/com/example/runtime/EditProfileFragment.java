package com.example.runtime;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private String gender;
    private String level;
    private String fullName;
    private int yearOfBirth;
    private int monthOfBirth;
    private int dayOfMonthOfBirth;
    private UserInstance userInstance = UserInstance.getInstance();
    private final int PICK_IMAGE_REQUEST=1;
    private Uri filePath;
    private CircleImageView imageViewProfile;
    private EditProfileVM editProfileVM;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == getActivity().RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            Glide.with(getActivity()).load(filePath).into(imageViewProfile);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        editProfileVM = new ViewModelProvider(getActivity()).get(EditProfileVM.class);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.edit_profile_fragment,container,false);

        final Context context=getContext();
        imageViewProfile = root.findViewById(R.id.imageEditProfile);
        editProfileVM.getImageFromData();

        Observer<String> resultObserverImage = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(context).load(s).into(imageViewProfile);
                Log.d("sun","aaaaaaaaaaaaaaaaa");
            }
        };


        editProfileVM.getImageLivedata().observe(this , resultObserverImage);

        final EditText editTextFullName = root.findViewById(R.id.fullNameEditProfileEt);

        gender = userInstance.getUser().getGender();
        dayOfMonthOfBirth = userInstance.getUser().getDayOfMonth();
        monthOfBirth = userInstance.getUser().getMonth();
        yearOfBirth = userInstance.getUser().getYear();
        level = userInstance.getUser().getRunningLevel();
        editTextFullName.setText(userInstance.getUser().getFullName());


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

        RadioGroup radioGroupGender = root.findViewById(R.id.genderGroupEditProfile);
        RadioGroup radioGroupLevel = root.findViewById(R.id.levelGroupEditProfile);

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


        final EditText editTextDate = root.findViewById(R.id.dateEditProfileET);

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
            }
        });



        Button buttonDone= root.findViewById(R.id.saveEditProfileButton);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = editTextFullName.getText().toString();
                userInstance.getUser().setFullName(fullName);
                userInstance.getUser().setGender(gender);
                userInstance.getUser().setRunningLevel(level);
                userInstance.getUser().setYear(yearOfBirth);
                userInstance.getUser().setMonth(monthOfBirth);
                userInstance.getUser().setDayOfMonth(dayOfMonthOfBirth);
                editProfileVM.saveUserEdit();
                if(filePath!=null)
                    editProfileVM.saveUserImageEdit(filePath);
            }
        });

        return root;
    }
}
