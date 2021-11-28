package com.workout.pahapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Run.class}, version = 4, exportSchema = false)
public abstract class RunningDatabase extends RoomDatabase {

    public abstract RunDao runDao();
    public abstract UserDao userDao();
    private static RunningDatabase INSTANCE;

    static RunningDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RunningDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RunningDatabase.class, "running_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RunDao mRunDao;
        private final UserDao mUserDao;

        PopulateDbAsync(RunningDatabase db) {
            mRunDao = db.runDao();
            mUserDao = db.userDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            return null;
        }
    }
}