package com.example.pahapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsAnimation;
import android.widget.Button;

import com.example.pahapp.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private FragmentHomeBinding binding;
    private View view;
    private LocationCallback locationCallback;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean permissionDenied = false;
    private boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private final LatLng defaultLocation = new LatLng(50.6745, -120.3273);
    private static final int DEFAULT_ZOOM = 15;
    ArrayList<ArrayList<LatLng>> pathPoints = new ArrayList<ArrayList<LatLng>>();
    private Long curTimeMillis;
    private Boolean isTracking = false;
    private RunViewModel mRunViewModel;
    private Float weight = 80f;
    TrackingService service = new TrackingService();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().findFragmentById(R.id.map).onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        requestPermissions();
        binding.startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRun();
                Log.d(String.valueOf(1), "Sosamba");
            }
        });
        binding.finishRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToSeeWholeTrack();
                endRun();
                unsubscribeFromObservers(service);
                pathPoints.clear();
            }
        });
        mapFragment.getMapAsync(this);

        subscribeToObservers(service);


    }
    private void unsubscribeFromObservers(TrackingService service){
        service.isTracking.removeObservers(getViewLifecycleOwner());
        service.pathPoints.removeObservers(getViewLifecycleOwner());
        service.timeRunInMillis.removeObservers(getViewLifecycleOwner());
    }
    private void subscribeToObservers(TrackingService service){ ;
        service.isTracking.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateTracking(aBoolean);

            }
        });
        service.pathPoints.observeForever(new Observer<ArrayList<ArrayList<LatLng>>>() {
            @Override
            public void onChanged(ArrayList<ArrayList<LatLng>> arrayLists) {
                pathPoints = arrayLists;
                addLatestPolyline();
                moveCameraToUser();
                Log.d(String.valueOf(2), "pathpoints observer" );
                addAllPolylines();
            }
        });
        service.timeRunInMillis.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                curTimeMillis = aLong;
                String formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeMillis);
                binding.duration.setText(formattedTime);
            }
        });

    }
    private void startRun(){
        if(isTracking){
            Log.d(String.valueOf(2), "Visibility" );
            binding.finishRun.setVisibility(Button.VISIBLE);
            sendCommand("ACTION_PAUSE_SERVICE");
        }else {
            sendCommand("ACTION_START_OR_RESUME_SERVICE");
            Log.d(String.valueOf(2), "Zalupility" );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
           mMap = googleMap;
           addAllPolylines();
    }
    private void updateTracking(Boolean isTracking){
        this.isTracking = isTracking;
        if(isTracking){
            binding.startRun.setText("Stop");
            binding.finishRun.setVisibility(View.VISIBLE);
            Log.d(String.valueOf(99), "isTrah ");
        }else{
            binding.startRun.setText("Start");
            binding.finishRun.setVisibility(View.GONE);
        }
    }
    private void addLatestPolyline(){
        int lastIndex = pathPoints.size() - 1;
        if(!pathPoints.isEmpty() && pathPoints.get(lastIndex).size() > 1){
            LatLng preLastLatLng = pathPoints.get(lastIndex).get(pathPoints.get(lastIndex).size() - 2);
            ArrayList<LatLng> arLastLatLng = pathPoints.get(lastIndex);
            Log.d(String.valueOf(99), "pathPoints are" + pathPoints);
            LatLng lastLatLng = arLastLatLng.get(arLastLatLng.size() - 1);
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(8f);
            polylineOptions.add(preLastLatLng);
            polylineOptions.add(lastLatLng);
            mMap.addPolyline(polylineOptions);
        }
    }
    private void addAllPolylines(){
        for (ArrayList<LatLng> polyline: pathPoints) {
            PolylineOptions polylineOptions = new PolylineOptions().color(Color.RED).width(8f).addAll(polyline);
            Log.d(String.valueOf(99), "addAllPolylines" + polylineOptions.getPoints().toString());
            mMap.addPolyline(polylineOptions);
            Log.d(String.valueOf(5), "map" + mMap.toString());
        }
    }
    private void endRun(){
        int distanceInMeters = 0;
        for (ArrayList<LatLng> polyline: pathPoints) {
            distanceInMeters += TrackingUtility.calculatePolylineLength(polyline);
        }
        float avgSpeed = Math.round((distanceInMeters / 1000f) / (curTimeMillis / 1000f / 60 / 60) * 10) / 10f;
        long dateTimeStamp = Calendar.getInstance().getTimeInMillis();
        float caloriesBurned = (distanceInMeters / 1000f) * weight;
        Run run = new Run(dateTimeStamp, avgSpeed, distanceInMeters, curTimeMillis, (int) caloriesBurned);
        mRunViewModel.insertRun(run);
        sendCommand("ACTION_STOP_SERVICE");
    }
    private void sendCommand(String action){
        Intent intent = new Intent(requireContext(),TrackingService.class);
        intent.setAction(action);
        requireContext().startService(intent);
    }
    private void moveCameraToUser(){
        int lastIndex = pathPoints.size() - 1;
        if(!pathPoints.isEmpty() && !pathPoints.get(lastIndex).isEmpty()){
            ArrayList<LatLng> last = pathPoints.get(lastIndex);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(last.get(last.size() -1), 15f));
        }
    }
    private void zoomToSeeWholeTrack(){
        LatLngBounds.Builder bounds = LatLngBounds.builder();
        for (ArrayList<LatLng> polyline: pathPoints) {
            for (LatLng pos: polyline) {
                bounds.include(pos);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), binding.map.getWidth(), binding.map.getHeight(), (int) (binding.map.getHeight() * 0.05f)));
    }
    private void requestPermissions(){
        if(TrackingUtility.hasLocationPermissions(requireContext())){
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this, "In order to use this app location permissions should be accepted",0,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
        }else{
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    0,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder dialog = new AppSettingsDialog.Builder(this);
            dialog.build().show();
        } else {
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}