package com.example.runtime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdepter extends RecyclerView.Adapter<MessagesAdepter.MessagesViewHolder> {
    private ArrayList<User> friend = new ArrayList<>();
    private Context context;
    private DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    private MessagesLitener messagesLitener;

    interface MessagesLitener{
        void onClickedMessages(User user,View view);
    }

    public MessagesAdepter(ArrayList<User> friend, Context context) {
        this.friend = friend;
        this.context = context;
    }

    public void setMessagesLitener(MessagesLitener messagesLitener) {
        this.messagesLitener = messagesLitener;
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder{
    private CircleImageView image;
    private TextView textViewName;

    public MessagesViewHolder(@NonNull final View itemView) {
        super(itemView);
        this.image =itemView.findViewById(R.id.cardImage);
        this.textViewName = itemView.findViewById(R.id.cardName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messagesLitener!=null)
                    messagesLitener.onClickedMessages(friend.get(getAdapterPosition()),itemView);
            }
        });

    }
}

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_messages,parent,false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(view);
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        float density = context.getResources().getDisplayMetrics().density;
        User user=friend.get(position);
        Log.d("userrrrr", "aaa");
        StorageReference userImageRef = dataBaseClass.retrieveImageStorageReference(user.getUserId());
        Glide.with(this.context).load(userImageRef).override((int)(120*density),(int)(120*density)).into(holder.image);
        holder.textViewName.setText(user.getFullName());
    }

    @Override
    public int getItemCount() {
        return friend.size();
    }
}
