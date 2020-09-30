package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendDialog extends DialogFragment {

    private FriendDialogVM viewModel;
    private ArrayList<User> mutualFriends = new ArrayList<>();
    private FriendDialogAdapter adapter;

    public FriendDialog() {
    }

    public static FriendDialog newInstance(String friendId) {
        FriendDialog dialog = new FriendDialog();
        Bundle args = new Bundle();
        args.putString("friendId", friendId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        String friendId;
        assert getArguments() != null;
        friendId = getArguments().getString("friendId");

        viewModel = new FriendModelFactory(friendId).create(FriendDialogVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_dialog, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Dialog_MinWidth);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView friendName = view.findViewById(R.id.friendDialogNameTV);
        final TextView friendGender = view.findViewById(R.id.friendDialogGenderTV);
        final TextView friendRunningLevel = view.findViewById(R.id.friendDialogRunningLevel);
        final CircleImageView friendImageView = view.findViewById(R.id.friendDialogImageView);
        RecyclerView friendDialogRecycler = view.findViewById(R.id.friendDialogRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext());
        friendDialogRecycler.setLayoutManager(manager);
        friendDialogRecycler.setHasFixedSize(true);
        adapter = new FriendDialogAdapter(mutualFriends, this.getContext());
        friendDialogRecycler.setAdapter(adapter);


        viewModel.getNameLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                friendName.setText(s);
            }
        });

        viewModel.getGenderLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("female")){
                    friendGender.setText(R.string.female);
                }else if (s.equals("male")) {
                    friendGender.setText(R.string.male);
                }

            }
        });

        viewModel.getRunningLevelLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                friendRunningLevel.setText(s);
            }
        });

        viewModel.getImageRefLiveData().observe(this, new Observer<StorageReference>() {
            @Override
            public void onChanged(StorageReference storageReference) {
                Glide.with(Objects.requireNonNull(getContext())).load(storageReference).into(friendImageView);
            }
        });

        viewModel.getMutualFriendsLiveData().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                mutualFriends.clear();
                mutualFriends.addAll(users);
                adapter.notifyDataSetChanged();
                //notifyAdapter
            }
        });

    }


}
