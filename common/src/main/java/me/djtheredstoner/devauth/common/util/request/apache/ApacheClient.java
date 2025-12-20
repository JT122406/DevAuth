package me.djtheredstoner.devauth.common.util.request.apache;

import com.google.gson.JsonObject;
import me.djtheredstoner.devauth.common.auth.microsoft.Constants;
import me.djtheredstoner.devauth.common.util.Util;
import me.djtheredstoner.devauth.common.util.request.Client;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ApacheClient implements Client {

    private static final Logger logger = LogManager.getLogger("DevAuth/HTTP/Apache");

    public ApacheClient() {
        logger.info("Using Apache client");
    }

    public static final HttpClient client = HttpClients.custom()
        .setUserAgent(Constants.USER_AGENT)
        .build();

    @Override
    public JsonObject jsonPost(String url, JsonObject body) {
        return new HttpBuilder<JsonObject, JsonObject>(url)
            .header("Accept", "application/json")
            .body(Http::jsonBody, body)
            .responseHandler(Http::checkStatus)
            .execute()
            .into(Http::jsonResponse);
    }

    @Override
    public JsonObject urlEncodedJsonPost(String url, Map<String, String> body) {
        return new HttpBuilder<Map<String, String>, JsonObject>(url)
            .body(Http::urlEncodedBody, body)
            .responseHandler(Http::checkStatus)
            .execute()
            .into(Http::jsonResponse);
    }

    @Override
    public JsonObject authorizedJsonGet(String url, String authorization) {
        try {
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", authorization);

            HttpResponse response = client.execute(request);
            String body = EntityUtils.toString(response.getEntity());

            Http.checkStatus(response, body);

            return Util.parser.parse(body).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("Error GETing url: " + url, e);
        }
    }
}
