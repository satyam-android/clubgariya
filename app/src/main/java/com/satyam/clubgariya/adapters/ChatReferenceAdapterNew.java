package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.ChatReferenceListFragmentListner;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;

import java.util.List;

public class ChatReferenceAdapterNew extends RecyclerView.Adapter<ChatReferenceAdapterNew.ChatReferenceViewHolder> {
    private static final String TAG = "ChatReferenceAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String uid;
    private ChatReferenceListFragmentListner callback;
    private List<ChatReference> options;
    private Context context;
    private StorageReference storageReference;

    public ChatReferenceAdapterNew(Context context, List<ChatReference> options, ChatReferenceListFragmentListner callback) {
        this.options = options;
        uid = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
        this.callback = callback;
        this.context = context;
    }

/*
    public ChatReferenceAdapterNew(Context context, @NonNull FirestoreRecyclerOptions<UserChatReference> options, UserListListner callback) {
        super(options);
        Log.e(TAG, "ChatReferenceAdapter: " +options.getSnapshots().size());
        this.options = options;
        uid = FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid();
        this.callback = callback;
        this.context = context;
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.e(TAG, "onDataChanged: " + options.getSnapshots().size());
        callback.renderingBottomItem(options.getSnapshots().size());


    @Override
    protected void onBindViewHolder(@NonNull ChatReferenceViewHolder holder, int position, @NonNull UserChatReference model) {

        if(!TextUtils.isEmpty(model.getProfile_image_url()))
        setImageFromUrl(model.getProfile_image_url(),holder.ivProfile);
        holder.tvMobile.setText(model.getMobileNumber());
        holder.tvLastMessage.setText(model.getLastMessage());
        holder.tvName.setText(model.getChattingWith());
        holder.layoutRow.setOnClickListener((view)->{
            callback.onUserClick(new AppContact(model.getMobileNumber(),model.getChattingWith(),"","","",model.getProfile_image_url(),true));
        });
    }
 }*/


    @NonNull
    @Override
    public ChatReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.chat_reference_row, parent, false);
        return new ChatReferenceViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatReferenceViewHolder holder, int position) {
        ChatReference model = options.get(position);
        if (model != null && model.getUsers() != null) {
            if(model.getReferenceType().equals(AppConstants.REFERENCE_TYPE_GROUP)){
                Log.e(TAG, "onBindViewHolder:for Group"+model.getReferenceName() );
                setProfileListView(holder, model.getReferenceName(), model.getLastMessage(), model.getProfileImage());
                AppDatabaseHelper.getInstance(context).getUserByUid(model.getReferenceId(), new AppDatabaseHelper.GetUserDetail() {
                    @Override
                    public void onUserSuccess(User user) {
                        setProfileListView(holder, user.getName(), model.getLastMessage(), user.getImageUrl());
                    }
                });
            }else{
                if (model.getUsers().get(0).getUserUid().equals(CurrentUserData.getInstance().getUid()))
                    AppDatabaseHelper.getInstance(context).getUserByUid(model.getUsers().get(1).getUserUid(), new AppDatabaseHelper.GetUserDetail() {
                        @Override
                        public void onUserSuccess(User user) {
                            setProfileListView(holder, user.getName(), model.getLastMessage(), user.getImageUrl());
                        }
                    });
                else
                    AppDatabaseHelper.getInstance(context).getUserByUid(model.getUsers().get(0).getUserUid(), new AppDatabaseHelper.GetUserDetail() {
                        @Override
                        public void onUserSuccess(User user) {
                            setProfileListView(holder, user.getName(), model.getLastMessage(), user.getImageUrl());
                        }
                    });

            }
        }
        holder.layoutRow.setOnClickListener((view) -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    User user = UserDB.getInstance(context).userDao().getContactDetail(model.getMobileNumber());
                    callback.onUserClick(model);
                }
            }).start();
        });
    }

    public void updateData(List<ChatReference> options) {
        this.options = options;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ChatReferenceViewHolder extends RecyclerView.ViewHolder {
        TextView tvLastMessage;
        SimpleDraweeView ivProfile;
        TextView tvName;
        TextView tvMobile;
        TextView tvTime;
        ConstraintLayout layoutRow;

        public ChatReferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobile = itemView.findViewById(R.id.tv_mobile);
            tvTime = itemView.findViewById(R.id.tv_time);
            layoutRow = itemView.findViewById(R.id.layout_chat_reference_row);
        }
    }


    private void setImageFromUrl(String imageUrl, SimpleDraweeView imageView) {
        Log.e(TAG, "setImageFromUrl: "+imageUrl );
        imageView.setImageURI(imageUrl);
    /*    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
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
        });*/
    }

    public void setProfileListView(ChatReferenceViewHolder holder, String name, String lastMessage, String profileImage) {
        if (!TextUtils.isEmpty(name))
            holder.tvName.setText(name);
        if (!TextUtils.isEmpty(lastMessage))
            holder.tvLastMessage.setText(lastMessage);
        if (!TextUtils.isEmpty(profileImage))
            holder.ivProfile.setImageURI(profileImage);
//            setImageFromUrl(profileImage, holder.ivProfile);
    }

}
