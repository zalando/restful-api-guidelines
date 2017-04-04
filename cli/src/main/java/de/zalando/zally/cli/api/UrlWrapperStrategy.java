package de.zalando.zally.cli.api;

import org.json.JSONObject;

public class UrlWrapperStrategy implements RequestWrapperStrategy {
    private final String url;

    public UrlWrapperStrategy(String url) {
        this.url = url;
    }

    public String wrap() {
        final JSONObject wrapper = new JSONObject().put("api_definition_url", url);
        return wrapper.toString();
    }
}
