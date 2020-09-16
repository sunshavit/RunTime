package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileVM profileVM;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        profileVM= new ViewModelProvider(getActivity()).get(ProfileVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container,false);
        final CircleImageView circleImageView = root.findViewById(R.id.profile_image);
        profileVM.getImageFromData();
        final Context context = getActivity();
        Observer<String> resultObserverImage = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(context).load(s).into(circleImageView);
            }
        };

        profileVM.getImageLivedata().observe(this , resultObserverImage);

        final TextView textViewLocation = root.findViewById(R.id.locationProfileTV);
        final TextView textViewAge = root.findViewById(R.id.ageProfileTV);
        final TextView textViewEmail = root.findViewById(R.id.emailProfileTV);
        final TextView textViewName = root.findViewById(R.id.fullNameProfileTV);
        final TextView textViewLevel = root.findViewById(R.id.levelProfileTV);
        Observer<User> observerUser = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                textViewAge.setText(user.getDayOfMonth()+"/"+user.getMonth()+"/"+user.getYear());
                textViewLevel.setText(user.getRunningLevel());
                textViewLocation.setText(profileVM.getAddress(context,user.getLatitude(),user.getLongitude()));
                textViewName.setText(user.getFullName());
            }
        };

        profileVM.getLiveDataUser().observe(this,observerUser);





        return root;
    }
}
