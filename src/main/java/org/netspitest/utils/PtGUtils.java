package org.netspitest.utils;

import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import org.netspitest.MAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PtGUtils {

    private static boolean isRunning = false;

    //false for now. will add this feature later using SiteMapNode
    private static boolean duplicateURLs = false;

    private static boolean onlyInScope = true;

    private static List<String> issuesURLs = new ArrayList<String>();

    /*
     * POST to GET is possible if : - Both Original and Modified Responses have same Response Body and the same Status Code
     * POST to GET is potentially possible if: - Both Original and Modified Responses have same Response Codes and the difference in response body length is +-5% of the original response body length
     */
   public static BypassConstants analyzeResponse(
                                        HttpResponse originalResponse, HttpResponse modifiedResponse) {
        byte[] originalResponseBody = Arrays.copyOfRange(originalResponse.toByteArray().getBytes(), originalResponse.bodyOffset(),
                originalResponse.toByteArray().getBytes().length);
        byte[] sessionResponseBody = Arrays.copyOfRange(modifiedResponse.toByteArray().getBytes(), modifiedResponse.bodyOffset(),
                modifiedResponse.toByteArray().getBytes().length);
        if (Arrays.equals(originalResponseBody, sessionResponseBody)
                && (originalResponse.statusCode() == modifiedResponse.statusCode())) {
            return BypassConstants.SAME;
        }
        if (originalResponse.statusCode() == modifiedResponse.statusCode() ) {
            int range = (int) (originalResponseBody.length * 0.05);
            int difference = originalResponseBody.length - sessionResponseBody.length;
            // Check if difference is in range
            if (difference <= range && difference >= -range) {
                return BypassConstants.SIMILAR;
            }
        }
        return BypassConstants.DIFFERENT;
   }

   public static boolean isRunning(){
       return isRunning;
   }

   public static void setRunning(boolean isRunning){
       PtGUtils.isRunning = isRunning;
   }


    public static boolean isDuplicateURLCheckBoxChecked() {
        return duplicateURLs;
    }

    public static void setDuplicateURLCheckBox(boolean duplicateURLs) {
        PtGUtils.duplicateURLs = duplicateURLs;
    }

    public static boolean isOnlyInScopeCheckBoxChecked() {
        return onlyInScope;
    }

    public static void setOnlyInScopeCheckBox(boolean onlyInScope) {
        PtGUtils.onlyInScope = onlyInScope;
    }

    public static String getURLWithoutParams(String url){
       String newurl = url.split("\\?")[0];
       return newurl;
    }

    public static boolean isIssueAlreadyReported(AuditIssue issue){
       for (int i=0; i < PtGUtils.issuesURLs.size(); i++){
           if(issuesURLs.get(i).equals(PtGUtils.getURLWithoutParams(issue.baseUrl()))){
               MAPI.getINSTANCE().logging().logToOutput("\ncomparing :" + PtGUtils.getURLWithoutParams(issue.baseUrl()) + " with list element " + issuesURLs.get(i));
               return true;
           }
        }
       return false;
    }

    public static void addIssueToIssuesList(AuditIssue issue){
//       MAPI.getINSTANCE().siteMap().issues().add(issue);
    }

    public static void addIssueToUniqueIssuesList(AuditIssue issue){
       Boolean result =  !(PtGUtils.isIssueAlreadyReported(issue)) ? issuesURLs.add(PtGUtils.getURLWithoutParams(issue.baseUrl())): false;
       if (result) {
           MAPI.getINSTANCE().logging().logToOutput("\nNew URL added to issues :" + PtGUtils.getURLWithoutParams(issue.baseUrl()));
       }
       else{
           MAPI.getINSTANCE().logging().logToOutput("\n URL already exists in issues :" + PtGUtils.getURLWithoutParams(issue.baseUrl()));
       }
    }
}
