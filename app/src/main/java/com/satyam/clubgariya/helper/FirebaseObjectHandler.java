package com.satyam.clubgariya.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.utils.AppConstants;

public class FirebaseObjectHandler {

    private static FirebaseObjectHandler instance;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;
    private StorageReference storageReference;


    private FirebaseObjectHandler(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(settings);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

    }

    public static FirebaseObjectHandler getInstance(){
        if(instance==null) instance=new FirebaseObjectHandler();
        return instance;
    }

    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }

    public CollectionReference getBlogCollection(){
        collectionReference=firebaseFirestore.collection(AppConstants.BLOG_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getCommentCollection(){
        collectionReference=firebaseFirestore.collection(AppConstants.COMMENT_COLLECTION_NODE);

        return collectionReference;
    }

    public StorageReference getStorageReference(){
        return storageReference;
    }

    public CollectionReference getUserCollection(){
        collectionReference=firebaseFirestore.collection(AppConstants.USER_COLLECTION_NODE);

        return collectionReference;
    }

    public CollectionReference getChatCollection(){
        collectionReference=firebaseFirestore.collection(AppConstants.CHAT_COLLECTION_NODE);

        return collectionReference;
    }

    public DocumentReference getChatDocumentReference(String node){
        documentReference=getChatCollection().document(node);
        return documentReference;
    }

    public DocumentReference getUserDocumentReference(String node){
        documentReference=getUserCollection().document(node);
        return documentReference;
    }

    public DocumentReference getCommentDocumentReference(String node){
        documentReference=getCommentCollection().document(node);
        return documentReference;
    }

    public DocumentReference getBlogDocumentReference(String node){
        documentReference=getBlogCollection().document(node);
        return documentReference;
    }

}
