package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.satyam.clubgariya.callbacks.IBlogCallBack;
import com.satyam.clubgariya.callbacks.IClubBlogRepository;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.modals.Blog;
import com.satyam.clubgariya.modals.Like;
import com.satyam.clubgariya.repositories.ClubBlogRepository;
import com.satyam.clubgariya.ui.WriteBlogFragment;

import java.util.ArrayList;
import java.util.List;

public class ClubBlogViewModel extends AndroidViewModel implements IClubBlogRepository {

    private MutableLiveData<List<Blog>> listMutableLiveData;
    private MutableLiveData<Boolean> showProgress;
 private ClubBlogRepository clubBlogRepository;
 private IBlogCallBack viewCallBack;
 private FirebaseAuth firebaseAuth;
 public String writePostText="+ Share your feeling with the club..";
    public ClubBlogViewModel(@NonNull Application application) {
        super(application);
        clubBlogRepository= ClubBlogRepository.getInstance(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void setViewCallBack(IBlogCallBack viewCallBack){
        this.viewCallBack=viewCallBack;
    }

    public void writeNewPost(View view){
        viewCallBack.changeScreen(WriteBlogFragment.newInstance());
    }

    public void updateLike(Blog blogData){
/*        boolean alreadyLiked = false;
        if (blogData.getLikes() == null) {
            blogData.setLikes(new ArrayList<Like>());
        } else {
            for (Like like : blogData.getLikes()) {
                if (like.getUid().equals(firebaseAuth.getUid()))
                    alreadyLiked = true;
            }
        }
        if (!alreadyLiked) {
            blogData.getLikes().add(new Like(firebaseAuth.getUid(), CurrentUserData.getInstance().getUserFullName()));
            clubBlogRepository.updateBlog(blogData);
        }*/

    }



    public LiveData<List<Blog>> getBlogList(){
        if(listMutableLiveData==null)
            listMutableLiveData=new MutableLiveData<>();
        clubBlogRepository.getClubBlogList();

        return listMutableLiveData;
    }

    public LiveData<Boolean> getShowProgress(){
        if(showProgress==null)
            showProgress=new MutableLiveData<>();

        return showProgress;
    }





    @Override
    public void onSuccess(List<Blog> blogList) {
        listMutableLiveData.setValue(blogList);
        showProgress.setValue(false);
    }

    @Override
    public void onFailure(String message) {
        showProgress.setValue(false);
    }
    // TODO: Implement the ViewModel
}