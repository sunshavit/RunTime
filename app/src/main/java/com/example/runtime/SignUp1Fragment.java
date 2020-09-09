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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUp1Fragment extends Fragment {

    interface OnNext1Listener{
        void onClickNext1(String email , String password);
    }
    SignUpVM viewModel;
    OnNext1Listener callBackMainActivity;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callBackMainActivity= (OnNext1Listener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnNextListener");
        }
        viewModel= new ViewModelProvider(getActivity()).get(SignUpVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sign_up1,container ,false);
        final EditText fullNameEt=root.findViewById(R.id.fullNameEt);
        final EditText passwordEt=root.findViewById(R.id.passwordEt);
        final EditText emailEt=root.findViewById(R.id.emailEt);
        final EditText passwordConfirmEt=root.findViewById(R.id.passwordConfirmEt);
        Button nextButton = root.findViewById(R.id.next1);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.setDataNext1(fullNameEt.getText().toString(),passwordEt.getText().toString(),passwordConfirmEt.getText().toString(),emailEt.getText().toString())){
                    callBackMainActivity.onClickNext1(emailEt.getText().toString(),passwordEt.getText().toString());
                }
                else {
                    Toast.makeText(getActivity(),"enter again",Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

}
