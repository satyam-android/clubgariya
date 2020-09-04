package com.satyam.clubgariya.callbacks;

import android.net.Uri;

public interface IUploadFile {
    void onUploadSuccess(Uri uri);
    void onUploadFail(String error);
    void onUploadProgress(int progress);
}
