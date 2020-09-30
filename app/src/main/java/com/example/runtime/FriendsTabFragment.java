package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class FriendsTabFragment extends Fragment implements FriendsTabAdapter.FriendTabListener, SwipeRefreshLayout.OnRefreshListener {

    private FriendsTabVM viewModel;
    private ArrayList<User> friends = new ArrayList<>();
    private FriendsTabAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(FriendsTabVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView friendTabRecycler = view.findViewById(R.id.friendsRecycler);
        adapter = new FriendsTabAdapter(friends, getContext());
        adapter.setFriendTabCallback(this);
        friendTabRecycler.setAdapter(adapter);
        friendTabRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext());
        friendTabRecycler.setLayoutManager(manager);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshFriendsTab);
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


        viewModel.getFriendsLiveData().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                Log.d("friendId", "num of users" + users.size());
                friends.clear();
                friends.addAll(users);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void onRemoveBtnClicked(String userId, String friendId) {
        viewModel.removeFriend(userId,friendId);
    }

    @Override
    public void onFriendCellClicked(String friendId) {
        FragmentManager fm = getFragmentManager();
        FriendDialog dialog = FriendDialog.newInstance(friendId);
        assert fm != null;
        dialog.show(fm, "friendDialog");
    }

    @Override
    public void onRefresh() {
        //swipeRefreshLayout.setRefreshing(true);
        viewModel.getFriendsIds();
    }
}
