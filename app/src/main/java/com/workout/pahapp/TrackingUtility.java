package com.workout.pahapp;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

public class TrackingUtility {
    public static Boolean hasLocationPermissions(Context context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            if(EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)){
                return true;
            }else {
                return false;
            }
        }else {
            if(EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                return true;
            }else{
                return false;
            }
        }
    }
    public static Float calculatePolylineLength(ArrayList<LatLng> polyline){
        Float distance = 0f;
        for(int i = 0; i<= polyline.size() - 2;i++){
            LatLng pos1 = polyline.get(i);
            LatLng pos2 = polyline.get(i + 1);
            float[] results = new float[1];
            Location.distanceBetween(pos1.latitude, pos1.longitude,pos2.latitude, pos2.longitude, results);
            distance += results[0];
        }
        return distance;
    }
    public static String getFormattedStopWatchTime(Long ms) {
        Long milliSeconds = ms;
        long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds);
        milliSeconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds);
        String resHours;
        String resMinutes;
        String resSeconds;
        if (hours < 10) {
            resHours = "0" + String.valueOf(hours);
        } else {
            resHours = String.valueOf(hours);
        }
        if (minutes < 10) {
            resMinutes = "0" + String.valueOf(minutes);
        } else {
            resMinutes = String.valueOf(minutes);
        }
        if (seconds < 10) {
            resSeconds = "0" + String.valueOf(seconds);
        } else {
            resSeconds = String.valueOf(seconds);
        }
        return resHours + ":" + resMinutes + ":" + resSeconds;
    }
    public static String getDate(Long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
//    public static byte[] bitmapToBytes(Bitmap bitmap){
//        int size = bitmap.getRowBytes() * bitmap.getHeight();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//        bitmap.copyPixelsToBuffer(byteBuffer);
//        return byteBuffer.array();
//    }
    public static byte[] bitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    public static Bitmap bytesToBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static float getRating(int distanceInMeters, long currTimeInMillis, String sex){
        float rating;
        float miles = distanceInMeters * 0.000621f;
        float minutes = (float) ((currTimeInMillis / (1000*60)) % 60);
        float pace = (float) (minutes / miles);
        float average = (float) ((distanceInMeters*0.001)/ (minutes/60));
        Log.d(String.valueOf(3), "average: " + average + "pace" + pace);
        if(sex=="Female"){
            if(average < 8.5f && average > 8.1f){
                rating = 3.5f;
            }else if(average < 8.1f && average>7.8f){
                rating = 3f;
            }else if(average < 7.8 && average > 7.5){
                rating = 2.5f;
            }else if(average < 7.5f){
                rating = 2f;
            }else if(average > 8.5f && average < 8.9f){
                rating = 4f;
            }else if(average> 8.9f && average<9.3){
                rating = 4.25f;
            }else if(average>9.3 && average < 9.7){
                rating = 4.5f;
            }else {
                rating = 5f;
            }
        }else{
            if (average < 9f && average> 8.6f){
                rating = 3.5f;
            }else if(average < 8.6f && average > 8.3f){
                rating= 3f;
            }else if (average<8.3f && average>8.0f){
                rating = 2.5f;
            }else if(average< 8.0f){
                rating = 2f;
            }else if(average > 9f && average<9.4f){
                rating = 4f;
            }else if(average > 9.4f && average<9.8f){
                rating = 4.25f;
            }else if(average > 9.8f && average <10.2f){
                rating = 4.5f;
            }else {
                rating = 5f;
            }
        }
        return rating;
    }

}
