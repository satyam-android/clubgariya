package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.satyam.clubgariya.callbacks.ChatReferenceListFragmentListner;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class ChatListViewModel extends AndroidViewModel {
    private static final String TAG = "ChatListViewModel";

    private ChatReferenceListFragmentListner listner;
    private List<ChatReference> references;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        registerChatReferenceChange();
    }

    public void setListner(ChatReferenceListFragmentListner listner){
        this.listner=listner;
    }

    public void registerChatReferenceChange() {
        //.whereArrayContains("allowedUserMobiles", CurrentUserData.getInstance().getUserMobile())//orderBy("lastMessageTime", Query.Direction.DESCENDING).
      CollectionReference  getMyChatReferences=FirebaseObjectHandler.getInstance().getChatReferenceCollection();
//      Query query=  getMyChatReferences.whereArrayContains("allowedUserMobiles", CurrentUserData.getInstance().getUserMobile());
//      Query query1=  getMyChatReferences.orderBy("lastMessageTime", Query.Direction.DESCENDING);
        getMyChatReferences.whereArrayContains(AppConstants.CHAT_REFERENCE_TAG_ALLOWED_USERS, CurrentUserData.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                references = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ChatReference chatReference = doc.toObject(ChatReference.class);
                    chatReference.setChatReferenceId(doc.getId());
                    Log.e(TAG, "CHat References: "+chatReference.getLastMessage() );
                    references.add(chatReference);
                }
                listner.onChatReferenceChange(references);
            }
        });
    }


}