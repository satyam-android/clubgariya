package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ChatImageAdapterCallback;
import com.satyam.clubgariya.modals.Chat;

import java.util.List;

public class ChatImageAdapter extends RecyclerView.Adapter<ChatImageAdapter.ChatImageHolder> {
    private static final String TAG = "ChatImageAdapter";
    private Context context;
    private List<String> imageUrls;
    private StorageReference storageReference;
    private int imageHeight;
    private int imageWidth;
    private LinearLayout.LayoutParams params;
    private Chat chat;
    private ChatImageAdapterCallback chatImageAdapterCallback;


    public ChatImageAdapter(Context context, Chat chat, ChatImageAdapterCallback chatImageAdapterCallback) {
        this.context = context;
        this.chat=chat;
        this.chatImageAdapterCallback=chatImageAdapterCallback;
        this.imageUrls = chat.getMediaList();
        if(imageUrls.size()>=4){
            imageHeight=250;
            imageWidth=250;
        }else{
            imageHeight=400;
            imageWidth=350;
        }
        params=new LinearLayout.LayoutParams(imageWidth,imageHeight);
    }

    @NonNull
    @Override
    public ChatImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " +imageUrls.size());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_row, parent, false);
        return new ChatImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatImageHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: "+position );
        if(position==3){
            holder.textView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
            setImageFromUrl(imageUrls.get(position),holder.imageView);
            if(imageUrls.size()>4){
                holder.textView.setText(imageUrls.size()-4+"  More");
            }
        }else{
            holder.textView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            setImageFromUrl(imageUrls.get(position),holder.imageView);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatImageAdapterCallback.onChatImageItemClick(chat,position);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatImageAdapterCallback.onChatImageItemClick(chat,position);
            }
        });
        holder.layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatImageAdapterCallback.onChatImageItemClick(chat,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (imageUrls.size() >= 4)
            return 4;
        else return imageUrls.size();
    }

    class ChatImageHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imageView;
        TextView textView;
        ConstraintLayout layoutContainer;

        public ChatImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_chat_row_image);
            textView = itemView.findViewById(R.id.tv_chat_row_text);
            layoutContainer = itemView.findViewById(R.id.layout_chat_image_container);
            layoutContainer.setLayoutParams(params);
        }
    }


    private void setImageFromUrl(String imageUrl,SimpleDraweeView imageView){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    imageView.setImageURI(task.getResult());
//                    requestManager.load(task.getResult()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
                    Log.e(TAG, "onComplete: " );

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
