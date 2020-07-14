package com.satyam.clubgariya.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.adapters.BlogAdapter;
import com.satyam.clubgariya.callbacks.IBlogCallBack;
import com.satyam.clubgariya.databinding.ClubBlogFragmentBinding;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.viewmodels.ClubBlogViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClubBlogFragment extends BaseFragment implements IBlogCallBack {

    private static final String TAG = "ClubBlogFragment";

    private ClubBlogViewModel mViewModel;
    private ClubBlogFragmentBinding binding;
    private View fragView;
    public List<Blog> blogList;
    private Blog commentBlogData;
    private BlogAdapter mAdapter;
    private Query query;

    public static ClubBlogFragment newInstance() {
        return new ClubBlogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.club_blog_fragment, container, false);
        blogList = new ArrayList<>();
        fragView = binding.getRoot();
        binding.rvMainClub.setHasFixedSize(true);
        binding.rvMainClub.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mAdapter.startListening();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        setUpRecyclerView();
        return fragView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
 /*       blogListner=FirebaseObjectHandler.getInstance().getBlogCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Blog.class));
                            break;
                        case MODIFIED:
                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Blog.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Blog.class));
                            break;
                    }
                }
            }
        });*/
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.startListening();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ClubBlogViewModel.class);
        binding.setClubBlog(mViewModel);
        mViewModel.setViewCallBack(this);
        addListners();

    }

    public void setUpRecyclerView() {
        // Init Paging Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(25)
                .build();
        query = FirebaseObjectHandler.getInstance().getBlogCollection().orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Blog> options = new FirestoreRecyclerOptions.Builder<Blog>().setQuery(query, new SnapshotParser<Blog>() {
            @NonNull
            @Override
            public Blog parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                Blog blog = snapshot.toObject(Blog.class);
                blog.setDocumentID(snapshot.getId());
                return blog;
            }
        }).build();
        mAdapter = new BlogAdapter(options, getActivity(), this);
        binding.rvMainClub.setAdapter(mAdapter);
    }


    public void addListners() {
        mViewModel.getBlogList().observe(getActivity(), new Observer<List<Blog>>() {
            @Override
            public void onChanged(List<Blog> blogList) {
                if (blogList.size() > 0) {
//                    blogAdapter.updateBlogList(blogList);
                }
            }
        });

        mViewModel.getShowProgress().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    showGlobalProgressBar("Loading data ..");
                else hideGlobalProgressBar();
            }
        });


    }

    @Override
    public void onLikeClick(Blog blogData) {
        mViewModel.updateLike(blogData);
    }

    @Override
    public void onCommentClick(Blog blogData) {
        commentBlogData = blogData;
        replaceFragment(CommentsFragment.newInstance(blogData));
    }

    @Override
    public void changeScreen(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onShareClick(Blog blogData) {

    }

}