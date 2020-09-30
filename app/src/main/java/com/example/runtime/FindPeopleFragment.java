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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class FindPeopleFragment extends Fragment implements FindPeopleAdapter.AddFriendBtnListener, FindPeopleAdapter.StrangerClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FindPeopleVM viewModel;
    private ArrayList<User> relevantUsers = new ArrayList<>();
    private FindPeopleAdapter adapter;
    private ArrayList<String> recentSentRequests = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;



    interface OnStrangerCellClickListener{
       void onStrangerCellClicked(String strangerId, boolean isRequested);
    }

    OnStrangerCellClickListener strangerCellCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //viewModel= new ViewModelProvider(getActivity()).get(FindPeopleVM.class);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(FindPeopleVM.class);

        try {
            strangerCellCallback = (OnStrangerCellClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnStrangerCellClickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.find_people_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.findPeopleRecycler);
        TextView locationTV = root.findViewById(R.id.findPeopleLocationTV);//geocode address


        Log.d("tag2", relevantUsers.size()+"");

        adapter = new FindPeopleAdapter(relevantUsers, getContext(), recentSentRequests);
        recyclerView.setAdapter(adapter);
        adapter.setAddFriendCallback(this);
        adapter.setStrangerClickCallback(this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshFindPeople);
        swipeRefreshLayout.setOnRefreshListener(this);


        viewModel.getSwipeLayoutBool().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        viewModel.getRelevantUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {

                relevantUsers.clear();
                relevantUsers.addAll(users);
               // Log.d("tag2", "inside observe " + users.get(0).getFullName());
                Log.d("tag2", relevantUsers.size()+"");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getSentRequests();
        viewModel.getRecentSentRequests().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {

                recentSentRequests.clear();
                recentSentRequests.addAll(strings);
                adapter.notifyDataSetChanged();
                // Log.d("tag2", "inside observe " + users.get(0).getFullName());
                Log.d("tag2", "inside observer");
                Log.d("tag2", recentSentRequests.size()+"");

            }
        });
    }

    @Override
    public void onSendFriendRequest(String strangerId) {
       viewModel.onSendFriendRequest(strangerId);
    }

    @Override
    public void onCancelFriendRequest(String strangerId) {
        viewModel.onCancelFriendRequest(strangerId);
    }

    @Override
    public void onStrangerClicked(String strangerId, boolean isRequested) {
        strangerCellCallback.onStrangerCellClicked(strangerId, isRequested);
    }

    @Override
    public void onRefresh() {
       //swipeRefreshLayout.setRefreshing(true);
       viewModel.retrieveUsersList();
       viewModel.getSentRequests();
    }
}
