package org.example;
import  burp.api.montoya.MontoyaApi;
import  burp.api.montoya.BurpExtension;
import burp.api.montoya.extension.ExtensionUnloadingHandler;

public class MyBurpExtensionTest implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        MAPI.initialize(montoyaApi); //only one instance can ever be created. this will also help in reducing the passing of the api instance in every method

        montoyaApi.extension().setName("POSTtoGET");
        montoyaApi.logging().logToOutput("test log entry 1");



        String hash = "";
        if (montoyaApi.persistence().preferences().stringKeys().contains("storedHash")){
            hash = montoyaApi.persistence().preferences().getString("storedHash");
        }

        MyFIrstHTTPHandler handler = new MyFIrstHTTPHandler(hash);
        montoyaApi.http().registerHttpHandler(handler);

        ExtensionUnloadingHandler unloadingHandler = new UnloadingHandler(handler);
        montoyaApi.extension().registerUnloadingHandler(unloadingHandler);
    }
}