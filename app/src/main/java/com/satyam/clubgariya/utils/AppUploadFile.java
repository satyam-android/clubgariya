package com.satyam.clubgariya.utils;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.callbacks.IUploadFile;

public class AppUploadFile {
    private static AppUploadFile instance;
    private static Uri fileUri;
    private static String fileName;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private IUploadFile iUploadFile;

    private AppUploadFile() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public static AppUploadFile getInstance() {
        if (instance == null) instance = new AppUploadFile();
        return instance;
    }

    public void uploadFile(Uri fileUri, String filePath, IUploadFile iUploadFile) {
        this.iUploadFile = iUploadFile;
        if (fileUri != null && !TextUtils.isEmpty(fileUri.toString())) {
//            StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID().toString() + UtilFunction.getInstance().getFileExtension(getContext(),localUri));
            StorageReference ref = storageReference.child(filePath);
            ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            iUploadFile.onUploadSuccess(uri);
                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                 iUploadFile.onUploadProgress((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iUploadFile.onUploadFail(e.getLocalizedMessage());
                }
            });
        }
    }


}
