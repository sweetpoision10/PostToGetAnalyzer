package org.netspitest;

import burp.api.montoya.MontoyaApi;

//to work with single instance of the Burp API and not having to create an instance again and again.
public  final  class MAPI {

    private static MontoyaApi INSTANCE;

    private MAPI(){
        //constructor should be private so that no instance will be created.
    }

    public static void initialize(MontoyaApi api){
        if(INSTANCE == null){
            INSTANCE = api;
        }
    }

    public static MontoyaApi getINSTANCE() {
        return INSTANCE;
    }

}
