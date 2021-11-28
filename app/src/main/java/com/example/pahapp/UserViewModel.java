package com.example.pahapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private RunRepository mRepository;
    private LiveData<List<User>> mAllUser;

    public UserViewModel(Application application){
        super(application);
        mRepository = new RunRepository(application);
        mAllUser = mRepository.getAllUsers();
    }
    LiveData<List<User>> getAllUser(){
        return mAllUser;
    }
    public void insertUser(User user){
        mRepository.insertUser(user);
    }
}
