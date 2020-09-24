package com.example.runtime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventFragment extends Fragment {

    //GoogleMap map;
    private CreateEventVM viewModel;
    private String eventId;
    private int eventYear;
    private int eventMonth;
    private int eventDayOfMonth;
    private int eventHourOfDay;
    private int eventMinute;
    private long longitude;
    private long latitude;
    private String manager;
    private String runningLevel;
    private ArrayList<String> runners;
    EditText locationEt;

    interface OnMapListener{
        void onMapOkClick();
    }

    private OnMapListener mapCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mapCallback = (CreateEventFragment.OnMapListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnMapListener");
        }

        viewModel= new ViewModelProvider(getActivity()).get(CreateEventVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.create_event_fragment,container,false);

        final EditText dateEt=root.findViewById(R.id.eventDateEt);
        final EditText timeEt=root.findViewById(R.id.eventTimeEt);
        locationEt=root.findViewById(R.id.eventLocationEt);
        Button InviteFriendsBtn=root.findViewById(R.id.inviteFriendsBtn);
        RadioGroup eventLevelRG=root.findViewById(R.id.eventLevelRG);
        Button doneBtn=root.findViewById(R.id.doneBtn);

        dateEt.setOnClickListener(new View.OnClickListener() {
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

                        eventYear=year;
                        eventMonth=month+1;
                        eventDayOfMonth=dayOfMonth;
                        dateEt.setText(eventDayOfMonth+"/"+eventMonth+'/'+eventYear);

                    }
                },year1,month1,dayOfMonth1);
                datePickerDialog.show();

            }
        });

        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar=Calendar.getInstance();
                int currentHourOfDay=calendar.get(calendar.HOUR_OF_DAY);
                int currentMinute=calendar.get(calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String chosenHour;
                        String chosenMinute;

                        if(hourOfDay < 10)
                            chosenHour="0"+hourOfDay;
                        else
                            chosenHour=hourOfDay+"";

                        if(minute < 10)
                            chosenMinute="0"+minute;
                        else
                            chosenMinute=minute+"";

                        eventHourOfDay=hourOfDay;
                        eventMinute=minute;
                        timeEt.setText(chosenHour+":"+chosenMinute);
                    }
                }, currentHourOfDay,currentMinute,true);
                timePickerDialog.show();
            }
        });

        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapCallback.onMapOkClick();
                //Intent intent = new Intent(getActivity(), MapActivity.class);
                //startActivity(intent);

            }
        });

        eventLevelRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.easyRBPartner:{
                        runningLevel = "easy";
                        break;
                    }
                    case R.id.mediumRBPartner:{
                        runningLevel = "medium";
                        break;
                    }
                    case R.id.expertRBPartner:{
                        runningLevel = "expert";
                        break;
                    }
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setEventData(eventYear,eventMonth,eventDayOfMonth,eventHourOfDay,
                        eventMinute,manager,runningLevel);
            }
        });

        viewModel.getStreetAddress().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                locationEt.setText(s);
            }
        });

        return root;
    }

    public void updateLocationEt(String streetAddress){
        locationEt.setText(streetAddress);
        Toast.makeText(getContext(),streetAddress+"updateLocationFun",Toast.LENGTH_LONG).show();

    }



}
