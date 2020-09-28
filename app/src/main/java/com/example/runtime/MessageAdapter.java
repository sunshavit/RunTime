package com.example.runtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runtime.model.Message;
import com.example.runtime.model.ModelWithID;

//TODO choose message
//TODO forward
//TODO delete message
public class MessageAdapter extends ListAdapter<ModelWithID<Message>, MessageAdapter.Message2ViewHolder> {
    public MessageAdapter() {
        super(MessageAdapter.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public Message2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new Message2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Message2ViewHolder holder, int position) {
        Message msg = getItem(position).getModel();
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

    private static final DiffUtil.ItemCallback<ModelWithID<Message>> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ModelWithID<Message>>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull ModelWithID<Message> oldMsg, @NonNull ModelWithID<Message> newMsg) {
                    return oldMsg.getId().equals(newMsg.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull ModelWithID<Message> oldMsg, @NonNull ModelWithID<Message> newMsg) {
                    return oldMsg.equals(newMsg);
                }
            };
}

