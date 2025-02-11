package org.example;

import burp.api.montoya.MontoyaApi;

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
