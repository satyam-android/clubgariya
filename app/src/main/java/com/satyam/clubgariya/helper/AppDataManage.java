package com.satyam.clubgariya.helper;

public class AppDataManage {
    private static AppDataManage instance;
    private AppDataManage(){

    }

    public static AppDataManage getInstance(){
        if(instance==null) instance=new AppDataManage();
        return instance;
    }

}
