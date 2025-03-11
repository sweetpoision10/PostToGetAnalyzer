package org.netspitest;
import  burp.api.montoya.MontoyaApi;
import  burp.api.montoya.BurpExtension;
import burp.api.montoya.extension.ExtensionUnloadingHandler;

public class PostToGetExtension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        MAPI.initialize(montoyaApi); //only one instance can ever be created. this will also help in reducing the passing of the api instance in every method

        montoyaApi.extension().setName("POSTtoGET");


        String hash = "";
        if (montoyaApi.persistence().preferences().stringKeys().contains("storedHash")){
            hash = montoyaApi.persistence().preferences().getString("storedHash");
        }

        PtGInterface ui = new PtGInterface();
        MAPI.getINSTANCE().userInterface().registerSuiteTab("PostToGet Analyzer", ui.getUI());
//        ui.setHashFieldTxt(hash); //initialize with the current hash variable value

        PostToGetHttpHandler handler = new PostToGetHttpHandler(hash, ui);
        montoyaApi.http().registerHttpHandler(handler);
        ui.setHTTPHandler(handler);

        ExtensionUnloadingHandler unloadingHandler = new UnloadingHandler(handler);
        montoyaApi.extension().registerUnloadingHandler(unloadingHandler);
    }
}