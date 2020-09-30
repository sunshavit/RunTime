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

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    private MessagesVM messagesVM;
    private ArrayList<User> friends = new ArrayList<>();
    private MessagesAdepter adapter;

    interface OnClickOnMessages{
        void onClickMessages(User user);
    }
    private OnClickOnMessages onClickOnMessages;

    @Override
    public void onAttach(@NonNull Context context) {
        messagesVM = new ViewModelProvider(getActivity()).get(MessagesVM.class);
        try {
            onClickOnMessages = (MessagesFragment.OnClickOnMessages) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnClickMessages");
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.messages_fragment,container,false);

        RecyclerView recyclerView = root.findViewById(R.id.recycleMessages);
        adapter = new MessagesAdepter(friends,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messagesVM.getFriends().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friends.clear();
                friends.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setMessagesLitener(new MessagesAdepter.MessagesLitener() {
            @Override
            public void onClickedMessages(User user, View view) {
                onClickOnMessages.onClickMessages(user);
            }
        });

        return root;
    }
}
