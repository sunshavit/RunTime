package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FindPeopleFragment extends Fragment {

    private FindPeopleVM viewModel;
    private ArrayList<User> relevantUsers = new ArrayList<>();
    private FindPeopleAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(FindPeopleVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.find_people_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.findPeopleRecycler);
        //TextView locationTV = root.findViewById(R.id.findPeopleLocationTV);//geocode address
        adapter = new FindPeopleAdapter(relevantUsers, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
        viewModel.retrieveUsersList();

        viewModel.getRelevantUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {

                relevantUsers.addAll(users);
                Log.d("tag", "inside observe" + users.get(0).getFullName());
                adapter.notifyDataSetChanged();

            }
        });


        return root;
    }
}
