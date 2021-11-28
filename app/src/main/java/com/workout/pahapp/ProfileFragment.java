package com.workout.pahapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                if (weight!= "" && height != "" && name!="" && age!="" && sex[0]!="") {
                    User user = new User(name, Integer.parseInt(height), Float.parseFloat(weight), age,sex[0]);
                    mUserViewModel.insertUser(user);
                    Snackbar mySnackbar = Snackbar.make(view, "Done!", 25);
                    mySnackbar.show();
                }else {
                    Snackbar mySnackbar = Snackbar.make(view, "Please enter all fields", 4);
                    mySnackbar.show();
                }
            }
        });
        return view;
    }
}