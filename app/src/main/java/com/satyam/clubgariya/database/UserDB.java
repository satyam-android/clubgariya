package com.satyam.clubgariya.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.satyam.clubgariya.database.dao.UserDao;
import com.satyam.clubgariya.database.tables.User;

@Database(entities = User.class, exportSchema = false, version = 5)
public abstract class UserDB extends RoomDatabase {

    private static final String DB_NAME = "CLubContacts";
    private static UserDB instance;


    public static synchronized UserDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDB.class, DB_NAME).fallbackToDestructiveMigration().build();
        return instance;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract UserDao userDao();
}
