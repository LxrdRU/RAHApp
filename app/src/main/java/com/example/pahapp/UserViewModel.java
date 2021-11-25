package com.example.pahapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private RunRepository mRepository;
    private MediatorLiveData<List<User>> mAllUser = new MediatorLiveData<List<User>>();

    public UserViewModel(Application application){
        super(application);
        mRepository = new RunRepository(application);
        mAllUser.addSource(mRepository.getAllUsers(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mAllUser.setValue(users);
            }
        });
    }
    MediatorLiveData<List<User>> getAllUser(){
        return mAllUser;
    }
    public void insertRun(User user){
        mRepository.insert(user);
    }
}
