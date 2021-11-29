package com.workout.pahapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.workout.pahapp.databinding.FragmentProfileBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel mUserViewModel;


    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        binding.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.userName.getText().toString();
                String height = binding.height.getText().toString();
                String weight = binding.weight.getText().toString();
                String age = binding.age.getText().toString();

                final String[] sex = new String[1];
                binding.male.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sex[0] = "Male";
                    }
                });
                binding.female.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sex[0] = "Female";
                    }
                });
                if (weight!= null && height != null && name!=null && age!=null && sex[0]!=null) {
                    User user = new User(name, Integer.parseInt(height), Float.parseFloat(weight), age,sex[0]);
                    mUserViewModel.insertUser(user);
                    Snackbar mySnackbar = Snackbar.make(view, "Done!", 3000);
                    mySnackbar.show();
                }else {
                    Snackbar mySnackbar = Snackbar.make(view, "Please enter all fields", 3000);
                    mySnackbar.show();
                }
            }
        });
        return view;
    }
}