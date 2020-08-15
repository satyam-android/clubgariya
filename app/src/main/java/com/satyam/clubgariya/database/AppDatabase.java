package com.satyam.clubgariya.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.satyam.clubgariya.database.dao.ChatDao;
import com.satyam.clubgariya.database.dao.ChatReferenceDao;
import com.satyam.clubgariya.database.dao.UserDao;
import com.satyam.clubgariya.database.tables.AppChat;
import com.satyam.clubgariya.database.tables.AppChatReference;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.utils.AppDataConverter;

@Database(entities = {User.class, AppChatReference.class, AppChat.class}, exportSchema = false, version = 7)
@TypeConverters({AppDataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "CLubContacts";
    private static AppDatabase instance;


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
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
    public abstract ChatDao chatDao();
    public abstract ChatReferenceDao chatReferenceDao();
}
