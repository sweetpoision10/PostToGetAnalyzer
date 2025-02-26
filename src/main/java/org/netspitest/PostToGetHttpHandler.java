package org.netspitest;

import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.requests.HttpTransformation;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import org.netspitest.utils.BypassConstants;
import org.netspitest.utils.PtGUtils;

import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;


public class PostToGetHttpHandler implements HttpHandler {

    private String hash = "";
    private PtGInterface ui;
    public String getHash() {
        return hash;
    }
    public PostToGetHttpHandler(String hash, PtGInterface ui){
        this.hash = hash;
        this.ui = ui;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent) {
        if(!this.hash.isEmpty() && httpRequestToBeSent.isInScope() && httpRequestToBeSent.toolSource().isFromTool(ToolType.PROXY) && httpRequestToBeSent.method().equals("POST") && PtGUtils.isRunning()) {
            return RequestToBeSentAction.continueWith(httpRequestToBeSent);
        }
        return null;
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived) {
        //respect in-scope if checkbox is checked
        if(PtGUtils.isOnlyInScope()) {
            if (httpResponseReceived.initiatingRequest().isInScope() && httpResponseReceived.initiatingRequest().method().equals("POST") && httpResponseReceived.toolSource().isFromTool(ToolType.PROXY) && PtGUtils.isRunning()) {
                HttpRequest originalRequest = httpResponseReceived.initiatingRequest();
                HttpRequest modifiedRequest = originalRequest.withTransformationApplied(HttpTransformation.TOGGLE_METHOD);

                //to cover scenarios where application does not allow duplicate entries ,
                // for example - adding a course with same name. In this case, the original request will succeed, and the second time will fail because of app logic.
                // So we repeat the original request one more time to make sure the response covers any duplicate checking logic and then send the modified request.

                HttpRequestResponse originalRepeatedHttpRequestResponse = MAPI.getINSTANCE().http().sendRequest(originalRequest);
                HttpRequestResponse modifiedHttpRequestResponse = MAPI.getINSTANCE().http().sendRequest(modifiedRequest);

                MAPI.getINSTANCE().logging().logToOutput("\n\nMethod: " + originalRequest.method());
//                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Request:\n" + originalRequest);
//                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Response:\n" + httpResponseReceived);
                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Repeated Request:\n" + originalRepeatedHttpRequestResponse.request().url());
//                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Repeated Response:\n" + originalRepeatedHttpRequestResponse.response());
                MAPI.getINSTANCE().logging().logToOutput("\nModified Request:\n" + modifiedHttpRequestResponse.request().url());
//                MAPI.getINSTANCE().logging().logToOutput("\nModified Response:\n" + modifiedHttpRequestResponse.response());

                BypassConstants result = PtGUtils.analyzeResponse(originalRepeatedHttpRequestResponse.response(), modifiedHttpRequestResponse.response());

                if (result.equals(BypassConstants.SAME)) {
                    AuditIssue auditIssue = auditIssue(
                            "PostToGet",
                            "Result: " + result,
                            null,
                            originalRequest.url(),
                            AuditIssueSeverity.LOW,
                            AuditIssueConfidence.CERTAIN,
                            null,
                            null,
                            AuditIssueSeverity.HIGH,
                            HttpRequestResponse.httpRequestResponse(originalRequest, httpResponseReceived), modifiedHttpRequestResponse
                    );

                    if(!PtGUtils.isIssueAlreadyReported(auditIssue)) {
                        PtGUtils.addIssueToUniqueIssuesList(auditIssue);
                        MAPI.getINSTANCE().siteMap().add(auditIssue);
                    }
                } else if (result.equals(BypassConstants.SIMILAR)) {
                    AuditIssue auditIssue = auditIssue(
                            "PostToGet",
                            "Result: " + result,
                            null,
                            originalRequest.url(),
                            AuditIssueSeverity.LOW,
                            AuditIssueConfidence.TENTATIVE,
                            null,
                            null,
                            AuditIssueSeverity.LOW,
                            HttpRequestResponse.httpRequestResponse(originalRequest, httpResponseReceived), modifiedHttpRequestResponse
                    );
                    if(!PtGUtils.isIssueAlreadyReported(auditIssue)) {
                        PtGUtils.addIssueToUniqueIssuesList(auditIssue);
                        MAPI.getINSTANCE().siteMap().add(auditIssue);
                    }
                } else {
                    ;
                }
                MAPI.getINSTANCE().logging().logToOutput("\nResult: " + result);
            }
        }

        // do not respect in-scope only
        else {
            if (httpResponseReceived.initiatingRequest().method().equals("POST") && httpResponseReceived.toolSource().isFromTool(ToolType.PROXY) && PtGUtils.isRunning()) {
                HttpRequest originalRequest = httpResponseReceived.initiatingRequest();
                HttpRequest modifiedRequest = originalRequest.withTransformationApplied(HttpTransformation.TOGGLE_METHOD);

                //to cover scenarios where application does not allow duplicate entries ,
                // for example - adding a course with same name. In this case, the original request will succeed, and the second time will fail because of app logic.
                // So we repeat the original request one more time to make sure the response covers any duplicate checking logic and then send the modified request.

                HttpRequestResponse originalRepeatedHttpRequestResponse = MAPI.getINSTANCE().http().sendRequest(originalRequest);
                HttpRequestResponse modifiedHttpRequestResponse = MAPI.getINSTANCE().http().sendRequest(modifiedRequest);

                MAPI.getINSTANCE().logging().logToOutput("\n\nMethod: " + originalRequest.method());
                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Request:\n" + originalRequest);
                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Response:\n" + httpResponseReceived);
                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Repeated Request:\n" + originalRepeatedHttpRequestResponse.request());
                MAPI.getINSTANCE().logging().logToOutput("\nOriginal Repeated Response:\n" + originalRepeatedHttpRequestResponse.response());
                MAPI.getINSTANCE().logging().logToOutput("\nModified Request:\n" + modifiedHttpRequestResponse.request());
                MAPI.getINSTANCE().logging().logToOutput("\nModified Response:\n" + modifiedHttpRequestResponse.response());

                BypassConstants result = PtGUtils.analyzeResponse(originalRepeatedHttpRequestResponse.response(), modifiedHttpRequestResponse.response());

                if (result.equals(BypassConstants.SAME)) {
                    AuditIssue auditIssue = auditIssue(
                            "PostToGet ",
                            "Result: " + result,
                            null,
                            originalRequest.url(),
                            AuditIssueSeverity.LOW,
                            AuditIssueConfidence.CERTAIN,
                            null,
                            null,
                            AuditIssueSeverity.HIGH,
                            HttpRequestResponse.httpRequestResponse(originalRequest, httpResponseReceived), modifiedHttpRequestResponse
                    );
                    if(!PtGUtils.isIssueAlreadyReported(auditIssue)) {
                        PtGUtils.addIssueToUniqueIssuesList(auditIssue);
                        MAPI.getINSTANCE().siteMap().add(auditIssue);
                    }
                } else if (result.equals(BypassConstants.SIMILAR)) {
                    AuditIssue auditIssue = auditIssue(
                            "PostToGet ",
                            "Result: " + result,
                            null,
                            originalRequest.url(),
                            AuditIssueSeverity.LOW,
                            AuditIssueConfidence.TENTATIVE,
                            null,
                            null,
                            AuditIssueSeverity.LOW,
                            HttpRequestResponse.httpRequestResponse(originalRequest, httpResponseReceived), modifiedHttpRequestResponse
                    );
                    if(!PtGUtils.isIssueAlreadyReported(auditIssue)) {
                        PtGUtils.addIssueToUniqueIssuesList(auditIssue);
                        MAPI.getINSTANCE().siteMap().add(auditIssue);
                    }
                } else {
                    ;
                }
                MAPI.getINSTANCE().logging().logToOutput("\nResult: " + result);
            }
        }
        return null;
    }


    public void setHash(String text) {
        this.hash = text;
    }
}
