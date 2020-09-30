package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;

public class FriendsRequestsTabFragment extends Fragment implements FriendsRequestsTabAdapter.OnRequestResponse, SwipeRefreshLayout.OnRefreshListener{

    private FriendsRequestsTabVM viewModel;
    private ArrayList<User> friendRequests = new ArrayList<>();
    private FriendsRequestsTabAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //viewModel= new ViewModelProvider(getActivity()).get(FriendsRequestsTabVM.class);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(FriendsRequestsTabVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_requests_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView friendsRequestsRecycler = view.findViewById(R.id.friendsRequestsRecycler);
        friendsRequestsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext());
        friendsRequestsRecycler.setLayoutManager(manager);
        adapter = new FriendsRequestsTabAdapter(friendRequests, this.getContext());
        adapter.setRequestCallback(this);
        friendsRequestsRecycler.setAdapter(adapter);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshFriendsRequestsTab);
        swipeRefreshLayout.setOnRefreshListener(this);

        /*swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

            }
        });*/

        viewModel.getSwipeLayoutBool().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swipeRefreshLayout.setRefreshing(true);
            }
        });


        viewModel.getFriendsRequestsLiveData().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendRequests.clear();
                friendRequests.addAll(users);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onRequestAccepted(String userId, String newFriendId, String fullName, String token) throws JSONException {
        viewModel.onRequestAccepted(userId, newFriendId, fullName, token);
    }

    @Override
    public void onRequestRemoved(String userId, String strangerId) {
        viewModel.onRequestRemoved(userId, strangerId);
    }

    @Override
    public void onRefresh() {
        //swipeRefreshLayout.setRefreshing(true);
        viewModel.getFriendsRequestsIds();
    }
}
