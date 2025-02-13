package org.example;

import burp.api.montoya.http.message.responses.HttpResponse;

import java.util.Arrays;

public final class PtGUtils {

    /*
     * POST to GET is possible if : - Both Original and Modified Responses have same Response Body and the same Status Code
     * POST to GET is potentially possible if: - Both Original and Modified Responses have same Response Codes and the difference in response body length is +-5% of the original response body length
    */
   public static String analyzeResponse(
                                        HttpResponse originalResponse, HttpResponse modifiedResponse) {
        byte[] originalResponseBody = Arrays.copyOfRange(originalResponse.toByteArray().getBytes(), originalResponse.bodyOffset(),
                originalResponse.toByteArray().getBytes().length);
        byte[] sessionResponseBody = Arrays.copyOfRange(modifiedResponse.toByteArray().getBytes(), modifiedResponse.bodyOffset(),
                modifiedResponse.toByteArray().getBytes().length);
        if (Arrays.equals(originalResponseBody, sessionResponseBody)
                && (originalResponse.statusCode() == modifiedResponse.statusCode())) {
            return "SAME";
        }
        if (originalResponse.statusCode() == modifiedResponse.statusCode() ) {
            int range = (int) (originalResponseBody.length * 0.05);
            int difference = originalResponseBody.length - sessionResponseBody.length;
            // Check if difference is in range
            if (difference <= range && difference >= -range) {
                return "SIMILAR";
            }
        }
        return "DIFFERENT";
   }
}
