package com.example.pahapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pahapp.databinding.FragmentDetailsBinding;
import com.example.pahapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private FragmentDetailsBinding binding;
    private BottomNavigationView botNavView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String time,avg_speed,duration,cals,distance;
    private Bitmap image;
    private float rating;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        botNavView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        botNavView = getActivity().findViewById(R.id.bottomNavigationView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        botNavView.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            binding.mapDetails.setImageBitmap(TrackingUtility.bytesToBitmap((byte[]) bundle.get("image")));
            binding.dateTimeDetails.setText(bundle.get("time").toString());
            binding.durationDetails.setText(bundle.get("duration").toString());
            binding.caloriesDetails.setText(bundle.get("calories").toString());
            binding.avgSpeedDetails.setText(bundle.get("avg_speed").toString());
            binding.distanceDetails.setText(bundle.get("distance").toString());
            binding.ratingDetails.setRating(bundle.getFloat("rating"));
        }
        return view;
    }


//        if(run!=null) {
//            binding.caloriesDetails.setText("Cal. Burned: " + run.mCaloriesBurned);
//            binding.avgSpeedDetails.setText(("Avg. Speed : " + run.mAvgSpeenInKMH));
//            binding.dateTimeDetails.setText("Date: " + TrackingUtility.getDate(run.mTimestamp,"dd/MM/yyyy hh:mm"));
//            binding.distanceDetails.setText("Distance : " + String.format("%.3f", (run.mDistanceInMeters * 0.001)) + " km");
//            binding.durationDetails.setText("Duration: " + TrackingUtility.getFormattedStopWatchTime(run.mTimeInMillis));
//            binding.mapDetails.setImageBitmap(TrackingUtility.bytesToBitmap(run.mImg));
//            binding.ratingDetails.setRating(run.mRating);
//        }

}