package com.satyam.clubgariya.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.satyam.clubgariya.database.tables.AppChatReference;
import com.satyam.clubgariya.modals.ChatReference;

import java.util.List;

@Dao
public interface ChatReferenceDao {
    @Query("Select * from ChatReferences")
    LiveData<List<AppChatReference>> getAllChatReferences();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChatReference(AppChatReference chatReference);

}
