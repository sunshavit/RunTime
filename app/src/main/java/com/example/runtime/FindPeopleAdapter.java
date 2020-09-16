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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindPeopleAdapter extends RecyclerView.Adapter<FindPeopleAdapter.UserViewHolder> {

    ArrayList<User> users;
    Context context;
    DataBaseClass dataBaseClass = DataBaseClass.getInstance();

    public FindPeopleAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_people_cell, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        Log.d("tag", "inside adapter" + user.getFullName());
        holder.userNameTV.setText(user.getFullName());
        //glide and get from storage
        StorageReference userImageRef = dataBaseClass.retrieveImageStorageReference(user.getUserId());
        Glide.with(context).load(userImageRef).placeholder(R.drawable.ic_launcher_background).into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userImageView;
        TextView userNameTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userImageView = itemView.findViewById(R.id.userImageRecyclerCell);
            userNameTV = itemView.findViewById(R.id.userNameTVRecyclerCell);

        }
    }
}
