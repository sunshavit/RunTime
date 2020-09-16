package com.example.runtime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Calendar;

public class CreateEventFragment extends Fragment {

    GoogleMap map;
    private CreateEventVM viewModel;
    private String eventId;
    private int eventYear;
    private int eventMonth;
    private int eventDayOfMonth;
    private int eventHourOfDay;
    private int eventMinute;
    //private Location location;
    //private String manager;
    //private String runningLevel;
    //private ArrayList<String> runners;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel= new ViewModelProvider(getActivity()).get(CreateEventVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.create_event_fragment,container,false);

        final EditText setDateEt=root.findViewById(R.id.eventDateEt);
        final EditText setTimeEt=root.findViewById(R.id.eventTimeEt);
        EditText setLocationEt=root.findViewById(R.id.eventLocationEt);
        Button InviteFriendsBtn=root.findViewById(R.id.inviteFriendsBtn);
        RadioGroup eventLevelRG=root.findViewById(R.id.eventLevelRG);

        setDateEt.setOnClickListener(new View.OnClickListener() {
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
                        setDateEt.setText(eventDayOfMonth+"/"+eventMonth+'/'+eventYear);

                    }
                },year1,month1,dayOfMonth1);
                datePickerDialog.show();

            }
        });

        setTimeEt.setOnClickListener(new View.OnClickListener() {
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
                        setTimeEt.setText(chosenHour+":"+chosenMinute);
                    }
                }, currentHourOfDay,currentMinute,true);
                timePickerDialog.show();
            }
        });

        setLocationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapFragment.class);
                startActivity(intent);
            }
        });




        return root;
    }
}
