package com.example.runtime;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindEventsFragment extends Fragment implements FindEventsAdapter.OnJoinEventListener {

    private FindEventsAdapter adapter;
    private ArrayList<Event> relevantEvents = new ArrayList<>();
    private ArrayList<String> myEvents = new ArrayList<>();
    private FindEventsVM viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(FindEventsVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.find_events_fragment,container,false);

        RecyclerView recyclerView = root.findViewById(R.id.findEventsRecycler);
        adapter = new FindEventsAdapter(relevantEvents,getContext(),myEvents);
        recyclerView.setAdapter(adapter);
        adapter.setJoinEventCallback(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        viewModel.getRelevantEvents().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                relevantEvents.clear();
                relevantEvents.addAll(events);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getMyEvents().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> usersEventsIds) {
                myEvents.clear();
                myEvents.addAll(usersEventsIds);
                adapter.notifyDataSetChanged();
            }
        });

        TextView locationTv = root.findViewById(R.id.locationTVRecycler);
        UserInstance userInstance = UserInstance.getInstance();
        double longitude = userInstance.getUser().getLongitude();
        double latitude = userInstance.getUser().getLatitude();
        String streetAddress= geocodeFunction(longitude,latitude);
        locationTv.setText(streetAddress);


        return root;
    }

    public String geocodeFunction(double longitude, double latitude) {
        Geocoder geocoder=new Geocoder(getContext());
        String streetAddress;
        try {
            List<Address>addresses = geocoder.getFromLocation(latitude,longitude,1);
            if(addresses.size()>0){
                Address address=addresses.get(0);
                streetAddress=address.getAddressLine(0);
                return streetAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";


    }

    @Override
    public void onJoinEvent(String eventId, String userId) {
        viewModel.onJoinEvent(eventId,userId);
    }

    @Override
    public void onCancelJoinEvent(String eventId, String userId) {
        viewModel.onCancelJoinEvent(eventId,userId);
    }
}
