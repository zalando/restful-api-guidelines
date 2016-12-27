package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ZallyApiClient {
    private final String url;
    private final String token;

    public ZallyApiClient(String url, String token) {
        this.url = url;
        this.token = token;
    }

    public JsonValue validate(String body) throws RuntimeException {
        HttpResponse<String> response;

        try {
            response = Unirest
                    .post(url)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException("API Error: " + e.getMessage());
        }

        return Json.parse(response.getBody());
    }
}
