package com.gretagrigute.moodformovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;


    @Database(entities = {MovieEntity.class}, version = 1, exportSchema = false)
    public abstract class AppDataBase extends RoomDatabase {
        private static final String LOG_TAG = AppDataBase.class.getSimpleName();
        private static final Object LOCK = new Object();
        private static final String DATABASE_NAME = "movielist";
        private static AppDataBase sInstance;

        public static AppDataBase getInstance (Context context){
            if (sInstance == null){
                synchronized (LOCK){
                    Log.d(LOG_TAG,"Creating new Database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,
                            AppDataBase.DATABASE_NAME)
                            //Temporary to check if DB works.
                            // Queries should be done in a separate thread to avoid locking the UI
                            // .allowMainThreadQueries()
                            .build();
                }
            }
            Log.d(LOG_TAG,"Getting the Database instance");
            return sInstance;
        }
        public abstract MovieDao movieDao();
    }

