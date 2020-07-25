package com.satyam.clubgariya.repositories;

public class LoginMobileRepository {
    private static LoginMobileRepository instance;
    private LoginMobileRepository(){

    }

    public static LoginMobileRepository getInstance(){
        if(instance==null) instance=new LoginMobileRepository();
        return instance;
    }


}
