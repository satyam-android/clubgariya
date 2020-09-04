package com.satyam.clubgariya.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.ui.MainActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PushChatMessage {
    private static final String TAG = "UploadMediaFile";
    private static PushChatMessage instance;
    private UploadChatListner listner;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<String> mediaStorageUri;
    private List<String> localStorageUri;
    private Chat chat;
    private int uploadCount;
    private Context context;
    private String chatMessage;
    private CollectionReference chatCollection;
    private ContentResolver contentResolver;

    private PushChatMessage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public static PushChatMessage getInstance() {
        if (instance == null) instance = new PushChatMessage();
        return instance;
    }


    public void uploadChatData(Context context, CollectionReference chatCollection, String chatMessage, List<String> localStorageUri, UploadChatListner listner) {
        this.context = context;
        this.listner = listner;
        this.localStorageUri = localStorageUri;
        this.chatMessage = chatMessage;
        this.chatCollection = chatCollection;
        this.contentResolver=context.getContentResolver();
        uploadCount = 0;
        mediaStorageUri=new ArrayList<>();
        if (localStorageUri.size() > 0) {
            try {
                uploadFile(Uri.parse(localStorageUri.get(uploadCount)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "onSuccess: "+e.getLocalizedMessage() );
            }
        } else {
            updateChat();
        }

    }

    private Uri getBitmap( Uri url)throws  IOException {
        File file=new File(UUID.randomUUID().toString()+".webp");
        InputStream input = contentResolver.openInputStream(url);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP,30,bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        bos.close();
        input.close();

        return Uri.fromFile(file);
    }

    private byte[] getImageByteArray(File fileImage) throws IOException {
        File file = fileImage;
//init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();

        return bytesArray;
    }

    private void uploadFile(Uri localUri) throws FileNotFoundException {
        StorageReference ref = storageReference.child(FirebaseAuth.getInstance().getUid() + "/" + UUID.randomUUID().toString() + UtilFunction.getInstance().getFileExtension(context, localUri));
       InputStream input = contentResolver.openInputStream(localUri);
    /*     Bitmap bmp = BitmapFactory.decodeStream(input);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        if(Build.VERSION_CODES.)
        bmp.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 25, baos);
        byte[] data = baos.toByteArray();
        //uploading the image*/
        Bitmap original = BitmapFactory.decodeStream(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 20, out);
//        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        UploadTask uploadTask = null;
        uploadTask = ref.putBytes(out.toByteArray());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadCount++;
                        mediaStorageUri.add(uri.toString());
                        Log.e(TAG, "onSuccess: Imge  " + taskSnapshot.getMetadata().getReference().toString());
                        if (localStorageUri.size() == uploadCount) {
                            updateChat();
                        } else {
                            try {
                                uploadFile(Uri.parse(localStorageUri.get(uploadCount)));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.e(TAG, "onSuccess: "+e.getLocalizedMessage() );
                            }
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                listner.uploadChatProgressCount(progress);
            }
        });
    }

    private void updateChat() {
        chat = new Chat(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid(), chatMessage, "" + System.currentTimeMillis(), mediaStorageUri, AppConstants.MESSAGE_SEND);
        chatCollection.add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                chat.setChatId(task.getResult().getId());
                listner.onChatUploadComplete(chat);
            }
        });
    }


    public interface UploadChatListner {
        void onChatUploadComplete(Chat chat);

        void uploadChatProgressCount(int progress);
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
