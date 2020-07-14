package com.satyam.clubgariya.repositories;

public class RegisterRepository {

    private static RegisterRepository repository;

    private RegisterRepository(){

    }

    public static RegisterRepository getInstance(){
        if(repository==null) repository=new RegisterRepository();
        return repository;
    }
}
