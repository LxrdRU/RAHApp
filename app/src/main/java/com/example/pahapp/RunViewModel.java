package com.example.pahapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;


import java.util.List;

public class RunViewModel extends AndroidViewModel {
    private RunRepository mRepository;
    private MediatorLiveData<List<Run>> mAllRuns;

    public RunViewModel (Application application){
        super(application);
        mRepository = new RunRepository(application);
        mAllRuns.addSource(mRepository.getAllRuns(), new Observer<List<Run>>() {
            @Override
            public void onChanged(List<Run> runs) {
                mAllRuns.setValue(runs);
            }
        });
    }
    MediatorLiveData<List<Run>> getAllRuns(){
        return mAllRuns;
    }
    public void insertRun(Run run){
        mRepository.insert(run);
    }
}
