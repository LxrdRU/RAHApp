package com.workout.pahapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RunRepository {

    private RunDao mRunDao;
    private LiveData<List<Run>> mAllRuns;
    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers;

    RunRepository(Application application) {
        RunningDatabase db = RunningDatabase.getDatabase(application);
        mRunDao = db.runDao();
        mAllRuns = mRunDao.getAllRuns();
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUsers();

    }

    LiveData<List<Run>> getAllRuns() {
        return mAllRuns;
    }

    public void insertRun(Run run) {
        new insertRunAsyncTask(mRunDao).execute(run);
    }

    private static class insertRunAsyncTask extends AsyncTask<Run, Void, Void> {

        private RunDao mAsyncTaskDao;

        insertRunAsyncTask(RunDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Run... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public void insertUser(User user) {
        new insertUserAsyncTask(mUserDao).execute(user);
    }

    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public void deleteRun(Integer id) {
        new deleteRunAsyncTask(mRunDao).execute(id);
    }

    private static class deleteRunAsyncTask extends AsyncTask<Integer, Void, Void> {

        private RunDao mAsyncTaskDao;

        deleteRunAsyncTask(RunDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteById(params[0]);
            return null;
        }
    }
}