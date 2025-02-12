package org.example;

import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.requests.HttpTransformation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;


public class MyFIrstHTTPHandler implements HttpHandler {

    private String hash = "";
    private MyUserInterface ui;
    public String getHash() {
        return hash;
    }
    public MyFIrstHTTPHandler(String hash, MyUserInterface ui){
        this.hash = hash;
        this.ui = ui;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        if(!this.hash.isEmpty() && httpRequestToBeSent.isInScope() && httpRequestToBeSent.toolSource().isFromTool(ToolType.PROXY) && httpRequestToBeSent.method().equals("POST")) {
//            HttpRequest modifiedRequest = httpRequestToBeSent.withAddedHeader("TestHash", this.hash);
            HttpRequest modifiedRequest = httpRequestToBeSent.withTransformationApplied(HttpTransformation.TOGGLE_METHOD);
            HttpRequestResponse httpRequestResponse = MAPI.getINSTANCE().http().sendRequest(modifiedRequest);
            MAPI.getINSTANCE().logging().logToOutput("Method: " + httpRequestToBeSent.method());
            MAPI.getINSTANCE().logging().logToOutput("\nOriginal Request:\n" + httpRequestToBeSent);
            MAPI.getINSTANCE().logging().logToOutput("\nModified Request:\n" + httpRequestResponse.request());
            MAPI.getINSTANCE().logging().logToOutput("\nResponse:\n" + httpRequestResponse.response());
            return RequestToBeSentAction.continueWith(httpRequestToBeSent);
        }
        return null;
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        String input = "";
        if(httpResponseReceived.initiatingRequest().isInScope() && httpResponseReceived.initiatingRequest().method().equals("POST")){
            if(httpResponseReceived.hasHeader("Age")){
                input = httpResponseReceived.headerValue("Age");
            }
            else if (httpResponseReceived.hasHeader("Date")){
                input = httpResponseReceived.headerValue("Date");
            }
            else{
                input = "Null";
            }

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(input.getBytes(StandardCharsets.UTF_8));
                this.hash = HexFormat.of().formatHex(digest.digest());
                MAPI.getINSTANCE().logging().logToOutput("last generated hash: " + this.hash);
                this.ui.setHashFieldTxt(this.hash); //updating the UI text field with the last generated hash
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void setHash(String text) {
        this.hash = text;
    }
}
