package com.satyam.clubgariya.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.satyam.clubgariya.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    TextView tvSource;
    TextView tv_comment;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSource = itemView.findViewById(R.id.tv_source);
        tv_comment = itemView.findViewById(R.id.tv_comment_data);
    }
}