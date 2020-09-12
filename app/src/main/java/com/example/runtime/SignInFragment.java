package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SignInFragment extends Fragment {
    SignUpVM viewModel;
    RegisterClass registerClass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sign_in,container ,false);
        final EditText emailEt=root.findViewById(R.id.emailEtSignIn);
        final EditText passwordEt=root.findViewById(R.id.passwordEtSignIn);

        Button signInBtn = root.findViewById(R.id.signIn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClass=RegisterClass.getInstance();
                final String email=emailEt.getText().toString();
                final String password=passwordEt.getText().toString();
                registerClass.signInUser(email,password);
            }
        });
        return root;
    }
}
