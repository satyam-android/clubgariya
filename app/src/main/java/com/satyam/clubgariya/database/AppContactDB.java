package com.satyam.clubgariya.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.satyam.clubgariya.database.dao.AppContactDao;
import com.satyam.clubgariya.database.tables.AppContact;

@Database(entities = AppContact.class, exportSchema = false, version = 1)
public abstract class AppContactDB extends RoomDatabase {

    private static final String DB_NAME = "CLubContacts";
    private static AppContactDB instance;


    public static synchronized AppContactDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), AppContactDB.class, DB_NAME).fallbackToDestructiveMigration().build();
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

    public abstract AppContactDao appContactDao();
}
