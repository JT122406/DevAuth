package me.djtheredstoner.devauth.common.util.request.apache;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.djtheredstoner.devauth.common.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Http {

    public static HttpEntity jsonBody(JsonElement element) {
        return new StringEntity(Util.gson.toJson(element), ContentType.APPLICATION_JSON);
    }

    private static List<NameValuePair> buildNameValuePairs(Map<String, String> params) {
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public static HttpEntity urlEncodedBody(Map<String, String> params) {
        return new UrlEncodedFormEntity(buildNameValuePairs(params), StandardCharsets.UTF_8);
    }

    public static JsonObject jsonResponse(HttpResponse res, String body) {
        return Util.parser.parse(body).getAsJsonObject();
    }

    public static void checkStatus(HttpResponse res, String body) {
        StatusLine status = res.getStatusLine();
        if (status.getStatusCode() != 200) {
            throw new RuntimeException(
                "Received bad status " + status.getStatusCode() + " " + status.getReasonPhrase() +
                " body: " + body
            );
        }
    }

}
