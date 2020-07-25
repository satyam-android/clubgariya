package com.satyam.clubgariya.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.satyam.clubgariya.database.tables.AppContact;

import java.util.List;

@Dao
public interface AppContactDao {
    @Query("Select * from AppContact")
    LiveData<List<AppContact>> getContactList();

    @Query("Select * from AppContact")
    List<AppContact> getAllContacts();

    @Query("Select * from AppContact where isClubUser= :isClubUser")
    LiveData<List<AppContact>> getClubContactList(boolean isClubUser);

    @Query("Select * from AppContact where isClubUser= :isClubUser")
    List<AppContact> getClubContact(boolean isClubUser);

    @Query("Select COUNT(*) from AppContact Where mobileNumber = :mobile")
    int checkIfContactExist(String mobile);

    @Query("DELETE from AppContact")
    void deleteContactTable();

    @Query("Select * from AppContact where mobileNumber=:number limit 1")
    AppContact getContactDetail(String number);

    @Update
    void updateContact(AppContact appContact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(AppContact appContact);

    @Delete
    void deleteContact(AppContact appContact);
}
