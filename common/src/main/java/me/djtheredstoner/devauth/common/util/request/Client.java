package me.djtheredstoner.devauth.common.util.request;

import com.google.gson.JsonObject;
import me.djtheredstoner.devauth.common.util.request.apache.ApacheClient;

import java.util.Map;

public interface Client {

    static Client getInstance() {
        try {
            Class.forName("java.net.http.HttpClient");
            try {
                return (Client) Class.forName("me.djtheredstoner.devauth.util.request.jdk.JDKClient")
                    .getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to construct JDKClient");
            }
        } catch (ClassNotFoundException e) {
            return new ApacheClient();
        }
    }

    JsonObject jsonPost(String url, JsonObject body);

    JsonObject urlEncodedJsonPost(String url, Map<String, String> body);

    JsonObject authorizedJsonGet(String url, String authorization);

}
