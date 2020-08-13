package com.satyam.clubgariya.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;

import java.util.List;

public class ChatDetailImageAdapter extends RecyclerView.Adapter<ChatDetailImageAdapter.ChatDetailImageHolder> {
    private static final String TAG = "ChatDetailImageAdapter";
    private List<String> imageUrls;
    private StorageReference storageReference;


    public ChatDetailImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ChatDetailImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_detail_image_row, parent, false);
        return new ChatDetailImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailImageHolder holder, int position) {
        setImageFromUrl(imageUrls.get(position), holder.simpleDraweeView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    class ChatDetailImageHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView simpleDraweeView;

        public ChatDetailImageHolder(@NonNull View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.iv_chat_detail_image);
        }
    }


    private void setImageFromUrl(String imageUrl, SimpleDraweeView imageView) {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    imageView.setImageURI(task.getResult());
//                    requestManager.load(task.getResult()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
                    Log.e(TAG, "onComplete: ");

                } else {
//                    requestManager.load(context.getDrawable(R.drawable.icon_loop)).into(imageView);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
