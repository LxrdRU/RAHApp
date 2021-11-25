package com.example.pahapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executor;

public class TrackingService extends LifecycleService {
    static TrackingService instance;
    private MutableLiveData<Long> timeRunInSeconds = new MutableLiveData<Long>();
    private Boolean serviseKilled = false;
    private Boolean isFirstRun = true;
    static MutableLiveData<Long> timeRunInMillis = new MutableLiveData<Long>();
    static MutableLiveData<Boolean> isTracking = new MutableLiveData<Boolean>();
    static MutableLiveData<ArrayList<ArrayList<LatLng>>> pathPoints = new MutableLiveData<ArrayList<ArrayList<LatLng>>>();
    FusedLocationProviderClient fusedLocationProviderClient;
    static Handler handler = new Handler();
    static Runnable timer;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(String.valueOf(2), "onLocationResult: ");
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (isTracking.getValue()) {
                    addPathPoint(location);
                    Log.d(String.valueOf(1), "pathPoints:" + pathPoints.getValue());
                    Log.d(String.valueOf(1), "onLocationResult: " + location.getLatitude() + location.getLongitude());
                }
            }
        }
    };

    private NotificationCompat.Builder baseNotificationBuilder;
    private NotificationCompat.Builder curNotificationBuilder;
    private Boolean isTimerEnabled = false;
    private Long lapTime =0L;
    private Long timeRun = 0L;
    private Long timeStarted = 0L;
    private Long lastSecondTimestamp = 0L;

    private void postInitialValues() {
        isTracking.setValue(false);
        pathPoints.setValue(new ArrayList());
        timeRunInSeconds.setValue(0L);
        timeRunInMillis.setValue(0L);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class).setAction("ACTION_SHOW_TRACKING_FRAGMENT");
        baseNotificationBuilder = new NotificationCompat.Builder(this,"tracking_channel").setAutoCancel(false).setOngoing(true).setSmallIcon(R.drawable.notification_icon).setContentTitle("Running app").setContentText("00:00:00").setContentIntent(PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        curNotificationBuilder = baseNotificationBuilder;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        isTracking.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateLocationTracking(aBoolean);
                updateNotificationTrackingState(aBoolean);
            }
        });
        postInitialValues();



    }


    private void killServise(){
        serviseKilled = true;
        isFirstRun=true;
        pauseService();
        postInitialValues();
        stopForeground(true);
        stopSelf();
        isTracking.removeObservers(TrackingService.this);
        Log.d(String.valueOf(1), "Kek:" + isTracking.hasActiveObservers());
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(String.valueOf(2), "ACTION_STOP" + startId);
        switch (intent.getAction()){
            case "ACTION_START_OR_RESUME_SERVICE":
                if (isFirstRun) {
                    startForegroundService();
                    isFirstRun = false;
                }else {
                    startTimer();
                }
            case "ACTION_PAUSE_SERVICE":
                Log.d(String.valueOf(2), "ACTION_PAUSE" );
                pauseService();
            case "ACTION_STOP_SERVICE":
                killServise();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer(){
        addEmptyPolyline();
        isTracking.setValue(true);
        Log.d(String.valueOf(7), "startTimer: true" + isTracking.getValue());
        timeStarted = System.currentTimeMillis();
        isTimerEnabled = true;
        Log.d(String.valueOf(8), "Eban" + isTracking.getValue() + isTimerEnabled);
        Runnable timerRunnable = new Runnable(){

            @Override
            public void run() {
                isTracking.setValue(true);
                Log.d(String.valueOf(7), "Eban" + isTracking.getValue() + isTimerEnabled);
                    timeRunInMillis.setValue(System.currentTimeMillis() - timeStarted);
                    timeRunInSeconds.setValue(timeRunInMillis.getValue() / 1000);


                    Log.d(String.valueOf(35), " Timer:" + timeRunInMillis.getValue() + " : " + timeRunInSeconds.getValue() + ":");

                    handler.postDelayed(this, 500);

            }
        };
        timer = timerRunnable;
        handler.postDelayed(timerRunnable,0);


//        new AsyncTaskStartTimer().execute();
    }


    private void pauseService(){
        isTracking.setValue(false);
        isTimerEnabled = false;
    }

    private void updateNotificationTrackingState(Boolean isTracking) {
        String notificationActionText;
        if(isTracking){
            notificationActionText = "Pause";
        }else{
            notificationActionText = "Resume";
        }
        PendingIntent pendingIntent;
        if(isTracking){

            Intent pauseIntent = new Intent(this,TrackingService.class);
            pauseIntent.setAction("ACTION_PAUSE_SERVICE");
            pendingIntent= PendingIntent.getService(this,1,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        }else{
            Intent resumeIntent = new Intent(this,TrackingService.class);
            resumeIntent.setAction("ACTION_START_OR_RESUME_SERVICE");
            pendingIntent = PendingIntent.getService(this,2,resumeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        try {
            curNotificationBuilder.getClass().getDeclaredField("mActions").set(curNotificationBuilder, new ArrayList<NotificationCompat.Action>());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(!serviseKilled){
            curNotificationBuilder = baseNotificationBuilder.addAction(R.drawable.ic_pause_black_24dp, notificationActionText, pendingIntent);
            notificationManager.notify(1,curNotificationBuilder.build());
        }
    }


        private void updateLocationTracking(Boolean isTracking){
            Log.d(String.valueOf(10), isTracking.toString());
            if(isTracking){
                if(TrackingUtility.hasLocationPermissions(this)){

                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setInterval(5000L);
                    locationRequest.setFastestInterval(2000L);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    SettingsClient client = LocationServices.getSettingsClient(this);
                    Task<LocationSettingsResponse> task = client.checkLocationSettings(request.build());
                    task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            Log.d(String.valueOf(5), "GreatSuccess");
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                    locationCallback,
                                    Looper.getMainLooper());
                        }
                    });

                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            Log.d(String.valueOf(35), "Exception: " + ((ApiException) e).getStatusCode());


                        }
                    });

                }
            }else{
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        }
    private void addEmptyPolyline() {
        if(pathPoints.getValue() != null){
            pathPoints.getValue().add(new ArrayList());
            if(pathPoints.getValue() != null) {
                pathPoints.postValue(pathPoints.getValue());
            }
        }else{
            pathPoints.postValue(new ArrayList<ArrayList<LatLng>>());
        }
    }
    private void addPathPoint(Location location){

        if(pathPoints.getValue().size() - 1 <= 1) {
            LatLng pos = new LatLng(location.getLatitude(),location.getLongitude());
            pathPoints.getValue().get(pathPoints.getValue().size() - 1).add(pos);
            pathPoints.postValue(pathPoints.getValue());
        }

    }
    private void startForegroundService() {
        startTimer();
        isTracking.setValue(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }
        startForeground(1, baseNotificationBuilder.build());
        timeRunInSeconds.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(!serviseKilled){
                    NotificationCompat.Builder notification = curNotificationBuilder.setContentText(TrackingUtility.getFormattedStopWatchTime(aLong * 1000L));
                    notificationManager.notify(1,notification.build());
                }
            }
        });
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager){
        NotificationChannel notificationChannel = new NotificationChannel("tracking_channel","Tracking",NotificationManager.IMPORTANCE_LOW);
        manager.createNotificationChannel(notificationChannel);
    }
}
