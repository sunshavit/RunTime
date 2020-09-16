package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.runtime.R;
import com.example.runtime.SignUpVM;

public class HomeFragment extends Fragment {

    HomeVM viewModel;
    findPeopleListener findPeopleCallback;

    interface findPeopleListener{
        void onFindPeopleClicked();
    }

    public void setFindPeopleCallback(findPeopleListener findPeopleCallback) {
        this.findPeopleCallback = findPeopleCallback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel= new ViewModelProvider(getActivity()).get(HomeVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate( R.layout.home_fragment,container,false);

        ToggleButton activeBtn = root.findViewById(R.id.active_btn);
        activeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    viewModel.updateActive(isChecked);

            }
        });


        //temporary button

        Button findPeopleButton = root.findViewById(R.id.findPeopleBtn);
        findPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPeopleCallback.onFindPeopleClicked();
            }
        });
        return root;

    }
}
