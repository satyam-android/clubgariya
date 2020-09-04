package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.satyam.clubgariya.callbacks.CreateChatGroup;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.ChatReferenceUser;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.UtilFunction;

import java.util.ArrayList;
import java.util.List;

public class UserListGroupViewModel extends AndroidViewModel {
    private static final String TAG = "UserListGroupViewModel";
    public int userCount;
    private ChatReference chatReference;
    private ChatReferenceUser referenceUser;
    private List<ChatReferenceUser> referenceUsers;
    private List<String> tokens;
    private List<String> userUids;
    private String topicName;
    private List<User> users;
    private CreateChatGroup listner;
    private User groupUser;
    private String chatReferenceId;



    public UserListGroupViewModel(@NonNull Application application) {
        super(application);
    }

    public void setGroupListner(CreateChatGroup listner) {
        this.listner = listner;
    }

    public void createGroup(List<User> users, String groupName, String groupDesc, String profileImage) {
        referenceUsers = new ArrayList<>();
        userUids = new ArrayList<>();
        tokens = new ArrayList<>();
        this.users = users;
        this.users.add(CurrentUserData.getInstance().getUser());
        topicName = CurrentUserData.getInstance().getUid()+"BITLOOPER"+groupName;
        createUser(groupName, groupDesc, profileImage);
    }


    private void createUser(String groupName, String description, String profileImage) {
        groupUser = new User(groupName, description, AppConstants.USER_PROFILE_STATUS_DEFAULT, "", AppConstants.USER_TYPE_GROUP, AppConstants.USER_BUSINESS_TYPE_DEFAULT, UtilFunction.getInstance().getUniqueGroupName(groupName), "", profileImage, System.currentTimeMillis(), 0.0, 0.0, groupName, true, new ArrayList<>(),new ArrayList<>());
        FirebaseObjectHandler.getInstance().getUserCollection().add(groupUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e(TAG, "onSuccess: User created");
                groupUser.setUid(documentReference.getId());
                createChatReference(groupName,description);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onSuccess: User created fail");
                listner.onGroupCreatedFail();
            }
        });
    }

    private void createChatReference(String groupName,String groupDesc) {
        createChatReferenceId();
        String userROle;
        for (int i = 0; i < users.size(); i++) {
            if (!TextUtils.isEmpty(users.get(i).getUid()) && users.get(i).getUid().equals(CurrentUserData.getInstance().getUid()))
                userROle = AppConstants.USER_ROLE_ADMIN;
            else userROle = AppConstants.USER_ROLE_MEMBER;
            userUids.add(users.get(i).getMobile());
            tokens.add(users.get(i).getFcm_token());
            referenceUsers.add(new ChatReferenceUser(users.get(i).getUid(), userROle));
        }
        chatReference = new ChatReference(chatReferenceId,System.currentTimeMillis(), groupDesc,groupName,CurrentUserData.getInstance().getUid(), AppConstants.REFERENCE_TYPE_GROUP, "",referenceUsers, userUids,groupUser.getUid());

        FirebaseObjectHandler.getInstance().getChatReferenceCollection().document(chatReferenceId).set(chatReference).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: REFRENCE created");
//                registerUserForNotification();
                listner.onGroupCreatedSuccess(chatReference,null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onSuccess: REFRENCE created fail");
                listner.onGroupCreatedFail();
            }
        });
    }


    private void createChatReferenceId() {
        // Get Collator instance
        int comapareValue = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid().compareTo(groupUser.getUid());
        if (comapareValue > 0) {
            //User UID is greater than contact UID
            chatReferenceId = groupUser.getUid() + FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
//            Log.e(TAG, "setupUpChatDatabase: UID greater  " + chatId);
        } else if (comapareValue == 0) {
            chatReferenceId = groupUser.getUid() + groupUser.getUid();
        } else if (comapareValue < 0) {
            //contact UID is greater than user UID
            chatReferenceId = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid() + groupUser.getUid();
//            Log.e(TAG, "setupUpChatDatabase: UID Less  " + chatId);
        }
    }

    private void registerUserForNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic(topicName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                listner.onGroupCreatedSuccess();
                ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onSuccess: REGISTRAtion Not created" );
                listner.onGroupCreatedFail();
            }
        });
    }
}
