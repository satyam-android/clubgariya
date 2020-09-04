package com.satyam.clubgariya.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.utils.AppConstants;

public class FirebaseObjectHandler {
    private static final String TAG = "FirebaseObjectHandler";
    private static FirebaseObjectHandler instance;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;
    private DocumentReference documentReferenceAppSettings;
    private StorageReference storageReference;


    private FirebaseObjectHandler() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(settings);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

    }

    public static FirebaseObjectHandler getInstance() {
        if (instance == null) instance = new FirebaseObjectHandler();
        return instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public CollectionReference getBlogCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.BLOG_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getUsersRawContactsCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.USERS_RAW_CONTACTS_COLLECTION);

        return collectionReference;
    }

    public CollectionReference getUsersClubContactsCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.USERS_CLUB_CONTACTS_COLLECTION);

        return collectionReference;
    }

    public CollectionReference getCommentCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.COMMENT_COLLECTION_NODE);

        return collectionReference;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public CollectionReference getUserCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.USER_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getChatReferenceCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.CHAT_REFERENCE_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getTransactionReferenceCollection() {
        collectionReference = firebaseFirestore.collection(AppConstants.USER_TRANSACTION_REFERENCES_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getChatCollection(String chatId) {
        collectionReference = firebaseFirestore.collection(AppConstants.CHAT_COLLECTION_NODE).document(chatId).collection(AppConstants.CHAT_SUB_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getTransactionCollection(String transactionId) {
        collectionReference = firebaseFirestore.collection(AppConstants.TRANSACTION_COLLECTION_NODE).document(transactionId).collection(AppConstants.TRANSACTION_SUB_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getCollectionReference(String collectionName) {
        return firebaseFirestore.collection(collectionName);
    }

    public CollectionReference getUserChatCollectionReference(String userId){
        return getCollectionReference(AppConstants.USER_CHAT_REFERENCES_COLLECTION_NODE).document(userId).collection(AppConstants.USER_CHAT_SUB_REFERENCES_COLLECTION_NODE);
    }

    public CollectionReference getUserTransactionCollectionReference(){
        return getCollectionReference(AppConstants.USER_TRANSACTION_REFERENCES_COLLECTION_NODE);
    }

    public DocumentReference getAppSettingsDocumentReference() {
        documentReference = getCollectionReference(AppConstants.APP_SETTINGS_COLLECTION_NODE).document(AppConstants.APP_SETTINGS_DOCUMENT_NODE);
        return documentReference;
    }

    public DocumentReference getUserRawContactReference(String node) {
        documentReference = getUsersRawContactsCollection().document(node);
        return documentReference;
    }

    public CollectionReference getUserContactSubCollection(String documentID){
        collectionReference = firebaseFirestore.collection(AppConstants.USERS_RAW_CONTACTS_COLLECTION).document(documentID).collection(AppConstants.USERS_SUB_CONTACTS_COLLECTION);

        return collectionReference;
    }

    public DocumentReference getUserDocumentReference(String node) {
        documentReference = getUserCollection().document(node);
        return documentReference;
    }

    public DocumentReference getCommentDocumentReference(String node) {
        documentReference = getCommentCollection().document(node);
        return documentReference;
    }

    public DocumentReference getBlogDocumentReference(String node) {
        documentReference = getBlogCollection().document(node);
        return documentReference;
    }



}
