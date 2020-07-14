package com.satyam.clubgariya.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.ArrayList;

public class ContactSyncService extends JobIntentService {
    private static final String TAG = "ContactSyncService";
    private static final int JOB_ID = 1;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        getAllContacts();
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, ContactSyncService.class, JOB_ID, intent);
    }

    private ArrayList getAllContacts() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                nameList.add(name);
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String timestamp=cur.getString(cur.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));
                        Log.e(TAG, "Timestamp: "+timestamp );
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String displayName = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME));
                        String displayNamePrimary = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_PRIMARY));
                        Log.e(TAG, "phoneNo: "+phoneNo );
                        Log.e(TAG, "displayName: "+displayName );
                        Log.e(TAG, "displayNamePrimary: "+displayNamePrimary );

                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }
}
