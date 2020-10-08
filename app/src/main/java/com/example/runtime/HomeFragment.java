package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import androidx.lifecycle.Observer;



public class HomeFragment extends Fragment implements UserInstance.OnGetUserListener {

    private HomeVM viewModel;
    private UserInstance user;
    private TextView title;
    private TextView locationtext;
    private TextView areYouComing;
    private ProgressBar progressBar;
    private  Button buttonNewEvent;
    private Button findPeopleButton;
    private Button findEventsBtn;

    /*findPeopleListener findPeopleCallback;
    CreateNewEventListener createnewEventCallBack;
    findEventsListener findEventsCallback;
*/
   /* interface CreateNewEventListener{
        void onCreateNewEvent(boolean isNew);
    }

    interface findPeopleListener{
        void onFindPeopleClicked();
    }

    interface findEventsListener{
        void onFindEventsClicked();
    }*/

    /*public void setFindPeopleCallback(findPeopleListener findPeopleCallback) {
        this.findPeopleCallback = findPeopleCallback;
    }

    public void setFindEventsCallback(findEventsListener findEventsCallback) {
        this.findEventsCallback = findEventsCallback;
    }
*/
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(HomeVM.class);
        viewModel.setContext(getContext());
       /* try {
            createnewEventCallBack = (CreateNewEventListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement CreateNewEventListener");
        }*/
    }

    @Override
    public void onGetUser() {
        title.setText("hello"+ " " +user.getUser().getFullName()+"!");
        Log.d("sun",user.getUser().getLatitude()+"");

       // String city = viewModel.getAddress(getContext(),user.getUser().getLatitude(),user.getUser().getLongitude());
      //  locationtext.setText(city);


        //String city = viewModel.getAddress(getContext(),user.getUser().getLatitude(),user.getUser().getLongitude());
       // locationtext.setText(city);

        if(user.getUser().getLongitude()!=0||user.getUser().getLatitude()!=0)
            locationtext.setText(viewModel.getAddress(getContext(),user.getUser().getLatitude(),user.getUser().getLongitude()));
        progressBar.setVisibility(View.GONE);
        buttonNewEvent.setVisibility(View.VISIBLE);
        findEventsBtn.setVisibility(View.VISIBLE);
        findPeopleButton.setVisibility(View.VISIBLE);
        areYouComing.setVisibility(View.VISIBLE);
        locationtext.setVisibility(View.VISIBLE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate( R.layout.home_fragment,container,false);
        user = UserInstance.getInstance();
        user.getUserFromDataBase();
        user.setCallBackUserGet(this);
        title = root.findViewById(R.id.helloLabelMain);
        areYouComing = root.findViewById(R.id.areYouComingTV);
        progressBar = root.findViewById(R.id.homeFragmentProgressBar);
        buttonNewEvent = root.findViewById(R.id.createNewEventBT);
        findPeopleButton = root.findViewById(R.id.findPeopleBtn);
        findEventsBtn = root.findViewById(R.id.findUpcomingEventsBT);
        locationtext = root.findViewById(R.id.locationText);
        if(user.getUser().getFullName()!=null){
            title.setText("hello"+ " " +user.getUser().getFullName()+"!");
            progressBar.setVisibility(View.GONE);
            buttonNewEvent.setVisibility(View.VISIBLE);
            findEventsBtn.setVisibility(View.VISIBLE);
            findPeopleButton.setVisibility(View.VISIBLE);
            areYouComing.setVisibility(View.VISIBLE);
            locationtext.setVisibility(View.VISIBLE);

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

        viewModel.getLocation().observe(getViewLifecycleOwner(),observerLocation);




        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createnewEventCallBack.onCreateNewEvent(true);
                Fragment fragment = CreateEventFragment.getCreateEventFragment(true);
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        findPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findPeopleCallback.onFindPeopleClicked();
                Fragment fragment = new FindPeopleFragment();
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        findEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findEventsCallback.onFindEventsClicked();
                Fragment fragment = new FindEventsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return root;


    }
}
