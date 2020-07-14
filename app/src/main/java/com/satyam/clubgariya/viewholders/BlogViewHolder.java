package com.satyam.clubgariya.viewholders;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.modals.Blog;

public class BlogViewHolder extends RecyclerView.ViewHolder {
   public TextView textView;
   public ImageView imageView;
   public TextView tvSourceName;
   public TextView tvLikeCount;
   public TextView tvCommentCount;
   public RelativeLayout layoutLike;
   public RelativeLayout layoutComment;
   public RelativeLayout layoutShare;
   public RelativeLayout layoutCommentUpdate;
   public TextView lastCommentText;
   public TextView lastCommentUser;

    public BlogViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_blog_description);
        imageView = itemView.findViewById(R.id.iv_post_Image);
        tvSourceName = itemView.findViewById(R.id.tv_source);
        tvLikeCount = itemView.findViewById(R.id.tv_like_count);
        tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
        layoutLike = itemView.findViewById(R.id.rl_layout_like);
        layoutComment = itemView.findViewById(R.id.rl_layout_comment);
        layoutShare = itemView.findViewById(R.id.rl_layout_share);
        lastCommentUser = itemView.findViewById(R.id.tv_comment_last_user);
        lastCommentText = itemView.findViewById(R.id.tv_comment_last_comment);
        layoutCommentUpdate = itemView.findViewById(R.id.rl_layout_update_comment);
    }

    public void bind(Blog blog, final Context context) {
        textView.setText(blog.getDescription());
        tvSourceName.setText(blog.getSource());
/*        List<Like> likes = blog.getLikes();
        List<Comment> comments = blog.getComments();
        if (likes != null && likes.size() > 0) {
            tvLikeCount.setText(String.valueOf(likes.size()));
        }

        if (comments != null && comments.size() > 0) {
            tvCommentCount.setText(String.valueOf(comments.size()));
        }*/
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(blog.getMedialLink());

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(context).load(task.getResult()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

                } else {
                    Glide.with(context).load(context.getDrawable(R.drawable.common_full_open_on_phone)).into(imageView);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
//        holder.layoutShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callBack.onShareClick(blogList.get(position));
//            }
//        });
//        holder.layoutComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callBack.onCommentClick(blogList.get(position));
//            }
//        });
//        holder.layoutLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callBack.onLikeClick(blogList.get(position));
//            }
//        });
    }
}