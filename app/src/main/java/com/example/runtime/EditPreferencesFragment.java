package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class EditPreferencesFragment extends Fragment {

    private EditPreferencesVM editPreferencesVM;
    private String gender;
    private String level;
    private int from;
    private int to;
    private ArrayList<Integer> fromAgesArray = new ArrayList<>();
    private ArrayList<Integer> toAgesArray = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        editPreferencesVM = new ViewModelProvider(getActivity()).get(EditPreferencesVM.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i;
        for (i = 0; i < 121 ; i++) {
            Integer age = i;
            fromAgesArray.add(age);
            toAgesArray.add(age);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.edit_preferences,container,false);
        //final Spinner spinnerFrom  = root.findViewById(R.id.fromAgeEditP);
        //final Spinner spinnerAgeTo = root.findViewById(R.id.toAgeEditP);
        final RadioButton radioButtonMale = root.findViewById(R.id.maleRBPartnerEditP);
        final RadioButton radioButtonFemale = root.findViewById(R.id.femaleRBPartnerEditP);
        final RadioButton radioButtonEasy = root.findViewById(R.id.easyRBPartnerEditP);
        final RadioButton radioButtonMedium = root.findViewById(R.id.mediumRBPartnerEditP);
        final RadioButton radioButtonExpert = root.findViewById(R.id.expertRBPartnerEditP);
        final RadioButton radioButtonBoth = root.findViewById(R.id.bothRBPartnerEditP);
        final RangeSlider slider =root.findViewById(R.id.slider_multiple_thumbs);

        editPreferencesVM.getUserPreferences();

        editPreferencesVM.getFromAge().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                from = integer;
                slider.setValues((float)from,(float)to);
            }
        });

        editPreferencesVM.getToAge().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                to = integer;
                slider.setValues((float)from,(float)to);
            }
        });

        editPreferencesVM.getPreferencesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserPreferences>() {
            @Override
            public void onChanged(UserPreferences userPreferences) {
                switch (userPreferences.getGender()){
                    case "male" :
                        radioButtonMale.setChecked(true);
                        break;
                    case "female" :
                        radioButtonFemale.setChecked(true);
                        break;
                    case "both" :
                        radioButtonBoth.setChecked(true);
                        break;
                }
                switch (userPreferences.getRuningLevel()){
                    case "easy" :
                        radioButtonEasy.setChecked(true);
                        break;
                    case "medium" :
                        radioButtonMedium.setChecked(true);
                        break;
                    case "expert" :
                        radioButtonExpert.setChecked(true);
                        break;
                }
            }
        });




        slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                float fromLocal = slider.getValues().get(0);
                float toLocal = slider.getValues().get(1);
                from = (int) fromLocal;
                to = (int) toLocal;
            }
        });


        ArrayAdapter<Integer> adapterFrom = new ArrayAdapter<>(getContext(), R.layout.spinner_item, fromAgesArray);
        //spinnerFrom.setAdapter(adapterFrom);
        //spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("spinner1", "position" +position +"");
//                Log.d("spinner1", " to " +to +"");
//                Log.d("spinner1",  "from" +from +"");
//                Log.d("spinner1", " size" + toAgesArray.size() +"");
//                if ((int)parent.getItemAtPosition(position) == 0){
//
//                }else{
//                    from = (int) parent.getItemAtPosition(position);
//                    toAgesArray.clear();
//                    int i;
//                    for (i = from; i < 121 ; i++) {
//                        Integer age = i;
//                        toAgesArray.add(age);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ArrayAdapter<Integer> adapterTo = new ArrayAdapter<>(getContext(), R.layout.spinner_item, toAgesArray);
//        spinnerAgeTo.setAdapter(adapterTo);
//        spinnerAgeTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("spinner", "position" +position +"");
//                Log.d("spinner", " to " +to +"");
//                Log.d("spinner",  "from" +from +"");
//                Log.d("spinner", " size" + toAgesArray.size() +"");
//                if((int)parent.getItemAtPosition(position) == 0){
//
//                }else{
//                    to = (int) parent.getItemAtPosition(position);
//                    fromAgesArray.clear();
//                    int i;
//                    Log.d("aaa",to+"");
//                    for (i = 1; i <= to ; i++) {
//                        Integer age = i;
//                        fromAgesArray.add(age);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        Button buttonDone = root.findViewById(R.id.signUpDoneEditP);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonEasy.isChecked())
                    level = "easy" ;
                else if(radioButtonMedium.isChecked())
                    level = "medium";
                else
                    level = "expert";
                if(radioButtonMale.isChecked())
                    gender = "male";
                else
                    gender = "female";

                UserPreferences userPreferences = new UserPreferences(from,to,gender,level);
                editPreferencesVM.updateLiveData(userPreferences);
                editPreferencesVM.savePreferences(userPreferences);

            }
        });

        return root;
    }
}
