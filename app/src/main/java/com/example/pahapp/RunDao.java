package com.example.pahapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RunDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Run run);

    @Query("DELETE FROM run_table")
    void deleteAll();

    @Query("SELECT * from run_table")
    LiveData<List<Run>> getAllRuns();
}