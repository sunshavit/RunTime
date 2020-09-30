package com.example.runtime;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runtime.model.Message;

//TODO choose message
//TODO forward
//TODO delete message
public class Messages2Adapter extends ListAdapter<Message, Messages2Adapter.Message2ViewHolder> {
    public Messages2Adapter() {
        super(Messages2Adapter.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public Message2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new Message2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Message2ViewHolder holder, int position) {
        Message msg = getItem(position);
        if(msg.getUserIdSent().equals(UserInstance.getInstance().getUser().getUserId())) {
            holder.message.setBackgroundColor(Color.LTGRAY);

        }
        holder.message.setText(msg.getContent());
        holder.time.setText(msg.getTime());
    }

    class Message2ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;

        Message2ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageItemTV);
            time = itemView.findViewById(R.id.messageTimeTV);
        }
    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Message>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Message oldMsg, @NonNull Message newMsg) {
                    return oldMsg.equals(newMsg);
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Message oldMsg, @NonNull Message newMsg) {
                    return oldMsg.equals(newMsg);
                }
            };
}

