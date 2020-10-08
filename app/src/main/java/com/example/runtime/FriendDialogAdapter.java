package com.example.runtime;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendDialogAdapter extends RecyclerView.Adapter<FriendDialogAdapter.MutualFriendsViewHolder>  {

    private ArrayList<User> mutualFriends = new ArrayList<>();
    DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    Context context;

    public FriendDialogAdapter(ArrayList<User> mutualFriends, Context context) {
        this.mutualFriends = mutualFriends;
        this.context = context;
    }

    @NonNull
    @Override
    public MutualFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mutual_friend_cell, parent, false);
        return new MutualFriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MutualFriendsViewHolder holder, int position) {
        /*StorageReference userImageRef = dataBaseClass.retrieveImageStorageReference(mutualFriends.get(position).getUserId());
        Glide.with(context).load(userImageRef).placeholder(R.drawable.ic_launcher_background).into(holder.mutualFriendImageView);*/
        OnSuccessListener<Uri> listener = new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(R.drawable.placeholder_small).into(holder.mutualFriendImageView);
            }
        };

        dataBaseClass.getImageUserId(mutualFriends.get(position).getUserId(), listener);
        holder.mutualFriendName.setText(mutualFriends.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return mutualFriends.size();
    }

    class MutualFriendsViewHolder extends RecyclerView.ViewHolder{

        CircleImageView mutualFriendImageView;
        TextView mutualFriendName;

        public MutualFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mutualFriendImageView = itemView.findViewById(R.id.mutualFriendImageView);
            mutualFriendName = itemView.findViewById(R.id.mutualFriendName);

        }
    }
}
