A simple POST to GET analyzer which auto-repeats POST requests from Burp Proxy to GET and copies all POST parameters to GET parameters.

Any responses that have same status code and same content length will be marked as SAME 

Any responses that have same status code but content length differeing by +-5% will be marked as SIMILAR

otherwise, result will be marked as DIFFERENT.

The SAME and SIMILAR results will be added to SiteMap Issues under the name PostToGet.

By default, only in-scope URLs will be processed.

Works only with traditional form submissions like Content-Type: application/x-www-form-urlencoded and multipart/form-data
