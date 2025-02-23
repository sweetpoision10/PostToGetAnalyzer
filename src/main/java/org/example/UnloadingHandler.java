package org.example;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;

//currently not used
public class UnloadingHandler implements ExtensionUnloadingHandler {

    private MontoyaApi api;
    private MyFIrstHTTPHandler handler;

    public UnloadingHandler(MyFIrstHTTPHandler handler){
        this.api = MAPI.getINSTANCE();
        this.handler = handler;
    }

    @Override
    public void extensionUnloaded() {
        this.api.persistence().preferences().setString("storedHash",this.handler.getHash());
    }
}
