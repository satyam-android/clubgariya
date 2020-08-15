package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.ChatReference;

public class ChatReferenceAdapter extends FirestoreRecyclerAdapter<ChatReference, ChatReferenceAdapter.ChatReferenceViewHolder> {
    private static final String TAG = "ChatReferenceAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String uid;
    private UserListListner callback;
    private FirestoreRecyclerOptions<ChatReference> options;
    private Context context;
    private StorageReference storageReference;


    public ChatReferenceAdapter(Context context, @NonNull FirestoreRecyclerOptions<ChatReference> options, UserListListner callback) {
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
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatReferenceViewHolder holder, int position, @NonNull ChatReference model) {

        if(!TextUtils.isEmpty(model.getPartner_profile_image()))
        setImageFromUrl(model.getPartner_profile_image(),holder.ivProfile);
        holder.tvMobile.setText("Mobile");
        holder.tvLastMessage.setText(model.getLastMessage());
        holder.tvName.setText(model.getPartnerName());
        holder.layoutRow.setOnClickListener((view)->{
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    User user= UserDB.getInstance(context).userDao().getContactDetail(model.getMobileNumber());
//                    callback.onUserClick(user);
                }
            }).start();
        });
    }



    @NonNull
    @Override
    public ChatReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.chat_reference_row, parent, false);
        return new ChatReferenceViewHolder(rowView);
    }

    class ChatReferenceViewHolder extends RecyclerView.ViewHolder {
        TextView tvLastMessage;
        SimpleDraweeView ivProfile;
        TextView tvName;
        TextView tvMobile;
        TextView tvTime;
        LinearLayout layoutRow;

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
