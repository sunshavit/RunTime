package com.example.runtime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventFragment extends Fragment implements InviteFriendsDialog.PassInvitedFriendsIdsToParentListener{

    private CreateEventVM viewModel;
    private int eventYear;
    private int eventMonth;
    private int eventDayOfMonth;
    private int eventHourOfDay;
    private int eventMinute;
    private String runningLevel;
    private String eventStatus;
    private TextView locationEt;
    private TextView dateEt;
    private static Bundle bundle;
    private String eventDate;
    private String eventTime;

    private InviteFriendsDialog inviteFriendsDialog;
    private ArrayList<String> invitedFriendsIds;

    interface OnMapListener{
        void onMapOkClick();
    }

    interface OnBackFromCreateEventListener{
        void toHomeFromCreateEvent();
    }

    private OnMapListener mapCallback;
    private OnBackFromCreateEventListener backFromCreateEventCallback;

    private static CreateEventFragment createEventFragment = null;


    public static CreateEventFragment getCreateEventFragment(boolean isNew){
        bundle = new Bundle();
        if(createEventFragment == null || isNew ){
            bundle.putBoolean("isNew", true);
            createEventFragment = new CreateEventFragment();
        }
        else
        {
            bundle.putBoolean("isNew", false);
        }

        createEventFragment.setArguments(bundle);
        return createEventFragment;
    };

    public CreateEventFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mapCallback = (CreateEventFragment.OnMapListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnMapListener");
        }

        try {
            backFromCreateEventCallback = (CreateEventFragment.OnBackFromCreateEventListener)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnBackFromCreateEventListener");
        }


        //viewModel= new ViewModelProvider(getActivity()).get(CreateEventVM.class);
        //viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(CreateEventVM.class);
        viewModel = new ViewModelProvider(getActivity()).get(CreateEventVM.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        dateEt.setText(viewModel.getEventDate());

       // viewModel= new ViewModelProvider(getActivity()).get(CreateEventVM.class);
        //viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(CreateEventVM.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.create_event_fragment,container,false);

        dateEt=root.findViewById(R.id.eventDateEt);
        final TextView timeEt=root.findViewById(R.id.eventTimeEt);
        locationEt=root.findViewById(R.id.eventLocationEt);
        Button inviteFriendsBtn=root.findViewById(R.id.inviteFriendsBtn);
        final ImageView easyImageIv = root.findViewById(R.id.image_easy);
        final ImageView mediumImageIv = root.findViewById(R.id.image_medium);
        final ImageView hardImageIv = root.findViewById(R.id.image_hard);
        final TextView easyTv = root.findViewById(R.id.easy_tv);
        final TextView mediumTv = root.findViewById(R.id.medium_tv);
        final TextView hardTv = root.findViewById(R.id.hard_tv);
        final RadioGroup eventStatusRg = root.findViewById(R.id.event_status);
        RadioButton publicRb = root.findViewById(R.id.public_event);
        RadioButton privateRb = root.findViewById(R.id.private_event);

        Button doneBtn=root.findViewById(R.id.doneBtn);
/*
        Bundle bundle = this.getArguments();
        String streetAddress = bundle.getString("streetAddress");
        final double longitude = bundle.getDouble("longitude");
        final double latitude = bundle.getDouble("latitude");
        locationEt.setText(streetAddress);*/

   Toast.makeText(getContext(),String.valueOf(getArguments().getBoolean("isNew")),Toast.LENGTH_LONG).show();
        if(getArguments()!=null){
            if(getArguments().getBoolean("isNew")){
                viewModel.setEventDate("");
                viewModel.setEventTime("");
                viewModel.setStreetAddress("");
                viewModel.setEasyImageView(R.drawable.easy_gray);
                viewModel.setMediumImageView(R.drawable.medium_gray);
                viewModel.setHardImageView(R.drawable.hard_gray);
                viewModel.setEasyTextView(Color.parseColor("#808080"));
                viewModel.setMediumTextView(Color.parseColor("#808080"));
                viewModel.setHardTextView(Color.parseColor("#808080"));
                viewModel.setPublicChecked(false);
                viewModel.setPrivateChecked(false);
                bundle.putBoolean("isNew", false);
            }
        }

        dateEt.setText(viewModel.getEventDate());
        timeEt.setText(viewModel.getEventTime());
        easyTv.setTextColor(viewModel.getEasyTextView());
        mediumTv.setTextColor(viewModel.getMediumTextView());
        hardTv.setTextColor(viewModel.getHardTextView());
        publicRb.setChecked(viewModel.isPublicChecked());
        privateRb.setChecked(viewModel.isPrivateChecked());

        if(viewModel.getIsFirstLaunch()){
            easyImageIv.setImageResource(R.drawable.easy_gray);
            mediumImageIv.setImageResource(R.drawable.medium_gray);
            hardImageIv.setImageResource(R.drawable.hard_gray);
        }
        else {
            easyImageIv.setImageResource(viewModel.getEasyImageView());
            mediumImageIv.setImageResource(viewModel.getMediumImageView());
            hardImageIv.setImageResource(viewModel.getHardImageView());
        }

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

                        String eventMonth1;
                        String eventDayOfMonth1;

                        if(eventMonth < 10)
                           eventMonth1 = "0"+eventMonth;
                        else
                            eventMonth1=eventMonth+"";

                        if(eventDayOfMonth < 10)
                            eventDayOfMonth1 = "0"+eventDayOfMonth;
                        else
                            eventDayOfMonth1 = eventDayOfMonth+"";

                        eventDate=eventDayOfMonth1+"/"+eventMonth1+'/'+eventYear;
                        dateEt.setText(eventDate);
                        viewModel.setEventDate(eventDate);

                    }
                },year1,month1,dayOfMonth1);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
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

                        eventTime = chosenHour+":"+chosenMinute;
                        timeEt.setText(eventTime);
                        viewModel.setEventTime(eventTime);
                    }
                }, currentHourOfDay,currentMinute,true);
                timePickerDialog.show();
            }
        });

        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapCallback.onMapOkClick();

            }
        });

        easyImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setIsFirstLaunch(false);
                easyImageIv.setImageResource(R.drawable.easy);
                viewModel.setEasyImageView(R.drawable.easy);
                easyTv.setTextColor(Color.parseColor("#056378"));
                viewModel.setEasyTextView(Color.parseColor("#056378"));
                mediumImageIv.setImageResource(R.drawable.medium_gray);
                viewModel.setMediumImageView(R.drawable.medium_gray);
                mediumTv.setTextColor(Color.parseColor("#808080"));
                viewModel.setMediumTextView(Color.parseColor("#808080"));
                hardImageIv.setImageResource(R.drawable.hard_gray);
                viewModel.setHardImageView(R.drawable.hard_gray);
                hardTv.setTextColor(Color.parseColor("#808080"));
                viewModel.setHardTextView(Color.parseColor("#808080"));
                runningLevel = "easy";

            }
        });

        mediumImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setIsFirstLaunch(false);
                easyImageIv.setImageResource(R.drawable.easy_gray);
                viewModel.setEasyImageView(R.drawable.easy_gray);
                easyTv.setTextColor(Color.parseColor("#808080"));
                viewModel.setEasyTextView(Color.parseColor("#808080"));
                mediumImageIv.setImageResource(R.drawable.medium);
                viewModel.setMediumImageView(R.drawable.medium);
                mediumTv.setTextColor(Color.parseColor("#056378"));
                viewModel.setMediumTextView(Color.parseColor("#056378"));
                hardImageIv.setImageResource(R.drawable.hard_gray);
                viewModel.setHardImageView(R.drawable.hard_gray);
                hardTv.setTextColor(Color.parseColor("#808080"));
                viewModel.setHardTextView(Color.parseColor("#808080"));
                runningLevel = "medium";

            }
        });

        hardImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setIsFirstLaunch(false);
                easyImageIv.setImageResource(R.drawable.easy_gray);
                viewModel.setEasyImageView(R.drawable.easy_gray);
                easyTv.setTextColor(Color.parseColor("#808080"));
                viewModel.setEasyTextView(Color.parseColor("#808080"));
                mediumImageIv.setImageResource(R.drawable.medium_gray);
                viewModel.setMediumImageView(R.drawable.medium_gray);
                mediumTv.setTextColor(Color.parseColor("#808080"));
                viewModel.setMediumTextView(Color.parseColor("#808080"));
                hardImageIv.setImageResource(R.drawable.hard);
                viewModel.setHardImageView(R.drawable.hard);
                hardTv.setTextColor(Color.parseColor("#056378"));
                viewModel.setHardTextView(Color.parseColor("#056378"));
                runningLevel = "expert";
            }
        });

     eventStatusRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(RadioGroup group, int checkedId) {
             switch (checkedId){
                 case R.id.public_event:{
                     eventStatus = "publicEvent";
                     viewModel.setPublicChecked(true);
                     break;
                 }
                 case R.id.private_event:{
                     eventStatus = "privateEvent";
                     viewModel.setPrivateChecked(true);
                     break;
                 }
             }
         }
     });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     /*           Log.d("details",dateEt.getText().toString()+""+ timeEt.getText().toString()+""+
                        locationEt.getText()+""+runningLevel+""+eventStatus);*/
                if(dateEt.getText().equals("") || timeEt.getText().equals("") ||
                       locationEt.getText().equals("")|| runningLevel==null || eventStatus==null ){
                    Snackbar.make(getView(),R.string.all_fields, Snackbar.LENGTH_LONG).show();

                }
                else{
                    viewModel.setEventData(eventYear,eventMonth,eventDayOfMonth,eventHourOfDay,
                            eventMinute,runningLevel,eventStatus,invitedFriendsIds);

                    Snackbar.make(getView(),R.string.new_event_snack_bar, Snackbar.LENGTH_LONG).show();
                    backFromCreateEventCallback.toHomeFromCreateEvent();

                }

            }
        });

        viewModel.getStreetAddress().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                locationEt.setText(s);

            }
        });

        inviteFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                InviteFriendsDialog dialog = new InviteFriendsDialog();
                dialog.setCancelable(true);
                dialog.setTargetFragment(CreateEventFragment.this,300);
                assert fm != null;
                dialog.show(fm,"inviteFriendsFragment");
            }
        });


        return root;
        }

    @Override
    public void onFinishEditDialog(ArrayList<String> invitedFriendsIds) {
        this.invitedFriendsIds = invitedFriendsIds;
    }



}


