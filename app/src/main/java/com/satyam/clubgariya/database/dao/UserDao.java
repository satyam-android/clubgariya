package com.satyam.clubgariya.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.satyam.clubgariya.database.tables.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select * from Users")
    LiveData<List<User>> getContactList();

    @Query("Select * from Users")
    List<User> getAllContacts();

    @Query("Select * from Users where clubMember= :isClubUser ORDER BY creationTime DESC")
    LiveData<List<User>> getClubContactList(boolean isClubUser);

    @Query("Select * from Users where clubMember= :isClubUser AND mobile !=:mobile ORDER BY creationTime DESC")
    LiveData<List<User>> getClubContactListExcludeNumber(boolean isClubUser, String mobile);

    @Query("Select name from Users where mobile=:mobile")
    LiveData<String> getNameByMobile(String mobile);

    @Query("Select name from Users where mobile=:mobile")
    String getNameFromMobile(String mobile);

    @Query("Select * from Users where clubMember= :isClubUser ORDER BY creationTime DESC")
    List<User> getClubContact(boolean isClubUser);

    @Query("Select COUNT(*) from Users Where mobile = :mobile")
    int checkIfContactExist(String mobile);

    @Query("DELETE from Users")
    void deleteContactTable();

    @Query("Select * from Users where mobile=:number limit 1")
    User getContactDetail(String number);

    @Query("Select * from Users where mobile=:number limit 1")
    LiveData<User> getUserDetailByNumber(String number);

    @Query("Select * from Users where uid=:uid limit 1")
    LiveData<User>  getContactDetailByUid(String uid);

    @Query("Select * from Users where uid=:uid limit 1")
    User  getUserDetailByUid(String uid);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("UPDATE Users SET name=:name WHERE mobile=:mobile")
    void updateContactName(String name, String mobile);

    @Query("UPDATE Users SET clubMember=:isMember WHERE mobile=:mobile")
    void updateContactClubMembership(boolean isMember, String mobile);

    @Query("UPDATE Users SET imageUrl=:imageUrl,email=:email,uid=:uid, creationTime=:creationTime,fcm_Token=:fcmToken,userStatus=:status,profileStatus=:profileStatus,totalCredit=:totalCredit,totalDebit=:totalDebit,clubMember=:clubMember WHERE mobile=:mobile")
    void updateUserData(String imageUrl, String email, String uid, long creationTime, String fcmToken, String status, String profileStatus, double totalCredit, double totalDebit, boolean clubMember, String mobile);

    @Query("UPDATE Users SET uid=:uid,clubMember=:isMember WHERE mobile=:mobile")
    void updateContactUidWithClubMembership(String uid, boolean isMember, String mobile);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(User appContact);

    @Delete
    void deleteContact(User appContact);
}
