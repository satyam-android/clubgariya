package com.satyam.clubgariya.helper;

import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.modals.Blog;

public class Temp {
    private static Temp temp;

    StorageReference storageReference;
    Uri fileUriList;
    Blog blog;
    CollectionReference collectionReference;

    private Temp(){

    }

    public static Temp getInstance(){
        if (temp==null)
            temp=new Temp();
        return temp;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public void setStorageReference(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public CollectionReference getDatabaseReference() {
        return collectionReference;
    }

    public void setDatabaseReference(CollectionReference databaseReference) {
        this.collectionReference = databaseReference;
    }

    public Uri getFileUriList() {
        return fileUriList;
    }

    public void setFileUriList(Uri fileUriList) {
        this.fileUriList = fileUriList;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
