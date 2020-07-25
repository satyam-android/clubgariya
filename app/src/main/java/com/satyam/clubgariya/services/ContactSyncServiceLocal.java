package com.satyam.clubgariya.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.satyam.clubgariya.database.AppContactDB;
import com.satyam.clubgariya.database.tables.AppContact;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.UserRegister;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.utils.CountryCodeToPhone;
import com.satyam.clubgariya.utils.UtilFunction;

import java.util.ArrayList;
import java.util.List;

public class ContactSyncServiceLocal extends JobIntentService {
    private static final String TAG = "ContactSyncService";
    private static final int JOB_ID = 1;
    AppContactDB contactDB;
    private List<AppContact> contactList;
    private List<AppContact> newContacts;
    private int dbSyncIndex;
    private int dbAddIndex;
    private  AppContact contact;
    private CountryCodeToPhone countryCodeToPhone;
    private CollectionReference usersRawContactsCollection;
    private CollectionReference usersClubContactsCollection;


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppConstants.IS_CONTACT_SERVICE_RUNNING=true;
        startContactSync();
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, ContactSyncServiceLocal.class, JOB_ID, intent);
    }


    private void startContactSync() {
        contactList = new ArrayList<>();
        usersRawContactsCollection=FirebaseObjectHandler.getInstance().getUsersRawContactsCollection();
        usersClubContactsCollection=FirebaseObjectHandler.getInstance().getUsersClubContactsCollection();
        contactDB = AppContactDB.getInstance(getApplicationContext());
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        AppSharedPreference preference = new AppSharedPreference(getApplicationContext());
        if (cur != null) {
            if (preference.getIntegerData(AppConstants.DATABASE_CONTACT_COUNT) < cur.getCount()) {
                // Contact Added
                newContacts=new ArrayList<>();
                dbSyncIndex=0;
                dbAddIndex=0;
                performContactAddition(cur,cr);
                if(FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()!=null)
                syncListForClubMember(newContacts);

            } else if (preference.getIntegerData(AppConstants.DATABASE_CONTACT_COUNT) > cur.getCount()) {
                //Contact Deleted

                performContactDeletion(cur,cr);


            }else if (preference.getIntegerData(AppConstants.DATABASE_CONTACT_COUNT) == cur.getCount()) {
                //Contact Unchanged
                String today=UtilFunction.getInstance().getDateTime("dd/MM/yyyy");
                String lastSyncDate=new AppSharedPreference(getApplicationContext()).getStringData(AppConstants.DATABASE_CONTACT_SYNC_DATE);
                Log.e(TAG, "startContactSync: "+today );
                performContactUpdation(cur,cr);
                if((lastSyncDate==null || !lastSyncDate.equalsIgnoreCase(today)) && FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser()!=null){
                    Log.e(TAG, "startContactSync: Syncing todays data" );
                    dbSyncIndex=0;
                    syncListForClubMember(contactDB.appContactDao().getClubContact(false));
                    new AppSharedPreference(getApplicationContext()).setStringData(AppConstants.DATABASE_CONTACT_SYNC_DATE,today);
                }

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
                        contact = new AppContact();
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
                                phoneNo = phoneNo.replaceFirst("0", countryCodeToPhone.getPhone(getCountryIso()));
                            } else {
                                if (getCountryIso() != null) {
                                    phoneNo = countryCodeToPhone.getPhone(getCountryIso()) + phoneNo;
                                }
                            }
                            contact = new AppContact(phoneNo, displayName, timestamp, AppConstants.DATABASE_CONTACT_STATUS_DEFAULT, false);
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
        AppConstants.IS_CONTACT_SERVICE_RUNNING=false;
        return super.onStopCurrentWork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstants.IS_CONTACT_SERVICE_RUNNING=false;
    }

    public void performContactAddition(Cursor cur,ContentResolver cr){
        fetchAllContact(cur,cr);
        addContactToDB();

    }

    public void performContactUpdation(Cursor cur,ContentResolver cr){
        fetchAllContact(cur,cr);
        addContactToDB();

    }


    public void performContactDeletion(Cursor cur,ContentResolver cr){
        fetchAllContact(cur,cr);
        List<AppContact> list=contactDB.appContactDao().getAllContacts();
        for(AppContact dbContact: list){
            boolean dataExist=false;
            for (AppContact localContact:contactList){
                if(dbContact.mobileNumber.equals(localContact.mobileNumber)){
                    dataExist=true;
                }
            }
            if(!dataExist){
                Log.e(TAG, "performContactDeletion: "+dbContact.mobileNumber+"  NAme  "+dbContact.displayName );
                contactDB.appContactDao().deleteContact(dbContact);
            }
        }
    }


    private void addContactToDB() {
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
    }

    private void syncNewContactWithClubMember(ArrayList<AppContact> list,int index){

    }


    private void syncListForClubMember(final List<AppContact> list) {
        if(list.size()>dbSyncIndex) {
            contact = list.get(dbSyncIndex);
            FirebaseObjectHandler.getInstance().getUserCollection().whereEqualTo(AppConstants.FIREBASE_CONTACT_NODE, contact.mobileNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().getDocuments().size()>0){
                            final UserRegister user=task.getResult().getDocuments().get(0).toObject(UserRegister.class);
                            Log.e(TAG, "Before Updating   Name  " + contact.displayName + "     Mobile   " + contact.mobileNumber + "    IsMember  " + contact.getisClubUser());
                            new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        contact.setisClubUser(true);
                                        contact.setUid(user.getUid());
                                        contactDB.appContactDao().updateContact(contact);
                                        AppContact con=contactDB.appContactDao().getContactDetail(contact.mobileNumber);
                                        Log.e(TAG, "After Update   Name  " + con.displayName + "     Mobile   " + con.mobileNumber + "    IsMember  " + con.getisClubUser()+"  UID "+con.getUid());
                                        dbSyncIndex++;
                                        syncListForClubMember(list);

                                    }
                                }).start();
                        }else {
                            Log.e(TAG, "onComplete: Data Not found"+contact.displayName);
                            dbSyncIndex++;
                            syncListForClubMember(list);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dbSyncIndex++;
                    syncListForClubMember(list);
                }
            });
        }
    }

    private String getCountryIso() {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        iso = telephonyManager.getNetworkCountryIso();
        if (TextUtils.isEmpty(iso))
            iso = telephonyManager.getNetworkCountryIso(1);
        if (TextUtils.isEmpty(iso))
            iso = telephonyManager.getNetworkCountryIso(2);
        if (telephonyManager.getNetworkCountryIso() != null &&
                !TextUtils.isEmpty(telephonyManager.getNetworkCountryIso()))
            iso = telephonyManager.getNetworkCountryIso();
        return iso;
    }

}
