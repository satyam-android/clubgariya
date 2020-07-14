package com.satyam.clubgariya.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.BlogAdapter;
import com.satyam.clubgariya.adapters.CommentAdapter;
import com.satyam.clubgariya.databinding.CommentsFragmentBinding;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.modals.Comment;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.viewholders.CommentViewHolder;
import com.satyam.clubgariya.viewmodels.CommentsViewModel;

public class CommentsFragment extends BaseFragment {

    private CommentsViewModel mViewModel;
    private CommentsFragmentBinding binding;
    private View fragView;
    private static Blog blogData;
    private Query query;
    private CommentAdapter mAdapter;



    public static CommentsFragment newInstance(Blog blog) {
        blogData = blog;
        return new CommentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.comments_fragment, container, false);
        fragView = binding.getRoot();
        binding.rvMainClubComment.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CommentsViewModel.class);
        binding.setCommentModel(mViewModel);
        mViewModel.setBlogData(blogData);
        setUpChatList();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.startListening();
    }

    public void setUpChatList(){
        binding.rvMainClubComment.setLayoutManager(new LinearLayoutManager(getContext()));
        query = FirebaseObjectHandler.getInstance().getCommentDocumentReference(blogData.getDocumentID()).collection(AppConstants.COMMENT_ALL_BLOG_NODE).orderBy("time", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, new SnapshotParser<Comment>() {
            @NonNull
            @Override
            public Comment parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                Comment comment = snapshot.toObject(Comment.class);
                return comment;
            }
        }).build();
        mAdapter = new CommentAdapter(options);
        binding.rvMainClubComment.setAdapter(mAdapter);

    }



}