package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.modals.ChatReferenceUser;
import com.satyam.clubgariya.modals.TransactionReferenceUser;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;

import java.util.List;

public class TransactionReferenceUserAdapter extends RecyclerView.Adapter<TransactionReferenceUserAdapter.ReferenceUserHolder> {

    private Context context;
    private List<TransactionReferenceUser> referenceUsers;

    public TransactionReferenceUserAdapter(Context context, List<TransactionReferenceUser> referenceUsers) {
        this.context = context;
        this.referenceUsers = referenceUsers;

    }

    @NonNull
    @Override
    public ReferenceUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.transaction_reference_user_row, parent, false);
        return new ReferenceUserHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceUserHolder holder, int position) {
        TransactionReferenceUser model = referenceUsers.get(position);
        if (model != null) {
            AppDatabaseHelper.getInstance(context).getUserByUid(model.getUserUid(), new AppDatabaseHelper.GetUserDetail() {
                @Override
                public void onUserSuccess(User user) {
                    if (user != null) {
                        holder.tvName.setText(user.getName());
                        if (!TextUtils.isEmpty(user.getImageUrl()))
                            holder.ivProfile.setImageURI(user.getImageUrl());
                        if(!TextUtils.isEmpty(user.getUserStatus()))
                            holder.tvLastMessage.setText(user.getUserStatus());

                    }
                }
            });

            if (model.getUserRole().equals(AppConstants.USER_ROLE_ADMIN)) {
                holder.ivDelete.setVisibility(View.GONE);
                holder.tvAdmin.setVisibility(View.VISIBLE);
            } else {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.tvAdmin.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return referenceUsers.size();
    }

    public class ReferenceUserHolder extends RecyclerView.ViewHolder {
        TextView tvLastMessage;
        SimpleDraweeView ivProfile;
        TextView tvName;
        TextView tvAdmin;
        ConstraintLayout layoutRow;
        ImageView ivDelete;

        public ReferenceUserHolder(@NonNull View itemView) {
            super(itemView);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAdmin = itemView.findViewById(R.id.tv_admin);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            layoutRow = itemView.findViewById(R.id.layout_transaction_reference_row);
        }
    }
}
