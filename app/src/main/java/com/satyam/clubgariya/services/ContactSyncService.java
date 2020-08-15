package com.satyam.clubgariya.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.utils.CountryCodeToPhone;
import com.satyam.clubgariya.utils.DateTimeUtilityFunctions;
import com.satyam.clubgariya.utils.UtilFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactSyncService extends JobIntentService {
    private static final String TAG = "ContactSyncService";
    private static final int JOB_ID = 1;
    AppDatabase contactDB;
    private List<User> contactList;
    private int dbSyncIndex;
    private int dbAddIndex;
    private  User contact;
    private CountryCodeToPhone countryCodeToPhone;
    private DocumentReference usersRawContactsReference;
    private CollectionReference usersClubContactsCollection;
    private String countryIso;


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppConstants.IS_CONTACT_SERVICE_RUNNING=true;
        startContactSync();
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, ContactSyncService.class, JOB_ID, intent);
    }


    private void startContactSync() {
        countryIso=UtilFunction.getInstance().getCountryIso(this);
        contactList = new ArrayList<>();
        contactDB = AppDatabase.getInstance(getApplicationContext());
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP+" ASC");
        AppSharedPreference preference = new AppSharedPreference(getApplicationContext());

        if (cur != null) {
            if (preference.getIntegerData(AppConstants.DATABASE_CONTACT_COUNT) < cur.getCount()) {
                // Contact Added
                Log.e(TAG, "startContactSync: Added" );
                performContactAddition(cur,cr);

            } else if (preference.getIntegerData(AppConstants.DATABASE_CONTACT_COUNT) > cur.getCount()) {
                //Contact Deleted
                Log.e(TAG, "startContactSync: Deleted" );
                fetchAllContact(cur,cr);
                performContactDeletion();


            }else if (preference.getIntegerData(AppConstants.DATABASE_CONTACT_COUNT) == cur.getCount()) {
                //Contact Unchanged
                Log.e(TAG, "startContactSync: Unchanged" );

                performContactUpdation(cur,cr);

            }
            preference.setIntegerData(AppConstants.DATABASE_CONTACT_COUNT, cur.getCount());
         /*   List<AppContact>  trueList=contactDB.appContactDao().getClubContactList(true);
            Log.e(TAG, "Writing DB Entries Detail");
            for (AppContact con:trueList  ) {
                Log.e(TAG, "Name  "+con.displayName+"     Mobile   "+con.mobileNumber+"    IsMember  "+con.getisClubUser() );
            }*/
        }
        if (cur != null) {
            cur.close();
        }
    }

    public void fetchAllContact(Cursor cur,ContentResolver cr){
        countryCodeToPhone = new CountryCodeToPhone();
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        contact = new User();
                        String timestamp = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));
                        String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String displayName = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME));
                        phoneNo = phoneNo.trim();
                        phoneNo = phoneNo.replaceAll(" ", "");
                        phoneNo = phoneNo.replace("-", "");
                        phoneNo = phoneNo.replace("(", "");
                        phoneNo = phoneNo.replace(")", "");
                        if (Patterns.PHONE.matcher(phoneNo).matches() && phoneNo.length() >= 10) {
                            if (phoneNo.startsWith("+")) {

                            } else if (phoneNo.startsWith("0")) {
                                phoneNo = phoneNo.replaceFirst("0", countryCodeToPhone.getPhone(countryIso));
                            } else {
                                if (countryIso!= null) {
                                    phoneNo = countryCodeToPhone.getPhone(countryIso) + phoneNo;
                                }
                            }
                            contact = new User(displayName,"",AppConstants.DATABASE_CONTACT_STATUS_DEFAULT,"",phoneNo,"","" , timestamp,0.0,0.0,"", false);
                            contactList.add(contact);
                        }
                    }
                    pCur.close();
                }
            }
        }
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.e(TAG, "onStopCurrentWork: " );
        AppConstants.IS_CONTACT_SERVICE_RUNNING=false;
        return super.onStopCurrentWork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        AppConstants.IS_CONTACT_SERVICE_RUNNING=false;
    }

    public void performContactAddition(Cursor cur,ContentResolver cr){
        dbAddIndex=0;
        fetchAllContact(cur,cr);
        addContactToDB(contactList);
        Collections.reverse(contactList);
        while (FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()== null){
            Log.e(TAG, "performContactAddition: loop" );
        }
        dbSyncIndex=0;
        usersRawContactsReference=FirebaseObjectHandler.getInstance().getUserRawContactReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid());
        usersClubContactsCollection=FirebaseObjectHandler.getInstance().getUsersClubContactsCollection();
        syncListForClubMember(contactList);

    }

    public void performContactUpdation(Cursor cur,ContentResolver cr){
        dbSyncIndex=0;
        dbAddIndex=0;
        fetchAllContact(cur,cr);
        addContactToDB(contactList);

        String today= DateTimeUtilityFunctions.getInstance().getDateTime("dd/MM/yyyy");
        String lastSyncDate=new AppSharedPreference(getApplicationContext()).getStringData(AppConstants.DATABASE_CONTACT_SYNC_DATE);
        Log.e(TAG, "startContactSync: "+today );

        if((lastSyncDate==null || !lastSyncDate.equalsIgnoreCase(today)) && FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()!=null){
//        if(FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()!=null){
            Log.e(TAG, "startContactSync: Syncing todays data" );
            dbSyncIndex=0;
            usersRawContactsReference=FirebaseObjectHandler.getInstance().getUserRawContactReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid());
            usersClubContactsCollection=FirebaseObjectHandler.getInstance().getUsersClubContactsCollection();

            syncListForClubMember(contactDB.userDao().getClubContact(false));
            new AppSharedPreference(getApplicationContext()).setStringData(AppConstants.DATABASE_CONTACT_SYNC_DATE,today);
        }

    }


    public void performContactDeletion(){
        List<User> list=contactDB.userDao().getAllContacts();
        for(User dbContact: list){
            boolean dataExist=false;
            for (User localContact:contactList){
                if(dbContact.getMobile().equals(localContact.getMobile())){
                    dataExist=true;
                }
            }
            if(!dataExist){
                Log.e(TAG, "performContactDeletion: "+dbContact.getMobile()+"  NAme  "+dbContact.getName() );
                contactDB.userDao().deleteContact(dbContact);
            }
        }
    }

 /*   private void addContactToDB() {
        for (AppContact contact : contactList) {
            if (contactDB.appContactDao().checkIfContactExist(contact.mobileNumber)!=0) {
                AppContact existContact = contactDB.appContactDao().getContactDetail(contact.mobileNumber);
                existContact.setDisplayName(contact.getDisplayName());
                contactDB.appContactDao().updateContact(existContact);
            } else {
                contactDB.appContactDao().insertContact(contact);
                newContacts.add(contact);

            }
        }
    }*/
    private void addContactToDB(final List<User> listData) {
        if(listData.size()>dbAddIndex) {
            final User contact = listData.get(dbAddIndex);
            if(contactDB.userDao().checkIfContactExist(contact.getMobile()) != 0) {
//                Log.e(TAG, "Updating Contact" + contact.mobileNumber + " Name " + contact.displayName + "    isCLubMember " + contact.getisClubUser());
                User existContact = contactDB.userDao().getContactDetail(contact.getMobile());
                existContact.setName(contact.getName());
                contactDB.userDao().updateContactName(existContact.getName(),existContact.getMobile());
            } else {
                contactDB.userDao().insertContact(contact);
            }
            dbAddIndex++;
            addContactToDB(listData);
        }else{
            Log.e(TAG, "addContactToDB: DONE" );
        }
    }

    private void syncListForClubMember(final List<User> list) {
        if(list.size()>dbSyncIndex) {
            contact = list.get(dbSyncIndex);
            if(contact.getMobile().contains("9213965120"));
            Log.e(TAG, "sync contact "+contact.getMobile()+"    Name "+contact.getName() );
            FirebaseObjectHandler.getInstance().getUserCollection().whereEqualTo(AppConstants.FIREBASE_CONTACT_NODE, contact.getMobile()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }

                        if(task.getResult().getDocuments().size()>0){
//                            final User user=task.getResult().getDocuments().get(0).toObject(User.class);
                            Log.e(TAG, "Before Updating   Name  " + contact.getName() + "     Mobile   " + contact.getMobile() + "    IsMember  " + contact.isClubMember());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    User user=task.getResult().getDocuments().get(0).toObject(User.class);
//                                    contactDB.userDao().updateContactUidWithClubMembership(user.getUid(),true,user.getMobile());
                                    contactDB.userDao().updateUserData(user.imageUrl,user.email,user.uid,user.creationTime,user.fcm_token,user.status,user.profileStatus,user.totalCredit,user.totalDebit,user.clubMember,user.mobile);
                                    contactDB.userDao().getContactDetail(contact.getMobile());
//                                    User con=contactDB.userDao().getContactDetail(contact.getMobile());
//                                    Log.e(TAG, "After Update   Name  " + con.getName() + "     Mobile   " + con.getMobile() + "    IsMember  " + con.isClubMember()+"  UID "+con.getUid());
                                    dbSyncIndex++;
                                    syncListForClubMember(list);

                                }
                            }).start();
                        }else {
                            Log.e(TAG, "onComplete: Data Not found"+contact.getName());
                            dbSyncIndex++;
                            syncListForClubMember(list);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: Data Not found"+contact.getName());
                    dbSyncIndex++;
                    syncListForClubMember(list);
                }
            });
        }
    }



}
