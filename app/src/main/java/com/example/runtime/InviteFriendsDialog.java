package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InviteFriendsDialog extends DialogFragment implements InviteFriendsDialogAdapter.InviteFriendToEventListener{

    private InviteFriendsDialogVM viewModel;
    private InviteFriendsDialogAdapter adapter;
    private ArrayList<User> myFriends = new ArrayList<>();
    private ArrayList<String> invitedFriendsIds = new ArrayList<>();

    public interface PassInvitedFriendsIdsToParentListener {
        void onFinishEditDialog(ArrayList<String> invitedFriendsIds);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(InviteFriendsDialogVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invite_friends_dialog, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Dialog_MinWidth);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button doneBtn = view.findViewById(R.id.doneBtnFriendDialog);
        Button cancelBtn = view.findViewById(R.id.cancelBtnFriendDialog);
        RecyclerView inviteFriendsRecycler = view.findViewById(R.id.myFriendsRecycler);
        adapter = new InviteFriendsDialogAdapter(myFriends,getContext());
        inviteFriendsRecycler.setAdapter(adapter);
        adapter.setInviteFriendToEventCallback(this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getContext());
        inviteFriendsRecycler.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(inviteFriendsRecycler.getContext(),
                ((LinearLayoutManager) manager).getOrientation());
        inviteFriendsRecycler.addItemDecoration(dividerItemDecoration);


        viewModel.getMyFriends().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                myFriends.clear();
                myFriends.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassInvitedFriendsIdsToParentListener listener = (PassInvitedFriendsIdsToParentListener) getTargetFragment();
                listener.onFinishEditDialog(invitedFriendsIds);
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public InviteFriendsDialog() {
    }

    @Override
    public void onInviteFriendRequests(String invitedFriendId) {
        invitedFriendsIds.add(invitedFriendId);
    }

    @Override
    public void onCancelInviteFriendRequests(String invitedFriendId) {
        invitedFriendsIds.remove(invitedFriendId);
    }

}
