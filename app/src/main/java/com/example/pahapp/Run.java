package com.example.pahapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_table")
public class Run {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    protected Integer mId;

    @NonNull
    @ColumnInfo(name = "timestamp")
    public Long mTimestamp;

    @NonNull
    @ColumnInfo(name = "avg_speed_KMH")
    public Float mAvgSpeenInKMH;

    @NonNull
    @ColumnInfo(name = "distance_in_meters")
    public Integer mDistanceInMeters;



    @NonNull
    @ColumnInfo(name = "time_in_millis")
    public Long mTimeInMillis;

    @NonNull
    @ColumnInfo(name = "calories_burned")
    public Integer mCaloriesBurned;
    public Run(){};
    public Run(@NonNull Long timestamp, @NonNull Float avgSpeedInKMH, @NonNull Integer distanceInMeters, @NonNull Long timeInMillis, @NonNull Integer caloriesBurned) {
        this.mTimestamp = timestamp;
        this.mAvgSpeenInKMH = avgSpeedInKMH;
        this.mDistanceInMeters = distanceInMeters;
        this.mTimeInMillis = timeInMillis;
        this.mCaloriesBurned = caloriesBurned;
    }
    public Run(@NonNull Integer id, @NonNull Long timestamp, @NonNull Float avgSpeedInKMH, @NonNull Integer distanceInMeters, @NonNull Long timeInMillis, @NonNull Integer caloriesBurned) {
        this.mId = id;
        this.mTimestamp = timestamp;
        this.mAvgSpeenInKMH = avgSpeedInKMH;
        this.mDistanceInMeters = distanceInMeters;
        this.mTimeInMillis = timeInMillis;
        this.mCaloriesBurned = caloriesBurned;
    }
    @NonNull
    public Integer getRunId(){
        return this.mId;
    }
    @NonNull
    public Long getRunTimestamp(){
        return this.mTimestamp;
    }
    @NonNull
    public Float getmAvgSpeenInKMH() {
        return this.mAvgSpeenInKMH;
    }

    @NonNull
    public Integer getmDistanceInMeters() {
        return this.mDistanceInMeters;
    }

    @NonNull
    public Long getmTimeInMillis() {
        return this.mTimeInMillis;
    }

    @NonNull
    public Integer getmCaloriesBurned() {
        return this.mCaloriesBurned;
    }
}