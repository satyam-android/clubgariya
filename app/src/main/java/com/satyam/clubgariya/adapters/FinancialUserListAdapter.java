package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.TransactionReferenceListFragmentListner;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;
import com.satyam.clubgariya.utils.UtilFunction;

import java.util.ArrayList;
import java.util.List;

public class FinancialUserListAdapter extends RecyclerView.Adapter<FinancialUserListAdapter.UserListModel> implements Filterable {
    private static final String TAG = "ChatUsersListAdapter";
    private Context context;
    private List<TransactionReference> appContacts;
    private List<TransactionReference> mOriginalValues;
    private List<TransactionReference> appContactsFilteredList;
    private TransactionReferenceListFragmentListner userListListner;

    public FinancialUserListAdapter(Context context, List<TransactionReference> appContacts, TransactionReferenceListFragmentListner userListListner) {
        this.context = context;
        this.appContacts = appContacts;
        this.userListListner = userListListner;
        this.appContactsFilteredList = appContacts;
    }

    @NonNull
    @Override
    public UserListModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.financial_list_user_row, parent, false);

        return new UserListModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListModel holder, final int position) {
        TransactionReference transactionReference = appContacts.get(position);
        if (transactionReference != null && transactionReference.getUsers() != null) {
            String uidForDetail;
            if(transactionReference.getReferenceType().equalsIgnoreCase(AppConstants.REFERENCE_TYPE_GROUP))
                uidForDetail=transactionReference.getGroupUserId();
            else if (transactionReference.getUsers().get(0).getUserUid().equals(CurrentUserData.getInstance().getUid()))
                uidForDetail = transactionReference.getUsers().get(1).getUserUid();
            else
                uidForDetail = transactionReference.getUsers().get(0).getUserUid();
            if (!TextUtils.isEmpty(uidForDetail))
                AppDatabaseHelper.getInstance(context).getUserByUid(uidForDetail, new AppDatabaseHelper.GetUserDetail() {
                    @Override
                    public void onUserSuccess(User user) {
                        setValueToRowView(holder, user.getName(),user.getUserStatus(), user.getImageUrl());
                    }
                });
        }
        UtilFunction.getInstance().getTransactionStatus(appContacts.get(position), holder.tvUserLastTransaction);

        holder.layoutMain.setOnClickListener(view -> userListListner.onUserClick(appContacts.get(position)));
    }

    @Override
    public int getItemCount() {
        return appContacts.size();
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values

                if (mOriginalValues == null)
                    mOriginalValues = new ArrayList<>(appContacts); // saves the original data in mOriginalValues

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    appContactsFilteredList.clear();
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        TransactionReference data = mOriginalValues.get(i);
                   /*     if (data.getUsers().get(0).getUserUid().equals(CurrentUserData.getInstance().getUid())) {
                            if (data.getUsers().get(1).getUserName().toLowerCase().contains(constraint.toString())) {
                                appContactsFilteredList.add(data);
                            }
                        } else {
                            if (data.getUsers().get(0).getUserName().toLowerCase().contains(constraint.toString())) {
                                appContactsFilteredList.add(data);
                            }
                        }*/
                    }
                    // set the Filtered result to return
                    results.count = appContactsFilteredList.size();
                    results.values = appContactsFilteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                appContacts = (ArrayList<TransactionReference>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class UserListModel extends RecyclerView.ViewHolder {
        ConstraintLayout layoutMain;
        TextView tvUserName;
        TextView tvUserLastTransaction;
        SimpleDraweeView profileImage;

        public UserListModel(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserLastTransaction = itemView.findViewById(R.id.tv_transaction_status);
            layoutMain = itemView.findViewById(R.id.ll_chat_list_user_row);
            profileImage = itemView.findViewById(R.id.iv_user_profile);
        }
    }

    public void updateListValue(List<TransactionReference> appContacts) {
        this.appContacts = appContacts;
        notifyDataSetChanged();
    }

    private void setImageFromUrl(String imageUrl, SimpleDraweeView imageView) {
        imageView.setImageURI(imageUrl);
       /*  StorageReference storageReference;
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
        });*/
    }


    public void setValueToRowView(UserListModel holder, String name, String status, String profileImage) {
        if (!TextUtils.isEmpty(name))
            holder.tvUserName.setText(name);
        if (!TextUtils.isEmpty(status))
            holder.tvUserLastTransaction.setText(status);
        if (!TextUtils.isEmpty(profileImage)) ;
        setImageFromUrl(profileImage, holder.profileImage);
    }
}
