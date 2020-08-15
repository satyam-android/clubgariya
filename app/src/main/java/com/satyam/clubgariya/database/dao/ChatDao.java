package com.satyam.clubgariya.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.satyam.clubgariya.database.tables.AppChat;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChat(AppChat chat);

    @Query("Select * from AppChats")
    LiveData<List<AppChat>>  getAllChatMessage();
}
