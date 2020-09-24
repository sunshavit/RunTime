package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import androidx.lifecycle.Observer;



public class HomeFragment extends Fragment implements UserInstance.OnGetUserListener {

    private HomeVM viewModel;
    private UserInstance user;
    private TextView title;
    private TextView locationtext;

    findPeopleListener findPeopleCallback;
    CreateNewEventListener createnewEventCallBack;

    interface CreateNewEventListener{
        void onCreateNewEvent();
    }

    interface findPeopleListener{
        void onFindPeopleClicked();
    }

    public void setFindPeopleCallback(findPeopleListener findPeopleCallback) {
        this.findPeopleCallback = findPeopleCallback;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(HomeVM.class);
        viewModel.setContext(getContext());
        try {
            createnewEventCallBack = (CreateNewEventListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement CreateNewEventListener");
        }
    }

    @Override
    public void onGetUser() {
        title.setText("hello"+ " " +user.getUser().getFullName());
        Log.d("sun",user.getUser().getLatitude()+"");

       // String city = viewModel.getAddress(getContext(),user.getUser().getLatitude(),user.getUser().getLongitude());
      //  locationtext.setText(city);


        //String city = viewModel.getAddress(getContext(),user.getUser().getLatitude(),user.getUser().getLongitude());
       // locationtext.setText(city);

        if(user.getUser().getLongitude()!=0||user.getUser().getLatitude()!=0)
            locationtext.setText(viewModel.getAddress(getContext(),user.getUser().getLatitude(),user.getUser().getLongitude()));


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate( R.layout.home_fragment,container,false);
        user = UserInstance.getInstance();
        user.getUserFromDataBase();
        user.setCallBackUserGet(this);
        title = root.findViewById(R.id.helloLabelMain);
        locationtext = root.findViewById(R.id.locationText);
        if(user.getUser().getFullName()!=null){
            title.setText("hello"+ " " +user.getUser().getFullName());

            //Log.d("home", ""+ user.getUser().getFullName());
            //Log.d("home", ""+ user.getUser().getLatitude());
           // String city = viewModel.getAddress(getActivity(),user.getUser().getLatitude(),user.getUser().getLongitude());

           // locationtext.setText(city);

        }


        Observer<String> observerLocation = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                locationtext.setText(s);
            }
        };

        viewModel.getLocation().observe(this,observerLocation);


        ToggleButton activeBtn = root.findViewById(R.id.active_btn);
        activeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    viewModel.updateActive(isChecked);

            }
        });

        Button buttonNewEvent = root.findViewById(R.id.createNewEventBT);

        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createnewEventCallBack.onCreateNewEvent();
            }
        });

        Button findPeopleButton = root.findViewById(R.id.findPeopleBtn);
        findPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPeopleCallback.onFindPeopleClicked();
            }
        });
        return root;


    }
}
