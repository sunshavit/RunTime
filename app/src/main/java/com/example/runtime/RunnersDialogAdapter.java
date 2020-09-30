package com.example.runtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RunnersDialogAdapter extends RecyclerView.Adapter<RunnersDialogAdapter.RunnersViewHolder> {

    private ArrayList<User> runners = new ArrayList<>();
    DataBaseClass dataBaseClass = DataBaseClass.getInstance();
    Context context;

    public RunnersDialogAdapter(ArrayList<User> runners, Context context) {
        this.runners = runners;
        this.context = context;
    }

    @NonNull
    @Override
    public RunnersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.runner_cell, parent, false);
        return new RunnersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunnersViewHolder holder, int position) {
        StorageReference userImageRef = dataBaseClass.retrieveImageStorageReference(runners.get(position).getUserId());
        Glide.with(context).load(userImageRef).placeholder(R.drawable.ic_launcher_background).into(holder.runnerImageView);
        holder.runnerName.setText(runners.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return runners.size();
    }

    class RunnersViewHolder extends RecyclerView.ViewHolder{

        CircleImageView runnerImageView;
        TextView runnerName;

        public RunnersViewHolder(@NonNull View itemView) {
            super(itemView);
            runnerImageView = itemView.findViewById(R.id.runnerImageView);
            runnerName = itemView.findViewById(R.id.runnerName);

        }
    }
}
