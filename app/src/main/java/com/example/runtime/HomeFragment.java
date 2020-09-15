package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.runtime.R;
import com.example.runtime.SignUpVM;

public class HomeFragment extends Fragment implements UserInstance.OnGetUserListener {

    private HomeVM viewModel;
    private UserInstance user;
    private TextView title;
    private DataBaseClass dataBaseClass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(HomeVM.class);
    }

    @Override
    public void onGetUser() {
        title.setText("hello"+ " " +user.getUser().getFullName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate( R.layout.home_fragment,container,false);
        user = UserInstance.getInstance();
        user.setCallBackUserGet(this);
        title = root.findViewById(R.id.helloLabelMain);

        ToggleButton activeBtn = root.findViewById(R.id.active_btn);
        activeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    viewModel.updateActive(isChecked);

            }
        });
        return root;





    }
}
