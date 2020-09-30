package com.example.runtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runtime.model.Message;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesFragment2 extends Fragment {

    Messages2VM messagesVM;
    private Messages2Adapter adapter;
    BroadcastReceiver receiver;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        messagesVM = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(Messages2VM.class);
    }

    public static MessagesFragment2 newInstance(User user) {
        MessagesFragment2 messagesFragment2 = new MessagesFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("id", user.getUserId());
        bundle.putString("name", user.getFullName());
        bundle.putString("token",user.getUserToken());
        messagesFragment2.setArguments(bundle);
        return messagesFragment2;
    }

    //TODO check why we need getViewLifecycleOwner instead of this
    //TODO when to observe livedata
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.messages_fragment2, container, false);
        messagesVM.setActiveConversationFriendId(getArguments().getString("id"));
        messagesVM.getAllMessages();
        final CircleImageView circleImageView = root.findViewById(R.id.messagesImage);
        final TextView textViewName = root.findViewById(R.id.messagesTVName);
        final Button buttonSend = root.findViewById(R.id.sendBTN);
        final EditText editTextMessage = root.findViewById(R.id.messageET);
        final RecyclerView rvMessage = root.findViewById(R.id.messages);
        messagesVM.setName(getArguments().getString("name"));
        messagesVM.setImage(getArguments().getString("id"));

        messagesVM.getLiveDataFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textViewName.setText(s);
            }
        });

        messagesVM.getLiveDataImage().observe(getViewLifecycleOwner(), new Observer<StorageReference>() {
            @Override
            public void onChanged(StorageReference storageReference) {
                Glide.with(requireContext()).load(storageReference).into(circleImageView);
            }
        });

        adapter = new Messages2Adapter();
        rvMessage.setAdapter(adapter);

        messagesVM.getMessagesLiveData(getArguments().getString("id")).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.submitList(messages);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    messagesVM.sendMessage(editTextMessage.getText().toString(),getArguments().getString("token"),getArguments().getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                messagesVM.addToList(intent.getStringExtra("message"));
            }
        };
        IntentFilter filter = new IntentFilter("messagesReceiver");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
