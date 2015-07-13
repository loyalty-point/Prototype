package thesis.loyaltypointapi.controllers;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by 11120_000 on 22/05/15.
 */
public class Helper {
    public static String objectToJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object jsonToObject(String json, Class myClass){
        Gson gson = new Gson();
        return gson.fromJson(json, myClass);
    }

    public static ResponseHandler<String> getResponseHandler() {
        return new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response)
                    throws HttpResponseException, IOException {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            }
        };
    }
}
