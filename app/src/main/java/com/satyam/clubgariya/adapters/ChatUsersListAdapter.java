package com.satyam.clubgariya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.satyam.clubgariya.R;
import com.satyam.clubgariya.callbacks.UserChatListListner;
import com.satyam.clubgariya.database.tables.AppContact;

import java.util.List;

public class ChatUsersListAdapter extends RecyclerView.Adapter<ChatUsersListAdapter.UserListModel> {

    private Context context;
    private List<AppContact> appContacts;
    private UserChatListListner userChatListListner;

    public ChatUsersListAdapter(Context context, List<AppContact> appContacts, UserChatListListner userChatListListner) {
        this.context = context;
        this.appContacts = appContacts;
        this.userChatListListner = userChatListListner;
    }

    @NonNull
    @Override
    public UserListModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_user_row, parent, false);

        return new UserListModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListModel holder, final int position) {
        holder.tvUserName.setText(appContacts.get(position).displayName);
        holder.tvUserLastChatMsg.setText(appContacts.get(position).displayName + " 1stchat");
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChatListListner.onUserClick(appContacts.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return appContacts.size();
    }

    public class UserListModel extends RecyclerView.ViewHolder {
        ConstraintLayout layoutMain;
        TextView tvUserName;
        TextView tvUserLastChatMsg;

        public UserListModel(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserLastChatMsg = itemView.findViewById(R.id.tv_last_chat_message);
            layoutMain = itemView.findViewById(R.id.ll_chat_list_user_row);
        }
    }

    public void updateListValue(List<AppContact> appContacts) {
        this.appContacts = appContacts;
        notifyDataSetChanged();
    }
}
